package org.mal.ls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import java.io.File;

import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.CodeActionParams;
import org.eclipse.lsp4j.CodeLens;
import org.eclipse.lsp4j.CodeLensParams;
import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;
import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.CompletionParams;
import org.eclipse.lsp4j.DefinitionParams;
import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.DidSaveTextDocumentParams;
import org.eclipse.lsp4j.DocumentFormattingParams;
import org.eclipse.lsp4j.DocumentOnTypeFormattingParams;
import org.eclipse.lsp4j.DocumentRangeFormattingParams;
import org.eclipse.lsp4j.DocumentSymbol;
import org.eclipse.lsp4j.DocumentSymbolParams;
import org.eclipse.lsp4j.Hover;
import org.eclipse.lsp4j.HoverParams;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.LocationLink;
import org.eclipse.lsp4j.ReferenceParams;
import org.eclipse.lsp4j.RenameParams;
import org.eclipse.lsp4j.SignatureHelp;
import org.eclipse.lsp4j.SignatureHelpParams;
import org.eclipse.lsp4j.SymbolInformation;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.WorkspaceEdit;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.mal.ls.compiler.lib.Lexer;
import org.mal.ls.context.DocumentContext;
import org.mal.ls.context.DocumentContextKeys;
import org.mal.ls.diagnostic.DiagnosticService;

public class MalTextDocumentService implements TextDocumentService {
  private MalLanguageServer server;
  private DocumentContext context;
  private CompletionItemsHandler ciHandler;

  public MalTextDocumentService(MalLanguageServer server) {
    this.server = server;
    this.context = new DocumentContext();
    this.ciHandler = new CompletionItemsHandler();
  }

  /**
   * Creates and returns a of completion items 
   */
  @Override
  public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(CompletionParams completionParams) {
    List<CompletionItem> completionItems = new ArrayList<>();
    ciHandler.setCursorPos(completionParams.getPosition());
    Map<String, CompletionItem> ciHashMap = ciHandler.getciHashMap();

    return CompletableFuture.supplyAsync(() -> {
      for (Map.Entry<String, CompletionItem> ci : ciHashMap.entrySet())
        completionItems.add(ci.getValue());
      return Either.forLeft(completionItems);
    });
  }

  @Override
  public CompletableFuture<CompletionItem> resolveCompletionItem(CompletionItem completionItem) {
    return null;
  }

  @Override
  public CompletableFuture<Hover> hover(HoverParams params) {
    return null;
  }

  @Override
  public CompletableFuture<SignatureHelp> signatureHelp(SignatureHelpParams params) {
    return null;
  }

  @Override
  public CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>> definition(
      DefinitionParams params) {
    return null;
  }

  @Override
  public CompletableFuture<List<? extends Location>> references(ReferenceParams referenceParams) {
    return null;
  }

  @Override
  public CompletableFuture<List<Either<SymbolInformation, DocumentSymbol>>> documentSymbol(
      DocumentSymbolParams params) {
    return null;
  }

  @Override
  public CompletableFuture<List<Either<Command, CodeAction>>> codeAction(CodeActionParams params) {
    return null;
  }

  @Override
  public CompletableFuture<List<? extends CodeLens>> codeLens(CodeLensParams codeLensParams) {
    return null;
  }

  @Override
  public CompletableFuture<CodeLens> resolveCodeLens(CodeLens codeLens) {
    return null;
  }

  @Override
  public CompletableFuture<List<? extends TextEdit>> formatting(DocumentFormattingParams documentFormattingParams) {
    return null;
  }

  @Override
  public CompletableFuture<List<? extends TextEdit>> rangeFormatting(
      DocumentRangeFormattingParams documentRangeFormattingParams) {
    return null;
  }

  @Override
  public CompletableFuture<List<? extends TextEdit>> onTypeFormatting(
      DocumentOnTypeFormattingParams documentOnTypeFormattingParams) {
    return null;
  }

  @Override
  public CompletableFuture<WorkspaceEdit> rename(RenameParams renameParams) {
    return null;
  }

  @Override
  public void didOpen(DidOpenTextDocumentParams params) {
    context.put(DocumentContextKeys.URI_KEY, params.getTextDocument().getUri());
    server.getClient().publishDiagnostics(DiagnosticService.getDiagnosticsParams(context));
  }

  @Override
  public void didChange(DidChangeTextDocumentParams params) {
    server.getClient().publishDiagnostics(DiagnosticService.getDiagnosticsParams(context));
  }

  @Override
  public void didClose(DidCloseTextDocumentParams didCloseTextDocumentParams) {
    DiagnosticService.clearDiagnostics(server.getClient());
  }

  @Override
  public void didSave(DidSaveTextDocumentParams didSaveTextDocumentParams) {
    server.getClient().publishDiagnostics(DiagnosticService.getDiagnosticsParams(context));
  }
}
