package ru.planet.wgprotect.message;

public interface MessageKey {


    String getKey();

    String getValue();

    default Message message() {
        return MessageStorage.message(this);
    }

}
