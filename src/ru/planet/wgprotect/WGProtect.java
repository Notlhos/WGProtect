package ru.planet.wgprotect;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.plugin.java.JavaPlugin;
import ru.planet.wgprotect.listeners.PlayerListener;
import ru.planet.wgprotect.manager.WGApi;
import ru.planet.wgprotect.utils.Config;

public class WGProtect extends JavaPlugin {
    private static WGProtect plugin = null;
    private WGApi worldManager = null;

    public static WGProtect getApi() {
        return plugin;
    }

    public void onEnable() {
        this.saveDefaultConfig();
        new Config(this.getConfig());
        plugin = this;
        WorldGuardPlugin wg = (WorldGuardPlugin) this.getServer().getPluginManager().getPlugin("WorldGuard");
        WorldEditPlugin we = (WorldEditPlugin) this.getServer().getPluginManager().getPlugin("WorldEdit");
        this.worldManager = new WGApi(wg, we);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    public WGApi getWorldManager() {
        return this.worldManager;
    }
}
