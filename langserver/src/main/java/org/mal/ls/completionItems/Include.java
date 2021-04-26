/** 
 * This class represents the completion item include
 */
package org.mal.ls.completionItems;

public class Include {
    private final String text = "include \"\"";
    private final String label = "include";
    private final String info = "Includes the source code from inclded file into the current specification.";

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