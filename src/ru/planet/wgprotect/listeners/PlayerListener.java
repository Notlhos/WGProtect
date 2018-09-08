package ru.planet.wgprotect.listeners;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
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
import ru.planet.wgprotect.Locale;
import ru.planet.wgprotect.manager.WGRevolver;
import ru.planet.wgprotect.worldedit.CheckCommand;

public class PlayerListener implements Listener {

    private WGRevolver wgRevolver;

    public PlayerListener(WGRevolver wgRevolver){
        this.wgRevolver = wgRevolver;
    }



    @EventHandler(priority = EventPriority.LOWEST)
    public void worldGuardBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (wgRevolver.getWorldGuardManager().isProtectedRegion(p, e.getBlock().getLocation())) {
            Locale.WORLDGUARD_BLOCKED.message().send(p);
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void worldGuardBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (wgRevolver.getWorldGuardManager().isProtectedRegion(p, e.getBlock().getLocation())) {
            Locale.WORLDGUARD_BLOCKED.message().send(p);
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void worldGuardBucketEmpty(PlayerBucketEmptyEvent e) {
        Player p = e.getPlayer();
        if (wgRevolver.getWorldGuardManager().isProtectedRegion(p, e.getBlockClicked().getLocation())) {
            Locale.WORLDGUARD_BLOCKED.message().send(p);
            e.setCancelled(true);
        }

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void worldGuardInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            ItemStack item = e.getItem();
            if (item == null) return;
            if (item.getType().equals(Material.ARMOR_STAND) || item.getType().equals(Material.PAINTING) || item.getType().equals(Material.ITEM_FRAME)) {
                if (wgRevolver.getWorldGuardManager().isProtectedRegion(p, e.getClickedBlock().getLocation())) {
                    Locale.WORLDGUARD_BLOCKED.message().send(p);
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void worldGuardBreakEntity(HangingBreakByEntityEvent e) {
        EntityType type = e.getEntity().getType();
        if (type.equals(EntityType.PAINTING) || type.equals(EntityType.ITEM_FRAME)) {
            if (!(e.getRemover() instanceof Player)) {
                e.setCancelled(true);
                return;
            }
            if (wgRevolver.getWorldGuardManager().isProtectedRegion((Player) e.getRemover(), e.getEntity().getLocation())) {
                Locale.WORLDGUARD_BLOCKED.message().send(e.getRemover());
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void worldGuardEntityDamage(EntityDamageByEntityEvent e) {
        EntityType type = e.getEntity().getType();
        if (type.equals(EntityType.ARMOR_STAND)) {
            if (!(e.getDamager() instanceof Player)) {
                e.setCancelled(true);
                return;
            }
            if (wgRevolver.getWorldGuardManager().isProtectedRegion((Player) e.getDamager(), e.getEntity().getLocation())) {
                Locale.WORLDGUARD_BLOCKED.message().send(e.getDamager());
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void worldGuardBucketFill(PlayerBucketFillEvent e) {
        Player p = e.getPlayer();
        if (wgRevolver.getWorldGuardManager().isProtectedRegion(p, e.getBlockClicked().getLocation())) {
            Locale.WORLDGUARD_BLOCKED.message().send(p);
            e.setCancelled(true);
        }

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void worldGuardCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if (wgRevolver.getSetting().isAdminUser(p.getName())) return;
        String[] cmd = e.getMessage().toLowerCase().split("\\s+");
        if (((cmd[0].equalsIgnoreCase("/rg")) || (cmd[0].equalsIgnoreCase("/region")) || (cmd[0].equalsIgnoreCase("/worldguard:rg")) || (cmd[0].equalsIgnoreCase("/worldguard:region"))) && (cmd.length > 2)) {
            if (wgRevolver.getSetting().isBlockedRegion(cmd[2]) || (wgRevolver.getSetting().isIgnoredRegion(cmd[2]))) e.setCancelled(true);
            if ((cmd.length > 3) && (cmd[2].equalsIgnoreCase("-w"))) {
                if (wgRevolver.getSetting().isBlockedRegion(cmd[4]) || (wgRevolver.getSetting().isIgnoredRegion(cmd[4]))) e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void worldEditCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        if (wgRevolver.getSetting().isAdminUser(player.getName())) return;
        String[] cmd = e.getMessage().replace("/", "").toLowerCase().split("\\s+");
        if (wgRevolver.getWorldGuardManager().isWeCmd(cmd[0]) && cmd.length >= 1) {
            CheckCommand command = wgRevolver.getWorldGuardManager().getCommand(cmd[0]);
            CuboidSelection selection = null;

            if (command != null) {
                selection = command.getCuboidSelection(player, cmd);
            }

            if (selection == null) {
                Selection select = wgRevolver.getWorldEditPlugin().getSelection(player);
                if (select instanceof CuboidSelection) {
                    selection = (CuboidSelection) select;
                }
            }

            if (selection == null) {
                Locale.WORLDEDIT_BLOCKED.message().send(player);
                e.setCancelled(true);
                return;
            }

            int selectionSize = selection.getArea();
            int playerLimit = wgRevolver.getSetting().getWorldEditLimit(wgRevolver.getGroup(player));
            if (playerLimit > 0 && selectionSize > playerLimit) {
                Locale.SELECTION_LIMIT
                        .message()
                        .tag("selection_limit", playerLimit)
                        .tag("player_selection", selectionSize)
                        .send(player);
                e.setCancelled(true);
                return;
            }

            if (!wgRevolver.getWorldGuardManager().canUseWorldEdit(player, selection)) {
                Locale.WORLDEDIT_BLOCKED.message().send(player);
                e.setCancelled(true);
            }
        }
    }
}
