package pl.extollite.hungergames.tasks;


import cn.nukkit.block.Block;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemIds;
import cn.nukkit.level.Location;
import cn.nukkit.player.Player;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.data.PlayerData;
import pl.extollite.hungergames.game.Game;
import pl.extollite.hungergames.manager.PlayerManager;

public class CompassTask implements Runnable {

	private final PlayerManager playerManager;

	public CompassTask() {
		this.playerManager = HG.getInstance().getPlayerManager();
		HG.getInstance().getServer().getScheduler().scheduleDelayedRepeatingTask(HG.getInstance(), this, 25, 25);
	}

	@Override
	public void run() {
		//TODO: make it not calculating same distance for two players again
		for (PlayerData p : playerManager.getPlayerMap().values()) {

			if (p.getPlayer().getInventory().contains(Item.get(ItemIds.COMPASS))) {
				setNearestPlayer(p.getPlayer(), p);
			}
		}
	}

	private int cal(int i) {
		if (i < 0) {
			return -i;
		}
		return i;
	}
	
	private void setNearestPlayer(Player p, PlayerData pd) {

		Game g = pd.getGame();
		Block b = p.getLocation().getBlock();
		int x = b.getX();
		int y = b.getY();
		int z = b.getZ();

		int i = 200000;

		Player player = null;

		for (Player p2 : g.getPlayers()) {
			if (!p2.equals(p)) {

				Location l = p2.getLocation();

				int c = cal((int) (x - l.getX())) + cal((int) (y - l.getY())) + cal((int) (z - l.getZ()));

				if (i > c) {
					player = p2;
					i = c;
				}
			}
		}
		if (player != null) p.setSpawn(player.getLocation());
	}
}
