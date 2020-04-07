package pl.extollite.hungergames.data;

import cn.nukkit.block.Block;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemIds;
import cn.nukkit.level.Location;
import cn.nukkit.network.protocol.types.ContainerIds;
import cn.nukkit.potion.Effect;
import cn.nukkit.registry.RegistryException;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.Identifier;
import cn.nukkit.utils.TextFormat;
import com.nukkitx.protocol.bedrock.data.ContainerType;
import lombok.Getter;
import pl.extollite.hungergames.hgutils.HGItemIds;
import pl.extollite.hungergames.hgutils.HGUtils;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.manager.ItemManager;
import pl.extollite.hungergames.manager.KitManager;
import sun.jvm.hotspot.utilities.HeapHprofBinWriter;

import java.util.*;

@Getter
public class ConfigData {
    public static String lang;

    public static boolean only_main_commands;

    public static Item wandItem;

    public static boolean bossbar;
    public static int maxchestcontent;
    public static int minchestcontent;
    public static int maxbonuscontent;
    public static int minbonuscontent;
    public static boolean teleportEnd;
    public static int teleportEndTime;
    public static Location globalExit;
    public static int freeRoam;
    public static boolean hide_player_name;

    //Rollback config info
    public static boolean breakblocks;
    public static List<Identifier> blocks = new ArrayList<>();
    public static boolean protectCooldown;
    public static boolean preventtrample;

    //Final
    public static boolean finalEnabled;
    public static boolean centerSpawn;
    public static int finalCountdownStart;
    public static int finalCountdownEnd;
    public static int finalRadius;
    public static int finalFreeze;


    //Spectate
    public static boolean spectateEnabled;
    public static boolean spectateOnDeath;
    public static boolean spectateFly;
    public static boolean spectateChat;


    public static void load(Config config){
        lang = config.getString("lang", "en_En");

        only_main_commands = config.getBoolean("only-main-commands");

        Identifier id = Identifier.fromString(config.getString("wand.id"));
        try{
            wandItem = Item.get(id);
        }
        catch (RegistryException ignored){
            HG.getInstance().getLogger().error(TextFormat.RED+"Unknown wand id, using blaze_rod!");
            wandItem = Item.get( ItemIds.BLAZE_ROD);
        }
        String[] lore = config.getStringList("wand.lore").toArray(new String[0]);
        for(int i = 0; i < lore.length; i++){
            lore[i] = HGUtils.colorize(lore[i]);
        }
        wandItem = wandItem.setLore(lore);
        wandItem = wandItem.setCustomName(HGUtils.colorize(config.getString("wand.name")));
        wandItem.setTag(wandItem.getTag().toBuilder().intTag("HGItemID", HGItemIds.WAND.getId()).buildRootTag());


        bossbar = config.getBoolean("settings.bossbar-countdown");
        maxchestcontent = config.getInt("settings.max-chestcontent");
        minchestcontent = config.getInt("settings.min-chestcontent");
        maxbonuscontent = config.getInt("settings.max-bonus-chestcontent");
        minbonuscontent = config.getInt("settings.min-bonus-chestcontent");
        teleportEnd = config.getBoolean("settings.teleport-at-end");
        teleportEndTime = config.getInt("settings.teleport-at-end-time");
        freeRoam = config.getInt("settings.free-roam");
        hide_player_name = config.getBoolean("settings.hide-player-name");

        breakblocks = config.getBoolean("rollback.allow-block-break");
        for(String name : config.getStringList("rollback.editable-blocks")){
            id = Identifier.fromString(name.toLowerCase());
            try{
                Block.get(id);
                blocks.add(id);
            }
            catch (RegistryException ignored){
                HG.getInstance().getLogger().error(TextFormat.RED+"Unknown block id: "+name+", skipping!");
            }
        }
        protectCooldown = config.getBoolean("rollback.protect-during-cooldown");
        preventtrample = config.getBoolean("rollback.prevent-trampling");


        finalEnabled = config.getBoolean("final.enabled");
        centerSpawn = config.getBoolean("final.center-on-first-spawn");
        finalCountdownStart = config.getInt("final.countdown-start");
        finalCountdownEnd = config.getInt("final.countdown-end");
        finalRadius = config.getInt("final.radius");
        finalFreeze = config.getInt("final.countdown-freeze");

        spectateEnabled = config.getBoolean("spectate.enabled");
        spectateOnDeath = config.getBoolean("spectate.on-death");
        spectateChat = config.getBoolean("spectate.chat");

        if(config.getBoolean("settings.globalexit.enable")){
            globalExit = Location.from((float)config.getDouble("settings.globalexit.x"), (float)config.getDouble("settings.globalexit.y"), (float)config.getDouble("settings.globalexit.z"), HG.getInstance().getServer().getLevel(config.getString("settings.globalexit.level")));
        }
        else
            globalExit = null;

        if(config.getSection("kits") != null){
            KitManager kitManager = new KitManager();
            kitManager.loadKits(config, "kits");
            HG.getInstance().setKitManager(kitManager);
        }

    }
}
