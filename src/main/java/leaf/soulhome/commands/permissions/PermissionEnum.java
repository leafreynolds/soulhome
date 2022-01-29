/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.soulhome.commands.permissions;

import leaf.soulhome.SoulHome;
import net.minecraftforge.server.permission.DefaultPermissionLevel;

import java.text.MessageFormat;

public enum PermissionEnum
{
    TEST("test", DefaultPermissionLevel.OP, ""),
    HOME("home", DefaultPermissionLevel.OP, "");

    private final String path;
    private final DefaultPermissionLevel level;
    private final String description;

    PermissionEnum(String path, DefaultPermissionLevel level, String description)
    {
        this.path = path;
        this.level = level;
        this.description = description;
    }

    public String getNode()
    {
        return MessageFormat.format("{0}.command.{1}", SoulHome.MODID, this.path);
    }

    public DefaultPermissionLevel getLevel()
    {
        return level;
    }

    public String getDescription()
    {
        return description;
    }
}
