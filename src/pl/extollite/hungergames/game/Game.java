package pl.extollite.hungergames.game;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockIds;
import cn.nukkit.blockentity.Barrel;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.Chest;
import cn.nukkit.blockentity.Sign;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.Sound;
import cn.nukkit.level.gamerule.GameRules;
import cn.nukkit.player.Player;
import cn.nukkit.registry.BlockEntityRegistry;
import cn.nukkit.registry.GeneratorRegistry;
import cn.nukkit.utils.Identifier;
import com.nukkitx.math.vector.Vector3i;
import de.lucgameshd.scoreboard.api.ScoreboardAPI;
import de.lucgameshd.scoreboard.network.DisplayEntry;
import de.lucgameshd.scoreboard.network.DisplaySlot;
import de.lucgameshd.scoreboard.network.Scoreboard;
import de.lucgameshd.scoreboard.network.ScoreboardDisplay;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.hgutils.HGUtils;
import pl.extollite.hungergames.data.ConfigData;
import pl.extollite.hungergames.data.Language;
import pl.extollite.hungergames.data.Leaderboard;
import pl.extollite.hungergames.data.PlayerData;
import pl.extollite.hungergames.events.*;
import pl.extollite.hungergames.manager.PlayerManager;
import pl.extollite.hungergames.tasks.*;
import pl.extollite.hungergames.tasks.TimerTask;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static cn.nukkit.blockentity.BlockEntityTypes.BARREL;
import static cn.nukkit.blockentity.BlockEntityTypes.CHEST;


/**
 * General game object
 */
@Getter
@Setter
@ToString
@SuppressWarnings("unused")
public class Game {

    private HG plugin;
    private Language lang;
    private String name;
    private List<Vector3i> spawns;
    private Bound bound;
    private List<Player> players = new ArrayList<>();
    private List<Player> spectators = new ArrayList<>();
    private List<Vector3i> chests = new ArrayList<>();
    private List<Vector3i> playerChests = new ArrayList<>();
    private List<Vector3i> chestLocations = new ArrayList<>();
    private int minChestsInGame = 0;
    private int maxChestsInGame = 0;
    private int bonusChestChance = 0;
    private Map<Integer, Item> items;
    private Map<Integer, Item> bonusItems;
    private Map<Player, Integer> kills = new HashMap<>();

    private PlayerManager playerManager;
    private Status status;
    private int minPlayers;
    private int maxPlayers;
    private int time;
    private Sign s;
    private int roamTime;
    private int chestRefillTime = 0;
    private Location exit;

    // Task ID's here!
    private FreeRoamTask freeRoam;
    private StartingTask starting;
    private FinalTask finalize;
    private TimerTask timer;

    private int finalCountdownStart;
    private int finalCountdownEnd;

    private boolean spectate = ConfigData.spectateEnabled;
    private boolean spectateOnDeath = ConfigData.spectateOnDeath;

    private Map<Player, Scoreboard> scoreboards = new HashMap<>();

    /**
     * Create a new game
     * <p>Internally used when loading from config on server start</p>
     *
     * @param name       Name of this game
     * @param bound      Bounding region of this game
     * @param spawns     List of spawns for this game
     * @param lobbySign  Lobby sign block
     * @param timer      Length of the game (in seconds)
     * @param minPlayers Minimum players to be able to start the game
     * @param maxPlayers Maximum players that can join this game
     * @param roam       Roam time for this game
     * @param isReady    If the game is ready to start
     */
    public Game(String name, Bound bound, List<Vector3i> spawns, Sign lobbySign, int timer, int minPlayers, int maxPlayers, int roam, boolean isReady) {
        this.plugin = HG.getInstance();
        this.playerManager = plugin.getPlayerManager();
        this.lang = plugin.getLanguage();
        this.name = name;
        this.bound = bound;
        this.spawns = spawns;
        this.s = lobbySign;
        this.time = timer;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.roamTime = roam;
        if (isReady) this.status = Status.READY;
        else this.status = Status.BROKEN;

        setLobbyBlock(lobbySign);

        this.items = plugin.getItems();
        this.bonusItems = plugin.getBonusItems();
    }

