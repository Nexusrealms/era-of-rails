package de.nexusrealms.newrailways.item;

import com.mojang.serialization.Codec;
import de.nexusrealms.newrailways.NewRailways;
import de.nexusrealms.newrailways.entity.RailwaysEntities;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LazyEntityReference;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.*;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Rarity;
import net.minecraft.util.dynamic.Codecs;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class RailwaysItems {
    //Vanilla minecarts
    public static final Item SPAWNER_MINECART = create("spawner_minecart", settings -> new MinecartItem(EntityType.SPAWNER_MINECART, settings), new Item.Settings().maxCount(1), ItemGroups.SPAWN_EGGS, (fabricItemGroupEntries, minecartItem) -> fabricItemGroupEntries.addAfter(Items.SPAWNER, minecartItem));
    public static final Item JUKEBOX_MINECART = create("jukebox_minecart", settings -> new MinecartItem(RailwaysEntities.JUKEBOX_MINECART, settings), new Item.Settings().maxCount(1), ItemGroups.REDSTONE, (fabricItemGroupEntries, minecartItem) -> fabricItemGroupEntries.addAfter(Items.TNT_MINECART, minecartItem));
    public static final Item REDSTONE_BLOCK_MINECART = create("redstone_block_minecart", settings -> new MinecartItem(RailwaysEntities.REDSTONE_BLOCK_MINECART, settings), new Item.Settings().maxCount(1), ItemGroups.REDSTONE, (fabricItemGroupEntries, minecartItem) -> fabricItemGroupEntries.addAfter(JUKEBOX_MINECART, minecartItem));
    public static final Item INPUT_MINECART = create("input_minecart", settings -> new MinecartItem(RailwaysEntities.INPUT_MINECART, settings), new Item.Settings().maxCount(1), ItemGroups.REDSTONE, (fabricItemGroupEntries, minecartItem) -> fabricItemGroupEntries.addAfter(REDSTONE_BLOCK_MINECART, minecartItem));


    //their copper variants
    public static final Item COPPER_MINECART = create("copper_minecart", settings -> new MinecartItem(RailwaysEntities.COPPER_MINECART, settings), new Item.Settings().maxCount(1), ItemGroups.REDSTONE, (fabricItemGroupEntries, minecartItem) -> fabricItemGroupEntries.addAfter(INPUT_MINECART, minecartItem));
    public static final Item CHEST_COPPER_MINECART = create("chest_copper_minecart", settings -> new MinecartItem(RailwaysEntities.CHEST_COPPER_MINECART, settings), new Item.Settings().maxCount(1), ItemGroups.REDSTONE, (fabricItemGroupEntries, minecartItem) -> fabricItemGroupEntries.addAfter(COPPER_MINECART, minecartItem));
    public static final Item COMMAND_BLOCK_COPPER_MINECART = create("command_block_copper_minecart", settings -> new MinecartItem(RailwaysEntities.COMMAND_BLOCK_COPPER_MINECART, settings), new Item.Settings().maxCount(1).rarity(Rarity.EPIC), ItemGroups.OPERATOR, (fabricItemGroupEntries, minecartItem) -> fabricItemGroupEntries.addAfter(Items.COMMAND_BLOCK_MINECART, minecartItem));
    public static final Item FURNACE_COPPER_MINECART = create("furnace_copper_minecart", settings -> new MinecartItem(RailwaysEntities.FURNACE_COPPER_MINECART, settings), new Item.Settings().maxCount(1), ItemGroups.REDSTONE, (fabricItemGroupEntries, minecartItem) -> fabricItemGroupEntries.addAfter(CHEST_COPPER_MINECART, minecartItem));
    public static final Item HOPPER_COPPER_MINECART = create("hopper_copper_minecart", settings -> new MinecartItem(RailwaysEntities.HOPPER_COPPER_MINECART, settings), new Item.Settings().maxCount(1), ItemGroups.REDSTONE, (fabricItemGroupEntries, minecartItem) -> fabricItemGroupEntries.addAfter(FURNACE_COPPER_MINECART, minecartItem));
    public static final Item TNT_COPPER_MINECART = create("tnt_copper_minecart", settings -> new MinecartItem(RailwaysEntities.TNT_COPPER_MINECART, settings), new Item.Settings().maxCount(1), ItemGroups.REDSTONE, (fabricItemGroupEntries, minecartItem) -> fabricItemGroupEntries.addAfter(HOPPER_COPPER_MINECART, minecartItem));
    public static final Item SPAWNER_COPPER_MINECART = create("spawner_copper_minecart", settings -> new MinecartItem(RailwaysEntities.SPAWNER_COPPER_MINECART, settings), new Item.Settings().maxCount(1), ItemGroups.SPAWN_EGGS, (fabricItemGroupEntries, minecartItem) -> fabricItemGroupEntries.addAfter(SPAWNER_MINECART, minecartItem));
    public static final Item JUKEBOX_COPPER_MINECART = create("jukebox_copper_minecart", settings -> new MinecartItem(RailwaysEntities.JUKEBOX_COPPER_MINECART, settings), new Item.Settings().maxCount(1), ItemGroups.REDSTONE, (fabricItemGroupEntries, minecartItem) -> fabricItemGroupEntries.addAfter(TNT_COPPER_MINECART, minecartItem));
    public static final Item REDSTONE_BLOCK_COPPER_MINECART = create("redstone_block_copper_minecart", settings -> new MinecartItem(RailwaysEntities.REDSTONE_BLOCK_COPPER_MINECART, settings), new Item.Settings().maxCount(1), ItemGroups.REDSTONE, (fabricItemGroupEntries, minecartItem) -> fabricItemGroupEntries.addAfter(JUKEBOX_COPPER_MINECART, minecartItem));
    public static final Item INPUT_COPPER_MINECART = create("input_copper_minecart", settings -> new MinecartItem(RailwaysEntities.INPUT_COPPER_MINECART, settings), new Item.Settings().maxCount(1), ItemGroups.REDSTONE, (fabricItemGroupEntries, minecartItem) -> fabricItemGroupEntries.addAfter(REDSTONE_BLOCK_COPPER_MINECART, minecartItem));

    //Custom items
    public static final Item CART_LINKING_TOOL = create("cart_linking_tool", CartLinkingTool::new, new Item.Settings().maxCount(1), ItemGroups.OPERATOR);

    private static <T extends Item> T create(String name, Function<Item.Settings, T> constructor, Item.Settings settings, RegistryKey<ItemGroup> itemGroup){
        return create(name, constructor, settings, itemGroup, FabricItemGroupEntries::add);
    }
    private static <T extends Item> T create(String name, Function<Item.Settings, T> constructor, Item.Settings settings, RegistryKey<ItemGroup> itemGroup, BiConsumer<FabricItemGroupEntries, T> itemGrouper){
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, NewRailways.id(name));
        settings.registryKey(key);
        T item = Registry.register(Registries.ITEM, key, constructor.apply(settings));
        ItemGroupEvents.modifyEntriesEvent(itemGroup).register(fabricItemGroupEntries -> itemGrouper.accept(fabricItemGroupEntries, item));
        return item;
    }
    public static void init(){
        Components.init();
    }
    public static class Tags {
        public static final TagKey<Item> LINK_CARTS = TagKey.of(RegistryKeys.ITEM, NewRailways.id("link_carts"));
    }
    public static class Components {
        public static final ComponentType<LazyEntityReference<AbstractMinecartEntity>> LINKING_PARENT = create("linking_parent", LazyEntityReference.createCodec(), LazyEntityReference.createPacketCodec());
        //public static final ComponentType<LazyEntityReference<AbstractMinecartEntity>> LINKING_CHILD = create("linking_child", LazyEntityReference.createCodec(), LazyEntityReference.createPacketCodec());

        private static <T> ComponentType<T> create(String name, Codec<T> codec, PacketCodec<? super RegistryByteBuf, T> packetCodec){
            return Registry.register(Registries.DATA_COMPONENT_TYPE, NewRailways.id(name), ComponentType.<T>builder().codec(codec).packetCodec(packetCodec).build());
        }
        public static void init(){}
    }
}
