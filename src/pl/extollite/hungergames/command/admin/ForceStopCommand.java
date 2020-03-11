package pl.extollite.hungergames.command.admin;

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

public class ForceStopCommand extends CommandManager {

    public ForceStopCommand() {
        super("hgaforcestop", "", "/hga forcestop <name>");
        List<CommandParameter[]> parameters = new LinkedList<>();
        parameters.add(new CommandParameter[]{
                new CommandParameter("Arena Name", CommandParamType.STRING, false),
        });
        this.setCommandParameters(parameters);
        Permission permission = new Permission("hg.admin.command.forcestop");
        HG.getInstance().getServer().getPluginManager().addPermission(permission);
        this.setPermission(permission.getName());
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if(args.length < 1)
            return false;
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args[0].equalsIgnoreCase("all")) {
                for (Game game : HG.getInstance().getGames()) {
                    Status status = game.getStatus();
                    if (status == Status.RUNNING || status == Status.WAITING || status == Status.BEGINNING || status == Status.COUNTDOWN || status == Status.FINAL || status == Status.FINAL_COUNTDOWN) {
                        game.stop(false);
                    }
                }
                HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_stop_all());
                return true;
            }
            Game g = Manager.getGame(args[0]);
            if (g == null) {
                HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_stop_noexist());
                return false;
            }
            g.stop(false);
            HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_stop_arena());
        }
        return true;
    }
}
