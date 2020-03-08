package pl.extollite.hungergames.events;

import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import lombok.Getter;
import pl.extollite.hungergames.game.Game;

/**
 * Called when a game starts the pregame stage of a game
 */
@SuppressWarnings("unused")
@Getter
public class GameStartEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private Game game;

	public GameStartEvent(Game game) {
		this.game = game;
	}

	public static HandlerList getHandlers() {
		return handlers;
	}
}
