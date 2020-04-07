package pl.extollite.hungergames.data;

import cn.nukkit.blockentity.Sign;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.gamerule.GameRules;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import com.nukkitx.math.vector.Vector3i;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.game.Bound;
import pl.extollite.hungergames.game.Game;
import pl.extollite.hungergames.manager.ItemManager;
import pl.extollite.hungergames.manager.KitManager;

import java.util.*;

public class ArenaData {
    public static void load(){
        Config arenas = new Config(HG.getInstance().getDataFolder()+"/arenas.yml", Config.YAML);
        if(arenas.exists("arenas")){
            for(String arena : arenas.getSection("arenas").getKeys(false)){
                boolean isReady = true;
                List<Vector3i> spawns = new ArrayList<>();
                Sign lobbysign;
                int timer;
                int minplayers;
                int maxplayers;
                Bound b;
                timer = arenas.getInt("arenas." + arena + ".info.timer");
                minplayers = arenas.getInt("arenas." + arena + ".info.min-players");
                maxplayers = arenas.getInt("arenas." + arena + ".info.max-players");
                Level lobby = HG.getInstance().getServer().getLevel(arenas.getString("arenas."+arena+".lobbysign.level"));
                lobbysign = (Sign)lobby.getBlockEntity(Vector3i.from(arenas.getInt("arenas."+arena+".lobbysign.x"), arenas.getInt("arenas."+arena+".lobbysign.y"), arenas.getInt("arenas."+arena+".lobbysign.z")));
                for(String spawn : arenas.getSection("arenas."+arena+".spawns").getKeys(false)){
                    spawns.add(Vector3i.from(arenas.getInt("arenas."+arena+".spawns."+spawn+".x"), arenas.getInt("arenas."+arena+".spawns."+spawn+".y"), arenas.getInt("arenas."+arena+".spawns."+spawn+".z")));
                }
                Level boundLevel = HG.getInstance().getServer().getLevel(arenas.getString("arenas."+arena+".bound.level"));
                boundLevel.setAutoSave(false);
                boundLevel.getGameRules().put(GameRules.DO_WEATHER_CYCLE, false);
                boundLevel.setThundering(false);
                boundLevel.setRaining(false);
                b = new Bound(
                        Location.from(arenas.getInt("arenas."+arena+".bound.x"), arenas.getInt("arenas."+arena+".bound.y"), arenas.getInt("arenas."+arena+".bound.z"), HG.getInstance().getServer().getLevel(arenas.getString("arenas."+arena+".bound.level"))),
                        Location.from(arenas.getInt("arenas."+arena+".bound.x2"), arenas.getInt("arenas."+arena+".bound.y2"), arenas.getInt("arenas."+arena+".bound.z2"), HG.getInstance().getServer().getLevel(arenas.getString("arenas."+arena+".bound.level")))
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

                if (arenas.exists("arenas." + arena + ".chest-refill")) {
                    int chestRefill = arenas.getInt("arenas." + arena + ".chest-refill");
                    game.setChestRefillTime(chestRefill);
                }

                if(arenas.getBoolean("arenas." + arena + ".random-chests.enabled", false)){
                    game.setMaxChestsInGame(arenas.getInt("arenas." + arena + ".random-chests.max-chests", 50));
                    game.setMinChestsInGame(arenas.getInt("arenas." + arena + ".random-chests.min-chests", 40));
                    game.setBonusChestChance(arenas.getInt("arenas." + arena + ".random-chests.bonusChance", 20));
                    List<Vector3i> chests = new LinkedList<>();
                    for(String pos : arenas.getSection("arenas."+arena+".random-chests.locations").getKeys(false)){
                        chests.add(Vector3i.from(arenas.getInt("arenas."+arena+".random-chests.locations."+pos+".x"), arenas.getInt("arenas."+arena+".random-chests.locations."+pos+".y"), arenas.getInt("arenas."+arena+".random-chests.locations."+pos+".z")));
                    }
                    game.setChestLocations(chests);
                    game.chooseChestLocations();
                }

                KitManager kitManager;
                if(arenas.getSection("arenas." + arena + ".kits") != null) {
                    kitManager = new KitManager();
                    kitManager.loadKits(arenas, "arenas." + arena + ".kits");
                }
                else
                    kitManager = HG.getInstance().getKitManager();
                game.setKitManager(kitManager);
            }
        }
        else{
            HG.getInstance().getLogger().info("No Arenas to load!");
        }
    }
}
