/*
 * Copyright 2019 Foreseeti AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mal.ls.compiler.lib;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.mal.ls.compiler.lib.TokenError.TokenErrorType;

public class Lexer {
  private MalLogger LOGGER;
  private String filename;
  private byte[] input;
  private int index;
  private int line;
  private int col;
  private int startLine;
  private int startCol;
  private List<Byte> lexeme;
  private List<TokenError> errors;
  private List<Token> comments = new ArrayList<>();
  private boolean eof;

  private static Map<String, TokenType> keywords;

  static {
    keywords = new HashMap<>();
    keywords.put("include", TokenType.INCLUDE);
    keywords.put("info", TokenType.INFO);
    keywords.put("category", TokenType.CATEGORY);
    keywords.put("abstract", TokenType.ABSTRACT);
    keywords.put("asset", TokenType.ASSET);
    keywords.put("extends", TokenType.EXTENDS);
    keywords.put("associations", TokenType.ASSOCIATIONS);
    keywords.put("let", TokenType.LET);
    keywords.put("E", TokenType.EXIST);
    keywords.put("C", TokenType.C);
    keywords.put("I", TokenType.I);
    keywords.put("A", TokenType.A);
  }

  private static Map<String, Byte> escapeSequences;

  static {
    escapeSequences = new HashMap<>();
    escapeSequences.put("\\b", (byte) '\b');
    escapeSequences.put("\\n", (byte) '\n');
    escapeSequences.put("\\t", (byte) '\t');
    escapeSequences.put("\\r", (byte) '\r');
    escapeSequences.put("\\f", (byte) '\f');
    escapeSequences.put("\\\"", (byte) '"');
    escapeSequences.put("\\\\", (byte) '\\');
  }

  public Lexer(File file) throws IOException {
    this(file, file.getName(), false, false);
  }

  public Lexer(File file, boolean verbose, boolean debug) throws IOException {
    this(file, file.getName(), verbose, debug);
  }

  public Lexer(File file, String relativeName) throws IOException {
    this(file, relativeName, false, false);
  }

  public Lexer(File file, String relativeName, boolean verbose, boolean debug) throws IOException {
    Locale.setDefault(Locale.ROOT);
    LOGGER = new MalLogger("LEXER", verbose, debug);
    try {
      LOGGER.debug(String.format("Creating lexer with file '%s'", relativeName));
      if (!file.exists()) {
        throw new IOException(String.format("%s: No such file or directory", relativeName));
      }
      this.filename = relativeName;
      this.input = Files.readAllBytes(file.toPath());
      this.index = 0;
      this.line = 1;
      this.col = 1;
      this.eof = input.length == 0;
    } catch (IOException e) {
      LOGGER.print();
      throw e;
    }
  }

  public static boolean syntacticallyEqual(Lexer l1, Lexer l2) {
    try {
      var tok1 = l1.next();
      var tok2 = l2.next();
      while (tok1.type != TokenType.EOF && tok2.type != TokenType.EOF) {
        if (tok1.type != tok2.type || !tok1.stringValue.equals(tok2.stringValue) || tok1.intValue != tok2.intValue
            || tok1.doubleValue != tok2.doubleValue) {
          return false;
        }
        tok1 = l1.next();
        tok2 = l2.next();
      }
      return tok1.type == TokenType.EOF && tok2.type == TokenType.EOF;
    } catch (CompilerException e) {
      return false;
    }
  }

  private String getLexemeString() {
    byte[] byteArray = new byte[lexeme.size()];
    for (int i = 0; i < lexeme.size(); i++) {
      byteArray[i] = lexeme.get(i).byteValue();
    }
    return new String(byteArray, StandardCharsets.UTF_8);
  }

  private Location getLocation() {
    return new Location(filename, new Position(startLine, startCol), new Position(line, col));
  }

  public Token next() throws CompilerException {
    startLine = line;
    startCol = col;
    lexeme = new ArrayList<>();
    errors = new ArrayList<>();
    if (eof) {
      LOGGER.print();
      return createToken(TokenType.EOF);
    }
    byte c = consume();
    switch (c) {
    case ' ':
    case '\t':
    case '\r':
    case '\n':
      return next();
    case '#':
      return createToken(TokenType.HASH);
    case ':':
      return createToken(TokenType.COLON);
    case '{':
      return createToken(TokenType.LCURLY);
    case '}':
      return createToken(TokenType.RCURLY);
    case '+':
      if (peek('>')) {
        consume();
        return createToken(TokenType.INHERIT);
      } else {
        return createToken(TokenType.PLUS);
      }
    case '-':
      if (peek('>')) {
        consume();
        return createToken(TokenType.OVERRIDE);
      } else if (peek("->")) {
        consume(2);
        return createToken(TokenType.RARROW);
      } else {
        return createToken(TokenType.MINUS);
      }
    case '&':
      return createToken(TokenType.ALL);
    case '|':
      return createToken(TokenType.ANY);
    case '!':
      if (peek('E')) {
        consume();
        return createToken(TokenType.NOTEXIST);
      } else {
        return createToken(TokenType.UNRECOGNIZEDTOKEN);
      }
    case '@':
      return createToken(TokenType.AT);
    case '[':
      return createToken(TokenType.LBRACKET);
    case ']':
      return createToken(TokenType.RBRACKET);
    case '(':
      return createToken(TokenType.LPAREN);
    case ')':
      return createToken(TokenType.RPAREN);
    case ',':
      return createToken(TokenType.COMMA);
    case '<':
      if (peek("--")) {
        consume(2);
        return createToken(TokenType.LARROW);
      } else if (peek('-')) {
        consume();
        return createToken(TokenType.REQUIRE);
      } else {
        return createToken(TokenType.UNRECOGNIZEDTOKEN);
      }
    case '=':
      return createToken(TokenType.ASSIGN);
    case '\\':
      if (peek('/')) {
        consume();
        return createToken(TokenType.UNION);
      } else {
        return createToken(TokenType.UNRECOGNIZEDTOKEN);
      }
    case '/':
      if (peek('\\')) {
        consume();
        return createToken(TokenType.INTERSECT);
      } else if (peek('/')) {
        while (!eof && !peek('\n') && !peek('\r')) {
          consume();
        }
        createComment(TokenType.SINGLECOMMENT);
        return next();
      } else if (peek('*')) {
        consume();
        while (!peek("*/")) {
          if (eof) {
            createComment(TokenType.MULTICOMMENT);
            return next();
          }
          consume();
        }
        consume(2);
        createComment(TokenType.MULTICOMMENT);
        return next();
      } else {
        return createToken(TokenType.DIVIDE);
      }
    case '.':
      if (peek('.')) {
        consume();
        return createToken(TokenType.RANGE);
      } else {
        return createToken(TokenType.DOT);
      }
    case '*':
      return createToken(TokenType.STAR);
    case '^':
      return createToken(TokenType.POWER);
    case '"':
      boolean closed = true;
      boolean invalidEscape = false;
      while (!peek('"')) {
        if (closed) {
          if (peek('\n')) {
            closed = false;
          }
        }
        if (peek('\\')) {
          consume();
          if (eof) {
            // Unterminated string starting at %s, new Position(startLine, startCol)
            errors.add(new TokenError(TokenErrorType.UNTERMINATEDSTRING));
            return createToken(TokenType.STRING);
          }
          if (input[index] < 32 || input[index] > 126) {
            throw exception(String.format("Invalid escape byte 0x%02X", input[index]));
          }
          consume();
          if (!invalidEscape) {
            var lexemeString = getLexemeString();
            String escapeSequence = lexemeString.substring(lexemeString.length() - 2);
            // lexeme = lexeme.subList(0, lexeme.size() - 2);
            if (!escapeSequences.containsKey(escapeSequence)) {
              // Invalid escape sequence '%s'", escapeSequence; }
              errors.add(new TokenError(TokenErrorType.INVALIDESCAPESEQUENCE));
            }
            invalidEscape = true;
          }
          // lexeme.add(escapeSequences.get(escapeSequence));
        } else if (eof) {
          // Unterminated string starting at %s, new Position(startLine, startCol)
          errors.add(new TokenError(TokenErrorType.UNTERMINATEDSTRING));
          return createToken(TokenType.STRING);
        } else {
          consume();
        }
      }
      consume();
      if (!closed) {
        errors.add(new TokenError(TokenErrorType.UNTERMINATEDSTRING));
      }
      return createToken(TokenType.STRING);
    default:
      if (isAlpha(c)) {
        while (isAlphaNumeric()) {
          consume();
        }
        var lexemeString = getLexemeString();
        if (keywords.containsKey(lexemeString)) {
          return createToken(keywords.get(lexemeString));
        } else {
          return createToken(TokenType.ID);
        }
      } else if (isDigit(c)) {
        while (isDigit()) {
          consume();
        }
        if (peek("..") || !peek('.')) {
          return createToken(TokenType.INT);
        } else if (peek('.')) {
          consume();
          while (isDigit()) {
            consume();
          }
          return createToken(TokenType.FLOAT);
        }
      }
      if (c < 0) {
        return createToken(TokenType.UNRECOGNIZEDTOKEN);
      } else {
        return createToken(TokenType.UNRECOGNIZEDTOKEN);
      }
    }
  }

  private void consume(int n) {
    for (int i = 0; i < n; i++) {
      consume();
    }
  }

  private byte consume() {
    if (eof) {
      throw new RuntimeException("Consuming past end-of-file");
    }
    if (input[index] == (byte) '\n') {
      line++;
      col = 1;
    } else {
      col++;
    }
    var c = input[index++];
    lexeme.add(c);
    if (index == input.length) {
      eof = true;
    }
    return c;
  }

  private boolean peek(String s) {
    var bytes = s.getBytes();
    if (input.length - index < bytes.length) {
      return false;
    }
    for (int i = 0; i < bytes.length; i++) {
      if (bytes[i] != input[index + i]) {
        return false;
      }
    }
    return true;
  }

  private boolean peek(char c) {
    return peek((byte) c);
  }

  private boolean peek(byte c) {
    if (eof) {
      return false;
    } else {
      return c == input[index];
    }
  }

  private void createComment(TokenType type) {
    var lexemeString = getLexemeString();
    lexemeString = lexemeString.substring(2, lexemeString.length());
    if (type == TokenType.MULTICOMMENT) {
      lexemeString = lexemeString.substring(0, lexemeString.length() - 2);
    }
    comments.add(new Token(type, getLocation(), lexemeString));
  }

  private Token createRawToken(TokenType type) {
    switch (type) {
    case INT:
      return new Token(type, getLocation(), Integer.parseInt(getLexemeString()));
    case FLOAT:
      return new Token(type, getLocation(), Double.parseDouble(getLexemeString()));
    case ID:
    case UNRECOGNIZEDTOKEN:
      return new Token(type, getLocation(), getLexemeString());
    case STRING:
      var lexemeString = getLexemeString();
      return new Token(type, getLocation(), lexemeString.substring(1, lexemeString.length() - 1), errors);
    default:
      return new Token(type, getLocation());
    }
  }

  private void readTrailingComments() throws CompilerException {
    // Trailing comments are all comments followed on the same line as the previous
    // token, including comments that follow previous trailing comments by exactly 1
    // line.
    startLine = line;
    startCol = col;
    lexeme = new ArrayList<>();
    if (eof || peek('\n')) {
      return;
    }
    byte c = consume();
    switch (c) {
    case ' ':
    case '\t':
      readTrailingComments();
      return;
    case '/':
      if (peek('/')) {
        while (!eof && !peek('\n') && !peek('\r')) {
          consume();
        }
        createComment(TokenType.SINGLECOMMENT);
        if (peek("\r\n")) {
          consume(2);
          readTrailingComments();
        } else if (peek('\n')) {
          consume();
          readTrailingComments();
        }
        return;
      } else if (peek('*')) {
        consume();
        while (!peek("*/")) {
          if (eof) {
            return;
          }
          consume();
        }
        consume(2);
        createComment(TokenType.MULTICOMMENT);
        readTrailingComments();
        return;
      }
      // Not a comment, we want to fall-through
    default:
      index--;
      col--;
      eof = false;
      return;
    }
  }

  private Token createToken(TokenType type) throws CompilerException {
    var token = createRawToken(type);
    var preComments = List.copyOf(comments);
    comments.clear();
    readTrailingComments();
    var postComments = List.copyOf(comments);
    comments.clear();
    return new Token(token, preComments, postComments);
  }

  private CompilerException exception(String msg) {
    Position pos = null;
    if (eof) {
      pos = new Position(line, col);
    } else {
      pos = new Position(startLine, startCol);
    }
    LOGGER.error(pos, msg);
    LOGGER.print();
    return new CompilerException("There were syntax errors");
  }

  private boolean isDigit() {
    if (eof) {
      return false;
    }
    return isDigit(input[index]);
  }

  private boolean isDigit(byte c) {
    return '0' <= c && c <= '9';
  }

  private boolean isAlpha(byte c) {
    return ('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z') || c == '_';
  }

  private boolean isAlphaNumeric() {
    if (eof) {
      return false;
    }
    return isAlphaNumeric(input[index]);
  }

  private boolean isAlphaNumeric(byte c) {
    return isDigit(c) || isAlpha(c);
  }
}
