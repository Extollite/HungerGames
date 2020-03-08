package pl.extollite.hungergames;

import cn.nukkit.item.Item;
import cn.nukkit.player.Player;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import lombok.Getter;
import pl.extollite.hungergames.command.admin.HGACommand;
import pl.extollite.hungergames.command.admin.WandCommand;
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

    private Map<UUID, WandLocations> wandLocationsMap = new HashMap<>();
    private List<Game> games = new LinkedList<>();
    private Map<Integer, Item> items = new HashMap<>();
    private Map<Integer, Item> bonusItems = new HashMap<>();

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        instance = this;
        List<String> authors = this.getDescription().getAuthors();
        this.getLogger().info(TextFormat.DARK_GREEN + "Plugin by " + authors.get(0));
        new ConfigData(this.getConfig());

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
        this.getServer().getCommandMap().register(this, this.mainAdminCommand);
        if(ConfigData.only_main_commands){
            mainAdminCommand.registerCommand(new WandCommand());
        }
        else{
            WandCommand wandCommand = new WandCommand();
            mainAdminCommand.registerCommand(new WandCommand());
            this.getServer().getCommandMap().register(this, wandCommand);
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
