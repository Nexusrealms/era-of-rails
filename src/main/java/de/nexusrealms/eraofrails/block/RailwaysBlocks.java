package de.nexusrealms.eraofrails.block;

import de.nexusrealms.eraofrails.EraOfRails;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class RailwaysBlocks {
    public static final Block COPPER_RAIL = createWithItem("copper_rail", CopperRailBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.POWERED_RAIL), CreativeModeTabs.REDSTONE_BLOCKS, Blocks.POWERED_RAIL);

    public static final Block SWITCH_RAIL = createWithItem("switch_rail", SwitchRailBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.RAIL), CreativeModeTabs.REDSTONE_BLOCKS, (fabricItemGroupEntries, item) -> fabricItemGroupEntries.addBefore(COPPER_RAIL, item));
    //public static final Block LOCKED_SWITCH_RAIL = createWithItem("locked_switch_rail", LockedSwitchRailBlock::new, AbstractBlock.Settings.copy(SWITCH_RAIL), ItemGroups.REDSTONE, SWITCH_RAIL);
    public static final Block INPUT_RAIL = createWithItem("input_rail", InputRailBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DETECTOR_RAIL), CreativeModeTabs.REDSTONE_BLOCKS, SWITCH_RAIL);
    public static final Block HALT_RAIL = createWithItem("halt_rail", HaltRailBlock::new, BlockBehaviour.Properties.ofFullCopy(COPPER_RAIL), CreativeModeTabs.REDSTONE_BLOCKS, INPUT_RAIL);
    public static final Block CROSS_RAIL = createWithItem("cross_rail", CrossRailBlock::new, BlockBehaviour.Properties.ofFullCopy(HALT_RAIL), CreativeModeTabs.REDSTONE_BLOCKS, HALT_RAIL);

    private static <T extends Block> T createWithItem(String name, Function<BlockBehaviour.Properties, T> constructor, BlockBehaviour.Properties settings, ResourceKey<CreativeModeTab> itemGroup){
        return createWithItem(name, constructor, settings, itemGroup, FabricItemGroupEntries::accept);
    }
    private static <T extends Block> T createWithItem(String name, Function<BlockBehaviour.Properties, T> constructor, BlockBehaviour.Properties settings, ResourceKey<CreativeModeTab> itemGroup, ItemLike after){
        return createWithItem(name, constructor, settings, itemGroup, (fabricItemGroupEntries, item) -> fabricItemGroupEntries.addAfter(after, item));
    }
    private static <T extends Block> T createWithItem(String name, Function<BlockBehaviour.Properties, T> constructor, BlockBehaviour.Properties settings, ResourceKey<CreativeModeTab> itemGroup, BiConsumer<FabricItemGroupEntries, Item> itemGrouper){
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, EraOfRails.id(name));
        T block = create(name, constructor, settings);
        Item item = new BlockItem(block, new net.minecraft.world.item.Item.Properties());
        Registry.register(BuiltInRegistries.ITEM, key, item);
        ItemGroupEvents.modifyEntriesEvent(itemGroup).register(entries -> itemGrouper.accept(entries, item));
        return block;
    }
    private static <T extends Block> T create(String name, Function<BlockBehaviour.Properties, T> constructor, BlockBehaviour.Properties settings){
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, EraOfRails.id(name));
        return Registry.register(BuiltInRegistries.BLOCK, key, constructor.apply(settings));
    }
    public static void init(){}
    public static class Tags{
        public static final TagKey<Block> HIGH_SPEED_RAIL = TagKey.create(Registries.BLOCK, EraOfRails.id("high_speed_rail"));
        public static final TagKey<Block> POWERED_HIGH_SPEED_RAIL = TagKey.create(Registries.BLOCK, EraOfRails.id("powered_high_speed_rail"));
        public static final TagKey<Block> INPUT_RAIL = TagKey.create(Registries.BLOCK, EraOfRails.id("input_rail"));
        public static final TagKey<Block> CROSS_RAIL = TagKey.create(Registries.BLOCK, EraOfRails.id("cross_rail"));

    }
}
