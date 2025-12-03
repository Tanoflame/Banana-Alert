package net.tanoflame.bananaalert.gui.description;

import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.tanoflame.bananaalert.PlayerList;
import net.tanoflame.bananaalert.PlayerListManager;
import net.tanoflame.bananaalert.gui.ClientScreen;
import net.tanoflame.bananaalert.util.Util;

import java.util.AbstractList;
import java.util.List;
import java.util.function.BiConsumer;

public class PlayerListOverviewScreen extends RefreshableGUIDescription {
    private static final int GRID_COLUMNS = 16;
    private static final int GRID_ROWS = 11;
    private static final int GRID_SIZE = 18;
    private static final int GRID_GAP = 5;

    public WListPanel<PlayerList, PlayerListEntry> playerListPanel;

    public PlayerListOverviewScreen() {
        WGridPanel root = new WGridPanel(GRID_SIZE);
        root.setSize(GRID_COLUMNS * GRID_SIZE, GRID_ROWS * GRID_SIZE);
        root.setGaps(GRID_GAP, GRID_GAP);
        root.setInsets(Insets.ROOT_PANEL);

        WLabel titleLabel = new WLabel(Text.translatable("gui.banana-alert.list_overview.title"));
        root.add(titleLabel, 0, 0, GRID_COLUMNS, 1);

        BiConsumer<PlayerList, PlayerListEntry> configurator = (PlayerList list, PlayerListEntry entry) -> {
            entry.list = list;
            entry.parent = this;
            entry.setNameLabel();
        };

        List<PlayerList> data = new AbstractList<>() {
            @Override
            public PlayerList get(int index) {
                return new java.util.ArrayList<>(PlayerListManager.getLists().values()).get(index);
            }

            @Override
            public int size() {
                return PlayerListManager.getLists().size();
            }
        };
        playerListPanel = new WListPanel<>(data, PlayerListEntry::new, configurator);

        playerListPanel.setGap(3);
        playerListPanel.setListItemHeight(GRID_SIZE);
        root.add(playerListPanel, 0, 1, GRID_COLUMNS, GRID_ROWS - 5);

        WButton addPlayerListButton = new WButton(Text.translatable("gui.banana-alert.list_overview.add_list"));
        addPlayerListButton.setOnClick(() -> {
            ClientScreen.openScreen(new AddPlayerListScreen(this));
        });
        root.add(addPlayerListButton, 0, GRID_ROWS - 4, GRID_COLUMNS / 2, 1);

        WButton settingsButton = new WButton(Text.translatable("gui.banana-alert.list_overview.settings"));
        settingsButton.setOnClick(() -> {
//            openScreen(new GlobalSettingsScreen());
        });
        root.add(settingsButton, GRID_COLUMNS / 2, GRID_ROWS - 4, GRID_COLUMNS / 2, 1);

        root.validate(this);
        setRootPanel(root);
    }

    @Override
    public void refreshGUI() {
        if (playerListPanel == null) return;
        // Full validate cascades layout through the entire widget tree,
        // ensuring newly created entries run their own layout immediately.
        if (getRootPanel() != null) {
            getRootPanel().validate(this);
        } else {
            // Fallback to list-only layout if root is missing (shouldn't happen)
            playerListPanel.layout();
        }

        // Re-configure visible entries so name/color changes show immediately
        for (WWidget child : playerListPanel.streamChildren().toList()) {
            if (child instanceof PlayerListEntry entry) {
                entry.setNameLabel();
            }
        }
    }

    public static class PlayerListEntry extends WPlainPanel {
        PlayerList list;
        PlayerListOverviewScreen parent;

        private final WLabel name;
        private final WButton editButton;
        private final WButton deleteButton;

        public PlayerListEntry() {
            name = new WLabel(Text.literal("Name"));
            editButton = new WButton(Text.translatable("gui.banana-alert.edit"));
            deleteButton = new WButton(Text.of("X"));

            editButton.setOnClick(() -> ClientScreen.openScreen(new PlayerListDetailsScreen(parent, list)));
            deleteButton.setOnClick(() -> {
                PlayerListManager.removeList(list.getId());
                if (parent != null) parent.refreshGUI();
                Util.DisplayToast(
                        Text.translatable("toast.banana-alert.delete_list.title"),
                        Text.translatable("toast.banana-alert.delete_list.description", list.getName())
                );
            });

            this.add(name, 0, 0, 100, GRID_SIZE);
            this.add(editButton, 0, 0, 50, GRID_SIZE);
            this.add(deleteButton, 0, 0, 18, GRID_SIZE);
        }

        @Override
        public boolean canResize() {
            return true;
        }

        @Override
        public void setSize(int x, int y) {
            super.setSize(x, y);
            this.layout();
        }

        @Override
        public void layout() {
            int padding = 4;
            int gap = 4;

            int height = Math.max(GRID_SIZE, this.getHeight());

            int deleteW = 18;
            int editW = 50;
            int contentH = Math.max(16, height - padding * 2);

            deleteButton.setSize(deleteW, contentH);
            editButton.setSize(editW, contentH);

            int right = this.getWidth() - padding;
            int deleteX = right - deleteW;
            int editX = deleteX - gap - editW;

            deleteButton.setLocation(deleteX, (height - contentH) / 2);
            editButton.setLocation(editX, (height - contentH) / 2);

            int nameW = Math.max(0, editX - gap - padding);
            name.setSize(nameW, contentH);
            name.setLocation(padding, (height - contentH) / 2);
        }

        public void setNameLabel() {
            MutableText label = Text.of(list.getName()).copy();
            if (list.getColor() != null) {
                label.formatted(list.getColor());
            }
            label.formatted(Formatting.BOLD);
            this.name.setText(label);
        }
    }
}
