package pl.extollite.hungergames.command.admin;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.permission.Permission;
import cn.nukkit.player.Player;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.hgutils.HGUtils;
import pl.extollite.hungergames.command.CommandManager;
import pl.extollite.hungergames.game.Game;
import pl.extollite.hungergames.game.Status;
import pl.extollite.hungergames.manager.Manager;

import java.util.LinkedList;
import java.util.List;

public class ChestRefillNowCommand extends CommandManager {

    public ChestRefillNowCommand() {
        super("hgachestrefillnow", "", "/hga chestrefillnow <name>");
        List<CommandParameter[]> parameters = new LinkedList<>();
        parameters.add(new CommandParameter[]{
                new CommandParameter("Arena Name", CommandParamType.STRING, false),
        });
        this.setCommandParameters(parameters);
        Permission permission = new Permission("hg.admin.command.chestrefillnow");
        HG.getInstance().getServer().getPluginManager().addPermission(permission);
        this.setPermission(permission.getName());
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if(args.length < 1)
            return false;
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Game g = Manager.getGame(args[0]);
            if (g == null) {
                HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_delete_noexist());
                return false;
            }
            if (g.getStatus() != Status.RUNNING) {
                HGUtils.sendMessage(p, HG.getInstance().getLanguage().getListener_not_running());
                return true;
            }
            g.refillChests();
            HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_chest_refill_now().replace("%arena%", g.getName()));
        }
        return true;
    }
}
