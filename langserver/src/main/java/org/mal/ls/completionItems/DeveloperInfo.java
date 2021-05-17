/** 
 * This class represents the completion item developer info
 */
package org.mal.ls.completionItems;

import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;

public class DeveloperInfo extends CompletionItemMal {
    private static final String text = "developer info: ";
    private static final String label = "developer info";
    private static final String info = "Tells other MAL writers why the attack step is used and other related information.";
    private static final CompletionItemKind kind = CompletionItemKind.Snippet;

    public DeveloperInfo() {
        super(text, label, info, kind);
    }
}