    /**
     * Create a new game
     * <p>Internally used when creating a game with the <b>/hg create</b> command</p>
     *
     * @param name       Name of this game
     * @param bound      Bounding region of this game
     * @param timer      Length of the game (in seconds)
     * @param minPlayers Minimum players to be able to start the game
     * @param maxPlayers Maximum players that can join this game
     * @param roam       Roam time for this game
     */
    public Game(String name, Bound bound, int timer, int minPlayers, int maxPlayers, int roam) {
        this.plugin = HG.getInstance();
        this.playerManager = plugin.getPlayerManager();
        this.lang = plugin.getLanguage();
        this.name = name;
        this.time = timer;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.roamTime = roam;
        this.spawns = new ArrayList<>();
        this.bound = bound;
        this.status = Status.NOTREADY;
        this.items = plugin.getItems();
        this.bonusItems = plugin.getBonusItems();
    }

    /**
     * Get the bounding region of this game
     *
     * @return Region of this game
     */
    public Bound getRegion() {
        return bound;
    }


    /**
     * Add an item to the items map for this game
     *
     * @param item ItemStack to add
     */
    public void addToItems(Item item) {
        this.items.put(this.items.size() + 1, item);
    }

    /**
     * Clear the items for this game
     */
    public void clearItems() {
        this.items.clear();
    }

    /**
     * Reset the items for this game to the plugin's default items list
     */
    public void resetItemsDefault() {
        this.items = HG.getInstance().getItems();
    }

    /**
     * Add an item to this game's bonus items
     *
     * @param item ItemStack to add to bonus items
     */
    public void addToBonusItems(Item item) {
        this.bonusItems.put(this.bonusItems.size() + 1, item);
    }

    /**
     * Clear this game's bonus items
     */
    public void clearBonusItems() {
        this.bonusItems.clear();
    }

    /**
     * Reset the bonus items for this game to the plugin's default bonus items list
     */
    public void resetBonusItemsDefault() {
        this.bonusItems = HG.getInstance().getBonusItems();
    }

    /**
     * Add a kill to a player
     *
     * @param player The player to add a kill to
     */
    public void addKill(Player player) {
        this.kills.put(player, this.kills.get(player) + 1);
    }

    public StartingTask getStartingTask() {
        return this.starting;
    }

    /**
     * Set the chest refill time for this game
     *
     * @param refill Remaining time in game (seconds : 30 second intervals)
     */
    public void setChestRefill(int refill) {
        this.chestRefillTime = refill;
    }

    /**
     * Set the status of the game
     *
     * @param status Status to set
     */
    public void setStatus(Status status) {
        this.status = status;
        updateLobbyBlock();
    }

    /**
     * Set the chest refill time
     *
     * @param time The remaining time in the game for the chests to refill
     */
    public void setChestRefillTime(int time) {
        this.chestRefillTime = time;
    }

    /**
     * Refill chests in this game
     */
    public void refillChests() {
        this.chests.clear();
    }

    /**
     * Add a game chest location to the game
     *
     * @param location Location of the chest to add (Needs to actually be a chest there)
     */
    public void addGameChest(Vector3i location) {
        chests.add(location);
    }

    /**
     * Add a player placed chest to the game
     *
     * @param location Location of the chest
     */
    public void addPlayerChest(Vector3i location) {
        playerChests.add(location);
    }

    /**
     * Check if chest at this location is logged
     *
     * @param location Location of chest to check
     * @return True if this chest was added already
     */
    public boolean isLoggedChest(Vector3i location) {
        return chests.contains(location) || playerChests.contains(location);
    }

    /**
     * Remove a game chest from the game
     *
     * @param location Location of the chest to remove
     */
    public void removeGameChest(Vector3i location) {
        chests.remove(location);
    }

