package org.mal.ls.completionItems;

public class Asset {
    private final String text = "asset  {\n\t[]\n}";
    private final String label = "asset";
    private final String info = "When the MAL compiler generates the Java code from the MAL specifications, an asset is translated into a java class.";

    public Asset() {}
    
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