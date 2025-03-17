package net.tanoflame.bananaalert.gui.description;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.minecraft.text.Text;
import net.tanoflame.bananaalert.BananaAlert;
import net.tanoflame.bananaalert.PlayerEntry;
import net.tanoflame.bananaalert.PlayerList;
import net.tanoflame.bananaalert.PlayerListManager;

public class AddPlayerScreen extends LightweightGuiDescription {
    private static final int GRID_COLUMNS = 10;
    private static final int GRID_ROWS = 5;
    private static final int GRID_SIZE = 18;
    private static final int GRID_GAP = 5;

    public AddPlayerScreen(PlayerList playerList) {
        WGridPanel root = new WGridPanel();
        root.setSize(GRID_COLUMNS * GRID_SIZE, GRID_ROWS * GRID_SIZE); // Set the screen size.
        root.setGaps(GRID_GAP, GRID_GAP);
        root.setInsets(Insets.ROOT_PANEL);

        WLabel nameLabel = new WLabel(Text.translatable("gui.banana-alert.player_screen.name"));
        nameLabel.setVerticalAlignment(VerticalAlignment.CENTER);
        root.add(nameLabel, 0, 0, 2, 1);

        WTextField nameField = new WTextField();
        root.add(nameField, 2, 0, 8, 1);

        WLabel notesLabel = new WLabel(Text.translatable("gui.banana-alert.player_screen.notes"));
        notesLabel.setVerticalAlignment(VerticalAlignment.CENTER);
        root.add(notesLabel, 0, 1, 2, 1);

        WTextField notesField = new WTextField();
        root.add(notesField, 2, 1, 8, 1);

        WButton saveButton = new WButton(Text.translatable("gui.banana-alert.save"));
        saveButton.setOnClick(() -> {
            String playerName = nameField.getText();
            String playerNotes = notesField.getText();

            if (!playerName.isEmpty()) {
                PlayerEntry newPlayer = PlayerListManager.addPlayerEntry(playerList.getId(), playerName);

                if (newPlayer == null) {
                    BananaAlert.showError("Failed to retrieve player by that name.");
                    return;
                }

                if (!playerNotes.isEmpty()) {
                    newPlayer.setNotes(playerNotes);
                }
//                closeScreen(); // Close the screen after saving
            } else {
                BananaAlert.showError("Both name and notes are required.");
            }
        });
        root.add(saveButton, 0, 4, 5, 1);

        // Cancel Button
        WButton cancelButton = new WButton(Text.translatable("gui.banana-alert.cancel"));
//        cancelButton.setOnClick(this::closeScreen);
        root.add(cancelButton, 5, 4, 5, 1);

        setRootPanel(root);
        root.validate(this);
    }
}
