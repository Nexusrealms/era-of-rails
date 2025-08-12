package de.nexusrealms.newrailways.block;

import de.nexusrealms.newrailways.NewRailways;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class RailwaysBlocks {
    public static final Block COPPER_RAIL = createWithItem("copper_rail", CopperRailBlock::new, AbstractBlock.Settings.copy(Blocks.POWERED_RAIL), ItemGroups.REDSTONE, (fabricItemGroupEntries, item) -> fabricItemGroupEntries.addAfter(Blocks.POWERED_RAIL, item));

    public static final Block SWITCH_RAIL = createWithItem("switch_rail", SwitchRailBlock::new, AbstractBlock.Settings.copy(Blocks.RAIL), ItemGroups.REDSTONE, (fabricItemGroupEntries, item) -> fabricItemGroupEntries.addBefore(COPPER_RAIL, item));

    private static <T extends Block> T createWithItem(String name, Function<AbstractBlock.Settings, T> constructor, AbstractBlock.Settings settings, RegistryKey<ItemGroup> itemGroup){
        return createWithItem(name, constructor, settings, itemGroup, FabricItemGroupEntries::add);
    }
    private static <T extends Block> T createWithItem(String name, Function<AbstractBlock.Settings, T> constructor, AbstractBlock.Settings settings, RegistryKey<ItemGroup> itemGroup, BiConsumer<FabricItemGroupEntries, Item> itemGrouper){
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, NewRailways.id(name));
        T block = create(name, constructor, settings);
        Item item = new BlockItem(block, new Item.Settings().registryKey(key));
        Registry.register(Registries.ITEM, key, item);
        ItemGroupEvents.modifyEntriesEvent(itemGroup).register(entries -> itemGrouper.accept(entries, item));
        return block;
    }
    private static <T extends Block> T create(String name, Function<AbstractBlock.Settings, T> constructor, AbstractBlock.Settings settings){
        RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK, NewRailways.id(name));
        settings.registryKey(key);
        return Registry.register(Registries.BLOCK, key, constructor.apply(settings));
    }
    public static void init(){}
    public static class Tags{
        public static final TagKey<Block> HIGH_SPEED_RAIL = TagKey.of(RegistryKeys.BLOCK, NewRailways.id("high_speed_rail"));
        public static final TagKey<Block> POWERED_HIGH_SPEED_RAIL = TagKey.of(RegistryKeys.BLOCK, NewRailways.id("powered_high_speed_rail"));

    }
}
