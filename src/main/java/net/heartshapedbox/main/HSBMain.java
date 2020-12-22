package net.heartshapedbox.main;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.heartshapedbox.body.impl.LegsBodyPart;

public class HSBMain implements ModInitializer {
	@Override
	public void onInitialize() {
		ServerTickEvents.END_SERVER_TICK.register( minecraftServer ->
			PlayerStream.all(minecraftServer).forEach(player -> {
				// Tick each players body parts (for things like broken legs, instant death from headshots, ect.)
				LegsBodyPart.tickPlayer(player);
			})
		);
	}
}
