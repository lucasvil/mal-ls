package org.mal.ls;

import java.util.List;

import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;

import org.mal.ls.compiler.lib.AST;
import org.mal.ls.compiler.lib.AST.Asset;
import org.mal.ls.compiler.lib.AST.Association;
import org.mal.ls.compiler.lib.AST.AttackStep;
import org.mal.ls.compiler.lib.AST.Category;
import org.mal.ls.compiler.lib.AST.ID;
import org.mal.ls.compiler.lib.AST.Meta;

public class DefinitionHandler {

  private String variable = "";

  public Range getDefinitionRange(AST ast) {
    System.err.println(this.variable);
    Range range = new Range();
    
    List<Category> categories = ast.getCategories();
    categories.forEach((category) -> {
      System.err.println(category.getName().getId());
      if (category.getName().getId().equals(this.variable)) {
        range.setStart(new Position(category.start.line-1, category.start.col-1));
        range.setEnd(new Position(category.end.line-1, category.end.col-1));
      }
      List<Asset> assets = category.getAssets();
      assets.forEach((asset) -> {
        System.err.println(asset.getName().getId());
        if (asset.getName().getId().equals(this.variable)) {
          range.setStart(new Position(asset.start.line-1, asset.start.col-1));
          range.setEnd(new Position(asset.end.line-1, asset.end.col-1));
        }
      });
    });
    return range;
  }

  public String getVariable(Position position, AST ast) {
    this.variable = "";
    int line = position.getLine()+1;
    int character = position.getCharacter()+1;
    List<Association> associations = ast.getAssociations();
    iterAssociation(associations, line, character);
    return this.variable;
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
          this.variable = assoList[i+1];
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