package ru.planet.wgprotect.manager;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import ru.planet.wgprotect.utils.Config;
import ru.planet.wgprotect.utils.RegionType;

import java.util.Iterator;

public class WGApi {
    private WorldGuardPlugin wgApi;
    private WorldEditPlugin weApi;

    public WGApi(WorldGuardPlugin wgApi, WorldEditPlugin weApi) {
        this.wgApi = wgApi;
        this.weApi = weApi;
    }

    public boolean checkPlayerSelection(Player p, CuboidSelection selection) {
        RegionManager regions = this.wgApi.getRegionManager(selection.getWorld());
        BlockVector vector1 = new BlockVector(selection.getNativeMinimumPoint());
        BlockVector vector2 = new BlockVector(selection.getNativeMaximumPoint());
        if(regions.getApplicableRegions(vector1).size() == 0 || regions.getApplicableRegions(vector2).size() == 0)return false;
        ApplicableRegionSet applicableRegionSet = regions.getApplicableRegions(new ProtectedCuboidRegion("__dummy__", vector1, vector2));
        for (ProtectedRegion region : applicableRegionSet) {
            if (Config.getBlockedRegions().contains(region.getId())) {
                return false;
            }
        }
        return applicableRegionSet.isOwnerOfAll(wgApi.wrapPlayer(p));
    }

    public RegionType isProtectedRegion(Player player, Location loc) {
        ApplicableRegionSet set = this.wgApi.getRegionManager(loc.getWorld()).getApplicableRegions(loc);
        Iterator var5 = set.iterator();

        ProtectedRegion region;
        do {
            if (!var5.hasNext()) {
                return RegionType.PLAYER;
            }

            region = (ProtectedRegion) var5.next();
            if (Config.getIgnoredRegions().contains(region.getId())) {
                return RegionType.PLAYER;
            }

            if (Config.getAllowedUsers().contains(player.getName())) {
                return RegionType.PLAYER;
            }

            if (Config.getBlockedRegions().contains(region.getId())) {
                return RegionType.ADMIN;
            }
        }
        while (!region.getOwners().getUniqueIds().contains(player.getUniqueId()) && !region.getMembers().getUniqueIds().contains(player.getUniqueId()));

        return RegionType.PLAYER;
    }

    public WorldEditPlugin getWeApi() {
        return this.weApi;
    }
}
