package org.mal.ls.completionItems;

public class Let {
    private final String text = "let ";
    private final String label = "let";
    private final String info = "This keyword is used to associate a given expression with a specific reusable name.";

    public Let() {}
    
    public String getText() {
        return this.text;
    }

    public String getLabel() {
        return this.label;
    }

    public String getInfo() {
        return this.info;
    }
}