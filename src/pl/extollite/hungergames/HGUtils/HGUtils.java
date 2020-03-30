package pl.extollite.hungergames.hgutils;

import cn.nukkit.command.CommandSender;
import cn.nukkit.player.Player;
import cn.nukkit.utils.TextFormat;
import pl.extollite.hungergames.HG;

import java.util.*;

public class HGUtils {

    public static void sendMessage(CommandSender sender, String message){
        sender.sendMessage(TextFormat.colorize('&', HG.getInstance().getLanguage().getPrefix()+message));
    }

    public static void sendTip(Player player, String message){
       player.sendTip(TextFormat.colorize('&', message));
    }

    public static void sendTitle(Player player, String message){
        player.sendTitle(TextFormat.colorize('&', message), "", 5, 10, 5);
    }

    public static String colorize(String message){
        return TextFormat.colorize('&', message);
    }

    public static void broadcast(String message){
        HG.getInstance().getServer().broadcastMessage(TextFormat.colorize('&', HG.getInstance().getLanguage().getPrefix()+message));
    }

    public static String translateStop(List<String> win) {
        StringBuilder bc = null;
        int count = 0;
        for (String s : win) {
            count++;
            if (count == 1) bc = new StringBuilder(s);
            else if (count == win.size()) {
                if(bc == null)
                    continue;
                bc.append(", and ").append(s);
            } else {
                if(bc == null)
                    continue;
                bc.append(", ").append(s);
            }
        }
        if (bc != null)
            return bc.toString();
        else
            return "No one";
    }

    public static List<String> convertPlayerListToStringList(List<Player> players) {
        List<String> winners = new ArrayList<>();
        for (Player player : players) {
            winners.add(player.getName());
        }
        return winners;
    }
}
