package pl.extollite.hungergames.command.user;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.permission.Permission;
import cn.nukkit.player.Player;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.HGUtils.HGUtils;
import pl.extollite.hungergames.command.CommandManager;
import pl.extollite.hungergames.game.Game;
import pl.extollite.hungergames.game.Status;
import pl.extollite.hungergames.manager.Manager;

import java.util.LinkedList;
import java.util.List;

public class JoinCommand extends CommandManager {

    public JoinCommand() {
        super("hgjoin", "", "/hg join <name>");
        List<CommandParameter[]> parameters = new LinkedList<>();
        parameters.add(new CommandParameter[]{
                new CommandParameter("Arena Name", CommandParamType.STRING, false),
        });
        this.setCommandParameters(parameters);
        Permission permission = new Permission("hg.command.join", null, Permission.DEFAULT_TRUE);
        HG.getInstance().getServer().getPluginManager().addPermission(permission);
        this.setPermission(permission.getName());
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if(args.length < 1)
            return false;
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (HG.getInstance().getPlayerManager().hasPlayerData(p) || HG.getInstance().getPlayerManager().hasSpectatorData(p)) {
                HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_join_in_game());
            } else {
                Game g = Manager.getGame(args[0]);
                if (g != null && !g.getPlayers().contains(p)) {
                    g.join(p);
                } else {
                    HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_delete_noexist());
                }
            }
        }
        return true;
    }
}
