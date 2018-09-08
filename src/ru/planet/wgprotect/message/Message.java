package ru.planet.wgprotect.message;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class Message implements Cloneable {

    private final String key;
    private String message;
    private Map<String, Object> tags = new HashMap<>();

    public Message(String key, String message) {
        this.key = key;
        this.message = ChatColor.translateAlternateColorCodes('&', message);
    }

    public String getKey() {
        return key;
    }

    public String getMessage() {
        return message;
    }

    public Message tag(String name, Object value) {
        tags.put(name, value);
        return this;
    }

    public String getCompleteValue() {
        String result_message = message;
        for (Map.Entry<String, Object> entry : tags.entrySet()) {
            String tag = "(" + entry.getKey() + ")";
            result_message = result_message.replace(tag, String.valueOf(entry.getValue()));
        }
        return result_message;
    }

    public void send(CommandSender player) {
        player.sendMessage(getCompleteValue());
    }

    public void broadcast() {
        Bukkit.getOnlinePlayers().forEach(player -> send(player));
    }

    public void send(CommandSender... players) {
        for (CommandSender player : players) {
            send(player);
        }
    }

    public Message clone() {
        try {
            return (Message) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
