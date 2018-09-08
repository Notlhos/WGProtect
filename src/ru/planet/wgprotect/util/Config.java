package ru.planet.wgprotect.util;

import com.google.common.base.Preconditions;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {

    private transient File file;

    public static <T, V> Map<T, V> fastSingleMap(T key, V value) {
        Map<T, V> map = new HashMap();
        map.put(key, value);
        return map;
    }

    public static <T extends Config> T load(Plugin p, String fileName, Class<T> clazz) {
        File file = new File(p.getDataFolder(), fileName);
        T t;
        if (file.exists() && file.length() != 0L) {
            t = GsonUtils.GSON.fromJson(GsonUtils.readFile(file), clazz);
            t.setFile(file);
            t.init();
            return t;
        } else {
            p.getDataFolder().mkdirs();

            try {
                t = clazz.newInstance();
                t.setFile(file);
                t.save();
                t.init();
                return t;
            } catch (Exception var5) {
                var5.printStackTrace();
                return null;
            }
        }
    }

    public final File getFile() {
        return this.file;
    }

    public final void setFile(File file) {
        this.file = file;
    }

    public void init() {

    }

    public void save() {
        try {
            Files.write(this.file.toPath(), GsonUtils.GSON.toJson(this).getBytes(StandardCharsets.UTF_8), new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING});
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }
}
