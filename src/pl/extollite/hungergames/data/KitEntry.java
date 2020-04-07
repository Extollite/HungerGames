package pl.extollite.hungergames.data;

import cn.nukkit.item.Item;
import cn.nukkit.player.Player;
import cn.nukkit.potion.Effect;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class KitEntry {
    private Map<Integer, Item> inventoryContents;
    private Item[] armorContents;
    private List<Effect> effects;
    private String permission;
    private String name;
    private List<String> menuContent;

    public void giveKit(Player player){
        player.getInventory().clearAll();
        player.getInventory().setContents(inventoryContents);
        player.getInventory().setArmorContents(armorContents);
        player.removeAllEffects();
        for(Effect effect : effects){
            player.addEffect(effect);
        }
        player.sendAllInventories();
    }
}
