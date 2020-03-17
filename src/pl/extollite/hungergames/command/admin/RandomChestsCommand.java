package pl.extollite.hungergames.command.admin;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.permission.Permission;
import cn.nukkit.player.Player;
import cn.nukkit.utils.Config;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.command.CommandManager;
import pl.extollite.hungergames.game.Game;
import pl.extollite.hungergames.hgutils.HGUtils;
import pl.extollite.hungergames.manager.Manager;

import java.util.LinkedList;
import java.util.List;

public class RandomChestsCommand extends CommandManager {

    public RandomChestsCommand() {
        super("hgarandomchests", "", "/hga randomchests <arena> <min-chests> <max-chests> <bonus-chance>");
        List<CommandParameter[]> parameters = new LinkedList<>();
        parameters.add(new CommandParameter[]{
                new CommandParameter("Arena Name", CommandParamType.STRING, false),
                new CommandParameter("Min Chests", CommandParamType.INT, false),
                new CommandParameter("Max Chests", CommandParamType.INT, false),
                new CommandParameter("Bonus Chest Chance", CommandParamType.INT, false),
        });
        this.setCommandParameters(parameters);
        Permission permission = new Permission("hg.admin.command.randomchests");
        HG.getInstance().getServer().getPluginManager().addPermission(permission);
        this.setPermission(permission.getName());
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length < 4)
            return false;
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Game g = Manager.getGame(args[0]);
            if (g == null) {
                HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_delete_noexist());
                return false;
            }
            int min = 0;
            int max = 0;
            int chance = 0;
            try {
                min = Integer.parseInt(args[1]);
                max = Integer.parseInt(args[2]);
                chance = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                return false;
            }
            if (min > max) {
                HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_create_minmax());
                return false;
            }

            Config arenas = new Config(HG.getInstance().getDataFolder() + "/arenas.yml", Config.YAML);
            g.setMaxChestsInGame(max);
            g.setMinChestsInGame(min);
            g.setBonusChestChance(chance);
            arenas.set("arenas." + g.getName() + ".random-chests.enabled", true);
            arenas.set("arenas." + g.getName() + ".random-chests.max-chests", max);
            arenas.set("arenas." + g.getName() + ".random-chests.min-chests", min);
            arenas.set("arenas." + g.getName() + ".random-chests.bonusChance", chance);
            arenas.save();
            HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_chestrandom_set().replace("%arena%", g.getName()));
        }
        return true;
    }
}
