package de.nexusrealms.eraofrails.datagen;

import de.nexusrealms.eraofrails.block.RailwaysBlocks;
import de.nexusrealms.eraofrails.entity.RailwaysEntities;
import de.nexusrealms.eraofrails.item.RailwaysItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.world.item.Items;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;

import java.util.concurrent.CompletableFuture;

public class TagGen {

    public static class ItemGen extends FabricTagProvider.ItemTagProvider {

        public ItemGen(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider wrapperLookup) {
            tag(RailwaysItems.Tags.LINK_CARTS)
                    .add(RailwaysItems.CART_LINKING_TOOL, Items.CHAIN);
        }
    }

    public static class BlockGen extends FabricTagProvider.BlockTagProvider {
        public BlockGen(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider wrapperLookup) {
            tag(RailwaysBlocks.Tags.POWERED_HIGH_SPEED_RAIL)
                    .add(RailwaysBlocks.COPPER_RAIL);
            tag(RailwaysBlocks.Tags.HIGH_SPEED_RAIL)
                    .addTag(RailwaysBlocks.Tags.POWERED_HIGH_SPEED_RAIL);
            tag(RailwaysBlocks.Tags.INPUT_RAIL)
                    .add(RailwaysBlocks.INPUT_RAIL);
            tag(RailwaysBlocks.Tags.CROSS_RAIL)
                    .add(RailwaysBlocks.CROSS_RAIL);
            tag(BlockTags.RAILS)
                    .addTag(RailwaysBlocks.Tags.INPUT_RAIL)
                    .addTag(RailwaysBlocks.Tags.HIGH_SPEED_RAIL)
                    .addTag(RailwaysBlocks.Tags.CROSS_RAIL)
                    .add(RailwaysBlocks.SWITCH_RAIL, RailwaysBlocks.HALT_RAIL)
                    //.add(RailwaysBlocks.LOCKED_SWITCH_RAIL)
            ;
        }

    }

    public static class EntityGen extends FabricTagProvider.EntityTypeTagProvider {

        public EntityGen(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider wrapperLookup) {
            tag(RailwaysEntities.Tags.COPPER_MINECARTS)
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
