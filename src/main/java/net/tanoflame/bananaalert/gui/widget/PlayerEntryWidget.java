package net.tanoflame.bananaalert.gui.widget;

import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import net.minecraft.text.Text;
import net.tanoflame.bananaalert.PlayerEntry;
import net.tanoflame.bananaalert.PlayerListManager;
import net.tanoflame.bananaalert.gui.ClientScreen;
import net.tanoflame.bananaalert.gui.description.EditPlayerScreen;

public class PlayerEntryWidget extends WGridPanel {

    public PlayerEntryWidget(PlayerEntry player, int gridSize, int gridColumns) {
        super(gridSize);
        this.setSize(gridColumns * 18, 18);

        // Player Name Label
        WLabel nameLabel = new WLabel(Text.of(player.getName()));
        this.add(nameLabel, 0, 0, gridColumns - 3, 1);

        // Edit Button
        WButton editButton = new WButton(Text.translatable("gui.banana-alert.edit"));
        editButton.setOnClick(() -> ClientScreen.openScreen(new EditPlayerScreen(player)));
        this.add(editButton, gridColumns - 3, 0, 2, 1);

        // Remove Button
        WButton removeButton = new WButton(Text.of("X"));
        removeButton.setOnClick(() -> {
            PlayerListManager.removePlayerEntry(player);
            // refresh screen
        });
        this.add(removeButton, gridColumns - 1, 0, 1, 1);
    }
}
