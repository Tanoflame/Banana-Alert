package net.tanoflame.bananaalert.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.entity.player.PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "getDisplayName", at = @At("RETURN"))
    public void getDisplayName(CallbackInfoReturnable<Text> cir, @Local MutableText mutableText) {
        mutableText.formatted(Formatting.BLUE);
    }
}
