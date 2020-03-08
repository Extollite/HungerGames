package pl.extollite.hungergames.data;

import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.utils.Utils;
import lombok.Getter;
import pl.extollite.hungergames.HG;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Getter
public class Language {
    //General Translations
    private String prefix;
    private String cmd_no_permission;

    //Game Message Translations
    private String player_joined_game;
    private String player_left_game;
    private String game_started;
    private String game_join;
    private String game_countdown;
    private String game_almost_over;
    private String game_ending_minsec;
    private String game_ending_min;
    private String game_ending_sec;
    private String game_teleport;
    private String game_teleported;
    private String game_chest_refill;
    private String players_to_start;
    private String arena_not_ready;
    private String arena_spectate;
    private String game_full;
    private String player_won;


    // Command Translations
    private String cmd_description;
    private List<String> cmd_admin_usage;
    private String cmd_admin_wand_description;

    // Admin Command Translations
    private String cmd_admin_description;
    private List<String> cmd_usage;

    private String cmd_sign_set;

    private String cmd_handler_nocmd;
    private String cmd_handler_playing;

    private String cmd_delete_attempt;
    private String cmd_delete_kicking;
    private String cmd_delete_deleted;
    private String cmd_delete_failed;
    private String cmd_delete_noexist;
    
    // Status Translations
    private String status_running;
    private String status_stopped;
    private String status_ready;
    private String status_waiting;
    private String status_broken;
    private String status_rollback;
    private String status_not_ready;
    private String status_beginning;
    private String status_countdown;
    private String status_final;


    //Bossbar Translations
    private String bossbar;

    //Lobby Sign Translations
    private String line_1;
    private String line_2;
    private String line_3;
    private String line_4_join;
    private String line_4_spectate;

    //Spectator Translations
    private String spectator_menu_title;
    private String spectator_menu_content;
    private String spectator_compass;

    //Roam Translations
    private String roam_game_started;
    private String roam_time;
    private String roam_finished;
    
    //Death Translations
    private List<String> death_fell_accident_generic;
    private List<String> death_attack_inFire;
    private List<String> death_attack_onFire;
    private List<String> death_attack_lava;
    private List<String> death_attack_lava_magma;
    private List<String> death_attack_inWall;
    private List<String> death_attack_drown;
    private List<String> death_attack_cactus;
    private List<String> death_attack_generic;
    private List<String> death_attack_explosion;
    private List<String> death_attack_explosion_player;
    private List<String> death_attack_magic;
    private List<String> death_attack_wither;
    private List<String> death_attack_mob;
    private List<String> death_attack_player;
    private List<String> death_attack_player_item;
    private List<String> death_attack_arrow;
    private List<String> death_attack_arrow_item;
    private List<String> death_attack_fall;
    private List<String> death_attack_outOfWorld;
    private List<String> death_attack_starve;

    private String listener_not_running;
    private String listener_no_edit_block;
    private String listener_no_interact;
    private String listener_sign_click_hand;

