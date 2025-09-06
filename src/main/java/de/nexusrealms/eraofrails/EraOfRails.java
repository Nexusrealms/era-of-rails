package de.nexusrealms.eraofrails;

import de.nexusrealms.eraofrails.block.RailwaysBlocks;
import de.nexusrealms.eraofrails.entity.RailwaysEntities;
import de.nexusrealms.eraofrails.item.RailwaysItems;
import de.nexusrealms.eraofrails.network.RailwaysPackets;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.LazyEntityReference;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EraOfRails implements ModInitializer {
	public static final String MOD_ID = "era-of-rails";

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
		RailwaysPackets.init();
		RailwaysEntities.init();
		RailwaysItems.init();
		RailwaysBlocks.init();
		LOGGER.info("Hello Fabric world!");

		initCartLinkEvents();
	}
	public void initCartLinkEvents(){
		UseEntityCallback.EVENT.register((playerEntity, world, hand, entity, entityHitResult) -> {
			ItemStack stack = playerEntity.getStackInHand(hand);
			if(!playerEntity.getWorld().isClient() && stack.isIn(RailwaysItems.Tags.LINK_CARTS) && entity instanceof AbstractMinecartEntity interacted){
				if(!stack.contains(RailwaysItems.Components.LINKING_PARENT)){
					if(interacted.getLinkedChild().isEmpty() && playerEntity.isSneaking()){
						stack.set(RailwaysItems.Components.LINKING_PARENT, new LazyEntityReference<>(interacted));
						playerEntity.getItemCooldownManager().set(stack, 20);
						return ActionResult.SUCCESS;
					}
				} else if(interacted.getLinkedParent().isEmpty()){
					if(stack.get(RailwaysItems.Components.LINKING_PARENT).resolve(world, AbstractMinecartEntity.class) instanceof AbstractMinecartEntity parent && !parent.equals(interacted)){
						parent.setLinkedChild(interacted);
						interacted.setLinkedParent(parent);
						stack.remove(RailwaysItems.Components.LINKING_PARENT);
						stack.decrementUnlessCreative(1, playerEntity);
						playerEntity.getItemCooldownManager().set(stack, 20);
						return ActionResult.SUCCESS;
					}
					if(stack.get(RailwaysItems.Components.LINKING_PARENT).resolve(world, AbstractMinecartEntity.class) == null) stack.remove(RailwaysItems.Components.LINKING_PARENT);
				}
				return ActionResult.FAIL;
			}
            return ActionResult.PASS;
        });
	}

}