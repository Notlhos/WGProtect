package ru.planet.wgprotect.manager;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import ru.planet.wgprotect.worldedit.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class WGManager {

    private WGRevolver wgRevolver;
    private List<String> commands;
    private List<CheckCommand> worldEditCommands;

    public WGManager(WGRevolver wgRevolver) {
        this.wgRevolver = wgRevolver;
        this.commands = Arrays.asList("set", "replace", "rep", "re", "walls", "regen", "cut", "hcyl", "cyl", "sphere", "hsphere", "pyramid", "hpyramid", "paste", "copy");
        this.worldEditCommands = Arrays.asList(
                new CylCommand(),
                new HcylCommand(),
                new HsphereCommand(),
                new SphereCommand(),
                new PasteCommand(wgRevolver.getWorldEditPlugin()),
                new PyramidCommand(),
                new HpyramidCommand(),
                new UpCommand()
        );

    }

    public boolean isWeCmd(String s) {
        final String checkCmd = s.replace("worldedit:", "");
        return commands.stream().anyMatch(s1 -> checkCmd.equalsIgnoreCase(s1));
    }


    public CheckCommand getCommand(String cmd) {
        for (CheckCommand command : worldEditCommands) {
            if (command.getName().equalsIgnoreCase(cmd)) {
                return command;
            }
        }
        return null;
    }

    public boolean canUseWorldEdit(Player player, CuboidSelection selection) {
        RegionManager regions = wgRevolver.getWorldGuardPlugin().getRegionManager(selection.getWorld());
        BlockVector vector1 = new BlockVector(selection.getNativeMinimumPoint());
        BlockVector vector2 = new BlockVector(selection.getNativeMaximumPoint());
        ApplicableRegionSet s1 = regions.getApplicableRegions(vector1);
        ApplicableRegionSet s2 = regions.getApplicableRegions(vector2);
        if (s1.size() == 0 || s2.size() == 0) return false;
        ApplicableRegionSet set = regions.getApplicableRegions(new ProtectedCuboidRegion("__dummy__", vector1, vector2));
        if (StreamSupport.stream(set.spliterator(), true).anyMatch(region -> wgRevolver.getSetting().isBlockedRegion(region.getId())))
            return false;
        LocalPlayer localPlayer = wgRevolver.getWorldGuardPlugin().wrapPlayer(player);
        return set.isOwnerOfAll(localPlayer) || set.isMemberOfAll(localPlayer);
    }

    public boolean isProtectedRegion(Player player, Location loc) {
        if (wgRevolver.getSetting().isAdminUser(player.getName())) return false;
        ApplicableRegionSet set = wgRevolver.getWorldGuardPlugin().getRegionManager(loc.getWorld()).getApplicableRegions(loc);
        if (StreamSupport.stream(set.spliterator(), true).anyMatch(region -> wgRevolver.getSetting().isIgnoredRegion(region.getId())))
            return false;
        if (StreamSupport.stream(set.spliterator(), true).anyMatch(region -> wgRevolver.getSetting().isBlockedRegion(region.getId())))
            return true;
        /*LocalPlayer localPlayer = wgRevolver.getWorldGuardPlugin().wrapPlayer(player);
        if (set.isOwnerOfAll(localPlayer) || set.isMemberOfAll(localPlayer)) return false;*/
        return false;
    }

    public Optional<ProtectedRegion> getRegion(Location location){
        return wgRevolver.getWorldGuardPlugin().getRegionManager(location.getWorld()).getApplicableRegions(location).getRegions()
                .stream()
                .findFirst();
    }
}
