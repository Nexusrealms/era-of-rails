package de.nexusrealms.newrailways.datagen;

import de.nexusrealms.newrailways.block.RailwaysBlocks;
import de.nexusrealms.newrailways.entity.RailwaysEntities;
import de.nexusrealms.newrailways.item.RailwaysItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class LangGen extends FabricLanguageProvider {
    protected LangGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    private void generateBlockTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder){
        translationBuilder.add(RailwaysBlocks.COPPER_RAIL, "Copper Powered Rail");
    }
    private void generateItemTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder){
        translationBuilder.add(RailwaysItems.SPAWNER_MINECART, "Minecart with Spawner");
        translationBuilder.add(RailwaysItems.JUKEBOX_MINECART, "Minecart with Jukebox");
        translationBuilder.add(RailwaysItems.REDSTONE_BLOCK_MINECART, "Minecart with Redstone Block");
        translationBuilder.add(RailwaysItems.COPPER_MINECART, "Copper Minecart");
        translationBuilder.add(RailwaysItems.CHEST_COPPER_MINECART, "Copper Minecart with Chest");
        translationBuilder.add(RailwaysItems.COMMAND_BLOCK_COPPER_MINECART, "Copper Minecart with Command Block");
        translationBuilder.add(RailwaysItems.FURNACE_COPPER_MINECART, "Copper Minecart with Furnace");
        translationBuilder.add(RailwaysItems.HOPPER_COPPER_MINECART, "Copper Minecart with Hopper");
        translationBuilder.add(RailwaysItems.TNT_COPPER_MINECART, "Copper Minecart with TNT");
        translationBuilder.add(RailwaysItems.SPAWNER_COPPER_MINECART, "Copper Minecart with Spawner");
        translationBuilder.add(RailwaysItems.JUKEBOX_COPPER_MINECART, "Copper Minecart with Jukebox");
        translationBuilder.add(RailwaysItems.REDSTONE_BLOCK_COPPER_MINECART, "Copper Minecart with Redstone Block");

    }
    private void generateEntityTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder){
        translationBuilder.add(RailwaysEntities.COPPER_MINECART, "Copper Minecart");
        translationBuilder.add(RailwaysEntities.JUKEBOX_MINECART, "Minecart with Jukebox");
        translationBuilder.add(RailwaysEntities.REDSTONE_BLOCK_MINECART, "Minecart with Redstone Block");
        translationBuilder.add(RailwaysEntities.CHEST_COPPER_MINECART, "Copper Minecart with Chest");
        translationBuilder.add(RailwaysEntities.COMMAND_BLOCK_COPPER_MINECART, "Copper Minecart with Command Block");
        translationBuilder.add(RailwaysEntities.FURNACE_COPPER_MINECART, "Copper Minecart with Furnace");
        translationBuilder.add(RailwaysEntities.HOPPER_COPPER_MINECART, "Copper Minecart with Hopper");
        translationBuilder.add(RailwaysEntities.TNT_COPPER_MINECART, "Copper Minecart with TNT");
        translationBuilder.add(RailwaysEntities.SPAWNER_COPPER_MINECART, "Copper Minecart with Spawner");
        translationBuilder.add(RailwaysEntities.JUKEBOX_COPPER_MINECART, "Copper Minecart with Jukebox");
        translationBuilder.add(RailwaysEntities.REDSTONE_BLOCK_COPPER_MINECART, "Copper Minecart with Redstone Block");

    }


    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        generateBlockTranslations(wrapperLookup, translationBuilder);
        generateItemTranslations(wrapperLookup, translationBuilder);
        generateEntityTranslations(wrapperLookup, translationBuilder);
    }
    private void generateEntityTranslationWithSpawnEgg(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder, EntityType<?> entityType, String name){
        translationBuilder.add(entityType, name);
        if(SpawnEggItem.forEntity(entityType) instanceof SpawnEggItem item){
            translationBuilder.add(item, name + " Spawn Egg");
        }
    }
}
