package pl.extollite.hungergames.data;

import cn.nukkit.entity.Attribute;
import cn.nukkit.item.Item;
import cn.nukkit.player.Player;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.game.Game;

import java.util.Map;
import java.util.UUID;

/**
 * Player data object for holding pre-game player info
 */
@Setter
@Getter
@ToString
@SuppressWarnings("WeakerAccess")
public class PlayerData {

	//Pregame data
	private Map<Integer, Item> inv;
	private Item[] equip;
	private int expL;
	private int expP;
	private float health;
	private int food;
	private float saturation;
	private int mode;
	private Player player;
	
	//InGame data
	private Game game;

	/** New player pre-game data file
	 * @param player Player to save
	 * @param game Game they will be entering
	 */
	public PlayerData(Player player, Game game) {
		this.game = game;
		this.player = player;
		inv = player.getInventory().getContents();
		equip = player.getInventory().getArmorContents();
		expL = player.getExperienceLevel();
		expP = player.getExperience();
		mode = player.getGamemode();
		food = player.getFoodData().getLevel();
		saturation = player.getFoodData().getFoodSaturationLevel();
		health = player.getHealth();
		player.getInventory().clearAll();
		player.setExperience(0, 0);
	}

	/** Restore a player's saved data
	 * @param player Player to restore data to
	 */
	public void restore(Player player) {
		if (player == null) return;
		player.getInventory().clearAll();
		player.setMovementSpeed(Player.DEFAULT_SPEED);
		player.setExperience(expP, expL);
		player.getFoodData().setLevel(food);
		player.getFoodData().setFoodSaturationLevel(saturation);
		player.getFoodData().sendFoodLevel();
		player.getInventory().setContents(inv);
		player.getInventory().setArmorContents(equip);
		player.setGamemode(mode);
		player.sendAllInventories();
		player.invulnerable = false;
		restoreHealth(player);
	}

	// Restores later if player has an item in their inventory which changes their max health value
	private void restoreHealth(Player player) {
		float att = Attribute.getAttribute(Attribute.MAX_HEALTH).getDefaultValue();
		if (health > att) {
			HG.getInstance().getServer().getScheduler().scheduleDelayedTask(HG.getInstance(), () -> player.setHealth(health), 10);
		} else {
			player.setHealth(health);
		}
	}

}
