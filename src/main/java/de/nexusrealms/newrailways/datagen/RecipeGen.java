package de.nexusrealms.newrailways.datagen;


import de.nexusrealms.newrailways.NewRailways;
import de.nexusrealms.newrailways.block.RailwaysBlocks;
import de.nexusrealms.newrailways.item.RailwaysItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class RecipeGen extends FabricRecipeProvider {
    public RecipeGen(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup wrapperLookup, RecipeExporter recipeExporter) {
        return new Generator(wrapperLookup, recipeExporter);
    }
    @Override
    public String getName() {
        return NewRailways.MOD_ID;
    }

    private static class Generator extends RecipeGenerator {

        protected Generator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
            super(registries, exporter);
        }
        //TODO add rail and minecart recipes
        @Override
        public void generate() {
            createShapeless(RecipeCategory.TRANSPORTATION, RailwaysItems.REDSTONE_BLOCK_MINECART)
                    .input(Blocks.REDSTONE_BLOCK)
                    .input(Items.MINECART)
                    .criterion("has_minecart", conditionsFromItem(Items.MINECART))
                    .offerTo(exporter);
            createShapeless(RecipeCategory.TRANSPORTATION, RailwaysItems.JUKEBOX_MINECART)
                    .input(Blocks.JUKEBOX)
                    .input(Items.MINECART)
                    .criterion("has_minecart", conditionsFromItem(Items.MINECART))
                    .offerTo(exporter);
            createShapeless(RecipeCategory.TRANSPORTATION, RailwaysItems.INPUT_MINECART)
                    .input(Blocks.OBSERVER)
                    .input(Items.MINECART)
                    .criterion("has_minecart", conditionsFromItem(Items.MINECART))
                    .offerTo(exporter);
            createShaped(RecipeCategory.TRANSPORTATION, RailwaysItems.COPPER_MINECART)
                    .pattern("# #")
                    .pattern("###")
                    .input('#', Items.COPPER_INGOT)
                    .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                    .offerTo(exporter);

            createShaped(RecipeCategory.TRANSPORTATION, RailwaysBlocks.COPPER_RAIL)
                    .pattern("C C")
                            .pattern("CRC")
                                    .pattern("CSC")
                                            .input('C', Items.COPPER_INGOT)
                                                    .input('R', Items.REDSTONE)
                                                            .input('S', Items.STICK)
                                                                    .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                                                                            .offerTo(exporter);

            createShapeless(RecipeCategory.TRANSPORTATION, RailwaysItems.CHEST_COPPER_MINECART)
                    .input(Blocks.CHEST)
                    .input(RailwaysItems.COPPER_MINECART)
                    .criterion(hasItem(RailwaysItems.COPPER_MINECART), conditionsFromItem(RailwaysItems.COPPER_MINECART))
                    .offerTo(exporter);
            createShapeless(RecipeCategory.TRANSPORTATION, RailwaysItems.FURNACE_COPPER_MINECART)
                    .input(Blocks.FURNACE)
                    .input(RailwaysItems.COPPER_MINECART)
                    .criterion(hasItem(RailwaysItems.COPPER_MINECART), conditionsFromItem(RailwaysItems.COPPER_MINECART))
                    .offerTo(exporter);
            createShapeless(RecipeCategory.TRANSPORTATION, RailwaysItems.HOPPER_COPPER_MINECART)
                    .input(Blocks.HOPPER)
                    .input(RailwaysItems.COPPER_MINECART)
                    .criterion(hasItem(RailwaysItems.COPPER_MINECART), conditionsFromItem(RailwaysItems.COPPER_MINECART))
                    .offerTo(exporter);
            createShapeless(RecipeCategory.TRANSPORTATION, RailwaysItems.TNT_COPPER_MINECART)
                    .input(Blocks.TNT)
                    .input(RailwaysItems.COPPER_MINECART)
                    .criterion(hasItem(RailwaysItems.COPPER_MINECART), conditionsFromItem(RailwaysItems.COPPER_MINECART))
                    .offerTo(exporter);
            createShapeless(RecipeCategory.TRANSPORTATION, RailwaysItems.REDSTONE_BLOCK_COPPER_MINECART)
                    .input(Blocks.REDSTONE_BLOCK)
                    .input(RailwaysItems.COPPER_MINECART)
                    .criterion(hasItem(RailwaysItems.COPPER_MINECART), conditionsFromItem(RailwaysItems.COPPER_MINECART))
                    .offerTo(exporter);
            createShapeless(RecipeCategory.TRANSPORTATION, RailwaysItems.JUKEBOX_COPPER_MINECART)
                    .input(Blocks.JUKEBOX)
                    .input(RailwaysItems.COPPER_MINECART)
                    .criterion(hasItem(RailwaysItems.COPPER_MINECART), conditionsFromItem(RailwaysItems.COPPER_MINECART))
                    .offerTo(exporter);
            createShapeless(RecipeCategory.TRANSPORTATION, RailwaysItems.INPUT_COPPER_MINECART)
                    .input(Blocks.OBSERVER)
                    .input(RailwaysItems.COPPER_MINECART)
                    .criterion(hasItem(RailwaysItems.COPPER_MINECART), conditionsFromItem(RailwaysItems.COPPER_MINECART))
                    .offerTo(exporter);
        }
    }
}