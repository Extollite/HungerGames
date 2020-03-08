package pl.extollite.hungergames.manager;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemColorArmor;
import cn.nukkit.item.ItemIds;
import cn.nukkit.item.ItemPotion;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.potion.Potion;
import cn.nukkit.registry.RegistryException;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;
import cn.nukkit.utils.Identifier;
import cn.nukkit.utils.TextFormat;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.HGUtils.HGUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Manage item stacks for kits and chests
 */
public class ItemManager {
    public static Item getItem(String args, boolean isStackable) {
        if (args == null) return null;
        int amount = 1;
        if (isStackable) {
            String a = args.split(" ")[1];
            try{
                amount = Integer.parseInt(a);
            }
            catch (NumberFormatException ignored){

            }
        }
        Item item = itemStringToStack(args.split(" ")[0], amount);
        if (item == null) return null;

        String[] ags = args.split(" ");
        for (String s : ags) {
            if (s.startsWith("enchant:")) {
                s = s.replace("enchant:", "").toUpperCase();
                String[] d = s.split(":");
                int level = 1;
                if (d.length != 1) {
                    try{
                        level = Integer.parseInt(d[1]);
                    }
                    catch (NumberFormatException ignored){

                    }
                }
                for (Enchantment e : Enchantment.getEnchantments()) {
                    if (e.getName().equalsIgnoreCase(d[0])) {
                        item.addEnchantment(e.setLevel(level));
                    }
                }
            } else if (s.startsWith("color:")) {
                if (item instanceof ItemColorArmor) {
                    BlockColor color = getColor(s);
                    if(color != null)
                        ((ItemColorArmor) item).setColor(color);
                }
            } else if (s.startsWith("name:")) {
                s = s.replace("name:", "").replace("_", " ");
                s = HGUtils.colorize(s);
                item.setCustomName(s);
            } else if (s.startsWith("lore:")) {
                s = s.replace("lore:", "").replace("_", " ");
                s = HGUtils.colorize(s);
                String[] lore = s.split(":");
                item.setLore(lore);
            }
        }
        return item;
    }

    private static Item itemStringToStack(String item, int amount) {
        String oldPotion = item.toUpperCase();
        String[] itemArr = item.split(":");
        if (oldPotion.startsWith("POTION:") || oldPotion.startsWith("SPLASH_POTION:") || oldPotion.startsWith("LINGERING_POTION:")) {
            return getPotion(item);
        }
        try{
            Identifier id = Identifier.fromString(itemArr[0].toUpperCase());
            return Item.get(id, 0, amount);
        }
        catch (RegistryException ignored){
            HG.getInstance().getLogger().error(TextFormat.RED+"Unknown item id: "+item+", skipping!");
            return null;
        }
    }

    // Get a potion item stack from a string
    // DEPRECATED - will remove in future
    private static Item getPotion(String item) {
        String[] potionData = item.split(":");
        try{
            Identifier id = Identifier.fromString(potionData[0]);
            return Item.get(id, Integer.parseInt(potionData[1]));
        }
        catch (RegistryException ignored){
            HG.getInstance().getLogger().error(TextFormat.RED+"Unknown potion id: "+item+", skipping!");
            return null;
        }
    }

    private static BlockColor getColor(String colorString) {
        String dyeString = colorString.replace("color:", "");
        try {
            DyeColor dc = DyeColor.valueOf(dyeString.toUpperCase());
            return dc.getColor();
        } catch (Exception ignore) {
        }
        try {
            return new BlockColor(Integer.parseInt(dyeString));
        } catch (Exception ignore) {
        }
        return null;
    }

    public static Item getSpectatorCompass() {
        Item compass = Item.get(ItemIds.COMPASS);
        compass.setCustomName(HGUtils.colorize(HG.getInstance().getLanguage().getSpectator_compass()));
        return compass;
    }

}
