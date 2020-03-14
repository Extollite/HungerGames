package pl.extollite.hungergames.command.user;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.permission.Permission;
import cn.nukkit.player.Player;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.HGUtils.HGUtils;
import pl.extollite.hungergames.command.CommandManager;
import pl.extollite.hungergames.form.SpectatorWindow;
import pl.extollite.hungergames.game.Game;

import java.util.LinkedList;
import java.util.List;

public class TpCommand extends CommandManager {

    public TpCommand() {
        super("hgtp", "", "/hg tp");
        List<CommandParameter[]> parameters = new LinkedList<>();
        this.setCommandParameters(parameters);
        Permission permission = new Permission("hg.command.tp", null, Permission.DEFAULT_TRUE);
        HG.getInstance().getServer().getPluginManager().addPermission(permission);
        this.setPermission(permission.getName());
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!HG.getInstance().getPlayerManager().hasSpectatorData(p)) {
                HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_leave_not_in_game());
                return true;
            } else {
                Game g = HG.getInstance().getPlayerManager().getData(p).getGame();
                if (g != null && g.getSpectators().contains(p)) {
                    p.showFormWindow(new SpectatorWindow(g));
                }
            }
        }
        return true;
    }
}
