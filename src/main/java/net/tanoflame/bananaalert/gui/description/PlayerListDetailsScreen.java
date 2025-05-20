package net.tanoflame.bananaalert.gui.description;

import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.tanoflame.bananaalert.PlayerEntry;
import net.tanoflame.bananaalert.PlayerList;
import net.tanoflame.bananaalert.PlayerListManager;
import net.tanoflame.bananaalert.gui.ClientScreen;
import net.tanoflame.bananaalert.gui.RefreshableGUIDescription;
import net.tanoflame.bananaalert.gui.widget.PlayerEntryWidget;

import java.util.List;

public class PlayerListDetailsScreen extends RefreshableGUIDescription {
    private static final int GRID_COLUMNS = 10;
    private static final int GRID_ROWS = 10;
    private static final int GRID_SIZE = 18;
    private static final int GRID_GAP = 5;

    private final PlayerList playerList;
    private final WGridPanel playerListPanel;

    public PlayerListDetailsScreen(PlayerList playerList) {
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

        this.playerListPanel = new WGridPanel();
        WScrollPanel scrollablePlayerList = new WScrollPanel(playerListPanel);
        scrollablePlayerList.setScrollingVertically(TriState.TRUE);
        root.add(scrollablePlayerList, 0, 5, GRID_COLUMNS, 4);

        refreshGUI();

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
        });
        root.add(saveButton, 7, 9, 3, 1);

        setRootPanel(root);
        root.validate(this);
    }

    @Override
    public void refreshGUI() {
        List<WWidget> children = this.playerListPanel.streamChildren().toList();
        for (WWidget child : children) {
            this.playerListPanel.remove(child);
        }

        List<PlayerEntry> players = PlayerListManager.getPlayersOfList(playerList.getId());
        for (int i = 0; i < players.size(); i++) {
            PlayerEntryWidget entry = new PlayerEntryWidget(players.get(i), GRID_SIZE, GRID_COLUMNS, this);
            this.playerListPanel.add(entry, 0, i, GRID_COLUMNS, 1);
        }

        this.playerListPanel.validate(this);
    }
}