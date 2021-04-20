package org.mal.ls;

import java.util.Arrays;
import java.util.HashMap;

import org.mal.ls.completionItems.AbstractAsset;
import org.mal.ls.completionItems.Asset;
import org.mal.ls.completionItems.Category;
import org.mal.ls.completionItems.DeveloperInfo;
import org.mal.ls.completionItems.Include;
import org.mal.ls.completionItems.Let;
import org.mal.ls.completionItems.ModelerInfo;
import org.mal.ls.completionItems.UserInfo;

public class CompletionItemsHandler {
    
    private HashMap ciHashMap = new HashMap<String, String[]>();
    
    private AbstractAsset abstractAsset = new AbstractAsset();
    private Asset asset = new Asset();
    private Category category = new Category();
    private DeveloperInfo devInfo = new DeveloperInfo();
    private Include include = new Include();
    private Let let = new Let();
    private ModelerInfo modInfo = new ModelerInfo();
    private UserInfo usrInfo = new UserInfo();

    public CompletionItemsHandler() {

        String abstractValue[] = {abstractAsset.getText(), abstractAsset.getLabel(), abstractAsset.getInfo()};
        this.ciHashMap.put("abstract", abstractValue);

        String assetValue[] = {asset.getText(), asset.getLabel(), asset.getInfo()};
        this.ciHashMap.put("asset", assetValue);

        String categoryValue[] = {category.getText(), category.getLabel(), category.getInfo()};
        this.ciHashMap.put("category", categoryValue);

        String devInfoValue[] = {devInfo.getText(), devInfo.getLabel(), devInfo.getInfo()};
        this.ciHashMap.put("devInfo", devInfoValue);

        String includeValue[] = {include.getText(), include.getLabel(), include.getInfo()};
        this.ciHashMap.put("include", includeValue);

        String letValue[] = {let.getText(), let.getLabel(), let.getInfo()};
        this.ciHashMap.put("let", letValue);

        String modInfoValue[] = {modInfo.getText(), modInfo.getLabel(), modInfo.getInfo()};
        this.ciHashMap.put("modInfo", modInfoValue);

        String usrInfoValue[] = {usrInfo.getText(), usrInfo.getLabel(), usrInfo.getInfo()};
        this.ciHashMap.put("usrInfo", usrInfoValue);
    }

    public HashMap<String, String[]> getciHashMap() {
        return this.ciHashMap;
    }
}