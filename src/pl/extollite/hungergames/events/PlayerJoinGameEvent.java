package pl.extollite.hungergames.events;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import cn.nukkit.player.Player;
import lombok.Getter;
import pl.extollite.hungergames.game.Game;

/**
 * Called when a player joins a game
 */
@Getter
public class PlayerJoinGameEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private Game game;
	private Player player;
	private boolean isCancelled;

	public PlayerJoinGameEvent(Game game, Player player) {
		this.game = game;
		this.player = player;
		this.isCancelled = false;
	}

	public static HandlerList getHandlers() {
		return handlers;
	}

	@Override
	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

}
