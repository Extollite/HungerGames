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
    private String game_final_border;


    // Command Translations
    private List<String> cmd_admin_usage;


    // Admin Command Translations
    private List<String> cmd_usage;

    private String cmd_sign_set;

    private String cmd_handler_nocmd;
    private String cmd_handler_playing;

    private String cmd_create_need_selection;
    private String cmd_create_divisible_1;
    private String cmd_create_divisible_2;
    private String cmd_create_minmax;
    private String cmd_create_created;

    private String cmd_spawn_same;
    private String cmd_spawn_set;

    private String cmd_exit_set;

    private String cmd_delete_attempt;
    private String cmd_delete_kicking;
    private String cmd_delete_deleted;
    private String cmd_delete_failed;
    private String cmd_delete_noexist;

    private String cmd_join_in_game;

    private String cmd_leave_left;
    private String cmd_leave_not_in_game;

    private String cmd_reload_attempt;
    private String cmd_reload_reloaded_arena;
    private String cmd_reload_reloaded_kit;
    private String cmd_reload_reloaded_items;
    private String cmd_reload_reloaded_config;
    private String cmd_reload_reloaded_success;

    private String cmd_toggle_locked;
    private String cmd_toggle_unlocked;

    private String cmd_start_starting;
    private String cmd_stop_all;
    private String cmd_stop_arena;
    private String cmd_stop_noexist;

    private String cmd_chest_refill;
    private String cmd_chest_refill_now;

    private String cmd_chestrandom_set;
    private String cmd_chestrandom_added;
    private String cmd_chestrandom_same;

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

    //Lobby Sign Translations
    private String line_1;
    private String line_2;
    private String line_3;
    private String line_4_join;
    private String line_4_spectate;

    //Scoreboard translations
    private String sb_lobby_title;

    private String sb_game_alive;
    private String sb_game_alive_count;
    private String sb_game_kills;
    private String sb_game_kills_count;
    private String sb_game_time;
    private String sb_game_time_value;


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
        Config langFile = new Config(HG.getInstance().getDataFolder() + "/lang/" + ConfigData.lang + ".yml", Config.YAML);
        if (langFile.getAll().isEmpty()) {
            langFile.load(HG.getInstance().getResource("lang" + "/" + ConfigData.lang + ".yml"));
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
        game_final_border = langFile.getString("game-final-border");

        game_chest_refill = langFile.getString("game-chests-refill");

        players_to_start = langFile.getString("players-to-start");
        arena_not_ready = langFile.getString("arena-not-ready");
        arena_spectate = langFile.getString("arena-spectate");
        game_full = langFile.getString("game-full");
        player_won = langFile.getString("player-won");

        cmd_usage = langFile.getStringList("cmd-usage");

        cmd_no_permission = langFile.getString("cmd-no-permission");
        cmd_admin_usage = langFile.getStringList("cmd-admin-usage");

        cmd_sign_set = langFile.getString("cmd-sign-set");

        cmd_exit_set = langFile.getString("cmd-exit-set");

        cmd_handler_nocmd = langFile.getString("cmd-handler-nocmd");
        cmd_handler_playing = langFile.getString("cmd-handler-playing");

        cmd_create_need_selection = langFile.getString("cmd-create-need-selection");
        cmd_create_divisible_1 = langFile.getString("cmd-create-divisible-1");
        cmd_create_divisible_2 = langFile.getString("cmd-create-divisible-2");
        cmd_create_minmax = langFile.getString("cmd-create-minmax");
        cmd_create_created = langFile.getString("cmd-create-created");

        cmd_spawn_same = langFile.getString("cmd-spawn-same");
        cmd_spawn_set = langFile.getString("cmd-spawn-set");

        cmd_delete_attempt = langFile.getString("cmd-delete-attempt");
        cmd_delete_kicking = langFile.getString("cmd-delete-kicking");
        cmd_delete_deleted = langFile.getString("cmd-delete-deleted");
        cmd_delete_failed = langFile.getString("cmd-delete-failed");
        cmd_delete_noexist = langFile.getString("cmd-delete-noexist");

        cmd_join_in_game = langFile.getString("cmd-join-in-game");

        cmd_leave_left = langFile.getString("cmd-leave-left");
        cmd_leave_not_in_game = langFile.getString("cmd-leave-not-in-game");

        cmd_reload_attempt = langFile.getString("cmd-reload-attempt");
        cmd_reload_reloaded_arena = langFile.getString("cmd-reload-reloaded-arena");
        cmd_reload_reloaded_config = langFile.getString("cmd-reload-reloaded-config");
        cmd_reload_reloaded_items = langFile.getString("cmd-reload-reloaded-items");
        cmd_reload_reloaded_kit = langFile.getString("cmd-reload-reloaded-kit");
        cmd_reload_reloaded_success = langFile.getString("cmd-reload-reloaded-success");

        cmd_start_starting = langFile.getString("cmd-start-starting");
        cmd_stop_all = langFile.getString("cmd-stop-all");
        cmd_stop_arena = langFile.getString("cmd-stop-arena");
        cmd_stop_noexist = langFile.getString("cmd-stop-noexist");

        cmd_toggle_unlocked = langFile.getString("cmd-toggle-unlocked");
        cmd_toggle_locked = langFile.getString("cmd-toggle-locked");

        game_chest_refill = langFile.getString("game-chests-refill");
        cmd_chest_refill = langFile.getString("cmd-chestrefill-set");
        cmd_chest_refill_now = langFile.getString("cmd-chestrefill-now");

        cmd_chestrandom_set = langFile.getString("cmd-chestrandom-set");
        cmd_chestrandom_added = langFile.getString("cmd-chestrandom-added");
        cmd_chestrandom_same = langFile.getString("cmd-chestrandom-same");

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

        sb_lobby_title = langFile.getString("sb-lobby-title");

        sb_game_alive = langFile.getString("sb-game-alive");
        sb_game_alive_count = langFile.getString("sb-game-alive-count");
        sb_game_kills = langFile.getString("sb-game-kills");
        sb_game_kills_count = langFile.getString("sb-game-kills-count");
        sb_game_time = langFile.getString("sb-game-time");
        sb_game_time_value = langFile.getString("sb-game-time-value");

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

    public List<String> getDeathMessage(String name) {
        switch (name) {
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
