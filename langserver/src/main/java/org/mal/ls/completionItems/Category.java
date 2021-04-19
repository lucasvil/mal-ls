package org.mal.ls.completionItems;

public class Category {
    private final String text = "category {\n\t[]\n}";
    private final String label = "category";
    private final String info = "Similar to a package in Java. A category consists of one or more assets. The category does not bear semantics, it is only there to enable structure for the language developer.";

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