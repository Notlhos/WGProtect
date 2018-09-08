package ru.planet.wgprotect.worldedit;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PasteCommand implements CheckCommand {

    private WorldEditPlugin worldEditPlugin;

    public PasteCommand(WorldEditPlugin worldEditPlugin) {
        this.worldEditPlugin = worldEditPlugin;
    }

    @Override
    public CuboidSelection getCuboidSelection(Player player, String... args) {
        try {
            LocalSession session = worldEditPlugin.getSession(player);
            ClipboardHolder holder = session.getClipboard();
            Clipboard clipboard = holder.getClipboard();
            Vector to = session.getPlacementPosition((com.sk89q.worldedit.entity.Player) worldEditPlugin.wrapCommandSender(player));
            Vector min = to.add(clipboard.getRegion().getMinimumPoint().subtract(clipboard.getOrigin()));
            Vector max = to.add(clipboard.getRegion().getMaximumPoint().subtract(clipboard.getOrigin()));
            return new CuboidSelection(player.getWorld(), min, max);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getName() {
        return "paste";
    }

}
