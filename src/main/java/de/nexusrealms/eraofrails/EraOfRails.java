package de.nexusrealms.eraofrails;

import de.nexusrealms.eraofrails.block.RailwaysBlocks;
import de.nexusrealms.eraofrails.entity.RailwaysEntities;
import de.nexusrealms.eraofrails.item.RailwaysItems;
import de.nexusrealms.eraofrails.network.RailwaysPackets;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EraOfRails implements ModInitializer {
	public static final String MOD_ID = "era-of-rails";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static ResourceLocation id(String name){
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
	}
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		RailwaysPackets.init();
		RailwaysEntities.init();
		RailwaysItems.init();
		RailwaysBlocks.init();
		LOGGER.info("Hello Fabric world!");

	}


}