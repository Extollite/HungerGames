package pl.extollite.hungergames.events;


import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import cn.nukkit.player.Player;
import lombok.Getter;
import pl.extollite.hungergames.game.Game;

import java.util.Collection;

/**
 * Called when a game ends
 */
@Getter
public class GameEndEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Game game;
    private Collection<Player> winners;
    private boolean death;

    public GameEndEvent(Game game, Collection<Player> winners, boolean death) {
        this.game = game;
        this.winners = winners;
        this.death = death;
    }

    public boolean byDeath() {
        return this.death;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }
}
