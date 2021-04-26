/** 
 * This class represents the completion item developer info
 */
package org.mal.ls.completionItems;

public class DeveloperInfo {
    private final String text = "developer info: \"\"";
    private final String label = "developer info";
    private final String info = "Tells other MAL writers why the attack step is used and other related information.";

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