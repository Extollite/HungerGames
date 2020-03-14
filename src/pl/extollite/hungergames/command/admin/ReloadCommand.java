package pl.extollite.hungergames.command.admin;

import cn.nukkit.block.BlockIds;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Location;
import cn.nukkit.math.BlockFace;
import cn.nukkit.permission.Permission;
import cn.nukkit.player.Player;
import cn.nukkit.utils.Config;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.HGUtils.HGUtils;
import pl.extollite.hungergames.command.CommandManager;
import pl.extollite.hungergames.data.ArenaData;
import pl.extollite.hungergames.data.ConfigData;
import pl.extollite.hungergames.data.RandomItems;
import pl.extollite.hungergames.game.Game;
import pl.extollite.hungergames.manager.Manager;

import java.util.LinkedList;
import java.util.List;

public class ReloadCommand extends CommandManager {

    public ReloadCommand() {
        super("hgareload", "", "/hga reload");
        List<CommandParameter[]> parameters = new LinkedList<>();
        this.setCommandParameters(parameters);
        Permission permission = new Permission("hg.admin.command.reload");
        HG.getInstance().getServer().getPluginManager().addPermission(permission);
        this.setPermission(permission.getName());
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        HGUtils.sendMessage(sender, HG.getInstance().getLanguage().getCmd_reload_attempt());
        HG.getInstance().stopAll();
        //plugin.getArenaConfig().saveCustomConfig();

        ConfigData.load(HG.getInstance().getConfig());
        HGUtils.sendMessage(sender, HG.getInstance().getLanguage().getCmd_reload_reloaded_config());

        HG.getInstance().getItems().clear();
        RandomItems.load();
        HGUtils.sendMessage(sender, HG.getInstance().getLanguage().getCmd_reload_reloaded_items());

        ArenaData.load();
        HGUtils.sendMessage(sender, HG.getInstance().getLanguage().getCmd_reload_reloaded_arena());

        HGUtils.sendMessage(sender, HG.getInstance().getLanguage().getCmd_reload_reloaded_success());
        return true;
    }
}
