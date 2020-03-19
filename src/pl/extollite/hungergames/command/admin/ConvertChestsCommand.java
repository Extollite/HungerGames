package pl.extollite.hungergames.command.admin;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSignPost;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.Sign;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.inventory.InventoryHolder;
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

public class ConvertChestsCommand extends CommandManager {

    public ConvertChestsCommand() {
        super("hgaconvertchests", "", "/hga convertchests <arena_name>");
        List<CommandParameter[]> parameters = new LinkedList<>();
        parameters.add(new CommandParameter[]{
                new CommandParameter("Arena Name", CommandParamType.STRING, false),
        });
        this.setCommandParameters(parameters);
        Permission permission = new Permission("hg.admin.command.convertchests");
        HG.getInstance().getServer().getPluginManager().addPermission(permission);
        this.setPermission(permission.getName());
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if(args.length < 1)
            return false;
        if(sender instanceof Player){
            Player p = (Player)sender;
            Game g = Manager.getGame(args[0]);
            if(g == null){
                HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_delete_noexist());
                return false;
            }
            Config arenas = new Config(HG.getInstance().getDataFolder()+"/arenas.yml", Config.YAML);
            for(BlockEntity blockEntity : g.getBound().getLevel().getBlockEntities()){
                if(blockEntity instanceof InventoryHolder && !g.getChestLocations().contains(blockEntity.getBlock().getPosition())){
                    Block b = blockEntity.getBlock();
                    g.addChestLocation(b.getPosition());
                    int chestNumber = g.getChestLocations().size();
                    arenas.set("arenas."+g.getName()+".random-chests.locations."+chestNumber+"s.x", b.getX());
                    arenas.set("arenas."+g.getName()+".random-chests.locations."+chestNumber+"s.y", b.getY());
                    arenas.set("arenas."+g.getName()+".random-chests.locations."+chestNumber+"s.z", b.getZ());
                    HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_chestrandom_added().replace("%number%", String.valueOf(chestNumber)));
                }
            }
            arenas.save();
        }
        return true;
    }
}
