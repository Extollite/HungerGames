package pl.extollite.hungergames.data;

import cn.nukkit.blockentity.Sign;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.utils.Config;
import com.nukkitx.math.vector.Vector3i;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.game.Bound;
import pl.extollite.hungergames.game.Game;

import java.util.*;

public class ArenaData {
    public static void load(){
        Config arenas = new Config(HG.getInstance().getDataFolder()+"/arenas.yml", Config.YAML);
        if(arenas.exists("arenas")){
            //new CompassTask();
            for(String arena : arenas.getKeys(false)){
                boolean isReady = true;
                List<Location> spawns = new ArrayList<>();
                Sign lobbysign;
                int timer;
                int minplayers;
                int maxplayers;
                Bound b;
                List<String> commands;
                timer = arenas.getInt("arenas." + arena + ".info.timer");
                minplayers = arenas.getInt("arenas." + arena + ".info.min-players");
                maxplayers = arenas.getInt("arenas." + arena + ".info.max-players");
                Level lobby = HG.getInstance().getServer().getLevelByName(arenas.getString("arenas."+arena+".lobbysign.level"));
                lobbysign = (Sign)lobby.getBlockEntity(Vector3i.from(arenas.getInt("arenas."+arena+".lobbysign.x"), arenas.getInt("arenas."+arena+".lobbysign.y"), arenas.getInt("arenas."+arena+".lobbysign.z")));
                for(String spawn : arenas.getSection("arenas."+arena+".spawns").getKeys(false)){
                    Level gameLevel = HG.getInstance().getServer().getLevelByName(arenas.getString("arenas."+arena+".spawns."+spawn+".level"));
                    gameLevel.setAutoSave(false);
                    spawns.add(Location.from(arenas.getInt("arenas."+arena+".spawns."+spawn+".x"), arenas.getInt("arenas."+arena+".spawns."+spawn+".y"), arenas.getInt("arenas."+arena+".spawns."+spawn+".z"), gameLevel));
                }
                b = new Bound(
                        Location.from(arenas.getInt("arenas."+arena+".b1.x"), arenas.getInt("arenas."+arena+".b1.y"), arenas.getInt("arenas."+arena+".b1.z"), HG.getInstance().getServer().getLevelByName(arenas.getString("arenas."+arena+".b1.level"))),
                        Location.from(arenas.getInt("arenas."+arena+".b2.x"), arenas.getInt("arenas."+arena+".b2.y"), arenas.getInt("arenas."+arena+".b2.z"), HG.getInstance().getServer().getLevelByName(arenas.getString("arenas."+arena+".b2.level")))
                        );
                Game game = new Game(arena, b, spawns, lobbysign, timer, minplayers, maxplayers, ConfigData.freeRoam, true);
                HG.getInstance().getGames().add(game);

                if (!arenas.getStringList("arenas." + arena + ".items").isEmpty()) {
                    Map<Integer, Item> items = new HashMap<>();
                    for (String itemString : arenas.getStringList("arenas." + arena + ".items")) {
                        RandomItems.loadItems(itemString, items);
                    }
                    game.setItems(items);
                }

                if (!arenas.getStringList("arenas." + arena + ".bonus").isEmpty()) {
                    Map<Integer, Item> items = new HashMap<>();
                    for (String itemString : arenas.getStringList("arenas." + arena + ".bonus")) {
                        RandomItems.loadItems(itemString, items);
                    }
                    game.setBonusItems(items);
                }

                if (arenas.exists("arenas." + arena + ".final.countdown-start") &&
                        arenas.exists("arenas." + arena + ".final.countdown-end")) {
                    int finalCountdownStart = arenas.getInt("arenas." + arena + ".final.countdown-start");
                    int finalCountdownEnd = arenas.getInt("arenas." + arena + ".final.countdown-end");
                    game.setFinalCountdownStart(finalCountdownStart);
                    game.setFinalCountdownEnd(finalCountdownEnd);
                }

                if (arenas.isList("arenas." + arena + ".commands")) {
                    commands = arenas.getStringList("arenas." + arena + ".commands");
                } else {
                    arenas.set("arenas." + arena + ".commands", Collections.singletonList("none"));
                    commands = Collections.singletonList("none");
                }
                game.setCommands(commands);

                if (arenas.exists("arenas." + arena + ".chest-refill")) {
                    int chestRefill = arenas.getInt("arenas." + arena + ".chest-refill");
                    game.setChestRefillTime(chestRefill);
                }
            }
        }
        else{
            HG.getInstance().getLogger().info("No Arenas to load!");
        }
    }
}
