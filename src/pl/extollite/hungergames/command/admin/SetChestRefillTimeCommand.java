package pl.extollite.hungergames.command.admin;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.permission.Permission;
import cn.nukkit.player.Player;
import cn.nukkit.utils.Config;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.hgutils.HGUtils;
import pl.extollite.hungergames.command.CommandManager;
import pl.extollite.hungergames.game.Game;
import pl.extollite.hungergames.manager.Manager;

import java.util.LinkedList;
import java.util.List;

public class SetChestRefillTimeCommand extends CommandManager {

    public SetChestRefillTimeCommand() {
        super("hgasetchestrefilltime", "", "/hga setchestrefilltime <arena> <time % 30>");
        List<CommandParameter[]> parameters = new LinkedList<>();
        parameters.add(new CommandParameter[]{
                new CommandParameter("Arena Name", CommandParamType.STRING, false),
        });
        this.setCommandParameters(parameters);
        Permission permission = new Permission("hg.admin.command.setchestrefilltime");
        HG.getInstance().getServer().getPluginManager().addPermission(permission);
        this.setPermission(permission.getName());
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if(args.length < 2)
            return false;
        if(sender instanceof Player){
            Player p = (Player)sender;
            Game g = Manager.getGame(args[0]);
            int time = Integer.parseInt(args[1]);
            if(time %30 != 0){
                HGUtils.sendMessage(p, "&c<time> must be in increments of 30");
                return true;
            }
            if(g == null){
                HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_delete_noexist());
                return false;
            }
            Config arenas = new Config(HG.getInstance().getDataFolder()+"/arenas.yml", Config.YAML);
            g.setChestRefillTime(time);
            arenas.set("arenas."+g.getName()+".chest-refill", time);
            arenas.save();
            HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_chest_refill().replace("%arena%", g.getName()).replace("%sec%", String.valueOf(time)));
        }
        return true;
    }
}