    public Language() {
        Config langFile = new Config(HG.getInstance().getDataFolder()+"/lang/"+ConfigData.lang+".yml", Config.YAML);
        if(langFile.getAll().isEmpty()){
            langFile.load(HG.getInstance().getResource("lang"+"/"+ConfigData.lang+".yml"));
            langFile.save();
        }
        prefix = langFile.getString("prefix");
        player_joined_game = langFile.getString("player-joined-game");
        player_left_game = langFile.getString("player-left-game");
        game_started = langFile.getString("game-started");
        game_join = langFile.getString("game-join");
        game_countdown = langFile.getString("game-countdown");
        game_almost_over = langFile.getString("game-almost-over");
        game_ending_minsec = langFile.getString("game-ending-minsec");
        game_ending_min = langFile.getString("game-ending-min");
        game_ending_sec = langFile.getString("game-ending-sec");
        game_teleport = langFile.getString("game-teleport");
        game_teleported = langFile.getString("game-teleported");

        game_chest_refill = langFile.getString("game-chests-refill");

        bossbar = langFile.getString("game-bossbar");

        players_to_start = langFile.getString("players-to-start");
        arena_not_ready = langFile.getString("arena-not-ready");
        arena_spectate = langFile.getString("arena-spectate");
        game_full = langFile.getString("game-full");
        player_won = langFile.getString("player-won");
        
        cmd_description = langFile.getString("cmd-description");
        cmd_usage = langFile.getStringList("cmd-usage");

        cmd_admin_description = langFile.getString("cmd-admin-description");
        cmd_no_permission = langFile.getString("cmd-no-permission");
        cmd_admin_usage = langFile.getStringList("cmd-admin-usage");
        cmd_admin_wand_description = langFile.getString("cmd-admin-wand-description");
        cmd_admin_wand_description = langFile.getString("cmd-sign-set");

        cmd_handler_nocmd = langFile.getString("cmd-handler-nocmd");
        cmd_handler_playing = langFile.getString("cmd-handler-playing");

        cmd_delete_attempt = langFile.getString("cmd-delete-attempt");
        cmd_delete_kicking = langFile.getString("cmd-delete-kicking");
        cmd_delete_deleted = langFile.getString("cmd-delete-deleted");
        cmd_delete_failed = langFile.getString("cmd-delete-failed");
        cmd_delete_noexist = langFile.getString("cmd-delete-noexist");

        status_running = langFile.getString("status-running");
        status_stopped = langFile.getString("status-stopped");
        status_ready = langFile.getString("status-ready");
        status_waiting = langFile.getString("status-waiting");
        status_broken = langFile.getString("status-broken");
        status_rollback = langFile.getString("status-rollback");
        status_not_ready = langFile.getString("status-notready");
        status_beginning = langFile.getString("status-beginning");
        status_countdown = langFile.getString("status-countdown");
        status_final = langFile.getString("status-final");

        line_1 = langFile.getString("lobby-sign.line-1");
        line_2 = langFile.getString("lobby-sign.line-2");
        line_3 = langFile.getString("lobby-sign.line-3");
        line_4_join = langFile.getString("lobby-sign.line-4-join");
        line_4_spectate = langFile.getString("lobby-sign.line-4-spectate");

        spectator_menu_title = langFile.getString("spectator-menu-title");
        spectator_menu_content = langFile.getString("spectator-menu-content");
        spectator_compass = langFile.getString("spectator-compass");

        roam_game_started = langFile.getString("roam-game-started");
        roam_time = langFile.getString("roam-time");
        roam_finished = langFile.getString("roam-finished");

         death_fell_accident_generic = langFile.getStringList("death-fell-accident-generic");
         death_attack_inFire = langFile.getStringList("death-attack-inFire");
         death_attack_onFire = langFile.getStringList("death-attack-onFire");
         death_attack_lava = langFile.getStringList("death-attack-lava");
         death_attack_lava_magma = langFile.getStringList("death-attack-lava-magma");
         death_attack_inWall = langFile.getStringList("death-attack-inWall");
         death_attack_drown = langFile.getStringList("death-attack-drown");
         death_attack_cactus = langFile.getStringList("death-attack-cactus");
         death_attack_generic = langFile.getStringList("death-attack-generic");
         death_attack_explosion = langFile.getStringList("death-attack-explosion");
         death_attack_explosion_player = langFile.getStringList("death-attack-explosion-player");
         death_attack_magic = langFile.getStringList("death-attack-magic");
         death_attack_wither = langFile.getStringList("death-attack-wither");
         death_attack_mob = langFile.getStringList("death-attack-mob");
         death_attack_player = langFile.getStringList("death-attack-player");
         death_attack_player_item = langFile.getStringList("death-attack-player-item");
         death_attack_arrow = langFile.getStringList("death-attack-arrow");
         death_attack_arrow_item = langFile.getStringList("death-attack-arrow-item");
         death_attack_fall = langFile.getStringList("death-attack-fall");
         death_attack_outOfWorld = langFile.getStringList("death-attack-outOfWorld");
         death_attack_starve = langFile.getStringList("death-attack-starve");

        listener_not_running = langFile.getString("listener-not-running");
        listener_no_edit_block = langFile.getString("listener-no-edit-block");
        listener_no_interact = langFile.getString("listener-no-interact");
        listener_sign_click_hand = langFile.getString("listener-sign-click-hand");
    }
    
    public List<String> getDeathMessage(String name){
        switch (name){
            case "death.fell.accident.generic":
                return death_fell_accident_generic;
            case "death.attack.inFire":
                return death_attack_inFire;
            case "death.attack.onFire":
                return death_attack_onFire;
            case "death.attack.lava":
                return death_attack_lava;
            case "death.attack.lava.magma":
                return death_attack_lava_magma;
            case "death.attack.inWall":
                return death_attack_inWall;
            case "death.attack.drown":
                return death_attack_drown;
            case "death.attack.cactus":
                return death_attack_cactus;
            case "death.attack.generic":
                return death_attack_generic;
            case "death.attack.explosion":
                return death_attack_explosion;
            case "death.attack.explosion.player":
                return death_attack_explosion_player;
            case "death.attack.magic":
                return death_attack_magic;
            case "death.attack.wither":
                return death_attack_wither;
            case "death.attack.mob":
                return death_attack_mob;
            case "death.attack.player":
                return death_attack_player;
            case "death.attack.player.item":
                return death_attack_player_item;
            case "death.attack.arrow":
                return death_attack_arrow;
            case "death.attack.arrow.item":
                return death_attack_arrow_item;
            case "death.attack.fall":
                return death_attack_fall;
            case "death.attack.outOfWorld":
                return death_attack_outOfWorld;
            case "death.attack.starve":
                return death_attack_starve;
            default:
                return null;
        }
    }
}
