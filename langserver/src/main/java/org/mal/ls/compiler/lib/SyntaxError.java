package org.mal.ls.compiler.lib;

public class SyntaxError {
  public final SyntaxErrorType type;

  public SyntaxError(SyntaxErrorType type) {
    this.type = type;
  }

  public enum SyntaxErrorType {
    MISSINGTOKEN("missing token"), SKIPPEDTOKEN("skipped token"),

    UNTERMINATEDSTRING("unterminated string"), INVALIDESCAPESEQUENCE("invalid escape sequence");

    private final String string;

    private SyntaxErrorType(String string) {
      this.string = string;
    }

    @Override
    public String toString() {
      return string;
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
