package net.tanoflame.bananaalert.gui.description;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.text.Text;
import net.tanoflame.bananaalert.PlayerList;
import net.tanoflame.bananaalert.PlayerListManager;
import net.tanoflame.bananaalert.gui.ClientScreen;
import net.tanoflame.bananaalert.gui.widget.PlayerListEntryWidget;

import java.util.List;

public class PlayerListOverviewScreen extends LightweightGuiDescription {
    private static final int GRID_COLUMNS = 16;
    private static final int GRID_ROWS = 11;
    private static final int GRID_SIZE = 18;
    private static final int GRID_GAP = 5;

    public PlayerListOverviewScreen() {
        WGridPanel root = new WGridPanel(GRID_SIZE);
        root.setSize(GRID_COLUMNS * GRID_SIZE, GRID_ROWS * GRID_SIZE);
        root.setGaps(GRID_GAP, GRID_GAP);
        root.setInsets(Insets.ROOT_PANEL);

        WLabel titleLabel = new WLabel(Text.translatable("gui.banana-alert.list_overview.title"));
        root.add(titleLabel, 0, 0, GRID_COLUMNS, 1);

        WGridPanel playerListPanel = new WGridPanel();
        playerListPanel.setGaps(0, 3);

        List<PlayerList> playerLists = PlayerListManager.getLists().values().stream().toList();
        for (int i = 0; i < playerLists.size(); i++) {
            PlayerList list = playerLists.get(i);
            PlayerListEntryWidget entryWidget = new PlayerListEntryWidget(list, GRID_SIZE, GRID_COLUMNS);

            entryWidget.setSize(GRID_COLUMNS * GRID_SIZE, GRID_SIZE);
            playerListPanel.add(entryWidget, 0, i, GRID_COLUMNS, 1);
        }

        WScrollPanel scrollablePlayerList = new WScrollPanel(playerListPanel);
        scrollablePlayerList.setScrollingVertically(TriState.TRUE);
        scrollablePlayerList.setScrollingHorizontally(TriState.FALSE);
        scrollablePlayerList.setSize(300, 140);
        root.add(scrollablePlayerList, 0, 1, GRID_COLUMNS, GRID_ROWS - 5);

        WButton addPlayerListButton = new WButton(Text.translatable("gui.banana-alert.list_overview.add_list"));
        addPlayerListButton.setOnClick(() -> {
            ClientScreen.openScreen(new AddPlayerListScreen());
        });
        root.add(addPlayerListButton, 0, GRID_ROWS - 4, GRID_COLUMNS / 2, 1);

        WButton settingsButton = new WButton(Text.translatable("gui.banana-alert.list_overview.settings"));
        settingsButton.setOnClick(() -> {
//            openScreen(new GlobalSettingsScreen());
        });
        root.add(settingsButton, GRID_COLUMNS / 2, GRID_ROWS - 4, GRID_COLUMNS / 2, 1);

        root.validate(this);
        setRootPanel(root);
    }
}
