package net.tanoflame.bananaalert.gui.description;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.tanoflame.bananaalert.BananaAlert;
import net.tanoflame.bananaalert.PlayerList;
import net.tanoflame.bananaalert.PlayerListManager;
import net.tanoflame.bananaalert.gui.ClientScreen;

public class AddPlayerListScreen extends LightweightGuiDescription {
    private static final int GRID_COLUMNS = 10;
    private static final int GRID_ROWS = 7;
    private static final int GRID_SIZE = 18;
    private static final int GRID_GAP = 5;

    public AddPlayerListScreen() {
        WGridPanel root = new WGridPanel();
        root.setSize(GRID_COLUMNS * GRID_SIZE, GRID_ROWS * GRID_SIZE);
        root.setGaps(GRID_GAP, GRID_GAP);
        root.setInsets(Insets.ROOT_PANEL);

        WLabel nameLabel = new WLabel(Text.translatable("gui.banana-alert.list_screen.list_name"));
        root.add(nameLabel, 0, 0, 3, 1);

        WTextField nameField = new WTextField();
        root.add(nameField, 3, 0, 7, 1);

        WLabel descriptionLabel = new WLabel(Text.translatable("gui.banana-alert.list_screen.description"));
        root.add(descriptionLabel, 0, 1, 3, 1);

        WTextField descriptionField = new WTextField();
        root.add(descriptionField, 3, 1, 7, 1);

        WLabel colorLabel = new WLabel(Text.translatable("gui.banana-alert.list_screen.color"));
        root.add(colorLabel, 0, 2, 3, 1);

        WTextField colorField = new WTextField();
        root.add(colorField, 3, 2, 7, 1);

        WToggleButton warningToggle = new WToggleButton(Text.translatable("gui.banana-alert.list_screen.warning"));
        root.add(warningToggle, 2, 3, 8, 1);

        WButton saveButton = new WButton(Text.translatable("gui.banana-alert.save"));
        saveButton.setOnClick(() -> {
            String name = nameField.getText();
            String description = descriptionField.getText();
            String color = colorField.getText();
            boolean warningsEnabled = warningToggle.getToggle();

            if (!name.isEmpty()) {
                PlayerList newList = new PlayerList(name);
                newList.setColor(color.isEmpty()? null : Formatting.byName(color));
                newList.setDescription(description);
                newList.setWarningsEnabled(warningsEnabled);
                PlayerListManager.addPlayerList(newList);
                ClientScreen.closeScreen();
            } else {
                BananaAlert.showError(Text.translatable("toast.banana-alert.error.require_list_name"));
            }
        });
        root.add(saveButton, 0, 5, 5, 1);

        WButton cancelButton = new WButton(Text.translatable("gui.banana-alert.cancel"));
        cancelButton.setOnClick(ClientScreen::closeScreen);
        root.add(cancelButton, 5, 5, 5, 1);

        setRootPanel(root);
        root.validate(this);
    }
}
