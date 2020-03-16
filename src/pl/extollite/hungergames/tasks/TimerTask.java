package pl.extollite.hungergames.tasks;

import cn.nukkit.player.Player;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.data.ConfigData;
import pl.extollite.hungergames.game.Game;
import pl.extollite.hungergames.game.Status;

import java.util.Optional;
import java.util.UUID;

public class TimerTask implements Runnable {

	private int remainingtime;
	private int finalCountdownStart;
	private int id;
	private Game game;

	public TimerTask(Game g, int time) {
		this.remainingtime = time;
		this.game = g;
		this.finalCountdownStart = ConfigData.finalCountdownStart;
		for(Player player : g.getPlayers()){
			player.invulnerable = false;
		}
		this.id = HG.getInstance().getServer().getScheduler().scheduleRepeatingTask(HG.getInstance(), this, 30 * 20).getTaskId();
	}
	
	@Override
	public void run() {
		if (game == null || ( game.getStatus() != Status.FINAL_COUNTDOWN && game.getStatus() != Status.RUNNING && game.getStatus() != Status.FINAL )) stop(); //A quick null check!

		if (ConfigData.finalEnabled && remainingtime == finalCountdownStart) {
			game.setFinalize(new FinalTask(game));
		}

		if (game.getChestRefillTime() > 0 && remainingtime == game.getChestRefillTime()) {
			game.refillChests();
			game.msgAll(HG.getInstance().getLanguage().getGame_chest_refill());
		}

		if (this.remainingtime < 10) {
			stop();
			game.stop(false);
		} else {
			int minutes = this.remainingtime / 60;
			int asd = this.remainingtime % 60;
			if (minutes != 0) {
				if (asd == 0)
					game.tipAll(HG.getInstance().getLanguage().getGame_ending_min().replace("%minutes%", String.valueOf(minutes)));
				else
					game.tipAll(HG.getInstance().getLanguage().getGame_ending_minsec().replace("%minutes%", String.valueOf(minutes)).replace("%seconds%", String.valueOf(asd)));
			} else game.tipAll(HG.getInstance().getLanguage().getGame_ending_sec().replace("%seconds%", String.valueOf(this.remainingtime)));
		}
		remainingtime = (remainingtime - 30);
	}
	
	public void stop() {
		HG.getInstance().getServer().getScheduler().cancelTask(id);
	}

}
