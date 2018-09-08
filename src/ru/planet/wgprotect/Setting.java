package ru.planet.wgprotect;

import ru.planet.wgprotect.util.Config;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Setting extends Config {

    private List<String> blockedRegions = Arrays.asList("spawn", "pvp", "otherRegion");
    private List<String> ignoredRegions = Arrays.asList("mine", "otherRegion");
    private List<String> users = Arrays.asList("someNick_1", "someNick_2");
    private Map<String, Integer> worldEditLimit = Config.fastSingleMap("default", 30000);

    public boolean isBlockedRegion(String region) { return blockedRegions.contains(region); }

    public boolean isIgnoredRegion(String region) {
        return ignoredRegions.contains(region);
    }

    public boolean isAdminUser(String user) {
        return users.contains(user.toLowerCase(Locale.ENGLISH));
    }

    public int getWorldEditLimit(String group) {
        if(worldEditLimit.containsKey(group)){
            return worldEditLimit.get(group);
        }
        return worldEditLimit.getOrDefault("default", 1);
    }
}
