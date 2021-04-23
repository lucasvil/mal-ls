/** 
 * This class represents the completion item category
 */
package org.mal.ls.completionItems;

public class Category {
    private final String text = "category {\n\t[]\n}";
    private final String label = "category";
    private final String info = "Similar to a package in Java. A category consists of one or more assets. The category does not bear semantics, it is only there to enable structure for the language developer.";

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