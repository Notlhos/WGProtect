package ru.planet.wgprotect.worldedit;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import org.bukkit.entity.Player;

public class PyramidCommand implements CheckCommand {

    @Override
    public CuboidSelection getCuboidSelection(Player player, String... args) {
        if (args.length < 3) {
            return null;
        }

        int i = 1;

        try {
            i = Integer.parseInt(args[2]);
        } catch (Exception e) {
        }

        return new CuboidSelection(player.getWorld(), player.getLocation().subtract((double) i, (double) i, (double) i), player.getLocation().add((double) i, (double) i, (double) i));
    }

    @Override
    public String getName() {
        return "pyramid";
    }

}
