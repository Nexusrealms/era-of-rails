package de.nexusrealms.eraofrails.datagen;

import de.nexusrealms.eraofrails.block.RailwaysBlocks;
import de.nexusrealms.eraofrails.entity.RailwaysEntities;
import de.nexusrealms.eraofrails.item.RailwaysItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class LangGen extends FabricLanguageProvider {
    protected LangGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    private void generateBlockTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder tb){
        generateBlockAndItem(tb, RailwaysBlocks.COPPER_RAIL, "Copper Powered Rail");
        generateBlockAndItem(tb, RailwaysBlocks.SWITCH_RAIL, "Rail Switch");
        //generateBlockAndItem(tb, RailwaysBlocks.LOCKED_SWITCH_RAIL, "Locked Rail Switch");
        generateBlockAndItem(tb, RailwaysBlocks.INPUT_RAIL, "Input Rail");
        generateBlockAndItem(tb, RailwaysBlocks.HALT_RAIL, "Halt Rail");

    }
    private void generateBlockAndItem(TranslationBuilder translationBuilder, Block block, String translation){
        translationBuilder.add(block, translation);
        translationBuilder.add(block.getTranslationKey().replace("block.", "item."), translation);
    }
    private void generateItemTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder){
        translationBuilder.add(RailwaysItems.SPAWNER_MINECART, "Minecart with Spawner");
        translationBuilder.add(RailwaysItems.JUKEBOX_MINECART, "Minecart with Jukebox");
        translationBuilder.add(RailwaysItems.REDSTONE_BLOCK_MINECART, "Minecart with Redstone Block");
        translationBuilder.add(RailwaysItems.INPUT_MINECART, "Input Minecart");

        translationBuilder.add(RailwaysItems.COPPER_MINECART, "Copper Minecart");
        translationBuilder.add(RailwaysItems.CHEST_COPPER_MINECART, "Copper Minecart with Chest");
        translationBuilder.add(RailwaysItems.COMMAND_BLOCK_COPPER_MINECART, "Copper Minecart with Command Block");
        translationBuilder.add(RailwaysItems.FURNACE_COPPER_MINECART, "Copper Minecart with Furnace");
        translationBuilder.add(RailwaysItems.HOPPER_COPPER_MINECART, "Copper Minecart with Hopper");
        translationBuilder.add(RailwaysItems.TNT_COPPER_MINECART, "Copper Minecart with TNT");
        translationBuilder.add(RailwaysItems.SPAWNER_COPPER_MINECART, "Copper Minecart with Spawner");
        translationBuilder.add(RailwaysItems.JUKEBOX_COPPER_MINECART, "Copper Minecart with Jukebox");
        translationBuilder.add(RailwaysItems.REDSTONE_BLOCK_COPPER_MINECART, "Copper Minecart with Redstone Block");
        translationBuilder.add(RailwaysItems.INPUT_COPPER_MINECART, "Input Copper Minecart");


    }
    private void generateEntityTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder){
        translationBuilder.add(RailwaysEntities.COPPER_MINECART, "Copper Minecart");
        translationBuilder.add(RailwaysEntities.JUKEBOX_MINECART, "Minecart with Jukebox");
        translationBuilder.add(RailwaysEntities.REDSTONE_BLOCK_MINECART, "Minecart with Redstone Block");
        translationBuilder.add(RailwaysEntities.INPUT_MINECART, "Input Minecart");
        translationBuilder.add(RailwaysEntities.CHEST_COPPER_MINECART, "Copper Minecart with Chest");
        translationBuilder.add(RailwaysEntities.COMMAND_BLOCK_COPPER_MINECART, "Copper Minecart with Command Block");
        translationBuilder.add(RailwaysEntities.FURNACE_COPPER_MINECART, "Copper Minecart with Furnace");
        translationBuilder.add(RailwaysEntities.HOPPER_COPPER_MINECART, "Copper Minecart with Hopper");
        translationBuilder.add(RailwaysEntities.TNT_COPPER_MINECART, "Copper Minecart with TNT");
        translationBuilder.add(RailwaysEntities.SPAWNER_COPPER_MINECART, "Copper Minecart with Spawner");
        translationBuilder.add(RailwaysEntities.JUKEBOX_COPPER_MINECART, "Copper Minecart with Jukebox");
        translationBuilder.add(RailwaysEntities.REDSTONE_BLOCK_COPPER_MINECART, "Copper Minecart with Redstone Block");
        translationBuilder.add(RailwaysEntities.INPUT_COPPER_MINECART, "Input Copper Minecart");

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
