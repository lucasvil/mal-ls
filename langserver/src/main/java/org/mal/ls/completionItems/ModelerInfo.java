/** 
 * This class represents the completion item modeler info
 */
package org.mal.ls.completionItems;

public class ModelerInfo {
    private final String text = "modeler info: \"\"";
    private final String label = "modeler info";
    private final String info = "Provides information to modellers or parser developers. It can be used to communicate assumptions or parsing requirements, which might otherwise be ambiguous";

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