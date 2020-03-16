package pl.extollite.hungergames.listener;

import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.player.Player;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.hgutils.HGItemIds;
import pl.extollite.hungergames.hgutils.HGUtils;
import pl.extollite.hungergames.data.WandLocations;
import pl.extollite.hungergames.game.Game;

public class WandListener implements Listener {
    public WandListener() {

    }

    @EventHandler
    private void onSelection(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerInteractEvent.Action action = event.getAction();
        if (event.getBlock() != null && action.equals(PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)) {
            if (!isWand(event.getItem())) return;
            if (!HG.getInstance().getWandLocationsMap().containsKey(player.getServerId())) return;
            Block b = event.getBlock();
            Location l = Location.from(b.getX(), b.getY(), b.getZ(), b.getLevel());
            event.setCancelled();
            for (Game game : HG.getInstance().getGames()) {
                if (game.getBound().isInRegion(l)) {
                    HGUtils.sendMessage(player, "&cThis location is already within an arena");
                    return;
                }
            }
            WandLocations wandLoc = HG.getInstance().getWandLocationsMap().get(player.getServerId());
            wandLoc.setLoc2(l);
            HGUtils.sendMessage(player, "Pos2: " + l.getX() + ", " + l.getY() + ", " + l.getZ());
            if (!wandLoc.hasValidSelection()) {
                HGUtils.sendMessage(player, "Now you need to set position 1!");
            }
        } else if (event.getBlock() != null && action.equals(PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)) {
            if (!isWand(event.getItem())) return;
            if (!HG.getInstance().getWandLocationsMap().containsKey(player.getServerId())) return;
            Block b = event.getBlock();
            Location l = Location.from(b.getX(), b.getY(), b.getZ(), b.getLevel());
            event.setCancelled();
            for (Game game : HG.getInstance().getGames()) {
                if (game.getBound().isInRegion(l)) {
                    HGUtils.sendMessage(player, "&cThis location is already within an arena");
                    return;
                }
            }
            WandLocations wandLoc = HG.getInstance().getWandLocationsMap().get(player.getServerId());
            wandLoc.setLoc1(l);
            HGUtils.sendMessage(player, "Pos1: " + l.getX() + ", " + l.getY() + ", " + l.getZ());
            if (!wandLoc.hasValidSelection()) {
                HGUtils.sendMessage(player, "Now you need to set position 2!");
            }
        }
    }

    @EventHandler
    public void onSelectionByDestroy(BlockBreakEvent event){
        if(event.isCancelled())
            return;
        Player player = event.getPlayer();
        if (event.getBlock() != null) {
            if (!isWand(event.getItem())) return;
            if (!HG.getInstance().getWandLocationsMap().containsKey(player.getServerId())) return;
            Block b = event.getBlock();
            Location l = Location.from(b.getX(), b.getY(), b.getZ(), b.getLevel());
            event.setCancelled();
            for (Game game : HG.getInstance().getGames()) {
                if (game.getBound().isInRegion(l)) {
                    HGUtils.sendMessage(player, "&cThis location is already within an arena");
                    return;
                }
            }
            WandLocations wandLoc = HG.getInstance().getWandLocationsMap().get(player.getServerId());
            wandLoc.setLoc1(l);
            HGUtils.sendMessage(player, "Pos1: " + l.getX() + ", " + l.getY() + ", " + l.getZ());
            if (!wandLoc.hasValidSelection()) {
                HGUtils.sendMessage(player, "Now you need to set position 2!");
            }
        }
    }

    private boolean isWand(Item item){
        return item.getTag().contains("HGItemID") && item.getTag().getInt("HGItemID") == HGItemIds.WAND.getId();
    }
}
