package ru.planet.wgprotect;

import org.bukkit.plugin.java.JavaPlugin;
import ru.planet.wgprotect.listeners.PlayerListener;
import ru.planet.wgprotect.manager.WGRevolver;
import ru.planet.wgprotect.message.MessageStorage;

import java.io.File;
import java.util.Arrays;

public class WGProtect extends JavaPlugin {

    private WGRevolver wgRevolver;

    public void onEnable() {
        wgRevolver = new WGRevolver(this);
        MessageStorage.load(new File(getDataFolder(), "messages.json"), Arrays.asList(Locale.values()));
        getServer().getPluginManager().registerEvents(new PlayerListener(wgRevolver), this);
    }

    public WGRevolver getWGRevolver() {
        return wgRevolver;
    }
}
