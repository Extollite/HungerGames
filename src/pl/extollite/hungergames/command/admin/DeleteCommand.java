package pl.extollite.hungergames.command.admin;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSignPost;
import cn.nukkit.blockentity.Sign;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.permission.Permission;
import cn.nukkit.player.Player;
import cn.nukkit.utils.Config;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.HGUtils.HGUtils;
import pl.extollite.hungergames.command.CommandManager;
import pl.extollite.hungergames.game.Game;
import pl.extollite.hungergames.game.Status;
import pl.extollite.hungergames.manager.Manager;

import java.util.LinkedList;
import java.util.List;

public class DeleteCommand extends CommandManager {

    public DeleteCommand() {
        super("hgadelete", "", "/hga delete <arena_name>");
        List<CommandParameter[]> parameters = new LinkedList<>();
        parameters.add(new CommandParameter[]{
                new CommandParameter("Arena Name", CommandParamType.STRING, false),
        });
        this.setCommandParameters(parameters);
        Permission permission = new Permission("hg.admin.command.delete");
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
            try{
                HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_delete_attempt().replace("%arena%", g.getName()));
                if (g.getStatus() == Status.BEGINNING || g.getStatus() == Status.FINAL || g.getStatus() == Status.FINAL_COUNTDOWN || g.getStatus() == Status.RUNNING) {
                    HGUtils.sendMessage(sender, "  &7- &cGame running! &aStopping..");
                    g.forceRollback();
                    g.stop(false);
                }
                if(!g.getPlayers().isEmpty()){
                    HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_delete_kicking());
                    for(Player player : g.getPlayers()){
                        g.leave(player, false);
                    }
                }
                Config arenas = new Config(HG.getInstance().getDataFolder()+"/arenas.yml", Config.YAML);
                arenas.set("arenas"+g.getName(), null);
                arenas.save();
                HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_delete_deleted().replace("%arena%", g.getName()));
                HG.getInstance().getGames().remove(g);
            }
            catch (Exception e){
                HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_delete_failed());
            }
        }
        return true;
    }
}
