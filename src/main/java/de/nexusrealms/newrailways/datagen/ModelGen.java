package de.nexusrealms.newrailways.datagen;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import de.nexusrealms.newrailways.block.RailwaysBlocks;
import de.nexusrealms.newrailways.block.SwitchRailBlock;
import de.nexusrealms.newrailways.item.RailwaysItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
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
import java.util.function.Consumer;

import static net.minecraft.client.data.BlockStateModelGenerator.*;

public class ModelGen extends FabricModelProvider {
    public ModelGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerStraightRail(RailwaysBlocks.COPPER_RAIL);
        registerSwitch(RailwaysBlocks.SWITCH_RAIL, blockStateModelGenerator);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(RailwaysItems.SPAWNER_MINECART, Models.GENERATED);
        itemModelGenerator.register(RailwaysItems.JUKEBOX_MINECART, Models.GENERATED);
        itemModelGenerator.register(RailwaysItems.REDSTONE_BLOCK_MINECART, Models.GENERATED);
        itemModelGenerator.register(RailwaysItems.COPPER_MINECART, Models.GENERATED);
        itemModelGenerator.register(RailwaysItems.CHEST_COPPER_MINECART, Models.GENERATED);
        itemModelGenerator.register(RailwaysItems.COMMAND_BLOCK_COPPER_MINECART, Models.GENERATED);
        itemModelGenerator.register(RailwaysItems.FURNACE_COPPER_MINECART, Models.GENERATED);
        itemModelGenerator.register(RailwaysItems.HOPPER_COPPER_MINECART, Models.GENERATED);
        itemModelGenerator.register(RailwaysItems.TNT_COPPER_MINECART, Models.GENERATED);
        itemModelGenerator.register(RailwaysItems.SPAWNER_COPPER_MINECART, Models.GENERATED);
        itemModelGenerator.register(RailwaysItems.JUKEBOX_COPPER_MINECART, Models.GENERATED);
        itemModelGenerator.register(RailwaysItems.REDSTONE_BLOCK_COPPER_MINECART, Models.GENERATED);
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
