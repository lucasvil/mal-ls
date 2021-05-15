/** 
 * This class represents the completion item user info
 */
package org.mal.ls.completionItems;

import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;

public class UserInfo extends CompletionItemMal {
    private static final String text = "user info: ";
    private static final String label = "user info";
    private static final String info = "Tells other MAL writers why the attack step is used and other related information.";
    private static final CompletionItemKind kind = CompletionItemKind.Snippet;

    public UserInfo() {
        super(text, label, info, kind);
    }
}