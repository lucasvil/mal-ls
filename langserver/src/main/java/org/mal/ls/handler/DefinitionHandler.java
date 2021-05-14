package org.mal.ls.handler;

import java.util.List;
import java.net.URI;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
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

public class DefinitionHandler {
  
  private String variable = "";
  private String definitionUri = "";
  private DefinitionContext dc;
  
  /*
  * returns the full uri path to the definition file
  */
  public String getDefinitionUri(String uri) {
    StringBuilder sb = new StringBuilder();
    String[] path = uri.split("/");
    for (int i = 0; i<path.length-1; i++) {
      sb.append(path[i]);
      sb.append("/");
    }
    sb.append(this.definitionUri);
    return sb.toString();
  }

  private void resetVariable() {
    this.variable = "";
  }
  
  private String setLowerCase(String str) {
    char c[] = str.toCharArray();
    c[0] = Character.toLowerCase(c[0]);
    return new String(c);
  }
  
  /*
   * Finds the range to which corresponds to the earlier found variable
   */
  public Range getDefinitionRange() {
    Range range = new Range();
    iterCategories(this.dc.getCategories(), range);
    iterAssets(this.dc.getAssets(), range);
    iterVariables(this.dc.getVariables(), range);
    iterAttackSteps(this.dc.getAttackSteps(), range);
    return range;
  }

  private void iterCategories(List<DefinitionCategory> categories, Range range) {
    categories.forEach((category) -> {
      if (setLowerCase(category.name).equals(this.variable))
        setRange(category, range);
    });
  }

  private void iterAssets(List<DefinitionAsset> assets, Range range) {
    assets.forEach((asset) -> {
      if (setLowerCase(asset.name).equals(this.variable))
        setRange(asset, range);
    });
  }

  private void iterVariables(List<DefinitionVariable> variables, Range range) {
    variables.forEach((variable) -> {
      if (setLowerCase(variable.name).equals(this.variable))
        setRange(variable, range);
    });
  }

  private void iterAttackSteps(List<DefinitionAttackStep> attackSteps, Range range) {
    attackSteps.forEach((as) -> {
      if (setLowerCase(as.name).equals(this.variable))
        setRange(as, range);
    });
  }

  private void setRange(DefinitionItem di, Range range) {
      range.setStart(new Position(di.startLine, di.startChar));
      range.setEnd(new Position(di.endLine, di.endChar));
      this.definitionUri = di.filename;
  }

  /*
   * Finds and sets the token name to the corresponding postion
   */
  public String getVariable(Position position, AST ast) {
    this.dc = new DefinitionContext(ast);
    resetVariable();
    System.err.println(position);
    int line = position.getLine();
    int character = position.getCharacter();

    iterAssociation(dc.getAssociations(), line, character);
    iterReaches(dc.getReaches(), line, character);
    iterRequires(dc.getRequires(), line, character);

    return this.variable;
  }

  private void iterAssociation(List<DefinitionAssociation> associations, int line, int character) {
    associations.forEach((association) -> {
      if(association.endLine >= line && 
        line >= association.startLine && 
        association.endChar >= character && 
        character >= association.startChar){
        this.variable = setLowerCase(association.name);
      }
    });
  }

  private void iterReaches(List<DefinitionReaches> reaches, int line, int character) {
    System.err.println("HERE");
    reaches.forEach((r) -> {
      System.err.println("HERE");
      System.err.println(r.name);
      System.err.println(r.startLine);
      System.err.println(r.endLine);
      System.err.println(r.startChar);
      if(r.endLine >= line && 
        line >= r.startLine && 
        r.endChar >= character && 
        character >= r.startChar){
        this.variable = setLowerCase(r.name);
      }
    });
  }

  private void iterRequires(List<DefinitionRequires> requires, int line, int character) {
    requires.forEach((r) -> {
      if(r.endLine >= line && 
        line >= r.startLine && 
        r.endChar >= character && 
        character >= r.startChar){
        this.variable = setLowerCase(r.name);
      }
    });
  }
}