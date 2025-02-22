package net.tanoflame.bananaalert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PlayerDataEntry {
    private final List<PlayerDataEntry> alts;
    private final UUID uuid;
    private String name;
    private String notes;

    public PlayerDataEntry(UUID uuid) {
        this.uuid = uuid;
        this.alts = new ArrayList<>();
    }

    public void addAlt(PlayerDataEntry entry) {
        this.alts.add(entry);
    }

    public void removeAlt(PlayerDataEntry entry) {
        this.alts.remove(entry);
    }

    public void removeAlt(UUID uuid) {
        Optional<PlayerDataEntry> foundEntry = this.alts.stream().filter(entry -> entry.uuid.equals(uuid)).findFirst();
        foundEntry.ifPresent(this::removeAlt);
    }

    public void removeAlt(String name) {
        Optional<PlayerDataEntry> foundEntry = this.alts.stream().filter(entry -> entry.name.equals(name)).findFirst();
        foundEntry.ifPresent(this::removeAlt);
    }

    /**
     * Returns a copy of the list of alts
     * @return List of alts registered to this player
     */
    public List<PlayerDataEntry> getAlts() {
        return new ArrayList<>(alts);
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return this.notes;
    }
}
