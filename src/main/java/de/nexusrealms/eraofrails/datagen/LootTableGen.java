package de.nexusrealms.eraofrails.datagen;

import de.nexusrealms.eraofrails.block.RailwaysBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

public abstract class LootTableGen extends SimpleFabricLootTableProvider {
    protected final HolderLookup.Provider registryLookup;

    protected LootTableGen(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup, LootContextParamSet contextType) {
        super(dataOutput, registryLookup, contextType);
        try {
            this.registryLookup = registryLookup.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Seriously mojang made a better datagen system than you");
            throw new RuntimeException(e);
        }
    }

    public static class Block extends FabricBlockLootTableProvider {
        protected Block(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(dataOutput, registryLookup);
        }

        @Override
        public void generate() {
            dropSelf(RailwaysBlocks.COPPER_RAIL);
            dropSelf(RailwaysBlocks.SWITCH_RAIL);
            //addDrop(RailwaysBlocks.LOCKED_SWITCH_RAIL);
            dropSelf(RailwaysBlocks.INPUT_RAIL);
            dropSelf(RailwaysBlocks.HALT_RAIL);
            dropSelf(RailwaysBlocks.CROSS_RAIL);

        }
    }

    public static class Entity extends LootTableGen {
        public Entity(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(output, registryLookup, LootContextParamSets.ENCHANTED_ENTITY);
        }

        @Override
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> lootTableBiConsumer) {

        }


    }
}
