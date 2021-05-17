package org.mal.ls.completionItems;

import java.util.ArrayList;
import java.util.List;
import java.lang.StringBuilder;
import org.mal.ls.handler.CompletionItemsHandler;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;
import org.eclipse.lsp4j.InsertTextFormat;
import org.eclipse.lsp4j.InsertTextMode;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.TextEdit;

public class CategorySnippet extends CompletionItemSnippetMal {
  private static final String text = "category name";
  private static final String afterText = " {\n\t\n}\n";
  private static final String label = "category-snippet";
  private static final CompletionItemKind kind = CompletionItemKind.Snippet;
  private static final InsertTextFormat textFormat = InsertTextFormat.Snippet;

  public CategorySnippet(Position cursorPos) {
    super(text, label, kind, textFormat, textEditInit(cursorPos));
  }

  private static TextEdit textEditInit(Position start) {
    int noLines = start.getLine();
    int noCharacters = start.getCharacter();
    Position end = new Position(noLines+1, noCharacters);
    Range range = new Range(start, end);
    TextEdit te = new TextEdit(range, afterText);
    return te;
  }
}