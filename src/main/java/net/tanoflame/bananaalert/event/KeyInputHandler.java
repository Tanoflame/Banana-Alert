package net.tanoflame.bananaalert.event;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.tanoflame.bananaalert.PlayerEntry;
import net.tanoflame.bananaalert.PlayerList;
import net.tanoflame.bananaalert.PlayerListManager;
import net.tanoflame.bananaalert.gui.ClientScreen;
import net.tanoflame.bananaalert.gui.description.PlayerListOverviewScreen;
import net.tanoflame.bananaalert.util.Util;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String KEY_CATEGORY_ALERT = "key.category.banana-alert";
    public static final String KEY_OPEN_LISTS_VIEW = "key.banana-alert.open_lists_view";
    public static final String KEY_TOGGLE_LIST1 = "key.banana-alert.toggle_list_1";
    public static final String KEY_TOGGLE_LIST2 = "key.banana-alert.toggle_list_2";

    public static KeyBinding openListsViewKey;
    public static KeyBinding toggleList1Key;
    public static KeyBinding toggleList2Key;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (openListsViewKey.wasPressed()) {
                ClientScreen.openScreen(new PlayerListOverviewScreen());
            }
//            else if (!PlayerListManager.getLists().isEmpty()) {
//                if (toggleList1Key.wasPressed()) {
//                    toggleRaycast((PlayerList) PlayerListManager.getLists().values().toArray()[0]);
//                } else if (toggleList2Key.wasPressed()) {
//                    toggleRaycast((PlayerList) PlayerListManager.getLists().values().toArray()[0]);
//                }
//            }
        });
    }

    public static void register() {
        openListsViewKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_OPEN_LISTS_VIEW,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_KP_ENTER,
                KEY_CATEGORY_ALERT
        ));

//        toggleList1Key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
//                KEY_TOGGLE_LIST1,
//                InputUtil.Type.KEYSYM,
//                GLFW.GLFW_KEY_KP_1,
//                KEY_CATEGORY_ALERT
//        ));
//
//        toggleList2Key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
//                KEY_TOGGLE_LIST2,
//                InputUtil.Type.KEYSYM,
//                GLFW.GLFW_KEY_KP_2,
//                KEY_CATEGORY_ALERT
//        ));

        registerKeyInputs();
    }

    private static void toggleRaycast(PlayerList list) {
        EntityHitResult result = Util.raycastEntities(MinecraftClient.getInstance().getCameraEntity(), 10d, 1.0f);
        if (result != null && result.getType() != HitResult.Type.MISS && result.getEntity().isPlayer()) {
            GameProfile gameProfile = ((PlayerEntity) result.getEntity()).getGameProfile();

            PlayerEntry entry = PlayerListManager.getPlayerEntry(gameProfile.getId());

            if (entry != null) {
                PlayerListManager.removePlayerEntry(entry);
            } else {
                PlayerListManager.addPlayerEntry(list.getId(), gameProfile.getId());
            }
        }
    }
}
