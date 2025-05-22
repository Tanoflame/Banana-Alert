package net.tanoflame.bananaalert.gui.widget;

import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import net.minecraft.text.Text;
import net.tanoflame.bananaalert.PlayerEntry;
import net.tanoflame.bananaalert.PlayerList;
import net.tanoflame.bananaalert.PlayerListManager;
import net.tanoflame.bananaalert.gui.ClientScreen;
import net.tanoflame.bananaalert.gui.RefreshableGUIDescription;
import net.tanoflame.bananaalert.gui.description.EditPlayerScreen;
import net.tanoflame.bananaalert.util.Util;

public class PlayerEntryWidget extends WGridPanel {

    public PlayerEntryWidget(PlayerEntry player, int gridSize, int gridColumns, RefreshableGUIDescription parent) {
        super(gridSize);
        this.setSize(gridColumns * gridSize, gridSize);

        // Player Name Label
        WLabel nameLabel = new WLabel(Text.of(player.getName()));
        this.add(nameLabel, 0, 0, gridColumns - 3, 1);

        // Edit Button
        WButton editButton = new WButton(Text.translatable("gui.banana-alert.edit"));
        editButton.setOnClick(() -> ClientScreen.openScreen(new EditPlayerScreen(player, parent)));
        this.add(editButton, gridColumns - 1, 0, 2, 1);

        // Remove Button
        WButton removeButton = new WButton(Text.of("X"));
        removeButton.setOnClick(() -> {
            PlayerListManager.removePlayerEntry(player);
            parent.refreshGUI();
            PlayerList list = PlayerListManager.getList(player.getPlayerListId());
            Util.DisplayToast(Text.translatable("toast.banana-alert.delete_player.title"),
                    Text.translatable("toast.banana-alert.delete_player.description", player.getName(), list.getName()));
        });
        this.add(removeButton, gridColumns + 1, 0, 1, 1);
    }
}
