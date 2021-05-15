/** 
 * This class represents the completion item define
 */
package org.mal.ls.completionItems;

import java.util.ArrayList;
import java.util.List;
import java.lang.StringBuilder;
import org.mal.ls.handler.CompletionItemsHandler;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;
import org.eclipse.lsp4j.InsertTextFormat;
import org.eclipse.lsp4j.InsertTextMode;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.TextEdit;

public class Define extends CompletionItemMal {
    private static final String text = "#key: \"\"";
    private static final String label = "define";
    private static final String info = "Defines information for entire MAL projects. The syntax is '#key: \"value\"', for example '#version: \"1.0.0\". The keys #id and #version must be present in every project.";
    private static final CompletionItemKind kind = CompletionItemKind.Snippet;

    public Define() {
        super(text, label, info, kind);
    }
}