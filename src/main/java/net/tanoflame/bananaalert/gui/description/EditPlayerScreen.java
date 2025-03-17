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
import net.tanoflame.bananaalert.PlayerListManager;

public class EditPlayerScreen extends LightweightGuiDescription {
    private static final int GRID_COLUMNS = 12;
    private static final int GRID_ROWS = 7;
    private static final int GRID_SIZE = 18;
    private static final int GRID_GAP = 5;

    public EditPlayerScreen(PlayerEntry player) {
        WGridPanel root = new WGridPanel();
        root.setSize(GRID_COLUMNS * GRID_SIZE, GRID_ROWS * GRID_SIZE);
        root.setGaps(GRID_GAP, GRID_GAP);
        root.setInsets(Insets.ROOT_PANEL);

        // Player UUID Display (Non-Editable)
        WLabel uuidLabel = new WLabel(Text.of("UUID:"));
        root.add(uuidLabel, 0, 0, 2, 1);

        WLabel uuidValue = new WLabel(Text.of(player.getUUID().toString()));
        root.add(uuidValue, 2, 0, GRID_COLUMNS - 2, 1);

        WLabel nameLabel = new WLabel(Text.of("Name:"));
        nameLabel.setVerticalAlignment(VerticalAlignment.CENTER);
        root.add(nameLabel, 0, 1, 2, 1);

        WTextField nameField = new WTextField();
        nameField.setText(player.getName());
        root.add(nameField, 2, 1, GRID_COLUMNS - 2, 1);

        WLabel notesLabel = new WLabel(Text.of("Notes:"));
        notesLabel.setVerticalAlignment(VerticalAlignment.CENTER);
        root.add(notesLabel, 0, 2, 2, 1);

        WTextField notesField = new WTextField();
        notesField.setText(player.getNotes() == null ? "" : player.getNotes());
        root.add(notesField, 2, 2, GRID_COLUMNS - 2, 1);

        WButton saveButton = new WButton(Text.of("Save"));
        saveButton.setOnClick(() -> {
            String newName = nameField.getText();
            String newNotes = notesField.getText();

            if (!newName.isEmpty() && !newNotes.isEmpty()) {
                player.setName(newName);
                player.setNotes(newNotes);
//                closeScreen();
            } else {
                BananaAlert.showError("Both name and notes are required.");
            }
        });
        root.add(saveButton, 0, 5, 4, 1);

        WButton deleteButton = new WButton(Text.of("Delete"));
        deleteButton.setOnClick(() -> {
            PlayerListManager.removePlayerEntry(player);
//            closeScreen();
        });
        root.add(deleteButton, 4, 5, 4, 1);

        WButton cancelButton = new WButton(Text.of("Cancel"));
//        cancelButton.setOnClick(this::closeScreen);
        root.add(cancelButton, 8, 5, 4, 1);

        setRootPanel(root);
        root.validate(this);
    }
}
