package ru.planet.wgprotect.manager;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.planet.wgprotect.Setting;
import ru.planet.wgprotect.util.Config;

public class WGRevolver {

    private WorldGuardPlugin worldGuardPlugin;
    private WorldEditPlugin worldEditPlugin;
    private WGManager wgManager;
    private Setting setting;
    private Permission permission;
    private Plugin plugin;

    public WGRevolver(Plugin plugin) {
        this.plugin = plugin;
        worldGuardPlugin = WGBukkit.getPlugin();
        try {
            worldEditPlugin = worldGuardPlugin.getWorldEdit();
        } catch (CommandException e) {
            e.printStackTrace();
        }
        load();
        wgManager = new WGManager(this);
        permission = plugin.getServer().getServicesManager().getRegistration(Permission.class).getProvider();
    }

    public WorldGuardPlugin getWorldGuardPlugin() {
        return worldGuardPlugin;
    }

    public WorldEditPlugin getWorldEditPlugin() {
        return worldEditPlugin;
    }

    public WGManager getWorldGuardManager() {
        return wgManager;
    }

    public Setting getSetting() { return setting; }

    public String getGroup(Player player){ return permission.getPrimaryGroup(player); }

    public void load(){
        setting = Config.load(plugin, "config.json", Setting.class);
    }
}
