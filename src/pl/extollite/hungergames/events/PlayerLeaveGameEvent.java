package pl.extollite.hungergames.events;

import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import cn.nukkit.player.Player;
import lombok.Getter;
import pl.extollite.hungergames.game.Game;

/**
 * Called when a player leaves a game
 */
@Getter
public class PlayerLeaveGameEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private Game game;
	private Player player;
	private boolean death;

	public PlayerLeaveGameEvent(Game game, Player player, boolean death) {
		this.game = game;
		this.player = player;
		this.death = death;
	}

	public static HandlerList getHandlers() {
		return handlers;
	}
}
