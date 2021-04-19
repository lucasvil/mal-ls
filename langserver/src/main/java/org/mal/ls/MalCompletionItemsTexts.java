package org.mal.ls;

import java.util.HashMap;

public class MalCompletionItemsTexts {
    private HashMap<String, String> ciHashMap;

    public MalCompletionItemsTexts() {
        ciHashMap = new HashMap<String, String>();
        
        ciHashMap.put("asset.text", "asset  {\n\t[]\n}");
        ciHashMap.put("asset.label", "asset");
        ciHashMap.put("asset.info", "When the MAL compiler generates the Java code from the MAL specifications, an asset is translated into a java class.");

        ciHashMap.put("abstractAsset.text", "abstract asset  {\n\t[]\n}");
        ciHashMap.put("abstractAsset.label", "abstract asset");
        ciHashMap.put("abstractAsset.info", "When the MAL compiler generates the Java code from the MAL specifications, an abstract asset is translated into a abstract java class.");
        
        ciHashMap.put("include.text", "include \"\"");
        ciHashMap.put("include.label", "include");
        ciHashMap.put("include.info", "Includes the source code from inclded file into the current specification.");

        ciHashMap.put("category.text", "category {\n\t[]\n}");
        ciHashMap.put("category.label", "category");
        ciHashMap.put("category.info", "Similar to a package in Java. A category consists of one or more assets. The category does not bear semantics, it is only there to enable structure for the language developer.");

        ciHashMap.put("userInfo.text", "user info: \"\"");
        ciHashMap.put("userInfo.label", "user info");
        ciHashMap.put("userInfo.info", "Tells other MAL writers why the attack step is used and other related information.");

        ciHashMap.put("developerInfo.text", "developer info: \"\"");
        ciHashMap.put("developerInfo.label", "developer info");
        ciHashMap.put("developerInfo.info", "Tells other MAL writers why the attack step is used and other related information.");

        ciHashMap.put("modelerInfo.text", "modeler info: \"\"");
        ciHashMap.put("modelerInfo.label", "modeler info");
        ciHashMap.put("modelerInfo.info", "Provides information to modellers or parser developers. It can be used to communicate assumptions or parsing requirements, which might otherwise be ambiguous");

    }

    public HashMap<String, String> getciHashMap() {
        return this.ciHashMap;
    }
}