package de.nexusrealms.eraofrails.datagen;


import de.nexusrealms.eraofrails.EraOfRails;
import de.nexusrealms.eraofrails.block.RailwaysBlocks;
import de.nexusrealms.eraofrails.item.RailwaysItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class RecipeGen extends FabricRecipeProvider {
    public RecipeGen(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput exporter) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, RailwaysItems.REDSTONE_BLOCK_MINECART)
                .requires(Blocks.REDSTONE_BLOCK)
                .requires(Items.MINECART)
                .unlockedBy("has_minecart", has(Items.MINECART))
                .save(exporter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, RailwaysItems.JUKEBOX_MINECART)
                .requires(Blocks.JUKEBOX)
                .requires(Items.MINECART)
                .unlockedBy("has_minecart", has(Items.MINECART))
                .save(exporter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, RailwaysItems.INPUT_MINECART)
                .requires(Blocks.OBSERVER)
                .requires(Items.MINECART)
                .unlockedBy("has_minecart", has(Items.MINECART))
                .save(exporter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, RailwaysItems.COPPER_MINECART)
                .pattern("# #")
                .pattern("###")
                .define('#', Items.COPPER_INGOT)
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, RailwaysBlocks.COPPER_RAIL, 6)
                .pattern("C C")
                .pattern("CRC")
                .pattern("CSC")
                .define('C', Items.COPPER_INGOT)
                .define('R', Items.REDSTONE)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                .save(exporter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, RailwaysBlocks.INPUT_RAIL, 6)
                .pattern("CRC")
                .pattern("COC")
                .pattern("CSC")
                .define('C', Items.IRON_INGOT)
                .define('R', Items.REDSTONE)
                .define('S', Items.STICK)
                .define('O', Blocks.OBSERVER)
                .unlockedBy(getHasName(Items.OBSERVER), has(Items.OBSERVER))
                .save(exporter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, RailwaysBlocks.SWITCH_RAIL, 6)
                .pattern("CSC")
                .pattern("SRC")
                .pattern("CSC")
                .define('C', Items.IRON_INGOT)
                .define('R', Items.REDSTONE)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(exporter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, RailwaysBlocks.HALT_RAIL, 6)
                .pattern("ISI")
                .pattern("IPI")
                .pattern("ISI")
                .define('I', Items.IRON_INGOT)
                .define('P', Items.REPEATER)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.REPEATER), has(Items.REPEATER))
                .save(exporter);
            /*ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, RailwaysBlocks.LOCKED_SWITCH_RAIL)
                    .pattern("CSC")
                    .pattern("SRC")
                    .pattern("COC")
                    .input('C', Items.IRON_INGOT)
                    .input('R', Items.REDSTONE)
                    .input('S', Items.STICK)
                    .input('O', Items.COMPARATOR)
                    .criterion(hasItem(Items.COMPARATOR), conditionsFromItem(Items.COMPARATOR))
                    .offerTo(exporter);*/

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, RailwaysItems.CHEST_COPPER_MINECART)
                .requires(Blocks.CHEST)
                .requires(RailwaysItems.COPPER_MINECART)
                .unlockedBy(getHasName(RailwaysItems.COPPER_MINECART), has(RailwaysItems.COPPER_MINECART))
                .save(exporter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, RailwaysItems.FURNACE_COPPER_MINECART)
                .requires(Blocks.FURNACE)
                .requires(RailwaysItems.COPPER_MINECART)
                .unlockedBy(getHasName(RailwaysItems.COPPER_MINECART), has(RailwaysItems.COPPER_MINECART))
                .save(exporter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, RailwaysItems.HOPPER_COPPER_MINECART)
                .requires(Blocks.HOPPER)
                .requires(RailwaysItems.COPPER_MINECART)
                .unlockedBy(getHasName(RailwaysItems.COPPER_MINECART), has(RailwaysItems.COPPER_MINECART))
                .save(exporter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, RailwaysItems.TNT_COPPER_MINECART)
                .requires(Blocks.TNT)
                .requires(RailwaysItems.COPPER_MINECART)
                .unlockedBy(getHasName(RailwaysItems.COPPER_MINECART), has(RailwaysItems.COPPER_MINECART))
                .save(exporter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, RailwaysItems.REDSTONE_BLOCK_COPPER_MINECART)
                .requires(Blocks.REDSTONE_BLOCK)
                .requires(RailwaysItems.COPPER_MINECART)
                .unlockedBy(getHasName(RailwaysItems.COPPER_MINECART), has(RailwaysItems.COPPER_MINECART))
                .save(exporter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, RailwaysItems.JUKEBOX_COPPER_MINECART)
                .requires(Blocks.JUKEBOX)
                .requires(RailwaysItems.COPPER_MINECART)
                .unlockedBy(getHasName(RailwaysItems.COPPER_MINECART), has(RailwaysItems.COPPER_MINECART))
                .save(exporter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, RailwaysItems.INPUT_COPPER_MINECART)
                .requires(Blocks.OBSERVER)
                .requires(RailwaysItems.COPPER_MINECART)
                .unlockedBy(getHasName(RailwaysItems.COPPER_MINECART), has(RailwaysItems.COPPER_MINECART))
                .save(exporter);
    }
    

    @Override
    public String getName() {
        return EraOfRails.MOD_ID + "/recipes";
    }


}