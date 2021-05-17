package org.mal.ls.completionItems;

import org.eclipse.lsp4j.CompletionItemKind;
import org.eclipse.lsp4j.InsertTextFormat;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.TextEdit;

public class AssetSnippet extends CompletionItemSnippetMal {
  private static final String text = "asset Name";
  private static final String label = "asset-snippet";
  private static final CompletionItemKind kind = CompletionItemKind.Snippet;
  private static final InsertTextFormat textFormat = InsertTextFormat.Snippet;

  public AssetSnippet(Position cursorPos) {
    super(text, label, kind, textFormat, textEditInit(cursorPos));
  }

  private static TextEdit textEditInit(Position start) {
    int noLines = start.getLine();
    int noCharacters = start.getCharacter();
    Position end = new Position(noLines + 1, noCharacters);
    Range range = new Range(start, end);
    TextEdit te = new TextEdit(range, createAfterString(noCharacters));
    return te;
  }

  private static String createAfterString(int noCharacters) {
    StringBuilder sb = new StringBuilder();
    sb.append(" {\n\t\n");
    for (int i = 0; i < noCharacters; i++)
      sb.append(" ");
    sb.append("}\n");
    for (int i = 0; i < noCharacters; i++)
      sb.append(" ");
    return sb.toString();
  }
}