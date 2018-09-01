package ru.planet.wgprotect.worldedit;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import org.bukkit.entity.Player;

public class HsphereCommand implements CheckCommand {

    @Override
    public CuboidSelection getCuboidSelection(Player player, String... args) {
        if (args.length < 3) {
            return null;
        }

        final String[] cr = args[2].split(",");
        int y2;
        final int x2;
        int z2 = x2 = (y2 = Integer.parseInt(cr[0]));
        try {
            y2 = Integer.parseInt(cr[1]);
            z2 = Integer.parseInt(cr[2]);
        } catch (Exception e) {
        }

        return new CuboidSelection(player.getWorld(), player.getLocation().subtract((double) x2, (double) y2, (double) z2), player.getLocation().add((double) x2, (double) y2, (double) z2));
    }

    @Override
    public String getName() {
        return "hsphere";
    }

}
