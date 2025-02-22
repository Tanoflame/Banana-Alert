package net.tanoflame.bananaalert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerListManager {
    private static final List<PlayerList> LISTS = new ArrayList<>();
    private static final List<PlayerDataEntry> PLAYER_DATA = new ArrayList<>();

    public static List<PlayerList> getLists() {
        return new ArrayList<>(LISTS);
    }

    public static void addList(PlayerList list) {
        LISTS.add(list);
    }

    public static PlayerList createList(String name) {
        PlayerList list = new PlayerList(name);
        addList(list);
        return list;
    }

    public static void deleteList(PlayerList list) {
        LISTS.remove(list);
    }

    public static boolean hasPlayerDataEntry(PlayerDataEntry entry) {
        AtomicBoolean found = new AtomicBoolean(false);
        PlayerListManager.getLists().stream().forEach(playerList -> {
            if (playerList.hasPlayerDataEntry(entry)) {
                found.set(true);
            }
        });
        return found.get();
    }

    public static List<PlayerDataEntry> getPlayerData() {
        return new ArrayList<>(PLAYER_DATA);
    }

    public static void addPlayerData(PlayerDataEntry entry) {
        PLAYER_DATA.add(entry);
    }

    public static void removePlayerData(PlayerDataEntry entry) {
        if (!hasPlayerDataEntry(entry)) {
            PLAYER_DATA.remove(entry);
        }
    }
}