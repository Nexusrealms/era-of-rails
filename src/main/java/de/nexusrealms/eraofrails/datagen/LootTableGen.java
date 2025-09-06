package de.nexusrealms.eraofrails.datagen;

import de.nexusrealms.eraofrails.block.RailwaysBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.context.ContextType;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

public abstract class LootTableGen extends SimpleFabricLootTableProvider {
    protected final RegistryWrapper.WrapperLookup registryLookup;

    protected LootTableGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup, ContextType contextType) {
        super(dataOutput, registryLookup, contextType);
        try {
            this.registryLookup = registryLookup.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Seriously mojang made a better datagen system than you");
            throw new RuntimeException(e);
        }
    }

    public static class Block extends FabricBlockLootTableProvider {
        protected Block(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(dataOutput, registryLookup);
        }

        @Override
        public void generate() {
            addDrop(RailwaysBlocks.COPPER_RAIL);
            addDrop(RailwaysBlocks.SWITCH_RAIL);
            //addDrop(RailwaysBlocks.LOCKED_SWITCH_RAIL);
            addDrop(RailwaysBlocks.INPUT_RAIL);
            addDrop(RailwaysBlocks.HALT_RAIL);

        }
    }

    public static class Entity extends LootTableGen {
        public Entity(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(output, registryLookup, LootContextTypes.ENCHANTED_ENTITY);
        }

        @Override
        public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> lootTableBiConsumer) {

        }


    }
}
