package pl.extollite.hungergames.listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.player.Player;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.hgutils.HGUtils;
import pl.extollite.hungergames.manager.PlayerManager;

import java.util.UUID;

/**
 * Internal event listener
 */
public class CancelListener implements Listener {

	private PlayerManager playerManager;

	public CancelListener() {
		playerManager = HG.getInstance().getPlayerManager();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	private void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if(event.isCancelled())
			return;
		Player player = event.getPlayer();
		if(player.isOp())
			return;
		if (player.hasPermission("hg.command.bypass")) return;
        UUID uuid = player.getServerId();
		String msg = event.getMessage();
		msg = msg.trim();
		String[] st = msg.split("\\s+");
		if (playerManager.hasData(uuid)) {
			if (st[0].equalsIgnoreCase("/hg")) {
				return;
			}
			event.setMessage("/");
			event.setCancelled(true);
			HGUtils.sendMessage(player, HG.getInstance().getLanguage().getCmd_handler_nocmd());
		} else if ("/tp".equalsIgnoreCase(st[0]) && st.length >= 2) {
			Player p = HG.getInstance().getServer().getPlayer(st[1]);
			if (p != null) {
				if (playerManager.hasPlayerData(uuid)) {
					HGUtils.sendMessage(player, HG.getInstance().getLanguage().getCmd_handler_playing());
					event.setMessage("/");
					event.setCancelled(true);
				}
			}
		} 
	}
}
