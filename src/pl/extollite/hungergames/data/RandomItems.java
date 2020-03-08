package pl.extollite.hungergames.data;

import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.manager.ItemManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Handler for random items
 */
public class RandomItems {
    public static void load() {
        Config item = new Config(HG.getInstance().getDataFolder()+"/items.yml", Config.YAML);
        if (item.getStringList("items").isEmpty()) {
            item.load(HG.getInstance().getResource("items.yml"));
            item.save();
        }
        // Regular items
        for (String s : item.getStringList("items")) {
            loadItems(s, HG.getInstance().getItems());
        }
        // Bonus items
        for (String s : item.getStringList("bonus")) {
            loadItems(s, HG.getInstance().getBonusItems());
        }
    }

    public static void loadItems(String itemString, Map<Integer, Item> map) {
        String[] amount = itemString.split(" ");
        if (itemString.contains("x:")) {
            for (String p : amount) {
                if (p.startsWith("x:")) {
                    int c = Integer.parseInt(p.replace("x:", ""));
                    while (c != 0) {
                        c--;
                        map.put(map.size() + 1, ItemManager.getItem(itemString.replace("x:", ""), true));
                    }
                }
            }
        } else {
            map.put(map.size() + 1, ItemManager.getItem(itemString, true));
        }
    }

}