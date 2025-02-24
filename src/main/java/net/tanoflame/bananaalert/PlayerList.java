package net.tanoflame.bananaalert;

import net.minecraft.util.Formatting;

import java.util.*;

public class PlayerList {
    private final UUID id;
    private String name;
    private String description;
    private Formatting color;

    public PlayerList(String name) {
        this(UUID.randomUUID(), name);
    }

    public PlayerList(UUID id, String name) {
        this.id = id;
        this.name = name;
        this.description = "";
    }

    public UUID getId() {
        return this.id;
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

    public Formatting getColor() {
        return this.color;
    }

    public void setColor(Formatting color) {
        if (!color.isColor()) {
            throw new IllegalArgumentException("Invalid color: " + color);
        }
        this.color = color;
    }
}