    /**
     * Remove a player placed chest from the game
     *
     * @param location Location of the chest
     */
    public void removePlayerChest(Vector3i location) {
        playerChests.remove(location);
    }

    /**
     * Check if a location is within the games arena
     *
     * @param location Location to be checked
     * @return True if location is within the arena bounds
     */
    public boolean isInRegion(Location location) {
        return bound.isInRegion(location);
    }

    /**
     * Join a player to the game
     *
     * @param player Player to join the game
     */
    public void join(Player player) {
        UUID uuid = player.getServerId();
        if (status != Status.WAITING && status != Status.STOPPED && status != Status.COUNTDOWN && status != Status.READY) {
            HGUtils.sendMessage(player, getLang().getArena_not_ready());
            if ((status == Status.RUNNING || status == Status.BEGINNING || status == Status.FINAL || status == Status.FINAL_COUNTDOWN) && ConfigData.spectateEnabled) {
                HGUtils.sendMessage(player, getLang().getArena_spectate().replace("%arena%", this.getName()));
            }
        } else if (maxPlayers <= players.size()) {
            HGUtils.sendMessage(player, "&c" + name + " " + getLang().getGame_full());
        } else if (!players.contains(player)) {
            // Call PlayerJoinGameEvent
            PlayerJoinGameEvent event = new PlayerJoinGameEvent(this, player);
            plugin.getServer().getPluginManager().callEvent(event);
            // If cancelled, stop the player from joining the game
            if (event.isCancelled()) return;

            if (player.getVehicle() != null) {
                player.dismount(player.getVehicle());
            }

            players.add(player);
            HG.getInstance().getServer().getScheduler().scheduleDelayedTask(HG.getInstance(), () -> {
                player.teleport(pickSpawn());

                playerManager.addPlayerData(new PlayerData(player, this));

                heal(player);
                freeze(player);
                kills.put(player, 0);
                HG.getInstance().getLobbySB().hideFor(player);
                if(ConfigData.hide_player_name)
                    player.setNameTagAlwaysVisible(false);

                if (players.size() == 1 && status == Status.READY)
                    status = Status.WAITING;
                if (players.size() >= minPlayers && (status == Status.WAITING || status == Status.READY)) {
                    startPreGame();
                } else if (status == Status.WAITING) {
                    HGUtils.broadcast(getLang().getPlayer_joined_game().replace("%player_name%",
                            player.getName()) + (minPlayers - players.size() <= 0 ? "!" : ":" +
                            getLang().getPlayers_to_start().replace("%amount%", String.valueOf((minPlayers - players.size())))));
                }

                updateLobbyBlock();
            }, 5);
        }
    }

    /**
     * Respawn all players in the game back to spawn points
     */
    public void respawnAll() {
        for (Player p : players) {
            p.teleport(pickSpawn());
        }
    }

    /**
     * Start the pregame countdown
     */
    public void startPreGame() {
        // Call the GameStartEvent
        GameStartEvent event = new GameStartEvent(this);
        plugin.getServer().getPluginManager().callEvent(event);

        status = Status.COUNTDOWN;
        starting = new StartingTask(this);
        updateLobbyBlock();
        if(ConfigData.sb_enable)
            updateScoreboard();
    }

    /**
     * Start the free roam state of the game
     */
    public void startFreeRoam() {
        status = Status.BEGINNING;
        updateLobbyBlock();
        bound.removeEntities();
        freeRoam = new FreeRoamTask(this);
    }

    /**
     * Start the game
     */
    public void startGame() {
        status = Status.RUNNING;
        timer = new TimerTask(this, time);
        updateLobbyBlock();
    }


    /**
     * Add a spawn location to the game
     *
     * @param location The location to add
     */
    public void addSpawn(Vector3i location) {
        this.spawns.add(location);
    }

    public void addChestLocation(Vector3i location) {
        this.chestLocations.add(location);
    }

