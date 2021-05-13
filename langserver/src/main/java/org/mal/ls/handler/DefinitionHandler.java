package org.mal.ls.handler;

import java.util.List;
import java.net.URI;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.mal.ls.module.ConversionModule;
import org.mal.ls.compiler.lib.AST;
import org.mal.ls.context.DefinitionContext;
import org.mal.ls.context.DefinitionContext.DefinitionAssociation;
import org.mal.ls.context.DefinitionContext.DefinitionAsset;
import org.mal.ls.context.DefinitionContext.DefinitionAttackStep;
import org.mal.ls.context.DefinitionContext.DefinitionCategory;
import org.mal.ls.context.DefinitionContext.DefinitionVariable;
import org.mal.ls.context.DefinitionContext.DefinitionReaches;
import org.mal.ls.context.DefinitionContext.DefinitionRequires;
import org.mal.ls.context.DefinitionContext.DefinitionItem;

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
      range.setStart(ConversionModule.compilerToClient(new Position(di.startLine, di.startChar)));
      range.setEnd(ConversionModule.compilerToClient(new Position(di.endLine, di.endChar)));
      this.definitionUri = di.filename;
  }

  /*
   * Finds and sets the token name to the corresponding postion
   */
  public String getVariable(Position position, AST ast) {
    this.dc = new DefinitionContext(ast);
    resetVariable();
    position = ConversionModule.clientToCompiler(position);
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
    reaches.forEach((r) -> {
      int startLine, startChar, endLine, endChar;
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
      int startLine, startChar, endLine, endChar;
      if(r.endLine >= line && 
        line >= r.startLine && 
        r.endChar >= character && 
        character >= r.startChar){
        this.variable = setLowerCase(r.name);
      }
    });
  }
}