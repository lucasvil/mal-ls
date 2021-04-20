package org.mal.ls.completionItems;

public class Include {
    private final String text = "include \"\"";
    private final String label = "include";
    private final String info = "Includes the source code from inclded file into the current specification.";

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