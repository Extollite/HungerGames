package pl.extollite.hungergames.events;


import cn.nukkit.block.Block;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import lombok.Getter;
import pl.extollite.hungergames.game.Game;

/**
 * Called when a player opens an empty chest in the game
 * <p>This will initiate the chest loading its contents</p>
 */
@Getter
public class ChestOpenEvent extends Event {
	private static final HandlerList handlers = new HandlerList();

	private Game game;
	private Block block;
	private boolean bonus;

	/** Create a new player open chest event
	 * @param game The game this is happening in
	 * @param block The block that is opening
	 * @param bonus If the chest is a bonus chest
	 */
	public ChestOpenEvent(Game game, Block block, boolean bonus) {
		this.game = game;
		this.block = block;
		this.bonus = bonus;
	}

	public static HandlerList getHandlers() {
		return handlers;
	}
}
