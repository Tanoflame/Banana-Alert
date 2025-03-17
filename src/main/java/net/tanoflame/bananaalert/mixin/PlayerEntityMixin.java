package net.tanoflame.bananaalert.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.tanoflame.bananaalert.PlayerEntry;
import net.tanoflame.bananaalert.PlayerList;
import net.tanoflame.bananaalert.PlayerListManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(net.minecraft.entity.player.PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Shadow public abstract GameProfile getGameProfile();

    @Inject(method = "getDisplayName", at = @At("RETURN"))
    public void getDisplayName(CallbackInfoReturnable<Text> cir, @Local MutableText mutableText) {
        UUID uuid = this.getGameProfile().getId();

        PlayerEntry entry = PlayerListManager.getPlayerEntry(uuid);
        if (entry != null && entry.getPlayerListId() != Util.NIL_UUID) {
            PlayerList list = PlayerListManager.getList(entry.getPlayerListId());

            mutableText.formatted(list.getColor());
        }
    }
}
