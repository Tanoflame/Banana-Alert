package net.tanoflame.bananaalert;

import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerEntry {
    private final List<UUID> alts;
    private final UUID uuid;
    private String name;
    private String notes;
    private UUID playerListId = Util.NIL_UUID;

    public PlayerEntry(UUID uuid) {
        // TODO: Add uuid check against Mojang API and name lookup
        this.uuid = uuid;
        this.alts = new ArrayList<>();
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

    public void setPlayerListId(UUID playerListId) {
        this.playerListId = playerListId;
    }

    public UUID getPlayerListId() {
        return this.playerListId;
    }
}
