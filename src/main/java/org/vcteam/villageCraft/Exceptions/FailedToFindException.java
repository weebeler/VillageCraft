package org.vcteam.villageCraft.Exceptions;

/**
 * General exception used when a finder fails (i.e. player finder, item finder)
 *
 * @author VCTeam
 */
public class FailedToFindException extends Exception {
    /**
     * Takes a string and displays it as what was being searched for when the error was thrown
     * @param missingObject name of the thing that couldn't be found
     */
    public FailedToFindException(String missingObject) {
        super("Failed to find a match while searching for " + missingObject + " !");
    }
}
