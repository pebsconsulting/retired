package org.xwiki.eclipse.editors.utils;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

public class Utils
{
    /**
     * Get a prefix from the current offset to a given character (not included). Examples (_ is the current offset):
     * <code>
     * [test_ : getPrefix(..., _, 0, '[', "]") = 'test'
     * [test]..._ : getPrefix(..., _, 0, '[', "]") = null
     * _ is the current offset
     * </code>
     * 
     * @param document
     * @param offset The starting offset *
     * @param startCharacter The start character that marks the beginning of the scanning region.
     * @param blockingCharacters A string containing all the blocking characters that will make the scanning fail.
     * @return The found prefix or null if a blocking character is encountered or if the starCharacter is not found.
     */
    public static String getPrefix(IDocument document, int offset, char startCharacter, String blockingCharacters)
    {
        String result = null;

        if (offset == 0) {
            return null;
        }

        try {
            int currentOffset = offset - 1;

            while (currentOffset >= 0) {
                if (blockingCharacters.indexOf(document.getChar(currentOffset)) != -1) {
                    result = null;
                    break;
                }

                if (document.getChar(currentOffset) == startCharacter) {
                    result = document.get(currentOffset + 1, offset - currentOffset - 1);
                    break;
                }

                currentOffset--;
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        return result;
    }
}
