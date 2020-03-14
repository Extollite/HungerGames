package pl.extollite.hungergames;

import cn.nukkit.item.Item;
import cn.nukkit.player.Player;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import lombok.Getter;
import pl.extollite.hungergames.command.admin.*;
import pl.extollite.hungergames.command.user.*;
import pl.extollite.hungergames.data.*;
import pl.extollite.hungergames.game.Game;
import pl.extollite.hungergames.listener.CancelListener;
import pl.extollite.hungergames.listener.GameListener;
import pl.extollite.hungergames.listener.SpectatorWindowListener;
import pl.extollite.hungergames.listener.WandListener;
import pl.extollite.hungergames.manager.PlayerManager;

import java.util.*;

@Getter
public class HG extends PluginBase {
    private static HG instance;
    public static HG getInstance() {
        return instance;
    }


    private Language language;
    private PlayerManager playerManager;
    private Leaderboard leaderboard;

    private HGACommand mainAdminCommand;
    private HGCommand mainCommand;

    private Map<UUID, WandLocations> wandLocationsMap = new HashMap<>();
    private List<Game> games = new LinkedList<>();
    private Map<Integer, Item> items = new HashMap<>();
    private Map<Integer, Item> bonusItems = new HashMap<>();

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        instance = this;
        List<String> authors = this.getDescription().getAuthors();
        this.getLogger().info(TextFormat.DARK_GREEN + "Plugin by " + String.join(", ", authors));
        ConfigData.load(this.getConfig());

        language = new Language();
        playerManager = new PlayerManager();
        leaderboard = new Leaderboard();
        RandomItems.load();
        ArenaData.load();

        getServer().getPluginManager().registerEvents(new WandListener(), this);
        getServer().getPluginManager().registerEvents(new CancelListener(), this);
        getServer().getPluginManager().registerEvents(new SpectatorWindowListener(), this);
        getServer().getPluginManager().registerEvents(new GameListener(), this);

