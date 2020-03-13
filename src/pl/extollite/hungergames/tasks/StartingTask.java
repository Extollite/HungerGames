package pl.extollite.hungergames.tasks;


import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.HGUtils.HGUtils;
import pl.extollite.hungergames.game.Game;

public class StartingTask implements Runnable {

    private int timer;
    private int id;
    private Game game;

    public StartingTask(Game g) {
        this.timer = 30;
        this.game = g;
        HGUtils.broadcast(HG.getInstance().getLanguage().getGame_started().replace("%arena%", g.getName()));
        HGUtils.broadcast(HG.getInstance().getLanguage().getGame_join().replace("%arena%", g.getName()));

        this.id = HG.getInstance().getServer().getScheduler().scheduleDelayedRepeatingTask(HG.getInstance(), this, 5 * 20, 20).getTaskId();
    }

    @Override
    public void run() {

        if (timer <= 0) {
            game.startFreeRoam();
            stop();
        } else if (timer > 5 && timer % 5 == 0) {
            game.msgAll(HG.getInstance().getLanguage().getGame_countdown().replace("%timer%", String.valueOf(timer)));
        } else if(timer <= 5){
            game.titleAll(String.valueOf(timer));
        }
        timer--;
    }

    public void stop() {
        HG.getInstance().getServer().getScheduler().cancelTask(id);
    }
}
