package ru.planet.wgprotect.message;

import com.google.gson.JsonObject;
import org.bukkit.ChatColor;
import ru.planet.wgprotect.util.GsonUtils;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MessageStorage {

    private static Map<String, Message> messages = new HashMap<>();

    public static void load(File database, Collection<MessageKey> keys) {
        JsonObject jsonObject = GsonUtils.loadFromFile(database);
        boolean hasMissedKeys = keys.stream().anyMatch(k -> !jsonObject.has(k.getKey()));
        keys.stream().filter(key -> !jsonObject.has(key.getKey())).forEach(key -> jsonObject.add(key.getKey(), GsonUtils.GSON.toJsonTree(key.getValue())));
        keys.forEach(key -> messages.put(key.getKey(), new Message(key.getKey(), jsonObject.get(key.getKey()).getAsString())));
        if (hasMissedKeys)
            GsonUtils.writeAsJsonToFile(database, new TreeMap(GsonUtils.GSON.fromJson(jsonObject, Map.class)));
    }

    public static Message message(MessageKey key) {
        return messages.get(key.getKey()).clone();
    }

    public static Message get(String key) {
        return messages.get(key).clone();
    }

}
