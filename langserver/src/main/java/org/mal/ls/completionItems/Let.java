/** 
 * This class represents the completion item let
 */
package org.mal.ls.completionItems;

public class Let {
    private final String text = "let ";
    private final String label = "let";
    private final String info = "This keyword is used to associate a given expression with a specific reusable name.";
    
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
}