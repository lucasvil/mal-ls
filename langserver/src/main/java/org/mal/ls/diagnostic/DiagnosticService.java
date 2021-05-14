package org.mal.ls.diagnostic;

import java.io.File;
import java.net.URI;
import java.util.List;

import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.PublishDiagnosticsParams;
import org.eclipse.lsp4j.services.LanguageClient;
import org.mal.ls.compiler.lib.Analyzer;
import org.mal.ls.compiler.lib.Parser;
import org.mal.ls.context.DocumentContext;
import org.mal.ls.context.DocumentContextKeys;

public class DiagnosticService {
  public static PublishDiagnosticsParams getDiagnosticsParams(DocumentContext context) {
    try {
      List<Diagnostic> diagnostics = Analyzer.analyze(Parser.parse(context.get(DocumentContextKeys.URI_KEY)));

      return new PublishDiagnosticsParams(context.get(DocumentContextKeys.URI_KEY), diagnostics);
    } catch (Exception e) {
      for (StackTraceElement elem : e.getStackTrace()) {
        System.err.println(elem.toString());
      }
      return new PublishDiagnosticsParams();
    }
  }

  public static void clearDiagnostics(LanguageClient client) {
    client.publishDiagnostics(new PublishDiagnosticsParams());
  }

}
