package pl.extollite.hungergames.command.admin;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Location;
import cn.nukkit.permission.Permission;
import cn.nukkit.player.Player;
import cn.nukkit.utils.Config;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.hgutils.HGUtils;
import pl.extollite.hungergames.command.CommandManager;
import pl.extollite.hungergames.data.ConfigData;
import pl.extollite.hungergames.game.Game;


import java.util.LinkedList;
import java.util.List;

public class SetExitCommand extends CommandManager {

    public SetExitCommand() {
        super("hgasetexit", "", "/hga setexit");
        List<CommandParameter[]> parameters = new LinkedList<>();
        this.setCommandParameters(parameters);
        Permission permission = new Permission("hg.admin.command.setexit");
        HG.getInstance().getServer().getPluginManager().addPermission(permission);
        this.setPermission(permission.getName());
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Location loc = p.getLocation();
            Config config = HG.getInstance().getConfig();
            config.set("settings.globalexit.enable", true);
            config.set("settings.globalexit.level", loc.getLevel().getId());
            config.set("settings.globalexit.x", loc.getX());
            config.set("settings.globalexit.y", loc.getY());
            config.save();
            ConfigData.load(HG.getInstance().getConfig());
            HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_exit_set() + " " + loc.getPosition().toString());
            for(Game g : HG.getInstance().getGames())
                g.setExit(loc);
        }
        return true;
    }
}
