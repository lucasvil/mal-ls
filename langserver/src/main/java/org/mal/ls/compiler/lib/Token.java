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

import java.util.List;

public class Token extends Location {
  public final TokenType type;
  public final String stringValue;
  public final double doubleValue;
  public final int intValue;
  public final List<Token> preComments;
  public final List<Token> postComments;
  public final List<TokenError> errors;

  public Token(TokenType type, Location location) {
    super(location);
    this.type = type;
    this.stringValue = "";
    this.doubleValue = 0.0;
    this.intValue = 0;
    preComments = List.of();
    postComments = List.of();
    this.errors = List.of();
  }

  public Token(TokenType type, Location location, List<TokenError> errors) {
    super(location);
    this.type = type;
    this.stringValue = "";
    this.doubleValue = 0.0;
    this.intValue = 0;
    preComments = List.of();
    postComments = List.of();
    this.errors = errors;
  }

  public Token(TokenType type, Location location, String stringValue) {
    super(location);
    this.type = type;
    this.stringValue = stringValue;
    this.doubleValue = 0.0;
    this.intValue = 0;
    preComments = List.of();
    postComments = List.of();
    this.errors = List.of();
  }

  public Token(TokenType type, Location location, String stringValue, List<TokenError> errors) {
    super(location);
    System.err.println("Token constructor: " + errors.size());
    this.type = type;
    this.stringValue = stringValue;
    this.doubleValue = 0.0;
    this.intValue = 0;
    preComments = List.of();
    postComments = List.of();
    this.errors = errors;
  }

  public Token(Token tok, List<Token> preComments, List<Token> postComments) {
    super(tok.filename, tok.start, tok.end);
    type = tok.type;
    stringValue = tok.stringValue;
    doubleValue = tok.doubleValue;
    intValue = tok.intValue;
    this.preComments = preComments;
    this.postComments = postComments;
    this.errors = tok.errors;
  }

  public Token(TokenType type, Location location, double doubleValue) {
    super(location);
    this.type = type;
    this.stringValue = "";
    this.doubleValue = doubleValue;
    this.intValue = 0;
    preComments = List.of();
    postComments = List.of();
    this.errors = List.of();
  }

  public Token(TokenType type, Location location, int intValue) {
    super(location);
    this.type = type;
    this.stringValue = "";
    this.doubleValue = 0.0;
    this.intValue = intValue;
    preComments = List.of();
    postComments = List.of();
    this.errors = List.of();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(type);
    sb.append(", ");
    sb.append(locationString());
    switch (type) {
    case FLOAT:
      sb.append(", ");
      sb.append(doubleValue);
      break;
    case INT:
      sb.append(", ");
      sb.append(intValue);
      break;
    case ID:
    case STRING:
      sb.append(", ");
      sb.append(stringValue);
      break;
    default:
      break;
    }
    sb.append(", Errors: ");
    sb.append(errors.size());
    return sb.toString();
  }
}
