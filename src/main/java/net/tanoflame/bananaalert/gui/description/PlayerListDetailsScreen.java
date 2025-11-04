package net.tanoflame.bananaalert.gui.description;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.tanoflame.bananaalert.PlayerEntry;
import net.tanoflame.bananaalert.PlayerList;
import net.tanoflame.bananaalert.PlayerListManager;
import net.tanoflame.bananaalert.gui.ClientScreen;
import net.tanoflame.bananaalert.util.Util;

import java.util.List;
import java.util.AbstractList;
import java.util.function.BiConsumer;

public class PlayerListDetailsScreen extends LightweightGuiDescription {
    private static final int GRID_COLUMNS = 10;
    private static final int GRID_ROWS = 10;
    private static final int GRID_SIZE = 18;
    private static final int GRID_GAP = 5;

    private final PlayerList playerList;
    private final WListPanel<PlayerEntry, PlayerEntryRow> playerListPanel;

    public PlayerListDetailsScreen(PlayerListOverviewScreen parent, PlayerList playerList) {
        this.playerList = playerList;
        WGridPanel root = new WGridPanel();
        root.setSize(GRID_COLUMNS * GRID_SIZE, GRID_ROWS * GRID_SIZE);
        root.setGaps(GRID_GAP, GRID_GAP);
        root.setInsets(Insets.ROOT_PANEL);

        WLabel titleLabel = new WLabel(Text.translatable("gui.banana-alert.list_details.title", playerList.getName()));
        root.add(titleLabel, 0, 0, GRID_COLUMNS, 1);

        WTextField nameField = new WTextField();
        nameField.setText(playerList.getName());
        root.add(nameField, 0, 1, GRID_COLUMNS, 1);

        WTextField descriptionField = new WTextField();
        descriptionField.setText(playerList.getDescription());
        root.add(descriptionField, 0, 2, GRID_COLUMNS, 1);

        WTextField colorField = new WTextField();
        colorField.setText(playerList.getColor().getName());
        root.add(colorField, 0, 3, GRID_COLUMNS, 1);

        WToggleButton warningToggle = new WToggleButton(Text.translatable("gui.banana-alert.list_screen.warning"));
        warningToggle.setToggle(playerList.isWarningsEnabled());
        root.add(warningToggle, 0, 4, 2, 1);

        BiConsumer<PlayerEntry, PlayerEntryRow> configurator = (player, row) -> {
            row.entry = player;
            row.parent = this;
            row.setNameLabel();
        };

        List<PlayerEntry> data = new AbstractList<>() {
            @Override
            public PlayerEntry get(int index) {
                return PlayerListManager.getPlayersOfList(playerList.getId()).get(index);
            }

            @Override
            public int size() {
                return PlayerListManager.getPlayersOfList(playerList.getId()).size();
            }
        };

        this.playerListPanel = new WListPanel<>(data, PlayerEntryRow::new, configurator);
        this.playerListPanel.setListItemHeight(GRID_SIZE);
        root.add(this.playerListPanel, 0, 5, GRID_COLUMNS, 4);

        WButton addPlayerButton = new WButton(Text.translatable("gui.banana-alert.list_details.add_player"));
        addPlayerButton.setOnClick(() -> ClientScreen.openScreen(new AddPlayerScreen(playerList, this)));
        root.add(addPlayerButton, 0, 9, 3, 1);

        WButton saveButton = new WButton(Text.translatable("gui.banana-alert.save"));
        saveButton.setOnClick(() -> {
            playerList.setName(nameField.getText());
            playerList.setDescription(descriptionField.getText());
            playerList.setColor(Formatting.byName(colorField.getText()));
            playerList.setWarningsEnabled(warningToggle.getToggle());
            ClientScreen.closeScreen();
            parent.refreshGUI();
        });
        root.add(saveButton, 7, 9, 3, 1);

        setRootPanel(root);
        root.validate(this);
    }

    public void refreshGUI() {
        // Re-validate to run layout and ensure visible rows update immediately
        if (getRootPanel() != null) getRootPanel().validate(this);
        // Update visible row labels (name might have changed)
        for (WWidget child : this.playerListPanel.streamChildren().toList()) {
            if (child instanceof PlayerEntryRow row) {
                row.setNameLabel();
            }
        }
    }

    public static class PlayerEntryRow extends WPlainPanel {
        PlayerEntry entry;
        PlayerListDetailsScreen parent;

        private final WLabel name;
        private final WButton editButton;
        private final WButton removeButton;

        public PlayerEntryRow() {
            name = new WLabel(Text.literal("Name"));
            editButton = new WButton(Text.translatable("gui.banana-alert.edit"));
            removeButton = new WButton(Text.of("X"));

            editButton.setOnClick(() -> ClientScreen.openScreen(new EditPlayerScreen(entry, parent)));
            removeButton.setOnClick(() -> {
                PlayerListManager.removePlayerEntry(entry);
                if (parent != null) parent.refreshGUI();
                PlayerList list = PlayerListManager.getList(entry.getPlayerListId());
                Util.DisplayToast(Text.translatable("toast.banana-alert.delete_player.title"),
                        Text.translatable("toast.banana-alert.delete_player.description", entry.getName(), list.getName()));
            });

            this.add(name, 0, 0, 100, GRID_SIZE);
            this.add(editButton, 0, 0, 50, GRID_SIZE);
            this.add(removeButton, 0, 0, 18, GRID_SIZE);
        }

        @Override
        public void layout() {
            int padding = 4;
            int gap = 4;

            int height = Math.max(GRID_SIZE, this.getHeight());

            int removeW = 18;
            int editW = 50;
            int contentH = Math.max(16, height - padding * 2);

            removeButton.setSize(removeW, contentH);
            editButton.setSize(editW, contentH);

            int right = this.getWidth() - padding;
            int removeX = right - removeW;
            int editX = removeX - gap - editW;

            removeButton.setLocation(removeX, (height - contentH) / 2);
            editButton.setLocation(editX, (height - contentH) / 2);

            int nameX = padding;
            int nameW = Math.max(0, editX - gap - nameX);
            name.setSize(nameW, contentH);
            name.setLocation(nameX, (height - contentH) / 2);
        }

        public void setNameLabel() {
            this.name.setText(Text.of(entry.getName()));
        }
    }
}