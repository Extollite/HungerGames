package pl.extollite.hungergames.tasks;


import cn.nukkit.player.Player;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.HGUtils.HGUtils;
import pl.extollite.hungergames.data.ConfigData;
import pl.extollite.hungergames.game.Game;
import pl.extollite.hungergames.game.Status;

import java.util.Optional;
import java.util.UUID;

public class FinalTask implements Runnable {

    private int timer;
    private int teleportCountdownEnd;
    private int id;
    private Game game;

    public FinalTask(Game g) {
        this.timer = ConfigData.finalCountdownStart - ConfigData.finalCountdownEnd + ConfigData.finalFreeze;
        this.teleportCountdownEnd = timer - ConfigData.finalFreeze;
        this.game = g;
        HGUtils.broadcast(HG.getInstance().getLanguage().getGame_started().replace("%arena%", g.getName()));
        HGUtils.broadcast(HG.getInstance().getLanguage().getGame_join().replace("%arena%", g.getName()));

        this.id = HG.getInstance().getServer().getScheduler().scheduleRepeatingTask(HG.getInstance(),this, timer * 20).getTaskId();
    }

    @Override
    public void run() {
        if (timer > teleportCountdownEnd) {
            game.msgAll(HG.getInstance().getLanguage().getGame_teleport().replace("%timer%", String.valueOf(timer - ConfigData.finalFreeze)));
        } else if(timer == teleportCountdownEnd){
            game.startFinal();
        } else if (timer == 0){
            for (Player p : game.getPlayers()) {
                p.sendTitle("Fight!");
                game.unFreeze(p);
                game.setStatus(Status.FINAL);
            }
            stop();
        } else{
            game.titleAll(String.valueOf(timer));
        }
        timer--;
    }

    public void stop() {
        HG.getInstance().getServer().getScheduler().cancelTask(id);
    }
}
