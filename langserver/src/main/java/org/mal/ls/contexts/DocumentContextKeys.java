package org.mal.ls.contexts;

import org.eclipse.lsp4j.Position;
import org.mal.ls.compiler.lib.AST;

public class DocumentContextKeys {
  public static final LanguageServerContext.Key<String> URI_KEY = new LanguageServerContext.Key<>();
  public static final LanguageServerContext.Key<Position> POSITION_KEY = new LanguageServerContext.Key<>();
  public static final LanguageServerContext.Key<AST> AST_KEY = new LanguageServerContext.Key<>();
}