    private Location pickSpawn() {
        double spawn = getRandomIntegerBetweenRange(maxPlayers - 1);
        if (containsPlayer(spawns.get(((int) spawn)))) {
            Collections.shuffle(spawns);
            for (Vector3i l : spawns) {
                if (!containsPlayer(l)) {
                    return Location.from(l, bound.getLevel());
                }
            }
        }
        return Location.from(spawns.get((int) spawn), bound.getLevel());
    }

    private boolean containsPlayer(Vector3i location) {
        if (location == null) return false;

        for (Player p : players) {
            if (p.getLocation().getBlock().getPosition().equals(location))
                return true;
        }
        return false;
    }

    /**
     * Send a message to all players in the game
     *
     * @param message Message to send
     */
    public void msgAll(String message) {
        plugin.getServer().broadcastMessage(HGUtils.colorize(message), players);
        plugin.getServer().broadcastMessage(HGUtils.colorize(message), spectators);
    }

    public void tipAll(String message) {
        for (Player p : players) {
            HGUtils.sendTip(p, message);
        }
        for (Player s : spectators) {
            HGUtils.sendTip(s, message);
        }
    }

    public void titleAll(String message) {
        for (Player p : players) {
            HGUtils.sendTitle(p, message);
        }
    }

    private void updateLobbyBlock() {
        String[] lines = new String[4];
        lines[0] = HGUtils.colorize(getLang().getLine_1());
        lines[1] = HGUtils.colorize(getLang().getLine_2().replace("%status%", status.getName()));
        lines[2] = HGUtils.colorize(getLang().getLine_3().replace("%players_count%", "" + players.size() + "/" + maxPlayers));
        if (status != Status.WAITING && status != Status.STOPPED && status != Status.COUNTDOWN && status != Status.READY)
            lines[3] = HGUtils.colorize(getLang().getLine_4_spectate().replace("%game_name%", this.name));
        lines[3] = HGUtils.colorize(getLang().getLine_4_join().replace("%game_name%", this.name));
        s.setText(lines);
    }

    private void heal(Player player) {
        player.removeAllEffects();
        player.setHealth(20);
        player.getFoodData().setLevel(20);
        player.getFoodData().sendFoodLevel();
        HG.getInstance().getServer().getScheduler().scheduleDelayedTask(HG.getInstance(), player::extinguish, 1);
    }

    /**
     * Freeze a player
     *
     * @param player Player to freeze
     */
    public void freeze(Player player) {
        player.setGamemode(Player.SURVIVAL);
        player.setImmobile(true);
    }

    /**
     * Unfreeze a player
     *
     * @param player Player to unfreeze
     */
    public void unFreeze(Player player) {
        player.setImmobile(false);
    }

    /**
     * Set the lobby block for this game
     *
     * @param sign The sign to which the lobby will be set at
     */
    public void setLobbyBlock(Sign sign) {
        try {
            this.s = sign;
            String[] lines = new String[4];
            lines[0] = HGUtils.colorize(getLang().getLine_1());
            lines[1] = HGUtils.colorize(getLang().getLine_2().replace("%status%", status.getName()));
            lines[2] = HGUtils.colorize(getLang().getLine_3().replace("%players_count%", "" + players.size() + "/" + maxPlayers));
            if (status != Status.WAITING && status != Status.STOPPED && status != Status.COUNTDOWN && status != Status.READY)
                lines[3] = HGUtils.colorize(getLang().getLine_4_spectate().replace("%game_name%", this.name));
            lines[3] = HGUtils.colorize(getLang().getLine_4_join().replace("%game_name%", this.name));
            s.setText(lines);
        } catch (Exception e) {
            return;
        }
        if (ConfigData.globalExit != null) {
            this.exit = ConfigData.globalExit;
        } else {
            this.exit = s.getLevel().getSafeSpawn();
        }
    }

    public void startFinal() {
        List<Player> playersInGame = new LinkedList<>();
        for (Player p : this.getPlayers()) {
            playersInGame.add(p);
            p.teleport(pickSpawn());
            freeze(p);
        }
        HG.getInstance().getServer().getPluginManager().callEvent(new GameFinalEvent(this, playersInGame));
        status = Status.FINAL_COUNTDOWN;
        updateLobbyBlock();
        msgAll(getLang().getGame_teleported());
    }