        loadCommmands();
    }

    @Override
    public void onDisable() {
        stopAll();
        instance = null;
        playerManager = null;
        language = null;
        leaderboard = null;
        this.getLogger().info(TextFormat.RED +"HG has been disabled!");
    }

    private void loadCommmands(){
        mainAdminCommand = new HGACommand();
        mainCommand = new HGCommand();
        this.getServer().getCommandMap().register(this, this.mainAdminCommand);
        this.getServer().getCommandMap().register(this, mainCommand);
        if(ConfigData.only_main_commands){
            mainCommand.registerCommand(new JoinCommand());
            mainCommand.registerCommand(new LeaveCommand());
            mainCommand.registerCommand(new ListCommand());
            mainCommand.registerCommand(new ListGamesCommand());
            mainCommand.registerCommand(new SpectateCommand());
            
            mainAdminCommand.registerCommand(new WandCommand());
            mainAdminCommand.registerCommand(new CreateCommand());
            mainAdminCommand.registerCommand(new AddSpawnCommand());
            mainAdminCommand.registerCommand(new SetSignCommand());
            mainAdminCommand.registerCommand(new SetExitCommand());
            mainAdminCommand.registerCommand(new DeleteCommand());
            mainAdminCommand.registerCommand(new ToggleCommand());
            mainAdminCommand.registerCommand(new ForceStartCommand());
            mainAdminCommand.registerCommand(new ForceStopCommand());
            mainAdminCommand.registerCommand(new SetChestRefillTimeCommand());
            mainAdminCommand.registerCommand(new ChestRefillNowCommand());
            mainAdminCommand.registerCommand(new ReloadCommand());
            mainAdminCommand.registerCommand(new TeleportWorldCommand());
        }
        else{
            JoinCommand joinCommand = new JoinCommand();
            mainCommand.registerCommand(joinCommand);
            this.getServer().getCommandMap().register(this, joinCommand);
            LeaveCommand leaveCommand = new LeaveCommand();
            mainCommand.registerCommand(leaveCommand);
            this.getServer().getCommandMap().register(this, leaveCommand);
            ListCommand listCommand = new ListCommand();
            mainCommand.registerCommand(listCommand);
            this.getServer().getCommandMap().register(this, listCommand);
            ListGamesCommand listgamesCommand = new ListGamesCommand();
            mainCommand.registerCommand(listgamesCommand);
            this.getServer().getCommandMap().register(this, listgamesCommand);
            SpectateCommand spectateCommand = new SpectateCommand();
            mainCommand.registerCommand(spectateCommand);
            this.getServer().getCommandMap().register(this, spectateCommand);
            
            WandCommand wandCommand = new WandCommand();
            mainAdminCommand.registerCommand(wandCommand);
            this.getServer().getCommandMap().register(this, wandCommand);
            CreateCommand createCommand = new CreateCommand();
            mainAdminCommand.registerCommand(createCommand);
            this.getServer().getCommandMap().register(this, createCommand);
            AddSpawnCommand addSpawnCommand = new AddSpawnCommand();
            mainAdminCommand.registerCommand(addSpawnCommand);
            this.getServer().getCommandMap().register(this, addSpawnCommand);
            SetSignCommand setSignCommand = new SetSignCommand();
            mainAdminCommand.registerCommand(setSignCommand);
            this.getServer().getCommandMap().register(this, setSignCommand);
            SetExitCommand setExitCommand = new SetExitCommand();
            mainAdminCommand.registerCommand(setExitCommand);
            this.getServer().getCommandMap().register(this, setExitCommand);
            DeleteCommand deleteCommand = new DeleteCommand();
            mainAdminCommand.registerCommand(deleteCommand);
            this.getServer().getCommandMap().register(this, deleteCommand);
            ToggleCommand toggleCommand = new ToggleCommand();
            mainAdminCommand.registerCommand(toggleCommand);
            this.getServer().getCommandMap().register(this, toggleCommand);
            ForceStartCommand forceStartCommand = new ForceStartCommand();
            mainAdminCommand.registerCommand(forceStartCommand);
            this.getServer().getCommandMap().register(this, forceStartCommand);
            ForceStopCommand forceStopCommand = new ForceStopCommand();
            mainAdminCommand.registerCommand(forceStopCommand);
            this.getServer().getCommandMap().register(this, forceStopCommand);
            SetChestRefillTimeCommand setChestRefillTimeCommand = new SetChestRefillTimeCommand();
            mainAdminCommand.registerCommand(setChestRefillTimeCommand);
            this.getServer().getCommandMap().register(this, setChestRefillTimeCommand);
            ChestRefillNowCommand chestRefillNowCommand = new ChestRefillNowCommand();
            mainAdminCommand.registerCommand(chestRefillNowCommand);
            this.getServer().getCommandMap().register(this, chestRefillNowCommand);
            ReloadCommand reloadCommand = new ReloadCommand();
            mainAdminCommand.registerCommand(reloadCommand);
            this.getServer().getCommandMap().register(this, reloadCommand);
            TeleportWorldCommand teleportWorldCommand = new TeleportWorldCommand();
            mainAdminCommand.registerCommand(teleportWorldCommand);
            this.getServer().getCommandMap().register(this, teleportWorldCommand);
        }
    }

    public void stopAll() {
        ArrayList<Player> ps = new ArrayList<>();
        for (Game g : games) {
            g.cancelTasks();
            g.forceRollback();
            ps.addAll(g.getPlayers());
            ps.addAll(g.getSpectators());
        }
        for (Player player : ps) {
            if (playerManager.hasPlayerData(player))
                playerManager.getPlayerData(player).getGame().leave(player, false);
            if (playerManager.hasSpectatorData(player))
                playerManager.getSpectatorData(player).getGame().leaveSpectate(player);
        }
        games.clear();
        items.clear();
        bonusItems.clear();
    }
}
