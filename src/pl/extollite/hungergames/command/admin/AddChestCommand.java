package pl.extollite.hungergames.command.admin;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockIds;
import cn.nukkit.block.BlockSignPost;
import cn.nukkit.blockentity.Sign;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Location;
import cn.nukkit.permission.Permission;
import cn.nukkit.player.Player;
import cn.nukkit.utils.Config;
import com.nukkitx.math.vector.Vector3i;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.command.CommandManager;
import pl.extollite.hungergames.game.Game;
import pl.extollite.hungergames.hgutils.HGUtils;
import pl.extollite.hungergames.manager.Manager;

import java.util.LinkedList;
import java.util.List;

public class AddChestCommand extends CommandManager {

    public AddChestCommand() {
        super("hgaaddchest", "", "/hga addchest");
        List<CommandParameter[]> parameters = new LinkedList<>();
        this.setCommandParameters(parameters);
        Permission permission = new Permission("hg.admin.command.addchest");
        HG.getInstance().getServer().getPluginManager().addPermission(permission);
        this.setPermission(permission.getName());
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player)sender;
            Game g = Manager.getGame(p.getLocation());
            if(g == null){
                HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_delete_noexist());
                return false;
            }
            Block b = p.getTargetBlock(3);
            if(b == null || b.getId().equals(BlockIds.AIR))
                return false;
            for(Vector3i chest : g.getChestLocations()){
                if(chest.equals(b.getPosition())){
                    HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_chestrandom_same());
                    return true;
                }
            }
            g.addChestLocation(b.getPosition());
            Config arenas = new Config(HG.getInstance().getDataFolder()+"/arenas.yml", Config.YAML);
            int chestNumber = g.getChestLocations().size();
            arenas.set("arenas."+g.getName()+".random-chests.locations."+chestNumber+"s.x", b.getX());
            arenas.set("arenas."+g.getName()+".random-chests.locations."+chestNumber+"s.y", b.getY());
            arenas.set("arenas."+g.getName()+".random-chests.locations."+chestNumber+"s.z", b.getZ());
            arenas.save();
            HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_chestrandom_added().replace("%number%", String.valueOf(chestNumber)));
        }
        return true;
    }
}
