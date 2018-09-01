package ru.planet.wgprotect.worldedit;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import org.bukkit.entity.Player;

public class UpCommand implements CheckCommand {

    @Override
    public CuboidSelection getCuboidSelection(Player player, String... args) {
        try {
            int v = Integer.parseInt(args[1]);
            return new CuboidSelection(player.getWorld(), player.getLocation().add(0, v, 0), player.getLocation().add(0, v, 0));
        } catch (Exception ex5) {
        }
        return null;
    }

    @Override
    public String getName() {
        return "up";
    }

}
