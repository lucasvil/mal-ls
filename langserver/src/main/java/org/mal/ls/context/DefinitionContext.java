package org.mal.ls.context;

import org.mal.ls.compiler.lib.AST;
import org.mal.ls.compiler.lib.AST.Asset;
import org.mal.ls.compiler.lib.AST.Association;
import org.mal.ls.compiler.lib.AST.AttackStep;
import org.mal.ls.compiler.lib.AST.Category;
import org.mal.ls.compiler.lib.AST.Expr;
import org.mal.ls.compiler.lib.AST.ID;
import org.mal.ls.compiler.lib.AST.Variable;
import org.mal.ls.compiler.lib.AST.Reaches;
import org.mal.ls.compiler.lib.AST.Requires;
import org.mal.ls.compiler.lib.Location;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DefinitionContext {
  private AST ast;
  private List<DefinitionAssociation> associations;
  private List<DefinitionCategory> categories;
  private List<DefinitionAsset> assets;
  private List<DefinitionVariable> variables;
  private List<DefinitionAttackStep> attackSteps;
  private List<DefinitionReaches> reaches;
  private List<DefinitionRequires> requires;

  public DefinitionContext(AST ast) {
    this.ast = ast;
    this.associations = new ArrayList<>();
    this.categories = new ArrayList<>();
    this.assets = new ArrayList<>();
    this.attackSteps = new ArrayList<>();
    this.variables = new ArrayList<>();
    this.reaches = new ArrayList<>();
    this.requires = new ArrayList<>();
    updateContext();
  }

  private void updateContext() {
    List<Association> associations = this.ast.getAssociations();
    iterAssociation(associations);
    List<Category> categories = this.ast.getCategories();
    iterCategories(categories);
  }

  private void iterAssociation(List<Association> associations) {
    associations.forEach((association) -> {
      this.associations.add(new DefinitionAssociation(this, association.leftAsset.id, association.filename, association.leftAsset.start.line, association.leftAsset.end.line, association.leftAsset.start.col, association.leftAsset.end.col));
      this.associations.add(new DefinitionAssociation(this, association.leftField.id, association.filename, association.leftField.start.line, association.leftField.end.line, association.leftField.start.col, association.leftField.end.col));
      this.associations.add(new DefinitionAssociation(this, association.leftAsset.id, association.filename, association.rightField.start.line, association.rightField.end.line, association.rightField.start.col, association.rightField.end.col));
      this.associations.add(new DefinitionAssociation(this, association.rightAsset.id, association.filename, association.rightAsset.start.line, association.rightAsset.end.line, association.rightAsset.start.col, association.rightAsset.end.col));
    });
  }

  private void iterCategories(List<Category> categories) {
    categories.forEach((category) -> {
      this.categories.add(new DefinitionCategory(this, category.getName().getId(), category.filename, category.start.line, category.end.line, category.start.col, category.end.col));
      iterAssets(category.getAssets());
    });
  }

  private void iterAssets(List<Asset> assets) {
    assets.forEach((asset) -> {
      this.assets.add(new DefinitionAsset(this, asset.getName().getId(), asset.filename, asset.start.line, asset.end.line, asset.start.col, asset.end.col));
      iterAttackSteps(asset.getAttacksteps());
      iterVariables(asset.getVariables());
    });
  }

  private void iterAttackSteps(List<AttackStep> attackSteps) {
    attackSteps.forEach((as) -> {
      this.attackSteps.add(new DefinitionAttackStep(this, as.getName().getId(), as.filename, as.start.line, as.end.line, as.start.col, as.end.col));
      Optional<Requires> requires = as.getRequires();
      if (!requires.isEmpty())
        iterRequires(requires);
      Optional<Reaches> reaches = as.getReaches();
      if (!reaches.isEmpty())
        iterReaches(reaches);
    });
  }

  private void iterRequires(Optional<Requires> requires) {
    Requires r = requires.orElse(null);
    List<Expr> e = r.requires;
    e.forEach((expr) -> {
      String str = trimString(expr.toString());
      String[] exprList = str.split(",");
      String name = "", filename = "";
      for (int i = 0; i<exprList.length; i++){
        if (exprList[i].contains("ID(")){
          filename = getFilename(exprList[i]);
          name = exprList[i+1];
          this.requires.add(new DefinitionRequires(this, name, filename, expr.start.line, expr.end.line, expr.start.col, expr.end.col));
        }
      }
    });
  }

  private void iterReaches(Optional<Reaches> reaches) {
    Reaches r = reaches.orElse(null);
    List<Expr> e = r.reaches;
    e.forEach((expr) -> {
      String str = trimString(expr.toString());
      String[] exprList = str.split(",");
      String name = "", filename = "";
      for (int i = 0; i<exprList.length; i++){
        if (exprList[i].contains("ID(")){
          filename = getFilename(exprList[i]);
          name = exprList[i+1];
          this.reaches.add(new DefinitionReaches(this, name, filename, expr.start.line, expr.end.line, expr.start.col, expr.end.col));
        }
      }
    });
  }

  private String trimString(String str) {
    str = str.replace(")","");
    str = str.replace(">","");
    str = str.replace("<","");
    str = str.replace(" ","");
    str = str.replace("\"","");
    return str;
  }

  private String getFilename(String str) {
    str = str.replace("ID(","");
    String[] list = str.split(":");
    return list[0];
  }

  private void iterVariables(List<Variable> variables) {
    variables.forEach((variable) -> {
      this.variables.add(new DefinitionVariable(this, variable.getName().getId(), variable.filename, variable.start.line, variable.end.line, variable.start.col, variable.end.col));
    });
  }

  public List<DefinitionAssociation> getAssociations() {
    return this.associations;
  }

  public List<DefinitionCategory> getCategories() {
    return this.categories;
  }

  public List<DefinitionAsset> getAssets() {
    return this.assets;
  }

  public List<DefinitionAttackStep> getAttackSteps() {
    return this.attackSteps;
  }

  public List<DefinitionReaches> getReaches() {
    return this.reaches;
  }

  public List<DefinitionRequires> getRequires() {
    return this.requires;
  }

  public List<DefinitionVariable> getVariables() {
    return this.variables;
  }

  public static class DefinitionAssociation extends DefinitionItem {
    public DefinitionAssociation(DefinitionContext dc, String name, String filename, int startLine, int endLine, int startChar, int endChar) {
      dc.super(name, filename, startLine, endLine, startChar, endChar);
    }
  }

  public static class DefinitionCategory extends DefinitionItem {
    public DefinitionCategory(DefinitionContext dc, String name, String filename, int startLine, int endLine, int startChar, int endChar) {
      dc.super(name, filename, startLine, endLine, startChar, endChar);
    }
  }

  public static class DefinitionAsset extends DefinitionItem {
    public DefinitionAsset(DefinitionContext dc, String name, String filename, int startLine, int endLine, int startChar, int endChar) {
      dc.super(name, filename, startLine, endLine, startChar, endChar);
    }
  }

  public static class DefinitionAttackStep extends DefinitionItem {
    public DefinitionAttackStep(DefinitionContext dc, String name, String filename, int startLine, int endLine, int startChar, int endChar) {
      dc.super(name, filename, startLine, endLine, startChar, endChar);
    }
  }

  public static class DefinitionVariable extends DefinitionItem {
    public DefinitionVariable(DefinitionContext dc, String name, String filename, int startLine, int endLine, int startChar, int endChar) {
      dc.super(name, filename, startLine, endLine, startChar, endChar);
    }
  }

  public static class DefinitionReaches extends DefinitionItem {
    public DefinitionReaches(DefinitionContext dc, String name, String filename, int startLine, int endLine, int startChar, int endChar) {
      dc.super(name, filename, startLine, endLine, startChar, endChar);
    }
  }

  public static class DefinitionRequires extends DefinitionItem {
    public DefinitionRequires(DefinitionContext dc, String name, String filename, int startLine, int endLine, int startChar, int endChar) {
      dc.super(name, filename, startLine, endLine, startChar, endChar);
    }
  }

  public class DefinitionItem {

    public String name;
    public String filename;
    public Integer startLine;
    public Integer endLine;
    public Integer startChar;
    public Integer endChar;

    public DefinitionItem(String name, String filename, int startLine, int endLine, int startChar, int endChar) {
      this.name = name;
      this.filename = filename;
      this.startLine = startLine;
      this.endLine = endLine;
      this.startChar = startChar;
      this.endChar = endChar;
    }
  }
}