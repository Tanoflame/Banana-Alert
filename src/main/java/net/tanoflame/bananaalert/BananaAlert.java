package net.tanoflame.bananaalert;

import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.tanoflame.bananaalert.storage.PlayerListStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BananaAlert implements ClientModInitializer {
	public static final String MOD_ID = "banana-alert";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		PlayerListStorage.loadAllDataFiles();

		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
			PlayerListStorage.saveAllDataFiles();
		});
	}
}