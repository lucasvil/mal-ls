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

/** 
 * This class represents the completion item asset
 */
public class AssetItem {
    private CompletionItem ci;
    private final String text = "asset ";
    private final String label = "asset";
    private final String info = "When the MAL compiler generates the Java code from the MAL specifications, an asset is translated into a java class.";
    private final CompletionItemKind kind = CompletionItemKind.Keyword;
    
    private CompletionItem ciSnippet;
    private final String textSnippet = "asset Name";
    private final String labelSnippet = "asset-snippet";
    private final CompletionItemKind kindSnippet = CompletionItemKind.Snippet;
    
    private CompletionItemsHandler ciHandler;

    public AssetItem() {
        initCi(this.text, this.label, this.kind, null);
    }

    public AssetItem(CompletionItemsHandler ciHandler) {
        this.ciHandler = ciHandler;
        updateCi();
    }

    private void initCi(String text, String label, CompletionItemKind kind, TextEdit te) {
        if (te!=null) {
            this.ciSnippet = new CompletionItem();
            ci = this.ciSnippet;
            List<TextEdit> additionalTextEdits = new ArrayList<>();
            additionalTextEdits.add(te);
            ci.setAdditionalTextEdits(additionalTextEdits);
        } else {
            this.ci = new CompletionItem();
            ci = this.ci;
        }
        ci.setInsertTextFormat(InsertTextFormat.Snippet);
        ci.setInsertTextMode(InsertTextMode.AdjustIndentation);
        ci.setInsertText(text);
        ci.setLabel(label);
        ci.setKind(kind);
        ci.setDetail(this.info);
    }

    private TextEdit textEditInit() {
        Position start = this.ciHandler.getCursorPos();
        int noLines = start.getLine();
        int noCharacters = start.getCharacter();
        Position end = new Position(noLines+1, noCharacters);
        Range range = new Range(start, end);
        TextEdit te = new TextEdit(range, createAfterString(noCharacters));
        return te;
    }

    private String createAfterString(int noCharacters) {
        StringBuilder sb = new StringBuilder();
        sb.append(" {\n\t\n");
        for (int i = 0; i<noCharacters; i++)
            sb.append(" ");
        sb.append("}\n");
        for (int i = 0; i<noCharacters; i++)
            sb.append(" ");
        return sb.toString();
    }

    /** 
     * Updates the values of position in the completion item 
     */
    public void updateCi() {
        initCi(this.textSnippet, this.labelSnippet, this.kindSnippet, textEditInit());
    }

    /** 
     * Returns the premade text when selecting the completion item 
     */
    public CompletionItem getCi() {
        return this.ci;
    }

    /** 
     * Returns the premade text when selecting the completion item 
     */
    public CompletionItem getCiSnippet() {
        updateCi();
        return this.ciSnippet;
    }
}