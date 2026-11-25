package smithed.unjank;

import net.fabricmc.api.ClientModInitializer;

import smithed.unjank.client.creativeTabs.ClientTabManager;

public class UnjankClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientTabManager.initialize();
	}
}