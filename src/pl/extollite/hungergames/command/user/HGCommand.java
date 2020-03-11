package pl.extollite.hungergames.command.user;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.*;
import cn.nukkit.permission.Permission;
import cn.nukkit.player.Player;
import cn.nukkit.utils.TextFormat;
import com.nukkitx.protocol.bedrock.data.CommandEnumData;
import com.nukkitx.protocol.bedrock.data.CommandParamData;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.HGUtils.HGUtils;
import pl.extollite.hungergames.command.CommandManager;

import java.util.*;

public class HGCommand extends CommandManager {

    private final Map<String, Command> commands;

    public HGCommand() {
        super("hg", "", "/hg <command>");

        this.setAliases(new String[]{"hungergames"});
        Permission permission = new Permission("hg.command", null, Permission.DEFAULT_TRUE);
        HG.getInstance().getServer().getPluginManager().addPermission(permission);
        this.setPermission(permission.getName());
        this.commands = new HashMap<>();
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (!this.testPermissionSilent(sender)) {
            HGUtils.sendMessage(sender, HG.getInstance().getLanguage().getCmd_no_permission());
            return false;
        }
        if (args.length < 1) {
            sendUsage(sender);
            return false;
        }
        Command cmd = this.commands.get(args[0]);
        if (cmd == null) {
            sendUsage(sender);
            return false;
        }
        String[] newArgs = args.length == 1 ? new String[0] : Arrays.copyOfRange(args, 1, args.length);
        cmd.execute(sender, cmd.getName(), newArgs);
        return false;
    }

    private void updateArguments() {
        List<CommandParameter[]> params = new LinkedList<>();
        this.commands.forEach((k, v) -> {
            List<CommandParameter> p = new ArrayList<>();
            p.add(new CommandParameter(k, false, new String[]{k}));
            v.getCommandParameters().forEach(s -> p.addAll(Arrays.asList(s)));
            params.add(p.toArray(new CommandParameter[0]));
        });
        this.setCommandParameters(params);
    }

    public void registerCommand(Command command) {
        this.commands.put(command.getName().replace("hg", "").replace("hungergames", "").toLowerCase(), command);
        this.updateArguments();
    }

    static public void sendUsage(CommandSender sender) {
        sender.sendMessage(TextFormat.GREEN + "-- HG " + HG.getInstance().getDescription().getVersion() + " Commands --");
        for(String message : HG.getInstance().getLanguage().getCmd_usage()){
            sender.sendMessage(TextFormat.colorize('&', message));
        }
    }

}
