/** 
 * This class represents the completion item modeler info
 */
package org.mal.ls.completionItems;

import org.eclipse.lsp4j.CompletionItemKind;

public class ModelerInfo extends CompletionItemMal {
    private static final String text = "modeler info: ";
    private static final String label = "modeler info";
    private static final String info = "Provides information to modellers or parser developers. It can be used to communicate assumptions or parsing requirements, which might otherwise be ambiguous";
    private static final CompletionItemKind kind = CompletionItemKind.Snippet;

    public ModelerInfo() {
        super(text, label, info, kind);
    }
}