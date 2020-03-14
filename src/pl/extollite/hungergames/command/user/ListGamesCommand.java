package pl.extollite.hungergames.command.user;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.permission.Permission;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.HGUtils.HGUtils;
import pl.extollite.hungergames.command.CommandManager;
import pl.extollite.hungergames.game.Game;

import java.util.LinkedList;
import java.util.List;


public class ListGamesCommand extends CommandManager {

    public ListGamesCommand() {
        super("hglistgames", "", "/hg listgames");
        List<CommandParameter[]> parameters = new LinkedList<>();
        this.setCommandParameters(parameters);
        Permission permission = new Permission("hg.command.listgames", null, Permission.DEFAULT_TRUE);
        HG.getInstance().getServer().getPluginManager().addPermission(permission);
        this.setPermission(permission.getName());
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        HGUtils.sendMessage(sender, "&6&l Games:");
        for(Game g : HG.getInstance().getGames())
            HGUtils.sendMessage(sender, " &4 - &6" + g.getName() + "&4:&6" + g.getStatus().getName());
        return true;
    }
}
