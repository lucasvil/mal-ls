package org.mal.ls.completionItems;

import java.util.ArrayList;
import java.util.List;
import java.lang.StringBuilder;
import org.mal.ls.CompletionItemsHandler;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;
import org.eclipse.lsp4j.InsertTextFormat;
import org.eclipse.lsp4j.InsertTextMode;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.TextEdit;

/** 
 * This class represents the completion item category
 */
public class Category {
    private CompletionItem ci;
    private final String text = "category ";
    private final String label = "category";
    private final String info = "Similar to a package in Java. A category consists of one or more assets. The category does not bear semantics, it is only there to enable structure for the language developer.";
    private final CompletionItemKind kind = CompletionItemKind.Keyword;
    
    private CompletionItem ciSnippet;
    private final String textSnippet = "category name";
    private final String afterText = " {\n\t\n}\n";
    private final String labelSnippet = "category-snippet";
    private final CompletionItemKind kindSnippet = CompletionItemKind.Snippet;
    
    private CompletionItemsHandler ciHandler;

    public Category() {
        initCi(this.text, this.label, this.kind, null);
    }

    public Category(CompletionItemsHandler ciHandler) {
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
        TextEdit te = new TextEdit(range, this.afterText);
        return te;
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