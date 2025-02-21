package net.tanoflame.bananaalert.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerListHud.class)
public class PlayerListMixin {
    @ModifyReturnValue(method = "getPlayerName", at = @At("RETURN"))
    public Text getPlayerName(Text original) {
        return original.copy().formatted(Formatting.RESET).formatted(Formatting.BLUE);
    }
}
