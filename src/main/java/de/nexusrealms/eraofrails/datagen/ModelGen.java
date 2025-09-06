package de.nexusrealms.eraofrails.datagen;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import de.nexusrealms.eraofrails.block.RailwaysBlocks;
import de.nexusrealms.eraofrails.block.HaltRailBlock;
import de.nexusrealms.eraofrails.block.SwitchRailBlock;
import de.nexusrealms.eraofrails.item.RailwaysItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.client.data.*;
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import static net.minecraft.client.data.BlockStateModelGenerator.*;

public class ModelGen extends FabricModelProvider {
    public ModelGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerStraightRail(RailwaysBlocks.COPPER_RAIL);
        registerSwitch(RailwaysBlocks.SWITCH_RAIL, blockStateModelGenerator);
        //registerSwitch(RailwaysBlocks.LOCKED_SWITCH_RAIL, blockStateModelGenerator);
        blockStateModelGenerator.registerStraightRail(RailwaysBlocks.INPUT_RAIL);
        registerAxialRail(RailwaysBlocks.HALT_RAIL, blockStateModelGenerator);

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(RailwaysItems.SPAWNER_MINECART, Models.GENERATED);
        itemModelGenerator.register(RailwaysItems.JUKEBOX_MINECART, Models.GENERATED);
        itemModelGenerator.register(RailwaysItems.REDSTONE_BLOCK_MINECART, Models.GENERATED);
        itemModelGenerator.register(RailwaysItems.INPUT_MINECART, Models.GENERATED);
        itemModelGenerator.register(RailwaysItems.COPPER_MINECART, Models.GENERATED);
        itemModelGenerator.register(RailwaysItems.CHEST_COPPER_MINECART, Models.GENERATED);
        itemModelGenerator.register(RailwaysItems.COMMAND_BLOCK_COPPER_MINECART, Models.GENERATED);
        itemModelGenerator.register(RailwaysItems.FURNACE_COPPER_MINECART, Models.GENERATED);
        itemModelGenerator.register(RailwaysItems.HOPPER_COPPER_MINECART, Models.GENERATED);
        itemModelGenerator.register(RailwaysItems.TNT_COPPER_MINECART, Models.GENERATED);
        itemModelGenerator.register(RailwaysItems.SPAWNER_COPPER_MINECART, Models.GENERATED);
        itemModelGenerator.register(RailwaysItems.JUKEBOX_COPPER_MINECART, Models.GENERATED);
        itemModelGenerator.register(RailwaysItems.REDSTONE_BLOCK_COPPER_MINECART, Models.GENERATED);
        itemModelGenerator.register(RailwaysItems.INPUT_COPPER_MINECART, Models.GENERATED);

    }
    public final void registerSwitch(Block rail, BlockStateModelGenerator modelGenerator) {
        WeightedVariant leftUnswitched = createWeightedVariant(modelGenerator.createSubModel(rail, "", Models.RAIL_FLAT, TextureMap::rail));
        WeightedVariant leftSwitched = createWeightedVariant(modelGenerator.createSubModel(rail, "_switched", Models.RAIL_FLAT, TextureMap::rail));
        WeightedVariant rightUnswitched = createWeightedVariant(modelGenerator.createSubModel(rail, "_right", Models.RAIL_FLAT, TextureMap::rail));
        WeightedVariant rightSwitched = createWeightedVariant(modelGenerator.createSubModel(rail, "_right_switched", Models.RAIL_FLAT, TextureMap::rail));
        modelGenerator.registerItemModel(rail);
        modelGenerator.blockStateCollector
                .accept(
                        VariantsBlockModelDefinitionCreator.of(rail)
                                .with(BlockStateVariantMap.models(SwitchRailBlock.SWITCHED, SwitchRailBlock.RIGHT_HANDED, SwitchRailBlock.ORIENTATION).generate((switched, rightHanded, direction) -> {
                                    WeightedVariant variant = switched ? (rightHanded ? rightSwitched : leftSwitched) : (rightHanded ? rightUnswitched : leftUnswitched);
                                    return switch (direction) {
                                        case NORTH -> variant;
                                        case EAST -> variant.apply(ROTATE_Y_90);
                                        case SOUTH -> variant.apply(ROTATE_Y_180);
                                        default -> variant.apply(ROTATE_Y_270);
                                    };
                                }))
                );
    }
    public final void registerAxialRail(Block rail, BlockStateModelGenerator modelGenerator) {
        WeightedVariant nsOff = createWeightedVariant(modelGenerator.createSubModel(rail, "", Models.RAIL_FLAT, TextureMap::rail));
        WeightedVariant raisedNEOff = createWeightedVariant(modelGenerator.createSubModel(rail, "", Models.TEMPLATE_RAIL_RAISED_NE, TextureMap::rail));
        WeightedVariant raisedSWOff = createWeightedVariant(modelGenerator.createSubModel(rail, "", Models.TEMPLATE_RAIL_RAISED_SW, TextureMap::rail));
        WeightedVariant nsOn = createWeightedVariant(modelGenerator.createSubModel(rail, "_on", Models.RAIL_FLAT, TextureMap::rail));
        WeightedVariant raisedNEOn = createWeightedVariant(modelGenerator.createSubModel(rail, "_on", Models.TEMPLATE_RAIL_RAISED_NE, TextureMap::rail));
        WeightedVariant raisedSWOn = createWeightedVariant(modelGenerator.createSubModel(rail, "_on", Models.TEMPLATE_RAIL_RAISED_SW, TextureMap::rail));

        modelGenerator.registerItemModel(rail);
        modelGenerator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(rail).with(BlockStateVariantMap.models(Properties.POWERED, Properties.STRAIGHT_RAIL_SHAPE, HaltRailBlock.AXIAL).generate((powered, shape, axial) -> {
            WeightedVariant var10000;
            switch (shape) {
                case NORTH_SOUTH -> var10000 = (powered ? nsOn : nsOff).apply(axial ? ROTATE_Y_180 : NO_OP);
                case EAST_WEST -> var10000 = (powered ? nsOn : nsOff).apply(axial ? ROTATE_Y_90 : ROTATE_Y_270);
                case ASCENDING_EAST -> var10000 = fourState(axial, powered, raisedSWOff.apply(ROTATE_Y_270), raisedNEOff.apply(ROTATE_Y_90), raisedSWOn.apply(ROTATE_Y_270),  raisedNEOn.apply(ROTATE_Y_90));
                case ASCENDING_WEST -> var10000 = fourState(axial, powered, raisedNEOff.apply(ROTATE_Y_270), raisedSWOff.apply(ROTATE_Y_90), raisedNEOn.apply(ROTATE_Y_270),  raisedSWOn.apply(ROTATE_Y_90));
                case ASCENDING_NORTH -> var10000 = fourState(axial, powered, raisedNEOff, raisedSWOff.apply(ROTATE_Y_180), raisedNEOn,  raisedSWOn.apply(ROTATE_Y_180));
                case ASCENDING_SOUTH -> var10000 = fourState(axial, powered, raisedSWOff, raisedNEOff.apply(ROTATE_Y_180), raisedSWOn,  raisedNEOn.apply(ROTATE_Y_180));
                default -> throw new UnsupportedOperationException("Fix you generator!");
            }

            return var10000;
        })));
    }
    private static <T> T fourState(boolean bl1, boolean bl2, T ff, T tf, T ft, T tt){
        return bl1 ? (bl2 ? tt : tf) : (bl2 ? ft : ff);
    }
    private void registerSpawnEgg(ItemModelGenerator generator, EntityType<?> entityType){
        if(SpawnEggItem.forEntity(entityType) instanceof SpawnEggItem item){
            generator.register(item, CustomModels.SPAWN_EGG);
        }
    }

    private static class CustomModels {
        protected static final Model BUILTIN_SLASH_ENTITY = new GuiLightFrontModel(Optional.of(Identifier.of("builtin/entity")), Optional.empty());
        protected static final Model SPAWN_EGG = new Model(Optional.of(Identifier.ofVanilla("item/template_spawn_egg")), Optional.empty());
        protected static class GuiLightFrontModel extends Model{


            public GuiLightFrontModel(Optional<Identifier> parent, Optional<String> variant, TextureKey... requiredTextureKeys) {
                super(parent, variant, requiredTextureKeys);
            }

            @Override
            public Identifier upload(Item item, TextureMap textures, BiConsumer<Identifier, ModelSupplier> modelCollector) {
                return super.upload(item, textures, modelCollector);
            }

            public Identifier upload(Identifier id, TextureMap textures, BiConsumer<Identifier, ModelSupplier> modelCollector) {
                Map<TextureKey, Identifier> map = this.createTextureMap(textures);
                modelCollector.accept(id, () -> {
                    JsonObject jsonObject = new JsonObject();
                    this.parent.ifPresent((identifier) -> jsonObject.addProperty("parent", identifier.toString()));
                    if (!map.isEmpty()) {
                        JsonObject textureObject = new JsonObject();
                        map.forEach((textureKey, identifier) -> textureObject.addProperty(textureKey.getName(), identifier.toString()));
                        jsonObject.add("textures", textureObject);
                        jsonObject.add("gui-light", new JsonPrimitive("front"));
                    }

                    return jsonObject;
                });
                return id;
            }
        }
    }
}
