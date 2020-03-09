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

/*    @Override
    public com.nukkitx.protocol.bedrock.data.CommandData generateCustomCommandData(Player player) {
        if (!this.testPermission(player)) {
            return null;
        } else {
            String[] aliasesEnum;
            if (this.getAliases().length > 0) {
                Set<String> aliasList = new HashSet();
                Collections.addAll(aliasList, this.getAliases());
                aliasList.add(this.getName().toLowerCase());
                aliasList.add("hungergames");
                aliasesEnum = aliasList.toArray(new String[0]);
            } else {
                aliasesEnum = new String[]{this.getName().toLowerCase()};
            }

            CommandEnumData aliases = new CommandEnumData(this.getName().toLowerCase() + "Aliases", aliasesEnum, false);
            String description = this.getDescription();
            CommandParamData[][] overloads = new CommandParamData[this.commandParameters.size()][];

            for(int i = 0; i < overloads.length; ++i) {
                CommandParameter[] parameters = (CommandParameter[])this.commandParameters.get(i);
                CommandParamData[] params = new CommandParamData[parameters.length];

                for(int i2 = 0; i2 < parameters.length; ++i2) {
                    params[i2] = Command.toNetwork(parameters[i2]);
                }

                overloads[i] = params;
            }

            return new com.nukkitx.protocol.bedrock.data.CommandData(this.name.toLowerCase(), description, Collections.emptyList(), (byte)0, aliases, overloads);
        }

        if (!this.testPermission(player)) {
            return null;
        }

        CommandData customData = this.commandData.clone();

        List<String> aliases = new ArrayList<>();
        aliases.add("hg");
        aliases.add("hungergames");

        customData.aliases = new CommandEnum("HungerGamesAliases", aliases);

        customData.description = player.getServer().getLanguage().translateString(this.getDescription());
        this.commandParameters.forEach((par) -> {
            if (par.length > 0 && this.commands.get(par[0].name).testPermissionSilent(player)) {
                CommandOverload overload = new CommandOverload();
                overload.input.parameters = par;
                customData.overloads.put(par[0].name, overload);
            }
        });
        if (customData.overloads.size() == 0) customData.overloads.put("default", new CommandOverload());
        return new com.nukkitx.protocol.bedrock.data.CommandData(this.getName().toLowerCase(), description, Collections.emptyList(), (byte)0, aliases, this.getOverloads());
    }*/

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
