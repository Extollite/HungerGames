package pl.extollite.hungergames.tasks;

import cn.nukkit.player.Player;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.HGUtils.HGUtils;
import pl.extollite.hungergames.game.Game;

import java.util.Optional;
import java.util.UUID;

public class FreeRoamTask implements Runnable {

	private Game game;
	private int id;

	public FreeRoamTask(Game g) {
		this.game = g;
		for (Player p : g.getPlayers()) {
			HGUtils.sendMessage(p, HG.getInstance().getLanguage().getRoam_game_started());
			HGUtils.sendMessage(p, HG.getInstance().getLanguage().getRoam_time().replace("%roam%", String.valueOf(g.getRoamTime())));
			p.setHealth(20);
			p.getFoodData().setLevel(20);
			p.getFoodData().sendFoodLevel();
			g.unFreeze(p);
		}
		this.id = HG.getInstance().getServer().getScheduler().scheduleDelayedTask(HG.getInstance(), this, g.getRoamTime() * 20).getTaskId();
	}

	@Override
	public void run() {
		game.msgAll(HG.getInstance().getLanguage().getRoam_finished());
		game.startGame();
	}

	public void stop() {
		HG.getInstance().getServer().getScheduler().cancelTask(id);
	}
}
