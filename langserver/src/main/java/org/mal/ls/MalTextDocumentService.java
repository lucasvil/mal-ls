package org.mal.ls;

import java.io.IOException;
import java.net.URISyntaxException;
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
import org.mal.ls.compiler.lib.AST;
import org.mal.ls.compiler.lib.MalDiagnosticLogger;
import org.mal.ls.compiler.lib.Parser;
import org.mal.ls.context.LanguageServerContextImpl;
import org.mal.ls.context.ContextKeys;
import org.mal.ls.context.LanguageServerContext;
import org.mal.ls.handler.CompletionItemsHandler;
import org.mal.ls.handler.DefinitionHandler;
import org.mal.ls.handler.DiagnosticHandler;

public class MalTextDocumentService implements TextDocumentService {
  private final MalLanguageServer server;
  private final LanguageServerContext context;
  private final CompletionItemsHandler ciHandler;
  private final DefinitionHandler defHandler;
  private final DiagnosticHandler diagnosticHandler;

  public MalTextDocumentService(MalLanguageServer server) {
    this.server = server;
    this.context = new LanguageServerContextImpl();
    this.ciHandler = new CompletionItemsHandler();
    this.defHandler = new DefinitionHandler();
    this.diagnosticHandler = new DiagnosticHandler();
  }

  /**
   * Creates and returns a list of completion items
   */
  @Override
  public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(CompletionParams completionParams) {
    ciHandler.setCursorPos(completionParams.getPosition());
    List<CompletionItem> completionItems = new ArrayList<>();
    ciHandler.addCompletionItemASTNames(context.get(ContextKeys.AST_KEY), completionItems);
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
    context.put(ContextKeys.URI_KEY, params.getTextDocument().getUri());
    List<Location> locationList = new ArrayList<>();
    return CompletableFuture.supplyAsync(() -> {
      String variable = this.defHandler.getVariable(params.getPosition(), context.get(ContextKeys.AST_KEY));
      if (!variable.equals(""))
        locationList.addAll(this.defHandler.getDefinitionLocations(context.get(ContextKeys.URI_KEY)));
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
    buildContext(params.getTextDocument().getUri());
  }

  @Override
  public void didChange(DidChangeTextDocumentParams params) {
    buildContext(params.getTextDocument().getUri());
  }

  @Override
  public void didClose(DidCloseTextDocumentParams params) {
    diagnosticHandler.clearDiagnostics(server.getClient());
  }

  @Override
  public void didSave(DidSaveTextDocumentParams params) {
    buildContext(params.getTextDocument().getUri());
  }

  private void buildContext(String uri) {
    context.put(ContextKeys.URI_KEY, uri);
    MalDiagnosticLogger.reset();
    try {
      AST ast = Parser.parse(context.get(ContextKeys.URI_KEY));
      context.put(ContextKeys.AST_KEY, ast);
    } catch (IOException | URISyntaxException e) {
      // TODO log error
    } finally {
      diagnosticHandler.sendDiagnostics(server.getClient(), context);
    }
  }
}
