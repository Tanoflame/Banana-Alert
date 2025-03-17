package net.tanoflame.bananaalert;

import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerEntry {
    private final List<UUID> alts;
    private final UUID uuid;
    private String name;
    private String notes;
    private UUID playerListId;

    public PlayerEntry(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.alts = new ArrayList<>();
        this.playerListId = Util.NIL_UUID;
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

    @Nullable
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
