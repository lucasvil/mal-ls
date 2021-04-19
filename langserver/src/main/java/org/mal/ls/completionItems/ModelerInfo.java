package org.mal.ls.completionItems;

public class ModelerInfo {
    private final String text = "modeler info: \"\"";
    private final String label = "modeler info";
    private final String info = "Provides information to modellers or parser developers. It can be used to communicate assumptions or parsing requirements, which might otherwise be ambiguous";

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