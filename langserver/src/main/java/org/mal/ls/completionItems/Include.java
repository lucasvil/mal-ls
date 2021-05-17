/** 
 * This class represents the completion item include
 */
package org.mal.ls.completionItems;

import org.eclipse.lsp4j.CompletionItemKind;

public class Include extends CompletionItemMal {
    private static final String text = "include ";
    private static final String label = "include";
    private static final String info = "Includes the source code from inclded file into the current specification.";
    private static final CompletionItemKind kind = CompletionItemKind.Snippet;

    public Include() {
        super(text, label, info, kind);
    }
}