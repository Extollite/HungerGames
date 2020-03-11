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
import pl.extollite.hungergames.manager.Manager;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AddSpawnCommand extends CommandManager {

    public AddSpawnCommand() {
        super("hgaaddspawn", "", "/hga addspawn");
        List<CommandParameter[]> parameters = new LinkedList<>();
        this.setCommandParameters(parameters);
        Permission permission = new Permission("hg.admin.command.addspawn");
        HG.getInstance().getServer().getPluginManager().addPermission(permission);
        this.setPermission(permission.getName());
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player)sender;
            Game g = Manager.getGame(p.getLocation());
            Location loc = p.getLocation();
            if(g == null){
                HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_delete_noexist());
                return false;
            }
            Config arenas = new Config(HG.getInstance().getDataFolder()+"/arenas.yml", Config.YAML);
            for(Location spawn : g.getSpawns()){
                if(spawn.getBlock().getPosition().equals(loc.getBlock().getPosition())){
                    HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_spawn_same());
                    return true;
                }
            }
            g.addSpawn(loc);
            arenas.set("arenas."+g.getName()+".spawns."+g.getSpawns().size()+"s.level", loc.getLevel().getId());
            arenas.set("arenas."+g.getName()+".spawns."+g.getSpawns().size()+"s.x", loc.getFloorX());
            arenas.set("arenas."+g.getName()+".spawns."+g.getSpawns().size()+"s.y", loc.getFloorY());
            arenas.set("arenas."+g.getName()+".spawns."+g.getSpawns().size()+"s.z", loc.getFloorZ());
            arenas.save();
            HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_spawn_set().replace("%number%", String.valueOf(g.getSpawns().size())));
            Manager.checkGame(g, p);
        }
        return true;
    }
}
