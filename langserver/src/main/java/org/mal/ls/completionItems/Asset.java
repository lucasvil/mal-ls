/** 
 * This class represents the completion item asset
 */
package org.mal.ls.completionItems;

public class Asset {
    private final String text = "asset  {\n\t[]\n}";
    private final String label = "asset";
    private final String info = "When the MAL compiler generates the Java code from the MAL specifications, an asset is translated into a java class.";

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