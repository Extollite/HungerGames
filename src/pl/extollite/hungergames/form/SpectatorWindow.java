package pl.extollite.hungergames.form;

import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.player.Player;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.HGUtils.HGUtils;
import pl.extollite.hungergames.game.Game;

import java.util.Optional;
import java.util.UUID;

public class SpectatorWindow extends FormWindowSimple {
    public SpectatorWindow(Game game) {
        super(HGUtils.colorize(HG.getInstance().getLanguage().getSpectator_menu_title()), HGUtils.colorize(HG.getInstance().getLanguage().getSpectator_menu_content()));
        for(Player p : game.getPlayers()){
            addButton(new ElementButton(p.getName()));
        }
    }
}
