/** 
 * This class represents the completion item asset
 */
package org.mal.ls.completionItems;

import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;

public class Asset {
    private final String text = "asset Name {\n\t\n}";
    private final String label = "asset";
    private final String info = "When the MAL compiler generates the Java code from the MAL specifications, an asset is translated into a java class.";
    private final CompletionItemKind kind = CompletionItemKind.Snippet;
    private CompletionItem ci;

    public Asset() {
        this.ci = new CompletionItem();
        this.ci.setInsertText(getText());
        this.ci.setLabel(getLabel());
        this.ci.setKind(getKind());
        this.ci.setDetail(getInfo());
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
    public String getText() {
        return this.text;
    }

    /** 
     * Returns the items name
     */
    public String getLabel() {
        return this.label;
    }

    /** 
     * Returns a description about the item
     */
    public String getInfo() {
        return this.info;
    }

    /** 
     * Returns a description about the item
     */
    public CompletionItemKind getKind() {
        return this.kind;
    }
}