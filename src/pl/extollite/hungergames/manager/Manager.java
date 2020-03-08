package pl.extollite.hungergames.manager;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockIds;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.player.Player;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.HGUtils.HGUtils;
import pl.extollite.hungergames.data.ConfigData;
import pl.extollite.hungergames.game.Game;
import pl.extollite.hungergames.game.Status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * General manager for games
 */
public class Manager {
	private static Random rg = new Random();
	
    /** Check the status of a game while being set up
     * @param game Game to check
     * @param player Player issuing the check
     */
	public static void checkGame(Game game, Player player) {
		if (game.getSpawns().size() <  game.getMaxPlayers()) {
			HGUtils.sendMessage(player, "&cYou still need &7" + (game.getMaxPlayers() - game.getSpawns().size()) + " &c more spawns!");
		} else if (game.getStatus() == Status.BROKEN) {
			HGUtils.sendMessage(player, "&cYour arena is marked as broken! use &7/hg debug &c to check for errors!");
			HGUtils.sendMessage(player, "&cIf no errors are found, please use &7/hg toggle " + game.getName() + "&c!");
		} else if (!game.isLobbyValid()) {
			HGUtils.sendMessage(player, "&cYour LobbyWall is invalid! Please reset them!");
			HGUtils.sendMessage(player, "&cSet lobbywall: &7/hg setlobbywall " + game.getName());
		} else {
			HGUtils.sendMessage(player, "&aYour HungerGames arena is ready to run!");
			game.setStatus(Status.WAITING);
		}
	}

    /** Fill chests in a game
     * @param block Chest to fill
     * @param game Game this chest is in
     * @param bonus Whether or not this is a bonus chest
     */
	public static void fillChests(Block block, Game game, boolean bonus) {
		Inventory i = ((InventoryHolder)game.getBound().getLevel().getBlockEntity(block.getPosition())).getInventory();
		List<Integer> slots = new ArrayList<>();
		for (int slot = 0; slot <= 26; slot++) {
			slots.add(slot);
		}
		Collections.shuffle(slots);
		i.clearAll();
		int max = bonus ? ConfigData.maxbonuscontent : ConfigData.maxchestcontent;
		int min = bonus ? ConfigData.minbonuscontent : ConfigData.minchestcontent;

		int c = rg.nextInt(max) + 1;
		c = Math.max(c, min);
		while (c != 0) {
			Item it = randomItem(game, bonus);
			int slot = slots.get(0);
			slots.remove(0);
			i.setItem(slot, it);
			c--;
		}
	}

    /** Get a random item from a game's item list
     * @param game Game to get the item from
     * @param bonus Whether or not its a bonus item
     * @return Random ItemStack
     */
	public static Item randomItem(Game game, boolean bonus) {
		if (bonus) {
		    int r = game.getBonusItems().size();
		    if (r == 0) {
		        return Item.get(BlockIds.AIR).clone();
            }
			int i = rg.nextInt(r) + 1;
			return game.getBonusItems().get(i);
		} else {
		    int r = game.getItems().size();
            if (r == 0) {
				return Item.get(BlockIds.AIR).clone();
			}
			int i = rg.nextInt(r) + 1;
			return game.getItems().get(i);
		}
	}

	/** Check if a location is in a game's bounds
	 * @param location The location to check for a game
	 * @return True if the location is within a game's bounds
	 */
	public static Game isInRegion(Location location) {
		for (Game g : HG.getInstance().getGames()) {
			if (g.isInRegion(location))
				return g;
		}
		return null;
	}

	/** Get a game at a location
	 * @param location The location to check for a game
	 * @return The game
	 */
	public static Game getGame(Location location) {
		for (Game g : HG.getInstance().getGames()) {
			if (g.isInRegion(location))
				return g;
		}
		return null;
	}

	/** Get a game by name
	 * @param name The name of the game to find
	 * @return The game
	 */
	public static Game getGame(String name) {
		for (Game g : HG.getInstance().getGames()) {
			if (g.getName().equalsIgnoreCase(name)) {
				return g;
			}
		}
		return null;
	}

}
