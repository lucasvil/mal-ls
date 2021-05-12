/** 
 * This class represents the completion item define
 */
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

public class Define {
    private final String text = "#key";
    private final String afterText = ": \"value\"\n";
    private final String label = "define";
    private final String info = "Defines information for entire MAL projects. The syntax is '#key: \"value\"', for example '#version: \"1.0.0\". The keys #id and #version must be present in every project.";
    private final CompletionItemKind kind = CompletionItemKind.Snippet;
    private CompletionItem ci;
    private CompletionItemsHandler ciHandler;

    public Define(CompletionItemsHandler ciHandler) {
        this.ciHandler = ciHandler;
        updateCi();
    }

    private void initCi(TextEdit te) {
        this.ci = new CompletionItem();
        List<TextEdit> additionalTextEdits = new ArrayList<>();
        additionalTextEdits.add(te);
        this.ci.setAdditionalTextEdits(additionalTextEdits);
        this.ci.setInsertTextFormat(InsertTextFormat.Snippet);
        this.ci.setInsertTextMode(InsertTextMode.AdjustIndentation);
        this.ci.setInsertText(this.text);
        this.ci.setLabel(this.label);
        this.ci.setKind(this.kind);
        this.ci.setDetail(this.info);
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
        initCi(textEditInit());
    }

    /** 
     * Returns the premade text when selecting the completion item 
     */
    public CompletionItem getCi() {
        updateCi();
        return this.ci;
    }
}