package pl.extollite.hungergames.command.admin;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.permission.Permission;
import cn.nukkit.player.Player;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.HGUtils.HGUtils;
import pl.extollite.hungergames.command.CommandManager;
import pl.extollite.hungergames.data.ConfigData;
import pl.extollite.hungergames.data.WandLocations;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WandCommand extends CommandManager {
    
    public WandCommand() {
        super("hgawand", HG.getInstance().getLanguage().getCmd_admin_wand_description(), "/mh set");
        List<CommandParameter[]> parameters = new LinkedList<>();
        this.setCommandParameters(parameters);
        Permission permission = new Permission("hg.admin.command.wand");
        HG.getInstance().getServer().getPluginManager().addPermission(permission);
        this.setPermission(permission.getName());
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player)sender;
            if(HG.getInstance().getWandLocationsMap().containsKey(p.getServerId())){
                HGUtils.sendMessage(p, "&7Wand Disabled");
                p.getInventory().remove(ConfigData.wandItem);
                p.sendAllInventories();
                HG.getInstance().getWandLocationsMap().remove(p.getServerId());
                return true;
            }
            p.getInventory().addItem(ConfigData.wandItem);
            p.sendAllInventories();
            HGUtils.sendMessage(p, "&7Wand Enabled");
            HG.getInstance().getWandLocationsMap().put(p.getServerId(), new WandLocations(null, null));
        }
        return true;
    }
}
