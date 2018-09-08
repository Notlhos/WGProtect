package ru.planet.wgprotect;

import ru.planet.wgprotect.message.MessageKey;

public enum Locale implements MessageKey {

    WORLDEDIT_BLOCKED("worldedit_blocked", "&cКоманды WorldEdit можно использовать только у себя в регионе!"),
    WORLDGUARD_BLOCKED("worldguard_blocked", "&cВы не можете ломать этот регион!"),
    SELECTION_LIMIT("selection_limit", "&7Нельзя выделять больше &c(selection_limit) &7блоков! У вас же &c(player_selection)"),
    REGION_BREAK_LIMIT("region_break_limit", "&7Вы сломали слишком много регионов! Список обнулится через 24 часа");

    private String key;
    private String value;

    Locale(String key, String value) {
        this.key = "message." + key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }
}
