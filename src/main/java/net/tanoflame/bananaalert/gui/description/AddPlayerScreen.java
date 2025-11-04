package net.tanoflame.bananaalert.gui.description;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.tanoflame.bananaalert.BananaAlert;
import net.tanoflame.bananaalert.PlayerEntry;
import net.tanoflame.bananaalert.PlayerList;
import net.tanoflame.bananaalert.PlayerListManager;
import net.tanoflame.bananaalert.gui.ClientScreen;
import net.tanoflame.bananaalert.util.Util;

public class AddPlayerScreen extends LightweightGuiDescription {
    private static final int GRID_COLUMNS = 10;
    private static final int GRID_ROWS = 5;
    private static final int GRID_SIZE = 18;
    private static final int GRID_GAP = 5;

    public AddPlayerScreen(PlayerList list, PlayerListDetailsScreen parent) {
        WGridPanel root = new WGridPanel();
        root.setSize(GRID_COLUMNS * GRID_SIZE, GRID_ROWS * GRID_SIZE);
        root.setGaps(GRID_GAP, GRID_GAP);
        root.setInsets(Insets.ROOT_PANEL);

        MutableText formattedName = Text.of(list.getName()).copy();
        if (list.getColor() != null) {
            formattedName.formatted(list.getColor());
        }
        formattedName.formatted(Formatting.BOLD);
        MutableText title = Text.translatable("gui.banana-alert.add_player.title", formattedName);
        WLabel titleLabel = new WLabel(title);
        root.add(titleLabel, 0, 0, GRID_COLUMNS, 1);

        WLabel nameLabel = new WLabel(Text.translatable("gui.banana-alert.player_screen.name"));
        nameLabel.setVerticalAlignment(VerticalAlignment.CENTER);
        root.add(nameLabel, 0, 1, 2, 1);

        WTextField nameField = new WTextField();
        root.add(nameField, 2, 1, 8, 1);

        WLabel notesLabel = new WLabel(Text.translatable("gui.banana-alert.player_screen.notes"));
        notesLabel.setVerticalAlignment(VerticalAlignment.CENTER);
        root.add(notesLabel, 0, 2, 2, 1);

        WTextField notesField = new WTextField();
        notesField.setMaxLength(256);
        root.add(notesField, 2, 2, 8, 1);

        WButton saveButton = new WButton(Text.translatable("gui.banana-alert.save"));
        saveButton.setOnClick(() -> {
            String playerName = nameField.getText();
            String playerNotes = notesField.getText();

            if (!playerName.isEmpty()) {
                PlayerEntry newPlayer = PlayerListManager.addPlayerEntry(list.getId(), playerName);

                if (newPlayer == null) {
                    BananaAlert.showError(Text.translatable("toast.banana-alert.error.retrieve_player_by_name"));
                    return;
                }

                if (!playerNotes.isEmpty()) {
                    newPlayer.setNotes(playerNotes);
                }
                ClientScreen.closeScreen();
                parent.refreshGUI();
                Util.DisplayToast(Text.translatable("toast.banana-alert.add_player.title"),
                        Text.translatable("toast.banana-alert.add_player.description", newPlayer.getName(), list.getName()));
            } else {
                BananaAlert.showError(Text.translatable("toast.banana-alert.error.require_player_name"));
            }
        });
        root.add(saveButton, 0, 4, 5, 1);

        WButton cancelButton = new WButton(Text.translatable("gui.banana-alert.cancel"));
        cancelButton.setOnClick(ClientScreen::closeScreen);
        root.add(cancelButton, 5, 4, 5, 1);

        setRootPanel(root);
        root.validate(this);
    }
}
