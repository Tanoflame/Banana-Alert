package net.tanoflame.bananaalert.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.glfw.GLFW;

public class ClientScreen extends CottonClientScreen {
    public final ClientScreen previousScreen;
    private boolean switchingScreens = false;

    public ClientScreen(GuiDescription description) {
        super(description);

        Screen lastScreen = MinecraftClient.getInstance().currentScreen;
        if (lastScreen instanceof ClientScreen) {
            previousScreen = (ClientScreen) lastScreen;
        } else {
            previousScreen = null;
        }
    }

    @Override
    public boolean keyPressed(int ch, int keyCode, int modifiers) {
        if (ch == GLFW.GLFW_KEY_ESCAPE) {
            closeScreen();
            return true;
        }
        return super.keyPressed(ch, keyCode, modifiers);
    }

    public static ClientScreen openScreen(LightweightGuiDescription description) {
        ClientScreen screen = new ClientScreen(description);
        if (MinecraftClient.getInstance().currentScreen instanceof ClientScreen current) {
            current.switchingScreens = true;
        }
        MinecraftClient.getInstance().setScreen(screen);
        return screen;
    }

    public static void closeScreen() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen instanceof ClientScreen screen && screen.previousScreen != null) {
            screen.switchingScreens = true;
            client.setScreen(screen.previousScreen);
        } else {
            client.setScreen(null);
        }
    }
}
