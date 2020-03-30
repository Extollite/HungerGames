package pl.extollite.hungergames.manager;

import cn.nukkit.item.Item;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.Config;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.data.KitEntry;
import pl.extollite.hungergames.hgutils.HGUtils;

import java.util.*;

public class KitManager {
    private Map<String, KitEntry> kits = new HashMap<>();

    public void addKit(String name, KitEntry kit) {
        kits.put(name, kit);
    }

    public void removeKit(String name) {
        kits.remove(name);
    }

    public void clearKits() {
        kits.clear();
    }

    public boolean hasKits() {
        return kits.size() > 0;
    }

    public KitEntry getKit(String name){
        return kits.get(name);
    }

    public List<KitEntry> getKits(){
        return new LinkedList<>(kits.values());
    }

    public void loadKits(Config config, String path){
        for(String key : config.getSection(path).getKeys(false)){
            Item[] armorContetns = new Item[4];
            armorContetns[0] = ItemManager.getItem(config.getString(path+"."+key+".helmet"), false);
            armorContetns[1] = ItemManager.getItem(config.getString(path+"."+key+".chestplate"), false);
            armorContetns[2] = ItemManager.getItem(config.getString(path+"."+key+".leggings"), false);
            armorContetns[3] = ItemManager.getItem(config.getString(path+"."+key+".boots"), false);
            String permission = config.getString(path+"."+key+".permission");
            Map<Integer, Item> inventoryContents = new HashMap<>();
            for(String item : config.getStringList(path+"."+key+".inventory")){
                if(inventoryContents.size() >= 36)
                    break;
                inventoryContents.put(inventoryContents.size(), ItemManager.getItem(item, true));
            }
            List<Effect> effects = new LinkedList<>();
            for(String effectString : config.getStringList(path+"."+key+".effects")){
                String[] splitted = effectString.split(":");
                Effect effect = Effect.getEffectByName(splitted[0]).setDuration(Integer.parseInt(splitted[1])).setAmplifier(Integer.parseInt(splitted[2]));
                effects.add(effect);
            }
            String name = HGUtils.colorize(config.getString(path+"."+key+".name"));
            List<String> menuContent = new LinkedList<>();
            for(String line : config.getStringList(path+"."+key+".menu-content")){
                menuContent.add(HGUtils.colorize(line));
            }
            KitEntry kitEntry = new KitEntry(inventoryContents, armorContetns, effects, permission, name, menuContent);
            this.addKit(name, kitEntry);
            HG.getInstance().setKitEnabled(true);
        }
    }
}
