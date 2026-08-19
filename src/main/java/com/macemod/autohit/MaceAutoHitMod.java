package com.macemod.autohit;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MaceAutoHitMod implements ModInitializer {
	public static final String MOD_ID = "maceautohit";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Mace Auto Hit loaded (client-only mod, singleplayer testing use).");
	}
}
