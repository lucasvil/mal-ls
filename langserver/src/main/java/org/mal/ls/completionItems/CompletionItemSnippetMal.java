package org.mal.ls.completionItems;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;
import org.eclipse.lsp4j.InsertTextFormat;
import org.eclipse.lsp4j.TextEdit;

public class CompletionItemSnippetMal {

  public CompletionItem ci;

  public CompletionItemSnippetMal(String text, String label, CompletionItemKind kind, InsertTextFormat textFormat) {
    this.ci = new CompletionItem();
    this.ci.setInsertText(text);
    this.ci.setLabel(label);
    this.ci.setKind(kind);
    this.ci.setInsertTextFormat(textFormat);
  }
}