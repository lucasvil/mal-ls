/** 
 * This class handles the instences of completion items
 */
package org.mal.ls.handler;

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
import org.mal.ls.completionItems.AssetSnippet;
import org.mal.ls.completionItems.AssociationItem;
import org.mal.ls.completionItems.AssociationSnippet;
import org.mal.ls.completionItems.CategoryItem;
import org.mal.ls.completionItems.CategorySnippet;
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
    
    private List<CompletionItem> completionItems;
    private List<CompletionItem> completionItemsSnippet;
    private Position cursorPos;

    public CompletionItemsHandler() {
        this.cursorPos = new Position(0,0);
        this.completionItems = new ArrayList<>();
        this.completionItemsSnippet = new ArrayList<>();
        initItems();
        updateItems();
    }

    private void initItems() {
        this.completionItems.add(new Abstract().ci);
        this.completionItems.add(new AND().ci);
        this.completionItems.add(new Append().ci);
        this.completionItems.add(new AssetItem().ci);
        this.completionItems.add(new AssociationItem().ci);
        this.completionItems.add(new CategoryItem().ci);
        this.completionItems.add(new Defense().ci);
        this.completionItems.add(new Define().ci);
        this.completionItems.add(new DeveloperInfo().ci);
        this.completionItems.add(new Existence().ci);
        this.completionItems.add(new Extends().ci);
        this.completionItems.add(new Include().ci);
        this.completionItems.add(new Intersection().ci);
        this.completionItems.add(new LeadsTo().ci);
        this.completionItems.add(new Let().ci);
        this.completionItems.add(new ModelerInfo().ci);
        this.completionItems.add(new NonExistence().ci);
        this.completionItems.add(new OR().ci);
        this.completionItems.add(new Require().ci);
        this.completionItems.add(new Union().ci);
        this.completionItems.add(new UserInfo().ci);
    }
    
    private void updateItems() {
        this.completionItemsSnippet.clear();
        this.completionItemsSnippet.add(new AssetSnippet(getCursorPos()).ci);
        this.completionItemsSnippet.add(new AssociationSnippet(getCursorPos()).ci);
        this.completionItemsSnippet.add(new CategorySnippet(getCursorPos()).ci);
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
        updateItems();
    }

    /**
     * Returns the created List containing all the completion items
     */
    public List<CompletionItem> getCompletionItems() {
        return this.completionItems;
    }

    /**
     * Returns the created list containing all the snippet completion items
     */
    public List<CompletionItem> getCompletionItemsSnippet() {
        return this.completionItemsSnippet;
    }

    /**
     * Iterates through the ast and adds completion items for entity names
     */
    public void addCompletionItemASTNames(AST ast, List<CompletionItem> completionItems) {
        List<Category> categories = ast.getCategories();
        categories.forEach((category) -> {
            completionItems.add(createCompletionItem(category.name.id));
            List<Asset> assets = category.getAssets();
            assets.forEach((asset) -> {
                completionItems.add(createCompletionItem(asset.name.id));
                List<Variable> letAsset = asset.getVariables();
                letAsset.forEach((let)-> {
                    completionItems.add(createCompletionItem(let.name.id));
                });
                List<AttackStep> attackSteps = asset.getAttacksteps();
                attackSteps.forEach((attackStep) -> {
                    completionItems.add(createCompletionItem(attackStep.name.id));
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