package pl.extollite.hungergames.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.PluginIdentifiableCommand;
import cn.nukkit.plugin.Plugin;
import pl.extollite.hungergames.HG;

public abstract class CommandManager extends Command implements PluginIdentifiableCommand {

    public CommandManager(String name, String desc, String usage) {
        super(name, desc, usage);
    }

    public Plugin getPlugin() {
        return HG.getInstance();
    }
}
