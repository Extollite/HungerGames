package pl.extollite.hungergames.command.user;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.permission.Permission;
import cn.nukkit.player.Player;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.hgutils.HGUtils;
import pl.extollite.hungergames.command.CommandManager;
import pl.extollite.hungergames.game.Game;

import java.util.LinkedList;
import java.util.List;

public class LeaveCommand extends CommandManager {

    public LeaveCommand() {
        super("hgleave", "", "/hg leave");
        List<CommandParameter[]> parameters = new LinkedList<>();
        this.setCommandParameters(parameters);
        Permission permission = new Permission("hg.command.leave", null, Permission.DEFAULT_TRUE);
        HG.getInstance().getServer().getPluginManager().addPermission(permission);
        this.setPermission(permission.getName());
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!HG.getInstance().getPlayerManager().hasPlayerData(p) && !HG.getInstance().getPlayerManager().hasSpectatorData(p)) {
                HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_leave_not_in_game());
                return true;
            } else if (HG.getInstance().getPlayerManager().hasSpectatorData(p)) {
                Game g = HG.getInstance().getPlayerManager().getData(p).getGame();
                if (g != null && !g.getSpectators().contains(p)) {
                    g.leaveSpectate(p);
                    HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_leave_left().replace("%arena%", g.getName()));
                }
            } else {
                Game g = HG.getInstance().getPlayerManager().getData(p).getGame();
                if (g != null && !g.getSpectators().contains(p)) {
                    g.leave(p, false);
                    HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_leave_left().replace("%arena%", g.getName()));
                }
            }
        }
        return true;
    }
}
