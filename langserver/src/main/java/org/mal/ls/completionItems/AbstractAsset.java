package org.mal.ls.completionItems;

public class AbstractAsset {
    private final String text = "abstract asset  {\n\t[]\n}";
    private final String label = "abstract asset";
    private final String info = "When the MAL compiler generates the Java code from the MAL specifications, an abstract asset is translated into a abstract java class.";

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