    public void forceRollback() {
        Level levelToUnload = bound.getLevel();
        GeneratorRegistry generatorRegistry = GeneratorRegistry.get();
        Identifier generator = Identifier.fromString(HG.getInstance().getServer().getConfig("worlds." + levelToUnload.getId() + ".generator"));
        String options = HG.getInstance().getServer().getConfig("worlds." + levelToUnload.getId() + ".options", "");

        HG.getInstance().getServer().getLevelManager().deregister(levelToUnload);
        HG.getInstance().getServer().unloadLevel(levelToUnload, true);
        HG.getInstance().getServer().loadLevel().id(levelToUnload.getId()).seed(levelToUnload.getSeed())
                .generator(generator == null ? generatorRegistry.getFallback() : generator)
                .generatorOptions(options).load().join().setAutoSave(false);
        Level newLevel = bound.getLevel();
        newLevel.getGameRules().put(GameRules.DO_WEATHER_CYCLE, false);
        newLevel.setThundering(false);
        newLevel.setRaining(false);
    }


    public void cancelTasks() {
        if (timer != null) timer.stop();
        if (starting != null) starting.stop();
        if (finalize != null) finalize.stop();
        if (freeRoam != null) freeRoam.stop();
    }

    /**
     * Stop the game
     */
    public void stop() {
        stop(false);
    }

    /**
     * Stop the game
     *
     * @param death Whether the game stopped after the result of a death (false = no winnings payed out)
     */
    public void stop(boolean death) {
        bound.removeEntities();
        List<Player> win = new ArrayList<>();
        cancelTasks();
        for (Player p : players) {
            heal(p);
            playerManager.getPlayerData(p).restore(p);
            playerManager.removePlayerData(p);
            win.add(p);
            exit(p);
        }
        players.clear();

        for (Player spectator : spectators) {
            spectator.setGamemode(Player.SURVIVAL);
            playerManager.getSpectatorData(spectator).restore(spectator);
            playerManager.removeSpectatorData(spectator);
            exit(spectator);
        }
        spectators.clear();

        if (!win.isEmpty() && death) {
            for (Player p : win) {
                plugin.getLeaderboard().addStat(p, Leaderboard.Stats.WINS);
                plugin.getLeaderboard().addStat(p, Leaderboard.Stats.GAMES);
            }
        }

        for (Vector3i loc : chests) {
            BlockEntity chest = bound.getLevel().getBlockEntity(loc);
            if (chest instanceof InventoryHolder) {
                ((InventoryHolder) chest).getInventory().clearAll();
                chest.scheduleUpdate();
            }
        }
        chests.clear();
        playerChests.clear();
        String winner = String.join(", ", HGUtils.convertPlayerListToStringList(win));
        // prevent not death winners from gaining a prize
        if (death)
            HGUtils.broadcast(getLang().getPlayer_won().replace("%arena%", name).replace("%winner%", winner));

        forceRollback();
        if (maxChestsInGame != 0)
            chooseChestLocations();
        status = Status.READY;
        updateLobbyBlock();
        HG.getInstance().updateLobbyScoreboard();

        plugin.getServer().getPluginManager().callEvent(new GameEndEvent(this, win, death));
    }

    /**
     * Make a player leave the game
     *
     * @param player Player to leave the game
     * @param death  Whether the player has died or not (Generally should be false)
     */
    public void leave(Player player, Boolean death) {
        HG.getInstance().getServer().getPluginManager().callEvent(new PlayerLeaveGameEvent(this, player, death));
        players.remove(player);
        UUID uuid = player.getServerId();
        unFreeze(player);
        if (death) {
            heal(player);
            playerManager.getPlayerData(uuid).restore(player);
            playerManager.removePlayerData(player);
            exit(player);
            player.getLevel().addSound(player.getPosition(), Sound.RANDOM_TOAST, 0.5F, 0.1F, player);
            if (spectate && spectateOnDeath && !isGameOver()) {
                spectate(player);
                player.sendTitle(getName(), "You are now spectating!", 10, 100, 10); //TODO this a temp test
            }
        } else {
            heal(player);
            playerManager.getPlayerData(uuid).restore(player);
            playerManager.removePlayerData(player);
            exit(player);
        }
        updateAfterDeath(player, death);
        if(ConfigData.sb_enable)
            updateScoreboard();
    }

