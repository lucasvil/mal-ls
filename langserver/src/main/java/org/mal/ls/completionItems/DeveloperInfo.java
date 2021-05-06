/** 
 * This class represents the completion item developer info
 */
package org.mal.ls.completionItems;

import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;

public class DeveloperInfo {
    private final String text = "developer info: ";
    private final String label = "developer info";
    private final String info = "Tells other MAL writers why the attack step is used and other related information.";
    private final CompletionItemKind kind = CompletionItemKind.Snippet;
    private CompletionItem ci;

    public DeveloperInfo() {
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