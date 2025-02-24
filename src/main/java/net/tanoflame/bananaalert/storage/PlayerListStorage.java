package net.tanoflame.bananaalert.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.tanoflame.bananaalert.BananaAlert;
import net.tanoflame.bananaalert.PlayerListManager;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;

public class PlayerListStorage {
    private static final Gson GSON = new GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .setPrettyPrinting()
            .create();
    private static final Path DATA_FOLDER = FabricLoader.getInstance().getConfigDir().resolve("BananaAlert/data");

    public static void loadDataFile() {
        Path filePath = DATA_FOLDER.resolve( "data.json");
        if (Files.exists(DATA_FOLDER)) {
            try (Reader reader = Files.newBufferedReader(filePath)) {
                GSON.fromJson(reader, PlayerListManager.class);
                BananaAlert.LOGGER.info("Loaded list data file at {} successfully.", filePath);
            } catch (IOException e) {
                BananaAlert.LOGGER.trace("Failed to read file: {}", filePath.toString(), e);
            }
        }
    }

    public static void saveDataFile() {
        Path filePath = DATA_FOLDER.resolve("data.json");
        try {
            if (!Files.exists(DATA_FOLDER)) {
                BananaAlert.LOGGER.info("Data Folder not found, creating at {}", DATA_FOLDER);
                Files.createDirectories(DATA_FOLDER);
            }

            try (Writer writer = Files.newBufferedWriter(filePath)) {
                GSON.toJson(new PlayerListManager(), writer);
            }
        } catch (IOException e) {
            BananaAlert.LOGGER.trace("Failed to save file: {}", filePath.toString(), e);
        }
        BananaAlert.LOGGER.info("List data file at {} SAVED.", filePath.toString());
    }
}
