package ru.planet.wgprotect.worldedit;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import org.bukkit.entity.Player;

public interface CheckCommand {


    CuboidSelection getCuboidSelection(Player player, String... args);

    String getName();

}
