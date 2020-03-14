package pl.extollite.hungergames.command.admin;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Level;
import cn.nukkit.permission.Permission;
import cn.nukkit.player.Player;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.HGUtils.HGUtils;
import pl.extollite.hungergames.command.CommandManager;

import java.util.LinkedList;
import java.util.List;

public class TeleportWorldCommand extends CommandManager {

    public TeleportWorldCommand() {
        super("hgateleportworld", "", "/hga teleportworld <name>");
        List<CommandParameter[]> parameters = new LinkedList<>();
        parameters.add(new CommandParameter[]{
                new CommandParameter("Arena Name", CommandParamType.STRING, false),
        });
        this.setCommandParameters(parameters);
        Permission permission = new Permission("hg.admin.command.teleportworld");
        HG.getInstance().getServer().getPluginManager().addPermission(permission);
        this.setPermission(permission.getName());
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if(args.length < 1)
            return false;
        if (sender instanceof Player) {
            Player p = (Player)sender;
            Level level = HG.getInstance().getServer().getLevel(args[0]);
            if(level == null){
                HGUtils.sendMessage(p, "World dont exists!");
                return true;
            }
            p.teleport(level.getSafeSpawn());
        }
        return true;
    }
}
