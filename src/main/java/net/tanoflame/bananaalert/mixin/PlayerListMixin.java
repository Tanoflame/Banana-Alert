package net.tanoflame.bananaalert.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.tanoflame.bananaalert.BananaAlert;
import net.tanoflame.bananaalert.PlayerEntry;
import net.tanoflame.bananaalert.PlayerList;
import net.tanoflame.bananaalert.PlayerListManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.UUID;

@Mixin(PlayerListHud.class)
public abstract class PlayerListMixin {
    @Shadow protected abstract Text applyGameModeFormatting(PlayerListEntry entry, MutableText name);

    @ModifyReturnValue(method = "getPlayerName", at = @At("RETURN"))
    public Text getPlayerName(Text original, @Local(argsOnly = true) PlayerListEntry entry) {
        UUID uuid = entry.getProfile().getId();

        PlayerEntry playerEntry = PlayerListManager.getPlayerEntry(uuid);
        if (playerEntry != null && playerEntry.getPlayerListId() != Util.NIL_UUID) {
            PlayerList list = PlayerListManager.getList(playerEntry.getPlayerListId());

            Text text = entry.getDisplayName();
            if (text != null) {
                text = this.applyGameModeFormatting(entry, entry.getDisplayName().copy().formatted(list.getColor()));
            } else {
                text = this.applyGameModeFormatting(entry, Text.literal(entry.getProfile().getName()).formatted(list.getColor()));
            }

            return text;
        }
        return original;
    }
}
