package net.tanoflame.bananaalert.gui;

import io.github.cottonmc.cotton.gui.widget.WToggleButton;
import net.minecraft.text.Text;

import java.util.function.BiConsumer;

public class WidgetGenerator {
    public static WToggleButton createToggle(String key, BiConsumer<Boolean, WToggleButton> onToggle) {
        WToggleButton toggleButton = new WToggleButton(Text.translatable(key));
        toggleButton.setOnToggle((isActive) -> onToggle.accept(isActive, toggleButton));
        return toggleButton;
    }
}
