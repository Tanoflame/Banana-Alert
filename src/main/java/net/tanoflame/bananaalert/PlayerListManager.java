package net.tanoflame.bananaalert;

import java.util.ArrayList;
import java.util.List;

public class PlayerListManager {
    private static final List<PlayerList> LISTS = new ArrayList<>();

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
}