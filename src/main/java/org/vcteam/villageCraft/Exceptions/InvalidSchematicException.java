package org.vcteam.villageCraft.Exceptions;

public class InvalidSchematicException extends Exception {
    /**
     * Thrown when a schematic is created with one of the locations set to null.
     */
    public InvalidSchematicException() {
        super("One of the locations of this schematic was null!");
    }
}
