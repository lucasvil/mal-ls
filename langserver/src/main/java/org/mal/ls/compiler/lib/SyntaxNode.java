package org.mal.ls.compiler.lib;

import java.util.List;

public class SyntaxNode extends Location {
  private SyntaxNode parent;
  private List<TokenType> expectedTokens;

  public SyntaxNode(Location location) {
    super(location);
  }

  public boolean isExpected(TokenType type) {
    return false;
  }
}
