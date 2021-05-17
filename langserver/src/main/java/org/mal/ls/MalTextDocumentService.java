package org.mal.ls;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.CodeActionParams;
import org.eclipse.lsp4j.CodeLens;
import org.eclipse.lsp4j.CodeLensParams;
import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.CompletionItem;
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
import org.mal.ls.compiler.lib.MalDiagnosticLogger;
import org.mal.ls.compiler.lib.AST;
import org.mal.ls.compiler.lib.Parser;
import org.mal.ls.context.DocumentContext;
import org.mal.ls.context.DocumentContextKeys;
import org.mal.ls.diagnostic.DiagnosticService;
import org.mal.ls.handler.CompletionItemsHandler;
import org.mal.ls.handler.DefinitionHandler;

public class MalTextDocumentService implements TextDocumentService {

  private AST ast;
  private CompletionItemsHandler ciHandler;
  private DefinitionHandler defHandler;
  private DocumentContext context;
  private MalLanguageServer server;

  public MalTextDocumentService(MalLanguageServer server) {
    this.server = server;
    this.context = new DocumentContext();
    this.ciHandler = new CompletionItemsHandler();
    this.defHandler = new DefinitionHandler();
  }

  /**
   * Creates and returns a list of completion items
   */
  @Override
  public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(CompletionParams completionParams) {
    ciHandler.setCursorPos(completionParams.getPosition());
    List<CompletionItem> completionItems = new ArrayList<>();
    ciHandler.addCompletionItemASTNames(this.ast, completionItems);
    completionItems.addAll(ciHandler.getCompletionItems());
    completionItems.addAll(ciHandler.getCompletionItemsSnippet());
    return CompletableFuture.supplyAsync(() -> {
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

  /**
   * Identifies token for location and returns location were token is initlized
   */
  @Override
  public CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>> definition(
      DefinitionParams params) {
    context.put(DocumentContextKeys.URI_KEY, params.getTextDocument().getUri());
    updateAST();
    List<Location> locationList = new ArrayList<>();
    return CompletableFuture.supplyAsync(() -> {
      String variable = this.defHandler.getVariable(params.getPosition(), this.ast);
      if (!variable.equals(""))
        locationList.addAll(this.defHandler.getDefinitionLocations(context.get(DocumentContextKeys.URI_KEY)));
      return Either.forLeft(locationList);
    });
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
    updateAST();
  }

  @Override
  public void didChange(DidChangeTextDocumentParams params) {
    updateAST();
    server.getClient().publishDiagnostics(DiagnosticService.getDiagnosticsParams(context));
  }

  @Override
  public void didClose(DidCloseTextDocumentParams didCloseTextDocumentParams) {
    MalDiagnosticLogger.reset();
    DiagnosticService.clearDiagnostics(server.getClient());
  }

  @Override
  public void didSave(DidSaveTextDocumentParams didSaveTextDocumentParams) {
    updateAST();
    server.getClient().publishDiagnostics(DiagnosticService.getDiagnosticsParams(context));
  }

  private void updateAST() {
    MalDiagnosticLogger.reset();
    try {
      File f = new File(new URI(context.get(DocumentContextKeys.URI_KEY)));
      this.ast = Parser.parse(f);
    } catch (Exception e) {
      /* TODO handle exceptions */
      System.err.print(e);
    }
  }
}
