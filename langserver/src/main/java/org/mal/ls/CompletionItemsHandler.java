/** 
 * This class handles the instences of completion items
 */
package org.mal.ls;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;

import org.mal.ls.completionItems.Abstract;
import org.mal.ls.completionItems.AND;
import org.mal.ls.completionItems.Append;
import org.mal.ls.completionItems.AssetItem;
import org.mal.ls.completionItems.Associations;
import org.mal.ls.completionItems.CategoryItem;
import org.mal.ls.completionItems.Defense;
import org.mal.ls.completionItems.Define;
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

import org.mal.ls.compiler.lib.AST;
import org.mal.ls.compiler.lib.AST.Asset;
import org.mal.ls.compiler.lib.AST.AttackStep;
import org.mal.ls.compiler.lib.AST.Category;
import org.mal.ls.compiler.lib.AST.ID;
import org.mal.ls.compiler.lib.AST.Variable;

public class CompletionItemsHandler {
    
    private Map<String, CompletionItem> ciHashMap;
    private Position cursorPos;

    private AssetItem asset;
    private Associations association;
    private CategoryItem category;
    private Define define;

    public CompletionItemsHandler() {
        this.cursorPos = new Position(0,0);
        this.ciHashMap = new HashMap<>();
        initItems();
        initUpdateItems();
    }

    private void initItems() {
        this.ciHashMap.put("abstract", new Abstract().getCi());
        this.ciHashMap.put("and", new AND().getCi());
        this.ciHashMap.put("append", new Append().getCi());
        this.ciHashMap.put("asset", new AssetItem().getCi());
        this.ciHashMap.put("association", new Associations().getCi());
        this.ciHashMap.put("category", new CategoryItem().getCi());
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
    
    private void initUpdateItems() {
        this.asset = new AssetItem(this);
        this.association = new Associations(this);
        this.category = new CategoryItem(this);
        this.define = new Define(this);

        this.ciHashMap.put("asset-snippet", this.asset.getCiSnippet());
        this.ciHashMap.put("association-snippet", this.association.getCiSnippet());
        this.ciHashMap.put("category-snippet", this.category.getCiSnippet());
        this.ciHashMap.put("define-snippet", this.define.getCi());
    }

    /**
     * Sets the postition of the current postion of the cursor 
     */
    public Position getCursorPos() {
        return this.cursorPos;
    }

    /**
     * Sets the postition of the current postion of the cursor 
     */
    public void setCursorPos(Position cursorPos) {
        this.cursorPos = cursorPos;
        updateCi();
    }

    private void updateCi() {
        this.ciHashMap.replace("asset-snippet", this.asset.getCiSnippet());
        this.ciHashMap.replace("association-snippet", this.association.getCiSnippet());
        this.ciHashMap.replace("category-snippet", this.category.getCiSnippet());
        this.ciHashMap.replace("define-snippet", this.define.getCi());
    }

    /**
     * Returns the created hashmap containing all the completion items
     */
    public Map<String, CompletionItem> getciHashMap() {
        return this.ciHashMap;
    }

    /**
     * Iterates through the ast and adds completion items for entity names
     */
    public void addCompletionItemASTNames(AST ast, List<CompletionItem> completionItems) {
        List<Category> categories = ast.getCategories();
        categories.forEach((category) -> {
            completionItems.add(createCompletionItem(category.getName().getId()));
            
            List<Asset> assets = category.getAssets();
            assets.forEach((asset) -> {
                completionItems.add(createCompletionItem(asset.getName().getId()));
                
                List<Variable> letAsset = asset.getVariables();
                letAsset.forEach((let)-> {
                    completionItems.add(createCompletionItem(let.getName().getId()));
                });

                List<AttackStep> attackSteps = asset.getAttacksteps();
                attackSteps.forEach((attackStep) -> {

                    completionItems.add(createCompletionItem(attackStep.getName().getId()));
                });
            });
        });
    }

    private CompletionItem createCompletionItem(String text) {
        CompletionItem ci = new CompletionItem();
        ci.setInsertText(text);
        ci.setLabel(text);
        ci.setKind(CompletionItemKind.Text);
        return ci;
    }
}