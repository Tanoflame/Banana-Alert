package net.tanoflame.bananaalert.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

public class ClientScreen extends CottonClientScreen {
    public ClientScreen previousScreen;

    public ClientScreen(GuiDescription description) {
        super(description);

        Screen lastScreen = MinecraftClient.getInstance().currentScreen;
        if (lastScreen instanceof ClientScreen) {
            previousScreen = (ClientScreen) lastScreen;
        }
    }

    @Override
    public void removed() {
        // TODO: Implement return system to previousScreen
    }

    public static ClientScreen openScreen(GuiDescription description) {
        ClientScreen screen =  new ClientScreen(description);
//        MinecraftClient.getInstance().currentScreen = null;
        MinecraftClient.getInstance().setScreen(screen);
        return screen;
    }
}
