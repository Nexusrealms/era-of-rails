package de.nexusrealms.eraofrails.datagen;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import de.nexusrealms.eraofrails.block.RailwaysBlocks;
import de.nexusrealms.eraofrails.block.HaltRailBlock;
import de.nexusrealms.eraofrails.block.SwitchRailBlock;
import de.nexusrealms.eraofrails.item.RailwaysItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TexturedModel;
import net.minecraft.world.level.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import static net.minecraft.data.models.BlockModelGenerators.*;
import static net.minecraft.data.models.blockstates.VariantProperties.Rotation.*;

public class ModelGen extends FabricModelProvider {
    public ModelGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        blockStateModelGenerator.createActiveRail(RailwaysBlocks.COPPER_RAIL);
        registerSwitch(RailwaysBlocks.SWITCH_RAIL, blockStateModelGenerator);
        //registerSwitch(RailwaysBlocks.LOCKED_SWITCH_RAIL, blockStateModelGenerator);
        blockStateModelGenerator.createActiveRail(RailwaysBlocks.INPUT_RAIL);
        registerAxialRail(RailwaysBlocks.HALT_RAIL, blockStateModelGenerator);
        blockStateModelGenerator.createTrivialBlock(RailwaysBlocks.CROSS_RAIL, TexturedModel.createDefault(TextureMapping::rail, ModelTemplates.RAIL_FLAT));
        blockStateModelGenerator.createSimpleFlatItemModel(RailwaysBlocks.CROSS_RAIL);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(RailwaysItems.SPAWNER_MINECART, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(RailwaysItems.JUKEBOX_MINECART, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(RailwaysItems.REDSTONE_BLOCK_MINECART, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(RailwaysItems.INPUT_MINECART, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(RailwaysItems.COPPER_MINECART, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(RailwaysItems.CHEST_COPPER_MINECART, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(RailwaysItems.COMMAND_BLOCK_COPPER_MINECART, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(RailwaysItems.FURNACE_COPPER_MINECART, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(RailwaysItems.HOPPER_COPPER_MINECART, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(RailwaysItems.TNT_COPPER_MINECART, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(RailwaysItems.SPAWNER_COPPER_MINECART, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(RailwaysItems.JUKEBOX_COPPER_MINECART, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(RailwaysItems.REDSTONE_BLOCK_COPPER_MINECART, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(RailwaysItems.INPUT_COPPER_MINECART, ModelTemplates.FLAT_ITEM);

    }
    public final void registerSwitch(Block rail, BlockModelGenerators modelGenerator) {
        Variant leftUnswitched = Variant.variant().with(VariantProperties.MODEL, modelGenerator.createSuffixedVariant(rail, "", ModelTemplates.RAIL_FLAT, TextureMapping::rail));
        Variant leftSwitched = Variant.variant().with(VariantProperties.MODEL, modelGenerator.createSuffixedVariant(rail, "_switched", ModelTemplates.RAIL_FLAT, TextureMapping::rail));
        Variant rightUnswitched = Variant.variant().with(VariantProperties.MODEL, modelGenerator.createSuffixedVariant(rail, "_right", ModelTemplates.RAIL_FLAT, TextureMapping::rail));
        Variant rightSwitched = Variant.variant().with(VariantProperties.MODEL, modelGenerator.createSuffixedVariant(rail, "_right_switched", ModelTemplates.RAIL_FLAT, TextureMapping::rail));
        modelGenerator.createSimpleFlatItemModel(rail);
        modelGenerator.blockStateOutput
                .accept(
                        MultiVariantGenerator.multiVariant(rail)
                                .with(PropertyDispatch.properties(SwitchRailBlock.SWITCHED, SwitchRailBlock.RIGHT_HANDED, SwitchRailBlock.ORIENTATION).generate((switched, rightHanded, direction) -> {
                                    Variant variant = switched ? (rightHanded ? rightSwitched : leftSwitched) : (rightHanded ? rightUnswitched : leftUnswitched);
                                    return switch (direction) {
                                        case NORTH -> variant;
                                        case EAST -> variant.with(VariantProperties.Y_ROT, R90);
                                        case SOUTH ->  variant.with(VariantProperties.Y_ROT, R180);
                                        default ->  variant.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270);
                                    };
                                }))
                );
    }
    public final void registerAxialRail(Block rail, BlockModelGenerators modelGenerator) {
        Variant nsOff = Variant.variant().with(VariantProperties.MODEL, modelGenerator.createSuffixedVariant(rail, "", ModelTemplates.RAIL_FLAT, TextureMapping::rail));
        Variant raisedNEOff = Variant.variant().with(VariantProperties.MODEL, modelGenerator.createSuffixedVariant(rail, "", ModelTemplates.RAIL_RAISED_NE, TextureMapping::rail));
        Variant raisedSWOff = Variant.variant().with(VariantProperties.MODEL, modelGenerator.createSuffixedVariant(rail, "", ModelTemplates.RAIL_RAISED_SW, TextureMapping::rail));
        Variant nsOn = Variant.variant().with(VariantProperties.MODEL, modelGenerator.createSuffixedVariant(rail, "_on", ModelTemplates.RAIL_FLAT, TextureMapping::rail));
        Variant raisedNEOn = Variant.variant().with(VariantProperties.MODEL, modelGenerator.createSuffixedVariant(rail, "_on", ModelTemplates.RAIL_RAISED_NE, TextureMapping::rail));
        Variant raisedSWOn = Variant.variant().with(VariantProperties.MODEL, modelGenerator.createSuffixedVariant(rail, "_on", ModelTemplates.RAIL_RAISED_SW, TextureMapping::rail));

        modelGenerator.createSimpleFlatItemModel(rail);
        modelGenerator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(rail).with(PropertyDispatch.properties(BlockStateProperties.POWERED, BlockStateProperties.RAIL_SHAPE_STRAIGHT, HaltRailBlock.AXIAL).generate((powered, shape, axial) -> {
            Variant var10000;
            switch (shape) {
                case NORTH_SOUTH -> var10000 = (powered ? nsOn : nsOff).with(VariantProperties.Y_ROT, axial ? R180 : R0);
                case EAST_WEST -> var10000 = (powered ? nsOn : nsOff).with(VariantProperties.Y_ROT, axial ? R90 : R270);
                case ASCENDING_EAST -> var10000 = fourState(axial, powered, raisedSWOff.with(VariantProperties.Y_ROT, R270), raisedNEOff.with(VariantProperties.Y_ROT, R90), raisedSWOn.with(VariantProperties.Y_ROT, R270),  raisedNEOn.with(VariantProperties.Y_ROT, R90));
                case ASCENDING_WEST -> var10000 = fourState(axial, powered, raisedNEOff.with(VariantProperties.Y_ROT, R270), raisedSWOff.with(VariantProperties.Y_ROT, R90), raisedNEOn.with(VariantProperties.Y_ROT, R270),  raisedSWOn.with(VariantProperties.Y_ROT, R90));
                case ASCENDING_NORTH -> var10000 = fourState(axial, powered, raisedNEOff, raisedSWOff.with(VariantProperties.Y_ROT, R180), raisedNEOn,  raisedSWOn.with(VariantProperties.Y_ROT, R180));
                case ASCENDING_SOUTH -> var10000 = fourState(axial, powered, raisedSWOff, raisedNEOff.with(VariantProperties.Y_ROT, R180), raisedSWOn,  raisedNEOn.with(VariantProperties.Y_ROT, R180));
                default -> throw new UnsupportedOperationException("Fix you generator!");
            }

            return var10000;
        })));
    }
    private static <T> T fourState(boolean bl1, boolean bl2, T ff, T tf, T ft, T tt){
        return bl1 ? (bl2 ? tt : tf) : (bl2 ? ft : ff);
    }

}
