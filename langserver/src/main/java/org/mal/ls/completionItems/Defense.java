/** 
 * This class represents the completion item defence
 */
package org.mal.ls.completionItems;

import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;

public class Defense {
    private final String text = "# ";
    private final String label = "defence";
    private final String info = "As opposed to attack steps defenses are boolean. A defense step represents the countermeasure of an attack step (e.g., passwordBruteForce attack step can be defended by twoFactorAuthentications defense step). While declaring an asset, it is possible to either enable or disable a defense step by setting the defense step to either TRUE or FALSE.";
    private final CompletionItemKind kind = CompletionItemKind.Operator;
    private CompletionItem ci;

    public Defense() {
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