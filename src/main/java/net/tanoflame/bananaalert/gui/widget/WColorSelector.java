package net.tanoflame.bananaalert.gui.widget;

import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.minecraft.client.gui.DrawContext;

import java.awt.Color;
import java.util.function.Consumer;

public class WColorSelector extends WWidget {
    private Color color;
    private Consumer<Color> onColorChange;

    public WColorSelector(Color initialColor, Consumer<Color> onColorChange) {
        this.color = initialColor;
        this.onColorChange = onColorChange;
        this.setSize(16, 16); // Default size
    }

    @Override
    public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
        // Draw the color preview box
//        fill(context, x, y, x + getWidth(), y + getHeight(), color.getRGB());
//
//        // Optional: Draw a border around the color box
//        fill(context, x, y, x + getWidth(), y + 1, 0xFF000000); // Top
//        fill(context, x, y, x + 1, y + getHeight(), 0xFF000000); // Left
//        fill(context, x + getWidth() - 1, y, x + getWidth(), y + getHeight(), 0xFF000000); // Right
//        fill(context, x, y + getHeight() - 1, x + getWidth(), y + getHeight(), 0xFF000000); // Bottom
    }

    @Override
    public InputResult onClick(int x, int y, int button) {
        if (button == 0) { // Left-click to change color
            cycleColor();
            if (onColorChange != null) {
                onColorChange.accept(color);
            }
            return InputResult.PROCESSED;
        }
        return InputResult.IGNORED;
    }

    private void cycleColor() {
        // Example: Cycle between predefined colors
        if (color.equals(Color.RED)) {
            color = Color.GREEN;
        } else if (color.equals(Color.GREEN)) {
            color = Color.BLUE;
        } else {
            color = Color.RED;
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color newColor) {
        this.color = newColor;
        if (onColorChange != null) {
            onColorChange.accept(newColor);
        }
    }
}
