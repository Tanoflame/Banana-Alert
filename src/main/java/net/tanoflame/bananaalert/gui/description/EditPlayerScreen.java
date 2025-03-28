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
import net.tanoflame.bananaalert.gui.ClientScreen;

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
        WLabel uuidLabel = new WLabel(Text.translatable("gui.banana-alert.player_screen.uuid"));
        root.add(uuidLabel, 0, 0, 2, 1);

        WTextField uuidValue = new WTextField();
        uuidValue.setEditable(false);
        uuidValue.setText(player.getUUID().toString());
        root.add(uuidValue, 2, 0, GRID_COLUMNS - 2, 1);

        WLabel nameLabel = new WLabel(Text.translatable("gui.banana-alert.player_screen.name"));
        nameLabel.setVerticalAlignment(VerticalAlignment.CENTER);
        root.add(nameLabel, 0, 1, 2, 1);

        WTextField nameField = new WTextField();
        nameField.setEditable(false);
        nameField.setText(player.getName());
        root.add(nameField, 2, 1, GRID_COLUMNS - 2, 1);

        WLabel notesLabel = new WLabel(Text.translatable("gui.banana-alert.player_screen.notes"));
        notesLabel.setVerticalAlignment(VerticalAlignment.CENTER);
        root.add(notesLabel, 0, 2, 2, 1);

        WTextField notesField = new WTextField();
        notesField.setMaxLength(256);
        notesField.setText(player.getNotes() == null ? "" : player.getNotes());
        root.add(notesField, 2, 2, GRID_COLUMNS - 2, 1);

        WButton saveButton = new WButton(Text.translatable("gui.banana-alert.save"));
        saveButton.setOnClick(() -> {
            String newName = nameField.getText();
            String newNotes = notesField.getText();

            if (!newName.isEmpty() && !newNotes.isEmpty()) {
                player.setName(newName);
                player.setNotes(newNotes);
                ClientScreen.closeScreen();
            } else {
                BananaAlert.showError(Text.translatable("gui.banana-alert.error.require_player_name"));
            }
        });
        root.add(saveButton, 0, 5, 4, 1);

        WButton deleteButton = new WButton(Text.translatable("gui.banana-alert.delete"));
        deleteButton.setOnClick(() -> {
            PlayerListManager.removePlayerEntry(player);
            ClientScreen.closeScreen();
        });
        root.add(deleteButton, 4, 5, 4, 1);

        WButton cancelButton = new WButton(Text.translatable("gui.banana-alert.cancel"));
        cancelButton.setOnClick(ClientScreen::closeScreen);
        root.add(cancelButton, 8, 5, 4, 1);

        setRootPanel(root);
        root.validate(this);
    }
}
