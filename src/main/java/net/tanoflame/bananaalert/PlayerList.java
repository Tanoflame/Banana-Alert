package net.tanoflame.bananaalert;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerList {
    private final List<PlayerDataEntry> entries = new ArrayList<>();
    private String name;
    private String description;

    public PlayerList(String name) {
        this.name = name;
        this.description = "";
    }

    public List<PlayerDataEntry> getEntries() {
        return new ArrayList<>(entries);
    }

    public void addEntry(PlayerDataEntry entry) {
        if (!PlayerListManager.hasPlayerDataEntry(entry)) {
            this.entries.add(entry);
        }

        if (!PlayerListManager.getPlayerData().contains(entry)) {
            PlayerListManager.addPlayerData(entry);
        }
    }

    public void removeEntry(PlayerDataEntry entry) {
        this.entries.remove(entry);

        PlayerListManager.removePlayerData(entry);
    }

    public void removeEntry(UUID uuid) {
        Optional<PlayerDataEntry> foundEntry = this.entries.stream().filter(entry -> entry.getUUID().equals(uuid)).findFirst();
        foundEntry.ifPresent(this::removeEntry);
    }

    public void removeEntry(String name) {
        Optional<PlayerDataEntry> foundEntry = this.entries.stream().filter(entry -> entry.getName().equals(name)).findFirst();
        foundEntry.ifPresent(this::removeEntry);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean hasPlayerDataEntry(PlayerDataEntry entry) {
        AtomicBoolean found = new AtomicBoolean(false);
        this.entries.stream().forEach(dataEntry -> {
            if (dataEntry == entry) {
                found.set(true);
            }
        });
        return found.get();
    }
}
