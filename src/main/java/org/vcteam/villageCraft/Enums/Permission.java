package org.vcteam.villageCraft.Enums;

/**
 * Separate permissions, each tied to one specific thing. For example, DEBUG is tied to the /debug command.
 * Multiple can be granted to one player. OP grants all permissions.
 *
 * @author VCTeam
 */
public enum Permission {
    OP,
    MANAGER,
    DEBUG,
    WORLD,
    ENTITY,
    INVENTORY,
    DEFAULT;
}
