package ru.planet.wgprotect.listeners;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import ru.planet.wgprotect.WGProtect;
import ru.planet.wgprotect.utils.Config;
import ru.planet.wgprotect.utils.Utils;
import ru.planet.wgprotect.worldedit.CheckCommand;

public class PlayerListener implements Listener {
    // $FF: synthetic field
    private static int[] $SWITCH_TABLE$pl$protect$utils$RegionType;

    @EventHandler(
            priority = EventPriority.LOWEST,
            ignoreCancelled = false
    )
    void wg(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (this.checkRG(p, e.getBlock().getLocation())) {
            e.setCancelled(true);
        }

    }

    @EventHandler(
            priority = EventPriority.LOWEST,
            ignoreCancelled = false
    )
    void wg(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (this.checkRG(p, e.getBlock().getLocation())) {
            e.setCancelled(true);
        }

    }

    @EventHandler(
            priority = EventPriority.LOWEST,
            ignoreCancelled = false
    )
    void wg(PlayerBucketEmptyEvent e) {
        Player p = e.getPlayer();
        if (this.checkRG(p, e.getBlockClicked().getLocation())) {
            e.setCancelled(true);
        }

    }

    @EventHandler(
            priority = EventPriority.LOWEST,
            ignoreCancelled = false
    )
    void wgPlace(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            ItemStack item = e.getItem();
            if (item != null) {
                if (item.getType().equals(Material.ARMOR_STAND) || item.getType().equals(Material.PAINTING) || item.getType().equals(Material.ITEM_FRAME)) {
                    if (this.checkRG(p, e.getClickedBlock().getLocation())) {
                        e.setCancelled(true);
                    }

                }
            }
        }
    }

    @EventHandler(
            priority = EventPriority.LOWEST,
            ignoreCancelled = false
    )
    void wgBreak(HangingBreakByEntityEvent e) {
        EntityType type = e.getEntity().getType();
        if (type.equals(EntityType.PAINTING) || type.equals(EntityType.ITEM_FRAME)) {
            if (!(e.getRemover() instanceof Player)) {
                e.setCancelled(true);
            } else {
                if (this.checkRG((Player) e.getRemover(), e.getEntity().getLocation())) {
                    e.setCancelled(true);
                }

            }
        }
    }

    @EventHandler(
            priority = EventPriority.LOWEST,
            ignoreCancelled = false
    )
    void wgBreak(EntityDamageByEntityEvent e) {
        EntityType type = e.getEntity().getType();
        if (type.equals(EntityType.ARMOR_STAND)) {
            if (!(e.getDamager() instanceof Player)) {
                e.setCancelled(true);
            } else {
                if (this.checkRG((Player) e.getDamager(), e.getEntity().getLocation())) {
                    e.setCancelled(true);
                }

            }
        }
    }

    @EventHandler(
            priority = EventPriority.LOWEST,
            ignoreCancelled = false
    )
    void wg(PlayerBucketFillEvent e) {
        Player p = e.getPlayer();
        if (this.checkRG(p, e.getBlockClicked().getLocation())) {
            e.setCancelled(true);
        }

    }

    @EventHandler(
            priority = EventPriority.LOWEST,
            ignoreCancelled = false
    )
    void wg(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if (Config.getAllowedUsers().contains(p.getName())) {
            return;
        }
        String[] cmd = e.getMessage().toLowerCase().split("\\s+");
        if (((cmd[0].equalsIgnoreCase("/rg")) || (cmd[0].equalsIgnoreCase("/region")) || (cmd[0].equalsIgnoreCase("/worldguard:rg")) || (cmd[0].equalsIgnoreCase("/worldguard:region"))) && (cmd.length > 2)) {
            for (String list : Config.getBlockedRegions()) {
                if (list.equalsIgnoreCase(cmd[2])) {
                    e.setCancelled(true);
                }
            }
            for (String list : Config.getIgnoredRegions()) {
                if (list.equalsIgnoreCase(cmd[2])) {
                    e.setCancelled(true);
                }
            }
            if ((cmd.length > 3) && (cmd[2].equalsIgnoreCase("-w"))) {
                for (String list : Config.getBlockedRegions()) {
                    if (list.equalsIgnoreCase(cmd[4])) {
                        e.setCancelled(true);
                    }
                }
                for (String list : Config.getIgnoredRegions()) {
                    if (list.equalsIgnoreCase(cmd[4])) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    void we(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        if (Config.getAllowedUsers().contains(player.getName())) {
            return;
        }
        String[] cmd = e.getMessage().replace("/", "").toLowerCase().split("\\s+");
        if (Utils.isWeCmd(cmd[0]) && cmd.length >= 1) {
            CheckCommand command = Utils.getCommand(cmd[0]);
            CuboidSelection selection = null;

            if (command != null) {
                selection = command.getCuboidSelection(player, cmd);
            }

            if (selection == null) {
                Selection select = WGProtect.getApi().getWorldManager().getWeApi().getSelection(player);
                if (select instanceof CuboidSelection) {
                    selection = (CuboidSelection) select;
                }
            }

            if (selection == null) {
                player.sendMessage(Config.getMessage("weError"));
                e.setCancelled(true);
                return;
            }

            int selectionSize = selection.getArea();
            int playerLimit = Config.getPlayerLimit(player);
            if (playerLimit > 0 && selectionSize > playerLimit) {
                player.sendMessage(Config.getMessage("selectError").replace("{selSize}", String.valueOf(selectionSize)).replace("{limit}", String.valueOf(playerLimit)));
                e.setCancelled(true);
                return;
            }

            if (!WGProtect.getApi().getWorldManager().checkPlayerSelection(player, selection)) {
                player.sendMessage(Config.getMessage("weError"));
                e.setCancelled(true);
                return;
            }
        }
    }

    private boolean checkRG(Player player, Location loc) {
        switch (WGProtect.getApi().getWorldManager().isProtectedRegion(player, loc)) {
            case ADMIN:
                player.sendMessage(Config.getMessage("wgAdmin"));
                return true;
            default:
                return false;
        }
    }
}
