package net.tanoflame.bananaalert.event;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.tanoflame.bananaalert.PlayerEntry;
import net.tanoflame.bananaalert.PlayerList;
import net.tanoflame.bananaalert.PlayerListManager;
import org.jetbrains.annotations.Nullable;

public class PlayerAttackEvent implements AttackEntityCallback {
    private static final long DISPLAY_COOLDOWN = 10000;

    private long lastDisplayed;

    public PlayerAttackEvent() {
        this.lastDisplayed = System.currentTimeMillis();
    }

    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (System.currentTimeMillis() >= lastDisplayed + DISPLAY_COOLDOWN && entity.isPlayer()) {
            PlayerEntity targetPlayer = (PlayerEntity) entity;
            PlayerEntry entry = PlayerListManager.getPlayerEntry(targetPlayer.getUuid());

            if (entry != null) {
                PlayerList list = PlayerListManager.getList(entry.getPlayerListId());

                if (list.isWarningsEnabled()) {
                    MinecraftClient.getInstance().getToastManager().add(SystemToast.create(MinecraftClient.getInstance(),
                            SystemToast.Type.PERIODIC_NOTIFICATION,
                            Text.of("Test"),
                            Text.of("You hit a player")));
                    this.lastDisplayed = System.currentTimeMillis();
                }
            }

            // TODO: Add support to send in chat instead of toast
//            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Test"));
        }

        return ActionResult.PASS;
    }
}
