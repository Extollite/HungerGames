package pl.extollite.hungergames.command.admin;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSignPost;
import cn.nukkit.block.BlockWallSign;
import cn.nukkit.blockentity.Sign;
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
import pl.extollite.hungergames.game.Game;
import pl.extollite.hungergames.manager.Manager;

import java.util.LinkedList;
import java.util.List;

public class SetSignCommand extends CommandManager {

    public SetSignCommand() {
        super("hgasetsign", "", "/hga setsign <arena_name>");
        List<CommandParameter[]> parameters = new LinkedList<>();
        parameters.add(new CommandParameter[]{
                new CommandParameter("Arena Name", CommandParamType.STRING, false),
        });
        this.setCommandParameters(parameters);
        Permission permission = new Permission("hg.admin.command.setsign");
        HG.getInstance().getServer().getPluginManager().addPermission(permission);
        this.setPermission(permission.getName());
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player)sender;
            Game g = Manager.getGame(args[0]);
            if(g == null)
                return false;
            Block b = p.getTargetBlock(6);
            if(!(b instanceof BlockSignPost))
                return false;
            g.setLobbyBlock((Sign)p.getLevel().getBlockEntity(b.getPosition()));
            Config arenas = new Config(HG.getInstance().getDataFolder()+"/arenas.yml", Config.YAML);
            arenas.set("arenas."+g.getName()+".lobbysign.level", b.getLevel().getName());
            arenas.set("arenas."+g.getName()+".lobbysign.x", b.getX());
            arenas.set("arenas."+g.getName()+".lobbysign.y", b.getY());
            arenas.set("arenas."+g.getName()+".lobbysign.z", b.getZ());
            arenas.save();
            HGUtils.sendMessage(p, HG.getInstance().getLanguage().getCmd_sign_set());
            Manager.checkGame(g, p);
        }
        return true;
    }
}