    private void updateAfterDeath(Player player, boolean death) {
        if (status == Status.RUNNING || status == Status.BEGINNING || status == Status.COUNTDOWN || status == Status.FINAL_COUNTDOWN || status == Status.FINAL) {
            if (isGameOver()) {
                if (!death) {
                    for (Player p : players) {
                        if (kills.get(p) >= 1) {
                            death = true;
                        }
                    }
                }
                boolean finalDeath = death;
                HG.getInstance().getServer().getScheduler().scheduleDelayedTask(plugin, () -> {
                    stop(finalDeath);
                    updateLobbyBlock();
                }, 20);

            }
        } else if (status == Status.WAITING) {
            msgAll(getLang().getPlayer_left_game().replace("%player_name%", player.getName()) +
                    (minPlayers - players.size() <= 0 ? "!" : ":" + getLang().getPlayers_to_start()
                            .replace("%amount%", String.valueOf((minPlayers - players.size())))));
        }
        updateLobbyBlock();
    }

	/* ON HOLD FOR NOW
	private boolean isGameOver() {
		if (players.size() <= 1) return true;
		for (Entry<UUID, PlayerData> f : HG.getPlugin().getPlayers().entrySet()) {

			Team t = f.getValue().getTeam();

			if (t != null && (t.getPlayers().size() >= players.size())) {
				List<UUID> ps = t.getPlayers();
				for (UUID u : players) {
					if (!ps.contains(u)) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	 */

    private boolean isGameOver() {
        return players.size() <= 1;
    }

    private void exit(Player player) {
        player.invulnerable = false;
        unFreeze(player);
        HG.getInstance().getLobbySB().showFor(player);
        if(ConfigData.hide_player_name)
            player.setNameTagAlwaysVisible(true);
        if (this.exit != null && this.getExit().getLevel() != null){
            if(ConfigData.sb_enable && this.getExit().getLevel().getId().equals(ConfigData.sb_lobby_world))
                ScoreboardAPI.setScoreboard(player, HG.getInstance().getLobbySB());
            player.teleport(this.getExit());

        }
        else
            player.teleport(HG.getInstance().getServer().getDefaultLevel().getSpawnLocation());
    }

