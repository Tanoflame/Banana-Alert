package net.tanoflame.bananaalert.gui.description;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.tanoflame.bananaalert.PlayerEntry;
import net.tanoflame.bananaalert.PlayerList;
import net.tanoflame.bananaalert.PlayerListManager;
import net.tanoflame.bananaalert.gui.ClientScreen;
import net.tanoflame.bananaalert.gui.RefreshableGUIDescription;
import net.tanoflame.bananaalert.util.Util;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MovePlayerScreen extends LightweightGuiDescription {
    private static final int GRID_COLUMNS = 10;
    private static final int GRID_ROWS = 7;
    private static final int GRID_SIZE = 18;
    private static final int GRID_GAP = 5;

    private final PlayerEntry player;
    @Nullable private UUID list;

    private boolean isToggling = false;
    private List<WToggleButton> buttons = new ArrayList<>();

    @Nullable private Runnable onSave;

    public MovePlayerScreen(PlayerEntry player, RefreshableGUIDescription refresh) {
        this.player = player;
        list = player.getPlayerListId();
        WGridPanel root = new WGridPanel(GRID_SIZE);
        root.setSize(GRID_COLUMNS * GRID_SIZE, GRID_ROWS * GRID_SIZE);
        root.setGaps(GRID_GAP, GRID_GAP);
        root.setInsets(Insets.ROOT_PANEL);

        WLabel title = new WLabel(Text.translatable("gui.banana-alert.move_player.title", player.getName()));
        title.setVerticalAlignment(VerticalAlignment.CENTER);
        root.add(title, 0, 0, GRID_COLUMNS, 1);

        WGridPanel playerListPanel = new WGridPanel();
        playerListPanel.setGaps(0, 3);

        WScrollPanel scrollablePlayerList = new WScrollPanel(playerListPanel);
        scrollablePlayerList.setScrollingVertically(TriState.TRUE);
        scrollablePlayerList.setScrollingHorizontally(TriState.FALSE);
//        scrollablePlayerList.setSize(300, 140);
        root.add(scrollablePlayerList, 0, 1, GRID_COLUMNS, GRID_ROWS - 2);

        List<PlayerList> playerLists = PlayerListManager.getLists().values().stream().toList();
        for (int i = 0; i < playerLists.size(); i++) {
            PlayerList list = playerLists.get(i);
            if (list.getId().equals(player.getPlayerListId())) continue;

            MutableText label = Text.of(list.getName()).copy();
            if (list.getColor() != null) {
                label.formatted(list.getColor());
            }
            label.formatted(Formatting.BOLD);

            // Make toggle button instead
            WToggleButton entryButton = new WToggleButton(label);
            entryButton.setOnToggle((Boolean isToggled) -> {
                if (isToggling) return;
                isToggling = true;

                if (isToggled) {
                    this.list = list.getId();
                    buttons.forEach((button) -> {
                        if (button.equals(entryButton)) return;
                        button.setToggle(false);
                    });
                } else {
                    entryButton.setToggle(true);
                }

                isToggling = false;
            });
            buttons.add(entryButton);

            entryButton.setSize(GRID_COLUMNS * GRID_SIZE, GRID_SIZE);
            playerListPanel.add(entryButton, 0, i, GRID_COLUMNS, 1);
        }

        // implement save and cancel buttons
        WButton saveButton = new WButton(Text.translatable("gui.banana-alert.save"));
        saveButton.setOnClick(() -> {
            if (this.list == null) return;

            PlayerListManager.movePlayerToList(this.list, player);

            PlayerList newList = PlayerListManager.getList(this.list);
            Util.DisplayToast(Text.translatable("toast.banana-alert.move_player.title"),
                    Text.translatable("toast.banana-alert.move_player.description", player.getName(), newList.getName()));
            ClientScreen.closeScreen();
            refresh.refreshGUI();
            if (onSave != null) onSave.run();
        });
        root.add(saveButton, 0, GRID_ROWS - 1, 5, 1);

        WButton cancelButton = new WButton(Text.translatable("gui.banana-alert.cancel"));
        cancelButton.setOnClick(ClientScreen::closeScreen);
        root.add(cancelButton, GRID_COLUMNS - 5, GRID_ROWS - 1, 5, 1);

        root.validate(this);
        setRootPanel(root);
    }

    public MovePlayerScreen setOnSave(@Nullable Runnable onSave) {
        this.onSave = onSave;
        return this;
    }
}
