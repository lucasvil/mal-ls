package org.mal.ls.module;

import org.eclipse.lsp4j.Position;

public class ConversionModule {
  public static Position compilerToClient(Position pos) {
    return new Position(pos.getLine()-1, pos.getCharacter()-1);
  }
  public static Position clientToCompiler(Position pos) {
    return new Position(pos.getLine()+1, pos.getCharacter()+1);
  }
}