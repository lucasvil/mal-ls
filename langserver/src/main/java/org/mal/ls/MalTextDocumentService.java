package org.mal.ls;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

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

import org.mal.ls.MalCompletionItemsTexts;
import org.mal.ls.MalDebugLogger;

public class MalTextDocumentService implements TextDocumentService {

  private MalCompletionItemsTexts texts = new MalCompletionItemsTexts();
  private MalDebugLogger logger = new MalDebugLogger();

  @Override
  public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(CompletionParams completionParams) {
    HashMap<String, String> ciHashMap = texts.getciHashMap();
    List<CompletionItem> completionItems = new ArrayList<>();

    return CompletableFuture.supplyAsync(() -> {  
      this.logger.log("TEEEESSSSTTTT");

      String key1 = "", key2 = "", key3 = "";
      for (Map.Entry<String, String> ci : ciHashMap.entrySet()) {
        String[] item = ci.getKey().split(".", 2);
        if(item[1] == "text")
          key1 = ci.getKey();
        if(item[1] == "label")
          key2 = ci.getKey();
        if(item[1] == "info") {
          key3 = ci.getKey();
          completionItems.add(addCompetionItem(ciHashMap.get(key1), ciHashMap.get(key2), ciHashMap.get(key3)));
        }
      }

      return Either.forLeft(completionItems);
    });
  }

  private CompletionItem addCompetionItem(String text, String label, String info) {
    CompletionItem ci = new CompletionItem();
    ci.setInsertText(text);
    ci.setLabel(label);
    ci.setKind(CompletionItemKind.Snippet);
    ci.setDetail(info);
    return ci;
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
  public void didOpen(DidOpenTextDocumentParams didOpenTextDocumentParams) {

  }

  @Override
  public void didChange(DidChangeTextDocumentParams didChangeTextDocumentParams) {

  }

  @Override
  public void didClose(DidCloseTextDocumentParams didCloseTextDocumentParams) {

  }

  @Override
  public void didSave(DidSaveTextDocumentParams didSaveTextDocumentParams) {

  }
}
