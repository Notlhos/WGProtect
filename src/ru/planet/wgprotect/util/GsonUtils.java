package ru.planet.wgprotect.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;

public class GsonUtils {

    public static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().enableComplexMapKeySerialization().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    public static void writeToFile(File file, String str) {
        try {
            Files.write(file.toPath(), str.getBytes(StandardCharsets.UTF_8), new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING});
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

    public static void writeAsJsonToFile(File file, Object obj) {
        writeToFile(file, GSON.toJson(obj));
    }

    public static String readFile(File file) {
        try {
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static JsonObject loadFromFile(File json) {
        if (!json.exists()) {
            json.getParentFile().mkdirs();
            writeToFile(json, "{}");
            return new JsonObject();
        }
        JsonReader reader = new JsonReader(new StringReader(readFile(json)));
        reader.setLenient(true);
        return GSON.fromJson(reader, JsonObject.class);
    }
}
