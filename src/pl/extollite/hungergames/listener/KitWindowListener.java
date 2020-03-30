package pl.extollite.hungergames.listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.player.Player;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.data.KitEntry;
import pl.extollite.hungergames.form.KitMenuWindow;
import pl.extollite.hungergames.form.KitWindow;

public class KitWindowListener implements Listener {
    public KitWindowListener() {
    }

    @EventHandler
    public void onResponse(PlayerFormRespondedEvent ev) {
        if (ev.wasClosed())
            return;
        if (ev.getWindow() instanceof KitMenuWindow) {
            ElementButton elementButton = ((FormResponseSimple) ev.getResponse()).getClickedButton();
            Player player = ev.getPlayer();
            KitEntry kit = HG.getInstance().getPlayerManager().getGame(player).getKitManager().getKit(elementButton.getText());
            if (kit != null) {
                StringBuilder contentBuilder = new StringBuilder();
                for(String content : kit.getMenuContent()){
                    contentBuilder.append(content).append("\n");
                }
                contentBuilder.setLength(contentBuilder.length()-1);
                player.showFormWindow(new KitWindow(kit.getName(), contentBuilder.toString()));
            }
        } else if (ev.getWindow() instanceof KitWindow) {
            int buttonId = ((FormResponseSimple) ev.getResponse()).getClickedButtonId();
            Player player = ev.getPlayer();
            if(buttonId == 0){
                KitEntry kit = HG.getInstance().getPlayerManager().getGame(player).getKitManager().getKit(((KitWindow) ev.getWindow()).getTitle());
                if(kit != null){
                    kit.giveKit(player);
                }
            }
            else{
                player.showFormWindow(new KitMenuWindow(ev.getPlayer(), HG.getInstance().getPlayerManager().getGame(ev.getPlayer())));
            }
        }
    }
}
