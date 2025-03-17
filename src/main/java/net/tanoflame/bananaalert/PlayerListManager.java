package net.tanoflame.bananaalert;

import com.google.gson.annotations.SerializedName;
import net.tanoflame.bananaalert.util.Util;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;

public class PlayerListManager {
    @SerializedName("lists")
    private static Map<UUID, PlayerList> LISTS = new HashMap<>();
    @SerializedName("players")
    private static Map<UUID, PlayerEntry> PLAYER_ENTRIES = new HashMap<>();

    public static Map<UUID, PlayerList> getLists() {
        return new HashMap<>(LISTS);
    }

    public static PlayerList getList(UUID id) {
        return LISTS.get(id);
    }
    
    public static Map<UUID, PlayerEntry> getPlayerEntries() {
        return new HashMap<>(PLAYER_ENTRIES);
    }

    // AddList, name
    public static PlayerList addPlayerList(String name) {
        PlayerList list = new PlayerList(name);
        addPlayerList(list);
        return list;
    }

    // AddList, uuid and name
    public static PlayerList addPlayerList(UUID uuid, String name) {
        PlayerList list = new PlayerList(uuid, name);
        addPlayerList(list);
        return list;
    }

    // AddList, PlayerList
    public static void addPlayerList(PlayerList list) {
        LISTS.put(list.getId(), list);
    }

    // RemoveList, PlayerList
    public static void removeList(PlayerList list) {
        removeList(list.getId());
    }

    // RemoveList, id
    public static void removeList(UUID id) {
        LISTS.remove(id);

        PLAYER_ENTRIES.entrySet().removeIf(entry -> entry.getValue().getPlayerListId().equals(id));
    }

    // GetPlayerEntry, player uuid
    @Nullable
    public static PlayerEntry getPlayerEntry(UUID uuid) {
        return PLAYER_ENTRIES.get(uuid);
    }

    // GetPlayerEntry, player name
    public static PlayerEntry getPlayerEntry(String name) {
        return Util.findByField(PLAYER_ENTRIES, PlayerEntry::getName, name);
    }

    // AddPlayerEntry, listId and player name
    public @Nullable static PlayerEntry addPlayerEntry(UUID listId, String name) {
        UUID playerId = null;
        if (BananaAlert.isDevEnvironment()) {
            playerId = UUID.randomUUID();
        } else {
            try {
                UUID uuid = BananaAlert.LOOKUP_CLIENT.getUUIDFromName(name);

                if (uuid.equals(Util.NIL_UUID)) {
                    return null;
                }

                playerId = uuid;
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        PlayerEntry entry = new PlayerEntry(playerId, name);
        addPlayerEntry(listId, entry);

        return entry;
    }

    // AddPlayerEntry, listId and player uuid
    public @Nullable static PlayerEntry addPlayerEntry(UUID listId, UUID playerId) {
        String name;
        try {
            name = BananaAlert.LOOKUP_CLIENT.getNameFromUUID(playerId);

            if (name == null) {
                return null;
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        PlayerEntry entry = new PlayerEntry(playerId, name);
        addPlayerEntry(listId, entry);
        return entry;
    }

    // AddPlayerEntry, listId and entry
    private @Nullable static boolean addPlayerEntry(UUID listId, PlayerEntry entry) {
        if (!PLAYER_ENTRIES.containsKey(entry.getUUID()) && LISTS.containsKey(listId)) {
            PLAYER_ENTRIES.put(entry.getUUID(), entry);
            entry.setPlayerListId(listId);
            return true;
        }
        return false;
    }

    // RemovePlayerEntry, player uuid
    public static void removePlayerEntry(UUID uuid) {
        PlayerEntry entry = getPlayerEntry(uuid);
        if (entry == null) return;

        removePlayerEntry(entry);
    }

    // RemovePlayerEntry, player name
    public static void removePlayerEntry(String name) {
        removePlayerEntry(getPlayerEntry(name));
    }

    // RemovePlayerEntry, entry
    public static void removePlayerEntry(PlayerEntry entry) {
        PLAYER_ENTRIES.remove(entry.getUUID());
    }

    // MovePlayerToList, newListId and player uuid
    public static void movePlayerToList(UUID listId, UUID playerId) {
        movePlayerToList(listId, getPlayerEntry(playerId));
    }

    // MovePlayerToList, newListId and player name
    public static void movePlayerToList(UUID listId, String name) {
        movePlayerToList(listId, getPlayerEntry(name));
    }

    // MovePlayerToList, newListId and entry
    public static void movePlayerToList(UUID listId, PlayerEntry entry) {
        if (LISTS.containsKey(listId)) {
            entry.setPlayerListId(listId);
        }
    }

    public static List<PlayerEntry> getPlayersOfList(UUID listId) {
        return PLAYER_ENTRIES.values().stream().filter(entry -> entry.getPlayerListId().equals(listId)).toList();
    }
}