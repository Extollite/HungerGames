package pl.extollite.hungergames.command.user;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.permission.Permission;
import cn.nukkit.player.Player;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.command.CommandManager;
import pl.extollite.hungergames.form.KitMenuWindow;
import pl.extollite.hungergames.game.Game;
import pl.extollite.hungergames.game.Status;
import pl.extollite.hungergames.hgutils.HGUtils;

import java.util.LinkedList;
import java.util.List;

public class KitCommand extends CommandManager {

    public KitCommand() {
        super("hgkit", "", "/hg kit");
        List<CommandParameter[]> parameters = new LinkedList<>();
        this.setCommandParameters(parameters);
        Permission permission = new Permission("hg.command.kit", null, Permission.DEFAULT_TRUE);
        HG.getInstance().getServer().getPluginManager().addPermission(permission);
        this.setPermission(permission.getName());
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!HG.getInstance().getPlayerManager().hasPlayerData(p)) {
                HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_leave_not_in_game());
                return true;
            } else {
                Game g = HG.getInstance().getPlayerManager().getData(p).getGame();
                if (g != null && (g.getStatus() == Status.COUNTDOWN || g.getStatus() == Status.WAITING) && g.getKitManager() != null) {
                    p.showFormWindow(new KitMenuWindow(p, g));
                }
                else
                    return false;
            }
        }
        return true;
    }
}
