package org.mal.ls.compiler.lib;

public class SyntaxError {
  public final SyntaxErrorType type;

  public SyntaxError(SyntaxErrorType type) {
    this.type = type;
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
