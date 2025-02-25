package net.tanoflame.bananaalert;

import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.tanoflame.bananaalert.event.KeyInputHandler;
import net.tanoflame.bananaalert.storage.PlayerListStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BananaAlert implements ClientModInitializer {
	public static final String MOD_ID = "banana-alert";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		PlayerListStorage.loadDataFile();

		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
			PlayerListStorage.saveDataFile();
		});

		KeyInputHandler.register();
	}

	public static boolean isDevEnvironment() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}
}

// TODO: Look into mojang api for uuid and name tag
// https://minecraft.wiki/w/Mojang_API#Query_player's_UUID