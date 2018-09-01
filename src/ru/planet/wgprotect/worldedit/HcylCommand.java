package ru.planet.wgprotect.worldedit;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import org.bukkit.entity.Player;

public class HcylCommand implements CheckCommand {

    @Override
    public CuboidSelection getCuboidSelection(Player player, String... args) {
        int x = 1;
        int y = 1;
        int z = 0;
        try {
            if (args[2].contains(",")) {
                x = Integer.parseInt(args[2].split(",")[0]);
                z = Integer.parseInt(args[2].split(",")[1]);
            } else {
                x = Integer.parseInt(args[2]);
            }
            y = Integer.parseInt(args[3]);
        } catch (Exception ex6) {
        }
        return new CuboidSelection(player.getWorld(), player.getLocation().subtract((double) x, (double) y, (double) z), player.getLocation().add((double) x, (double) y, (double) z));
    }

    @Override
    public String getName() {
        return "hcyl";
    }

}
