package de.nexusrealms.newrailways;

import de.nexusrealms.newrailways.block.RailwaysBlocks;
import de.nexusrealms.newrailways.entity.RailwaysEntities;
import de.nexusrealms.newrailways.item.RailwaysItems;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewRailways implements ModInitializer {
	public static final String MOD_ID = "new-railways";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier id(String name){
		return Identifier.of(MOD_ID, name);
	}
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		RailwaysEntities.init();
		RailwaysItems.init();
		RailwaysBlocks.init();
		LOGGER.info("Hello Fabric world!");
	}
}