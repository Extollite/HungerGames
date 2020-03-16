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

public class ListCommand extends CommandManager {

    public ListCommand() {
        super("hglist", "", "/hg list");
        List<CommandParameter[]> parameters = new LinkedList<>();
        this.setCommandParameters(parameters);
        Permission permission = new Permission("hg.command.list", null, Permission.DEFAULT_TRUE);
        HG.getInstance().getServer().getPluginManager().addPermission(permission);
        this.setPermission(permission.getName());
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!HG.getInstance().getPlayerManager().hasPlayerData(p) && !HG.getInstance().getPlayerManager().hasSpectatorData(p)) {
                HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_leave_not_in_game());
                return true;
            } else {
                Game g = HG.getInstance().getPlayerManager().getData(p).getGame();
                if (g != null) {
                    StringBuilder playerNames = new StringBuilder();
                    for (Player player : g.getPlayers()) {
                        playerNames.append("&6, &c").append(player.getName());
                    }
                    HGUtils.sendMessage(p, "&6Players:" + playerNames.substring(3));
                }
            }
        }
        return true;
    }
}
