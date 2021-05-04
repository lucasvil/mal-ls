/** 
 * This class represents the completion item leads to
 */
package org.mal.ls.completionItems;

import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;

public class LeadsTo {
    private final String text = "-> ";
    private final String label = "leads to";
    private final String info = "The successful compromise of this attack step allows the attacker to consequently compromise further attack steps. Also specifies steps affected by defenses and existence checks.";
    private final CompletionItemKind kind = CompletionItemKind.Operator;
    private CompletionItem ci;

    public LeadsTo() {
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