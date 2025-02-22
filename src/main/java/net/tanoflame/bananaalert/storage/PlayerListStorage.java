package net.tanoflame.bananaalert.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.tanoflame.bananaalert.BananaAlert;
import net.tanoflame.bananaalert.PlayerList;
import net.tanoflame.bananaalert.PlayerListManager;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class PlayerListStorage {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path DATA_FOLDER = FabricLoader.getInstance().getConfigDir().resolve("BananaAlert/data");

    public static void loadAllDataFiles() {
        BananaAlert.LOGGER.info("Loading Data Files from {}", DATA_FOLDER);
        PlayerListManager.getLists().forEach(PlayerListStorage::saveDataFile);
    }

    public static PlayerList loadDataFile(String listName) {
        Path filePath = DATA_FOLDER.resolve(listName + ".json");
        if (Files.exists(DATA_FOLDER)) {
            try (Reader reader = Files.newBufferedReader(filePath)) {
                PlayerList playerList = GSON.fromJson(reader, PlayerList.class);
                PlayerListManager.addList(playerList);
                BananaAlert.LOGGER.info("Loaded list data file at {} successfully.", filePath);
                return playerList;
            } catch (IOException e) {
                BananaAlert.LOGGER.trace("Failed to read file: {}", filePath.toString(), e);
            }
        }
        return null;
    }

    public static void saveAllDataFiles() {
        BananaAlert.LOGGER.info("Saving Data Files to {}", DATA_FOLDER);
        PlayerListManager.getLists().forEach(PlayerListStorage::saveDataFile);
    }

    public static void saveDataFile(PlayerList list) {
        Path filePath = DATA_FOLDER.resolve(list.getName() + ".json");
        try {
            if (!Files.exists(DATA_FOLDER)) {
                BananaAlert.LOGGER.info("Data Folder not found, creating at {}", DATA_FOLDER);
                Files.createDirectories(DATA_FOLDER);
            }

            try (Writer writer = Files.newBufferedWriter(filePath)) {
                GSON.toJson(list, writer);
            }
        } catch (IOException e) {
            BananaAlert.LOGGER.trace("Failed to save file: {}", filePath.toString(), e);
        }
        BananaAlert.LOGGER.info("List data file at {} SAVED.", filePath.toString());
    }
}