    public boolean isLobbyValid() {
        try {
            if (s != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private static double getRandomIntegerBetweenRange(double max) {
        return (int) (Math.random() * ((max - (double) 0) + 1)) + (double) 0;
    }

    /**
     * Put a player into spectator for this game
     *
     * @param spectator The player to spectate
     */
    public void spectate(Player spectator) {
        UUID uuid = spectator.getServerId();
        if (plugin.getPlayerManager().hasPlayerData(uuid)) {
            PlayerData spectatorData = playerManager.getPlayerData(uuid);
            playerManager.addSpectatorData(spectatorData);
            playerManager.removePlayerData(uuid);
        } else {
            playerManager.addSpectatorData(new PlayerData(spectator, this));
        }
        this.spectators.add(spectator);
        spectator.setGamemode(Player.SPECTATOR);
        spectator.teleport(Location.from(this.getSpawns().get(0), bound.getLevel()));
        // spectator.getInventory().setItem(0, ItemManager.getSpectatorCompass());
    }

    /**
     * Remove a player from spectator of this game
     *
     * @param spectator The player to remove
     */
    public void leaveSpectate(Player spectator) {
        exit(spectator);
        UUID uuid = spectator.getServerId();
        spectator.setGamemode(Player.SURVIVAL);
        playerManager.getSpectatorData(uuid).restore(spectator);
        playerManager.removeSpectatorData(uuid);
        spectators.remove(spectator);
    }

    public void chooseChestLocations() {
        List<Vector3i> copyChestLocation = new LinkedList<>(chestLocations);
        Collections.shuffle(copyChestLocation);
        int chestsToCreate = ThreadLocalRandom.current().nextInt(minChestsInGame, maxChestsInGame + 1);
        int currChests = 0;
        Level level = bound.getLevel();
        for (Vector3i loc : copyChestLocation) {
            if (currChests < chestsToCreate) {
                if (new Random().nextDouble() <= bonusChestChance / 100D) {
                    BlockEntity entity = level.getBlockEntity(loc);
                    if (entity != null && !entity.isClosed())
                        entity.close();
                    level.setBlock(loc, Block.get(BlockIds.BARREL), true, true);
                    BlockEntityRegistry.get().newEntity(BARREL, level.getChunk(loc), loc);
                } else {
                    level.setBlock(loc, Block.get(BlockIds.CHEST), true, true);
                    BlockEntityRegistry.get().newEntity(CHEST, level.getChunk(loc), loc);
                }
                currChests++;
            } else {
                level.setBlock(loc, Block.get(BlockIds.AIR), true, true);
                BlockEntity entity = level.getBlockEntity(loc);
                if (entity != null && !entity.isClosed())
                    entity.close();
            }
        }
    }
    public void updateScoreboard(){
        String alive_title = HGUtils.colorize(lang.getSb_game_alive());
        String alive_count = HGUtils.colorize(lang.getSb_game_alive_count()).replace("%players_count%", players.size() +"/"+maxPlayers);
        String kills_title = HGUtils.colorize(lang.getSb_game_kills());
        String time_title = HGUtils.colorize(lang.getSb_game_time());
        int remainingTime = timer.getRemainingtime();
        String timer;
        if(remainingTime == time){
            String minutes = String.valueOf(remainingTime/60);
            String seconds = String.valueOf(remainingTime%60);
            timer = (minutes.length() == 1 ? "0"+minutes : minutes)+":"+(seconds.length() == 1 ? "0"+seconds : seconds);
        }
        else{
            timer = "FreeRoam!";
        }
        String time_value = HGUtils.colorize(lang.getSb_game_time_value()).replace("%timer%", timer);
        for(Player player : players){
            Scoreboard scoreboard = ScoreboardAPI.createScoreboard();
            ScoreboardDisplay display = scoreboard.addDisplay(DisplaySlot.SIDEBAR, this.getName(), HGUtils.colorize(lang.getLine_1()));
            display.addLine(alive_title, 0);
            display.addLine(alive_count, 1);
            display.addLine(kills_title, 2);
            display.addLine(HGUtils.colorize(lang.getSb_game_alive_count()).replace("%player_kills%", String.valueOf(kills.get(player))), 3);
            display.addLine(time_title, 4);
            display.addLine(timer, 5);
            if(scoreboards.containsKey(player))
                scoreboards.get(player).hideFor(player);
            scoreboard.showFor(player);
            scoreboards.put(player, scoreboard);
        }
        for(Player player : spectators){
            Scoreboard scoreboard = ScoreboardAPI.createScoreboard();
            ScoreboardDisplay display = scoreboard.addDisplay(DisplaySlot.SIDEBAR, this.getName(), HGUtils.colorize(lang.getLine_1()));
            display.addLine(alive_title, 0);
            display.addLine(alive_count, 1);
            display.addLine(kills_title, 2);
            display.addLine(HGUtils.colorize(lang.getSb_game_alive_count()).replace("%player_kills%", String.valueOf(kills.get(player))), 3);
            display.addLine(time_title, 4);
            display.addLine(timer, 5);
            if(scoreboards.containsKey(player))
                scoreboards.get(player).hideFor(player);
            scoreboard.showFor(player);
            scoreboards.put(player, scoreboard);
        }
    }
}
