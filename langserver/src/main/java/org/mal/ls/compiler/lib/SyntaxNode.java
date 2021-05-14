package org.mal.ls.compiler.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SyntaxNode {
  private SyntaxNode parent;
  public List<Object> children;
  private List<Token> tokens;
  private Stack<TokenType> childNames;

  public SyntaxNode(Location location) {
    this.parent = null;
    this.childNames = null;
    this.children = new ArrayList<>();
  }

  public SyntaxNode() {
    this.parent = null;
    this.childNames = null;
    this.children = new ArrayList<>();
  }

  // Root
  public SyntaxNode(TokenType[] expectedTokens) {
    this.parent = null;
    this.childNames = new Stack<>();
    for (TokenType type : expectedTokens) {
      this.childNames.push(type);
    }
    this.children = new ArrayList<>();
  }

  // Nodes
  public SyntaxNode(Stack<TokenType> expectedTokens, SyntaxNode parent) {
    this.parent = parent;
    this.childNames = expectedTokens;
    this.children = new ArrayList<>();
  }

  public void addChild(SyntaxNode node) {
    node.addParent(this);
    this.children.add(node);
  }

  private void addParent(SyntaxNode parent) {
    this.parent = parent;
  }

  public void addLeaf(Token tok) {
    children.add(tok);
  }

  public Token getToken(TokenType type) {
    if (isMissing(type)) {
      return null;
    }
    for (Object o : children) {
      if (o instanceof Token) {
        if (((Token) o).type == type) {
          return (Token) o;
        }
      }
    }
    return null;
  }

  private boolean isMissing(TokenType type) {
    for (Object o : children) {
      if (o instanceof MissingToken) {
        if (((MissingToken) o).type == type) {
          return true;
        }
      }
    }
    return false;
  }

  public Location getLocation() {
    return null;
  }

  public String locationString() {
    return "";
  }

  public int getChildrenSize() {
    return children.size();
  }

  public Token getChild(int i) {
    return (Token) this.children.get(i);
  }

  /**
   * Algorithm:
   * 
   * 1. if node expects any tokens, if not return false.
   * 
   * 2. if token is expected by node at some point.
   * 
   * 3. if token is expected by parents, add expected as missing and return false.
   * 
   * 4. if not skip token and continue
   * 
   * @param tok
   * @return
   */
  public boolean expects(Token tok) {
    if (childNames.empty()) {
      return false;
    }
    if (childNames.contains(tok.type)) {
      while (childNames.peek() != tok.type) {
        addLeaf(new MissingToken(childNames.pop(), tok));
      }
      childNames.pop();
      addLeaf(tok);
      return true;
    } else if (isExpected(tok.type)) {
      while (!childNames.empty()) {
        addLeaf(new MissingToken(childNames.pop(), tok));
      }
      return false;
    } else {
      addLeaf(new SkippedToken(tok.type, tok));
      return true;
    }
  }

  public boolean expectsToken() {
    return !this.childNames.isEmpty();
  }

  public boolean isExpected(TokenType type) {
    if (childNames.contains(type)) {
      return true;
    } else if (parent != null) {
      return parent.isExpected(type);
    } else {
      return false;
    }
  }

  public String getNodeString() {
    return this.toString();
  }

  public String toString(int level) {
    StringBuilder sb = new StringBuilder();
    String indent = " ".repeat(level);
    sb.append(String.format("%sNode = {%n", indent));
    if (children.size() > 0) {
      for (Object o : children) {
        if (o instanceof Token) {
          sb.append(String.format("%s %s%n", indent, ((Token) o)));
        } else if (o instanceof SyntaxNode) {
          SyntaxNode node = (SyntaxNode) o;
          sb.append(node.toString(level + 1));
        }
      }
    }
    sb.append(String.format("%s}%n", indent));
    return sb.toString();
  }

  @Override
  public String toString() {
    return toString(0);
  }
}
