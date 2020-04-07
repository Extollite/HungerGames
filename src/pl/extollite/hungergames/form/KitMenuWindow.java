package pl.extollite.hungergames.form;

import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.player.Player;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.data.KitEntry;
import pl.extollite.hungergames.game.Game;
import pl.extollite.hungergames.hgutils.HGUtils;

public class KitMenuWindow extends FormWindowSimple {
    public KitMenuWindow(Player player, Game game) {
        super(HGUtils.colorize(HG.getInstance().getLanguage().getKit_menu_title()), HGUtils.colorize(HG.getInstance().getLanguage().getKit_menu_content()));
        for(KitEntry entry : game.getKitManager().getKits()){
            if(entry.getPermission() != null && player.hasPermission(entry.getPermission())){
                this.addButton(new ElementButton(entry.getName()));
            }
        }
    }
}
