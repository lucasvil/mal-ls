package org.mal.ls.compiler.lib;

public class TokenError {
  public final TokenErrorType type;

  public TokenError(TokenErrorType type) {
    this.type = type;
  }

  public enum TokenErrorType {
    UNTERMINATEDSTRING("unterminated string"), INVALIDESCAPESEQUENCE("invalid escape sequence");

    private final String string;

    private TokenErrorType(String string) {
      this.string = string;
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    switch (type) {
    case UNTERMINATEDSTRING:
      sb.append("String literal is not properly closed by a double-quote.");
      break;
    case INVALIDESCAPESEQUENCE:
      sb.append("Invalid escape sequence.");
    default:
    }
    return sb.toString();
  }
}
