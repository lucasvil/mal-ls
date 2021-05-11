package org.mal.ls;

import java.util.List;

import java.net.URI;

import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;

import org.mal.ls.compiler.lib.AST;
import org.mal.ls.compiler.lib.AST.Asset;
import org.mal.ls.compiler.lib.AST.Association;
import org.mal.ls.compiler.lib.AST.AttackStep;
import org.mal.ls.compiler.lib.AST.Category;
import org.mal.ls.compiler.lib.AST.ID;
import org.mal.ls.compiler.lib.AST.Variable;
import org.mal.ls.compiler.lib.Location;

public class DefinitionHandler {

  private String variable = "";
  private String definitionUri = "";

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

  public Range getDefinitionRange(AST ast) {
    Range range = new Range();
    
    List<Category> categories = ast.getCategories();
    categories.forEach((category) -> {
      if (setLowerCase(category.getName().getId()).equals(this.variable))
        setRange(category, range);
      
      List<Asset> assets = category.getAssets();
      assets.forEach((asset) -> {
        List<Variable> variables = asset.getVariables();
        variables.forEach((variable) -> {
          if (setLowerCase(variable.getName().getId()).equals(this.variable))
            setRange(variable, range);
        });

        if (setLowerCase(asset.getName().getId()).equals(this.variable))
          setRange(asset, range);
        
        List<AttackStep> attackSteps = asset.getAttacksteps();
        attackSteps.forEach((attackStep) -> {
          if (setLowerCase(attackStep.getName().getId()).equals(this.variable))
            setRange(attackStep, range);
        });
      });
    });
    return range;
  }

  private void setRange(Location obj, Range range) {
      range.setStart(new Position(obj.start.line-1, obj.start.col-1));
      range.setEnd(new Position(obj.end.line-1, obj.end.col-1));
      this.definitionUri = obj.filename;
  }

  public String getVariable(Position position, AST ast) {
    this.variable = "";
    int line = position.getLine()+1;
    int character = position.getCharacter()+1;
    List<Association> associations = ast.getAssociations();
    List<Category> categories = ast.getCategories();
    iterAssociation(associations, line, character);
    iterAttacksteps(categories, line, character);
    return this.variable;
  }

  private void iterAttacksteps(List<Category> categories, int line, int character) {
    categories.forEach((category) -> {
      //String[] catList = trimString(association.toString(0));
      List<Asset> assets = category.getAssets();
      assets.forEach((asset) -> {
        List<AttackStep> attackSteps = asset.getAttacksteps();
        attackSteps.forEach((attackStep) -> {
          String[] asList = trimAsString(attackStep.toString(0));
          int startLine, startChar, endLine, endChar;
          if (asList != null) {
            
            for (int i=0; i<asList.length-1; i+=2) {
              String[] asStr = asList[i].split(":");
              startLine = Integer.parseInt(asStr[1]);
              startChar = Integer.parseInt(asStr[2]);
              endLine = Integer.parseInt(asStr[3]);
              endChar = Integer.parseInt(asStr[4]);
              if(endLine>=line && line>=startLine && endChar>=character && character>=startChar){
                this.variable = setLowerCase(asList[i+1]);
              }
            }
          }
        });
      });
    });
  }

  private String setLowerCase(String str) {
    char c[] = str.toCharArray();
    c[0] = Character.toLowerCase(c[0]);
    return new String(c);
  }

  private String[] trimAsString(String str) {
    String[] list = str.split("meta =");
    String[] asList;
    str = list[1];
    str = str.replace(")","");
    str = str.replace(">","");
    str = str.replace("<","");
    str = str.replace(" ","");
    str = str.replace("\"","");
    str = str.replace("{", "");
    str = str.replace("}", "");
    str = str.replace("OVERRIDES,", "");
    str = str.replace("reaches=", "");
    str = str.replace("requires=", "");
    str = str.replace("\n", "");
    list = str.split(",");
    if (list[1].equals("NO_REQUIRES")) {
      if (list.length <= 3) {
        return null;
      } else if(list.length <= 6) {
        asList = new String[2];
        asList[0] = list[4];
        asList[1] = list[5];
      } else {
        asList = new String[4];
        asList[0] = list[5];
        asList[1] = list[6];
        asList[2] = list[8];
        asList[3] = list[9];
      }
    } else {
      if (list.length <= 6) {
        asList = new String[2];
        asList[0] = list[3];
        asList[1] = list[4];
      } else if (list.length <= 9) {
        asList = new String[4];
        asList[0] = list[3];
        asList[1] = list[4];
        asList[2] = list[7];
        asList[3] = list[8];
      } else if (list.length <= 10) {
        asList = new String[4];
        asList[0] = list[4];
        asList[1] = list[5];
        asList[2] = list[7];
        asList[3] = list[8];
      } else if (list.length <= 14) {
        asList = new String[6];
        if (list[5].contains("Reaches(")) {
          asList[0] = list[3];
          asList[1] = list[4];
          asList[2] = list[8];
          asList[3] = list[9];
          asList[4] = list[11];
          asList[5] = list[12];
        } else {
          asList[0] = list[4];
          asList[1] = list[5];
          asList[2] = list[7];
          asList[3] = list[8];
          asList[4] = list[11];
          asList[5] = list[12];
        }
      } else {
        asList = new String[8];
        asList[0] = list[4];
        asList[1] = list[5];
        asList[2] = list[7];
        asList[3] = list[8];
        asList[4] = list[12];
        asList[5] = list[13];
        asList[6] = list[15];
        asList[7] = list[16];
      }
    }
    return asList;
  }

  private void iterAssociation(List<Association> associations, int line, int character) {
    associations.forEach((association) -> {
      String[] assoList = trimString(association.toString(0));
      int startLine, startChar, endLine, endChar;
      
      for (int i=0; i<assoList.length-1; i+=2) {
        String[] assoStr = assoList[i].split(":");
        startLine = Integer.parseInt(assoStr[1]);
        startChar = Integer.parseInt(assoStr[2]);
        endLine = Integer.parseInt(assoStr[3]);
        endChar = Integer.parseInt(assoStr[4]);
        if(endLine>=line && line>=startLine && endChar>=character && character>=startChar){
          this.variable = setLowerCase(assoList[i+1]);
        }
      }
    });
  }

  private String[] trimString(String str) {
    str = str.replace("ID(","");
    str = str.replace(")","");
    str = str.replace(">","");
    str = str.replace("<","");
    str = str.replace(" ","");
    str = str.replace("\"","");
    String[] list = str.split(",");
    String[] assoList = new String[8];
    assoList[0] = list[1];
    assoList[1] = list[2];
    assoList[2] = list[3];
    assoList[3] = list[4];
    assoList[4] = list[9];
    assoList[5] = list[10];
    assoList[6] = list[11];
    assoList[7] = list[12];
    return assoList;
  }
}