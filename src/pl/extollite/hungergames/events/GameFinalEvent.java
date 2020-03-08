package pl.extollite.hungergames.events;

import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import cn.nukkit.player.Player;
import lombok.Getter;
import pl.extollite.hungergames.game.Game;

import java.util.Collection;

@Getter
public class GameFinalEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private Game game;
    private Collection<Player> finalist;

    public GameFinalEvent(Game game, Collection<Player> finalist) {
        this.game = game;
        this.finalist = finalist;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }
}
