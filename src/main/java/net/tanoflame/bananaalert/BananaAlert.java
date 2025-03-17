package net.tanoflame.bananaalert;

import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.tanoflame.bananaalert.event.KeyInputHandler;
import net.tanoflame.bananaalert.event.PlayerAttackEvent;
import net.tanoflame.bananaalert.storage.PlayerListStorage;
import net.tanoflame.bananaalert.util.ProfileLookupClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BananaAlert implements ClientModInitializer {
	public static final String MOD_ID = "banana-alert";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final ProfileLookupClient LOOKUP_CLIENT = new ProfileLookupClient();

	@Override
	public void onInitializeClient() {
		PlayerListStorage.loadDataFile();

		ClientLifecycleEvents.CLIENT_STOPPING.register(this::onStopping);

		AttackEntityCallback.EVENT.register(new PlayerAttackEvent());

		KeyInputHandler.register();
    }

	public void onStopping(MinecraftClient client) {
		PlayerListStorage.saveDataFile();
	}

	public static boolean isDevEnvironment() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}

	public static void showError(String message) {
		System.out.println(message); // For now, just print the error to the console
	}
}

// TODO: Look into mojang api for uuid and name tag
// https://minecraft.wiki/w/Mojang_API#Query_player's_UUID