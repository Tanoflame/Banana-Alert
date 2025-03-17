package net.tanoflame.bananaalert.gui.widget;

import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.tanoflame.bananaalert.PlayerList;
import net.tanoflame.bananaalert.PlayerListManager;
import net.tanoflame.bananaalert.gui.ClientScreen;
import net.tanoflame.bananaalert.gui.description.PlayerListDetailsScreen;

public class PlayerListEntryWidget extends WGridPanel {

    public PlayerListEntryWidget(PlayerList list, int gridSize, int gridColumns) {
        super(gridSize);
        this.setSize(gridColumns * gridSize, gridSize);

        MutableText label = Text.of(list.getName()).copy();
        if (list.getColor() != null) {
            label.formatted(list.getColor());
        }
        label.formatted(Formatting.BOLD);
        WLabel nameLabel = new WLabel(label);
        add(nameLabel, 0, 0, gridColumns - 3, 1);

        WButton editButton = new WButton(Text.translatable("gui.banana-alert.edit"));
        editButton.setOnClick(() -> {
            ClientScreen.openScreen(new PlayerListDetailsScreen(list));
        });
        add(editButton, gridColumns - 3, 0, 2, 1);

        WButton deleteButton = new WButton(Text.of("X"));
        deleteButton.setOnClick(() -> {
            PlayerListManager.removeList(list.getId());
            // TODO: Refresh the UI after deletion
        });
        add(deleteButton, gridColumns - 1, 0, 2, 1);
    }
}
