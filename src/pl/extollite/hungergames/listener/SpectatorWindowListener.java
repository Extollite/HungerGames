package pl.extollite.hungergames.listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.player.Player;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.form.SpectatorWindow;


public class SpectatorWindowListener implements Listener {
    public SpectatorWindowListener() {
    }

    @EventHandler
    public void onResponse(PlayerFormRespondedEvent ev){
        if(ev.wasClosed())
            return;
        if(!(ev.getResponse() instanceof FormResponseSimple))
            return;
        if(!(ev.getWindow() instanceof SpectatorWindow))
            return;
        ElementButton elementButton = ((FormResponseSimple) ev.getResponse()).getClickedButton();
        Player player = HG.getInstance().getServer().getPlayer(elementButton.getText());
        if(player != null){
            ev.getPlayer().teleport(player.getLocation());
        }
    }
}
