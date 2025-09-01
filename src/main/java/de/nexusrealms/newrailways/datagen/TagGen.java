package de.nexusrealms.newrailways.datagen;

import de.nexusrealms.newrailways.NewRailways;
import de.nexusrealms.newrailways.block.RailwaysBlocks;
import de.nexusrealms.newrailways.entity.RailwaysEntities;
import de.nexusrealms.newrailways.item.RailwaysItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class TagGen {

    public static class ItemGen extends FabricTagProvider.ItemTagProvider {

        public ItemGen(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            valueLookupBuilder(RailwaysItems.Tags.LINK_CARTS)
                    .add(RailwaysItems.CART_LINKING_TOOL, Items.CHAIN);
        }
    }

    public static class BlockGen extends FabricTagProvider.BlockTagProvider {
        public BlockGen(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            valueLookupBuilder(RailwaysBlocks.Tags.POWERED_HIGH_SPEED_RAIL)
                    .add(RailwaysBlocks.COPPER_RAIL);
            valueLookupBuilder(RailwaysBlocks.Tags.HIGH_SPEED_RAIL)
                    .addTag(RailwaysBlocks.Tags.POWERED_HIGH_SPEED_RAIL);
            valueLookupBuilder(RailwaysBlocks.Tags.INPUT_RAIL)
                    .add(RailwaysBlocks.INPUT_RAIL);
            valueLookupBuilder(BlockTags.RAILS)
                    .addTag(RailwaysBlocks.Tags.INPUT_RAIL)
                    .addTag(RailwaysBlocks.Tags.HIGH_SPEED_RAIL)
                    .add(RailwaysBlocks.SWITCH_RAIL)
                    .add(RailwaysBlocks.LOCKED_SWITCH_RAIL);
        }

    }

    public static class EntityGen extends FabricTagProvider.EntityTypeTagProvider {

        public EntityGen(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            valueLookupBuilder(RailwaysEntities.Tags.COPPER_MINECARTS)
                    .add(
                            RailwaysEntities.COPPER_MINECART,
                            RailwaysEntities.CHEST_COPPER_MINECART,
                            RailwaysEntities.COMMAND_BLOCK_COPPER_MINECART,
                            RailwaysEntities.FURNACE_COPPER_MINECART,
                            RailwaysEntities.HOPPER_COPPER_MINECART,
                            RailwaysEntities.TNT_COPPER_MINECART,
                            RailwaysEntities.SPAWNER_COPPER_MINECART,
                            RailwaysEntities.JUKEBOX_COPPER_MINECART,
                            RailwaysEntities.REDSTONE_BLOCK_COPPER_MINECART,
                            RailwaysEntities.INPUT_COPPER_MINECART);
        }
    }
}
