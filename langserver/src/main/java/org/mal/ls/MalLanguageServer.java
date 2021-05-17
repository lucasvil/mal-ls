package org.mal.ls;

import java.util.concurrent.CompletableFuture;

import org.eclipse.lsp4j.CompletionOptions;
import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.ServerCapabilities;
import org.eclipse.lsp4j.TextDocumentSyncKind;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.LanguageClientAware;
import org.eclipse.lsp4j.services.LanguageServer;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4j.services.WorkspaceService;

public class MalLanguageServer implements LanguageServer, LanguageClientAware {
  private TextDocumentService textDocumentService;
  private WorkspaceService workspaceService;
  private LanguageClient client;
  private int errorCode = 1;

  public MalLanguageServer() {
    this.textDocumentService = new MalTextDocumentService(this);
    this.workspaceService = new MalWorkspaceService();
  }

  @Override
  public CompletableFuture<InitializeResult> initialize(InitializeParams initializeParams) {
    final InitializeResult initializeResult = new InitializeResult(new ServerCapabilities());

    initializeResult.getCapabilities().setTextDocumentSync(TextDocumentSyncKind.Full);
    CompletionOptions completionOptions = new CompletionOptions();
    initializeResult.getCapabilities().setCompletionProvider(completionOptions);
    initializeResult.getCapabilities().setDefinitionProvider(Boolean.TRUE);
    return CompletableFuture.supplyAsync(() -> initializeResult);
  }

  @Override
  public CompletableFuture<Object> shutdown() {
    errorCode = 0;
    return null;
  }

  @Override
  public void exit() {
    System.exit(errorCode);
  }

  @Override
  public TextDocumentService getTextDocumentService() {
    return this.textDocumentService;
  }

  @Override
  public WorkspaceService getWorkspaceService() {
    return this.workspaceService;
  }

  @Override
  public void connect(LanguageClient languageClient) {
    this.client = languageClient;
  }

  public LanguageClient getClient() {
    return this.client;
  }
}
