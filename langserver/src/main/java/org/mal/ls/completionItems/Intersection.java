/** 
 * This class represents the completion item intersection
 */
package org.mal.ls.completionItems;

import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;

public class Intersection {
    private final String text = "/\\ ";
    private final String label = "intersection";
    private final String info = "Designates the intersection of the sets of associated assets designated by the roles foo and bar.";
    private final CompletionItemKind kind = CompletionItemKind.Operator;
    private CompletionItem ci;

    public Intersection() {
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