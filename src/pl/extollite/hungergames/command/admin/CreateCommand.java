package pl.extollite.hungergames.command.admin;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Location;
import cn.nukkit.permission.Permission;
import cn.nukkit.player.Player;
import cn.nukkit.utils.Config;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.HGUtils.HGUtils;
import pl.extollite.hungergames.command.CommandManager;
import pl.extollite.hungergames.data.ConfigData;
import pl.extollite.hungergames.data.WandLocations;
import pl.extollite.hungergames.game.Bound;
import pl.extollite.hungergames.game.Game;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CreateCommand extends CommandManager {

    public CreateCommand() {
        super("hgacreate", "", "/hga create <name> <min-players> <max-players> <time>");
        List<CommandParameter[]> parameters = new LinkedList<>();
        parameters.add(new CommandParameter[]{
                new CommandParameter("Arena Name", CommandParamType.STRING, false),
                new CommandParameter("Min Players", CommandParamType.INT, false),
                new CommandParameter("Max Players", CommandParamType.INT, false),
                new CommandParameter("Time", CommandParamType.INT, false),
        });
        this.setCommandParameters(parameters);
        Permission permission = new Permission("hg.admin.command.create");
        HG.getInstance().getServer().getPluginManager().addPermission(permission);
        this.setPermission(permission.getName());
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player)sender;
            if(!HG.getInstance().getWandLocationsMap().containsKey(p.getServerId())){
                HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_create_need_selection());
                return true;
            }
            WandLocations wandLocations = HG.getInstance().getWandLocationsMap().get(p.getServerId());
            if(!wandLocations.hasValidSelection()){
                HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_create_need_selection());
                return true;
            }
            int min = 0;
            int max = 0;
            int time = 0;
            try {
                min = Integer.parseInt(args[1]);
                max = Integer.parseInt(args[2]);
                time = Integer.parseInt(args[3]);
            }
            catch(NumberFormatException e){
                return false;
            }
            if(min > max){
                HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_create_minmax());
                return false;
            }
            if(time %30 != 0){
                HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_create_divisible_1());
                HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_create_divisible_2());
                return true;
            }
            Config arenas = new Config(HG.getInstance().getDataFolder()+"/arenas.yml", Config.YAML);
            Location loc1 = wandLocations.getLoc1();
            Location loc2 = wandLocations.getLoc2();
            arenas.set("arenas." + args[0] +".bound.world", loc1.getLevel().getName());
            arenas.set("arenas." + args[0] +".bound.x", loc1.getFloorX());
            arenas.set("arenas." + args[0] +".bound.y", loc1.getFloorY());
            arenas.set("arenas." + args[0] +".bound.z", loc1.getFloorZ());
            arenas.set("arenas." + args[0] +".bound.x2", loc2.getFloorX());
            arenas.set("arenas." + args[0] +".bound.y2", loc2.getFloorY());
            arenas.set("arenas." + args[0] +".bound.z2", loc2.getFloorZ());
            arenas.set("arenas." + args[0] +".info.timer", time);
            arenas.set("arenas." + args[0] +".info.min-players", min);
            arenas.set("arenas." + args[0] +".info.max-players", max);
            arenas.set("arenas." + args[0] + ".commands", Collections.singletonList("none"));
            arenas.save();
            Bound b = new Bound(loc1, loc2);
            HG.getInstance().getGames().add(new Game(args[0], b, time, min, max, ConfigData.freeRoam));
            HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_create_created().replace("%arena%", args[0]));
        }
        return true;
    }
}
