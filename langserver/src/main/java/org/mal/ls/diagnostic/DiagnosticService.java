package org.mal.ls.diagnostic;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.PublishDiagnosticsParams;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.services.LanguageClient;
import org.mal.ls.compiler.lib.CompilerException;
import org.mal.ls.compiler.lib.Lexer;
import org.mal.ls.compiler.lib.SyntaxError;
import org.mal.ls.compiler.lib.TokenType;
import org.mal.ls.compiler.lib.Token;
import org.mal.ls.context.DocumentContext;
import org.mal.ls.context.DocumentContextKeys;

public class DiagnosticService {
  public static PublishDiagnosticsParams getDiagnosticsParams(DocumentContext context) {
    try {
      File file = new File(new URI(context.get(DocumentContextKeys.URI_KEY)));
      Lexer lexer = new Lexer(file);
      Token token;
      List<Diagnostic> diagnostics = new ArrayList<>();
      while ((token = lexer.next()).type != TokenType.EOF) {
        if (token.type == TokenType.UNRECOGNIZEDTOKEN) {
          diagnostics.add(new Diagnostic(
              new Range(new Position(token.start.line - 1, token.start.col - 1),
                  new Position(token.end.line - 1, token.end.col - 1)),
              String.format("'%s' cannot be resolved.", token.stringValue)));
        }
        for (SyntaxError error : token.errors) {
          switch (error.type) {
          case UNTERMINATEDSTRING:
            diagnostics.add(new Diagnostic(
                new Range(new Position(token.start.line - 1, token.start.col - 1),
                    new Position(token.end.line - 1, token.end.col - 1)),
                "String literal is not properly closed by a double-quote."));
            break;
          case INVALIDESCAPESEQUENCE:
            diagnostics.add(new Diagnostic(new Range(new Position(token.start.line - 1, token.start.col - 1),
                new Position(token.end.line - 1, token.end.col - 1)), "Invalid escapesequence."));
            break;
          default:
          }
        }
      }
      return new PublishDiagnosticsParams(context.get(DocumentContextKeys.URI_KEY), diagnostics);
    } catch (IOException | CompilerException | URISyntaxException e) {
      System.err.println(e.getMessage());
      return new PublishDiagnosticsParams();
    }
  }

  public static void clearDiagnostics(LanguageClient client) {
    client.publishDiagnostics(new PublishDiagnosticsParams());
  }
}
