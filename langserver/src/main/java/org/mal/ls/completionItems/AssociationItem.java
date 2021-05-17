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

/** 
 * This class represents the completion item associations
 */
public class AssociationItem extends CompletionItemMal {
    private static final String text = "associations";
    private static final String label = "associations";
    private static final String info = "Any number of Asset1 instantiations can be connected to any number of Asset2 instantiations. Inline references from Asset1 to Asset2 use the name bar. Conversely, Asset2 refers to Asset1 with the name foo.";
    private static final CompletionItemKind kind = CompletionItemKind.Keyword;

    public AssociationItem() {
        super(text, label, info, kind);
    }
}