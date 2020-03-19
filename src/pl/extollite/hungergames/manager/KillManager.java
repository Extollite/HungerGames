package pl.extollite.hungergames.manager;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockIds;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.impl.EntityLiving;
import cn.nukkit.event.entity.EntityDamageByBlockEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.locale.TranslationContainer;
import cn.nukkit.player.Player;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.data.Leaderboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class KillManager {
    public static String getDeathMessage(EntityDamageEvent cause){
        String message = "death.attack.generic";

        List<String> params = new ArrayList<>();
        Player player = ((Player)cause.getEntity());
        params.add(player.getDisplayName());
        Entity killer = null;
        switch (cause.getCause()) {
            case ENTITY_ATTACK:
                if (cause instanceof EntityDamageByEntityEvent) {
                    Entity e = ((EntityDamageByEntityEvent) cause).getDamager();
                    killer = e;
                    if (e instanceof Player) {
                        message = "death.attack.player";
                        params.add(((Player) e).getDisplayName());
                        break;
                    } else if (e instanceof EntityLiving) {
                        message = "death.attack.mob";
                        params.add(e.getName());
                        break;
                    } else {
                        params.add("Unknown");
                    }
                }
                break;
            case PROJECTILE:
                if (cause instanceof EntityDamageByEntityEvent) {
                    Entity e = ((EntityDamageByEntityEvent) cause).getDamager();
                    killer = e;
                    if (e instanceof Player) {
                        message = "death.attack.arrow";
                        params.add(((Player) e).getDisplayName());
                    } else if (e instanceof EntityLiving) {
                        message = "death.attack.arrow";
                        params.add(e.getName());
                        break;
                    } else {
                        params.add("Unknown");
                    }
                }
                break;
            case SUICIDE:
                message = "death.attack.generic";
                break;
            case VOID:
                message = "death.attack.outOfWorld";
                break;
            case FALL:
                if (cause != null) {
                    if (cause.getFinalDamage() > 2) {
                        message = "death.fell.accident.generic";
                        break;
                    }
                }
                message = "death.attack.fall";
                break;

            case SUFFOCATION:
                message = "death.attack.inWall";
                break;

            case LAVA:
                Block block = player.getLevel().getBlock(player.getPosition().add(0, -1, 0));
                if (block.getId() == BlockIds.MAGMA) {
                    message = "death.attack.lava.magma";
                    break;
                }
                message = "death.attack.lava";
                break;

            case FIRE:
                message = "death.attack.onFire";
                break;

            case FIRE_TICK:
                message = "death.attack.inFire";
                break;

            case DROWNING:
                message = "death.attack.drown";
                break;

            case CONTACT:
                if (cause instanceof EntityDamageByBlockEvent) {
                    if (((EntityDamageByBlockEvent) cause).getDamager().getId() == BlockIds.CACTUS) {
                        message = "death.attack.cactus";
                    }
                }
                break;

            case BLOCK_EXPLOSION:
            case ENTITY_EXPLOSION:
                if (cause instanceof EntityDamageByEntityEvent) {
                    Entity e = ((EntityDamageByEntityEvent) cause).getDamager();
                    killer = e;
                    if (e instanceof Player) {
                        message = "death.attack.explosion.player";
                        params.add(((Player) e).getDisplayName());
                    } else if (e instanceof EntityLiving) {
                        message = "death.attack.explosion.player";
                        params.add(e.getName());
                        break;
                    }
                } else {
                    message = "death.attack.explosion";
                }
                break;

            case MAGIC:
                message = "death.attack.magic";
                break;

            case HUNGER:
                message = "death.attack.starve";
                break;

            case CUSTOM:
                break;

            default:
                break;

        }
        if(killer instanceof Player){
            HG.getInstance().getLeaderboard().addStat((Player)killer, Leaderboard.Stats.KILLS);
        }
        List<String> messages = HG.getInstance().getLanguage().getDeathMessage(message);
        if(messages == null){
            return message;
        }
        String newMsg = messages.get(ThreadLocalRandom.current().nextInt(0, messages.size()));
        for(int i = 0; i < params.size(); i++){
            newMsg = newMsg.replace("{%"+i+"}", params.get(i));
        }
        return newMsg;
    }
}
