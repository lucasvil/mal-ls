package org.mal.ls.compiler.lib;

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
