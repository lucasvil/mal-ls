/** 
 * This class handles the instences of completion items
 */
package org.mal.ls;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.lsp4j.CompletionItem;

import org.mal.ls.completionItems.Abstract;
import org.mal.ls.completionItems.AND;
import org.mal.ls.completionItems.Append;
import org.mal.ls.completionItems.Asset;
import org.mal.ls.completionItems.Associations;
import org.mal.ls.completionItems.Category;
import org.mal.ls.completionItems.Defense;
import org.mal.ls.completionItems.DeveloperInfo;
import org.mal.ls.completionItems.Existence;
import org.mal.ls.completionItems.Extends;
import org.mal.ls.completionItems.Include;
import org.mal.ls.completionItems.Intersection;
import org.mal.ls.completionItems.LeadsTo;
import org.mal.ls.completionItems.Let;
import org.mal.ls.completionItems.ModelerInfo;
import org.mal.ls.completionItems.NonExistence;
import org.mal.ls.completionItems.OR;
import org.mal.ls.completionItems.Require;
import org.mal.ls.completionItems.Union;
import org.mal.ls.completionItems.UserInfo;

public class CompletionItemsHandler {
    
    private Map<String, CompletionItem> ciHashMap;

    public CompletionItemsHandler() {
        this.ciHashMap = new HashMap<>();

        this.ciHashMap.put("abstract", new Abstract().getCi());
        this.ciHashMap.put("and", new AND().getCi());
        this.ciHashMap.put("append", new Append().getCi());
        this.ciHashMap.put("asset", new Asset().getCi());
        this.ciHashMap.put("association", new Associations().getCi());
        this.ciHashMap.put("category", new Category().getCi());
        this.ciHashMap.put("defense", new Defense().getCi());
        this.ciHashMap.put("devInfo", new DeveloperInfo().getCi());
        this.ciHashMap.put("existence", new Existence().getCi());
        this.ciHashMap.put("extends", new Extends().getCi());
        this.ciHashMap.put("include", new Include().getCi());
        this.ciHashMap.put("intersection", new Intersection().getCi());
        this.ciHashMap.put("leadsTo", new LeadsTo().getCi());
        this.ciHashMap.put("let", new Let().getCi());
        this.ciHashMap.put("modInfo", new ModelerInfo().getCi());
        this.ciHashMap.put("non-exsistence", new NonExistence().getCi());
        this.ciHashMap.put("or", new OR().getCi());
        this.ciHashMap.put("require", new Require().getCi());
        this.ciHashMap.put("union", new Union().getCi());
        this.ciHashMap.put("usrInfo", new UserInfo().getCi());
    }

    /**
     * Returns the created hashmap containing all the completion items
     */
    public Map<String, CompletionItem> getciHashMap() {
        return this.ciHashMap;
    }
}