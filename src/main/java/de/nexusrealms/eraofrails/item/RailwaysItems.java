package de.nexusrealms.eraofrails.item;

import com.mojang.serialization.Codec;
import de.nexusrealms.eraofrails.EraOfRails;
import de.nexusrealms.eraofrails.entity.RailwaysEntities;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.entity.vehicle.MinecartSpawner;
import net.minecraft.world.item.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.BlockPos;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class RailwaysItems {
    //Vanilla minecarts
    public static final BetterMinecartItem SPAWNER_MINECART = create("spawner_minecart", settings -> new BetterMinecartItem(MinecartSpawner::new, settings), new Item.Properties().stacksTo(1), CreativeModeTabs.SPAWN_EGGS, (fabricItemGroupEntries, BetterMinecartItem) -> fabricItemGroupEntries.addAfter(Items.SPAWNER, BetterMinecartItem));
    public static final BetterMinecartItem JUKEBOX_MINECART = create("jukebox_minecart", settings -> new BetterMinecartItem(RailwaysEntities.JUKEBOX_MINECART, settings), new Item.Properties().stacksTo(1), CreativeModeTabs.REDSTONE_BLOCKS, (fabricItemGroupEntries, BetterMinecartItem) -> fabricItemGroupEntries.addAfter(Items.TNT_MINECART, BetterMinecartItem));
    public static final BetterMinecartItem REDSTONE_BLOCK_MINECART = create("redstone_block_minecart", settings -> new BetterMinecartItem(RailwaysEntities.REDSTONE_BLOCK_MINECART, settings), new Item.Properties().stacksTo(1), CreativeModeTabs.REDSTONE_BLOCKS, (fabricItemGroupEntries, BetterMinecartItem) -> fabricItemGroupEntries.addAfter(JUKEBOX_MINECART, BetterMinecartItem));
    public static final BetterMinecartItem INPUT_MINECART = create("input_minecart", settings -> new BetterMinecartItem(RailwaysEntities.INPUT_MINECART, settings), new Item.Properties().stacksTo(1), CreativeModeTabs.REDSTONE_BLOCKS, (fabricItemGroupEntries, BetterMinecartItem) -> fabricItemGroupEntries.addAfter(REDSTONE_BLOCK_MINECART, BetterMinecartItem));


    //their copper variants
    public static final BetterMinecartItem COPPER_MINECART = create("copper_minecart", settings -> new BetterMinecartItem(RailwaysEntities.COPPER_MINECART, settings), new Item.Properties().stacksTo(1), CreativeModeTabs.REDSTONE_BLOCKS, (fabricItemGroupEntries, BetterMinecartItem) -> fabricItemGroupEntries.addAfter(INPUT_MINECART, BetterMinecartItem));
    public static final BetterMinecartItem CHEST_COPPER_MINECART = create("chest_copper_minecart", settings -> new BetterMinecartItem(RailwaysEntities.CHEST_COPPER_MINECART, settings), new Item.Properties().stacksTo(1), CreativeModeTabs.REDSTONE_BLOCKS, (fabricItemGroupEntries, BetterMinecartItem) -> fabricItemGroupEntries.addAfter(COPPER_MINECART, BetterMinecartItem));
    public static final BetterMinecartItem COMMAND_BLOCK_COPPER_MINECART = create("command_block_copper_minecart", settings -> new BetterMinecartItem(RailwaysEntities.COMMAND_BLOCK_COPPER_MINECART, settings), new Item.Properties().stacksTo(1).rarity(Rarity.EPIC), CreativeModeTabs.OP_BLOCKS, (fabricItemGroupEntries, BetterMinecartItem) -> fabricItemGroupEntries.addAfter(Items.COMMAND_BLOCK_MINECART, BetterMinecartItem));
    public static final BetterMinecartItem FURNACE_COPPER_MINECART = create("furnace_copper_minecart", settings -> new BetterMinecartItem(RailwaysEntities.FURNACE_COPPER_MINECART, settings), new Item.Properties().stacksTo(1), CreativeModeTabs.REDSTONE_BLOCKS, (fabricItemGroupEntries, BetterMinecartItem) -> fabricItemGroupEntries.addAfter(CHEST_COPPER_MINECART, BetterMinecartItem));
    public static final BetterMinecartItem HOPPER_COPPER_MINECART = create("hopper_copper_minecart", settings -> new BetterMinecartItem(RailwaysEntities.HOPPER_COPPER_MINECART, settings), new Item.Properties().stacksTo(1), CreativeModeTabs.REDSTONE_BLOCKS, (fabricItemGroupEntries, BetterMinecartItem) -> fabricItemGroupEntries.addAfter(FURNACE_COPPER_MINECART, BetterMinecartItem));
    public static final BetterMinecartItem TNT_COPPER_MINECART = create("tnt_copper_minecart", settings -> new BetterMinecartItem(RailwaysEntities.TNT_COPPER_MINECART, settings), new Item.Properties().stacksTo(1), CreativeModeTabs.REDSTONE_BLOCKS, (fabricItemGroupEntries, BetterMinecartItem) -> fabricItemGroupEntries.addAfter(HOPPER_COPPER_MINECART, BetterMinecartItem));
    public static final BetterMinecartItem SPAWNER_COPPER_MINECART = create("spawner_copper_minecart", settings -> new BetterMinecartItem(RailwaysEntities.SPAWNER_COPPER_MINECART, settings), new Item.Properties().stacksTo(1), CreativeModeTabs.SPAWN_EGGS, (fabricItemGroupEntries, BetterMinecartItem) -> fabricItemGroupEntries.addAfter(SPAWNER_MINECART, BetterMinecartItem));
    public static final BetterMinecartItem JUKEBOX_COPPER_MINECART = create("jukebox_copper_minecart", settings -> new BetterMinecartItem(RailwaysEntities.JUKEBOX_COPPER_MINECART, settings), new Item.Properties().stacksTo(1), CreativeModeTabs.REDSTONE_BLOCKS, (fabricItemGroupEntries, BetterMinecartItem) -> fabricItemGroupEntries.addAfter(TNT_COPPER_MINECART, BetterMinecartItem));
    public static final BetterMinecartItem REDSTONE_BLOCK_COPPER_MINECART = create("redstone_block_copper_minecart", settings -> new BetterMinecartItem(RailwaysEntities.REDSTONE_BLOCK_COPPER_MINECART, settings), new Item.Properties().stacksTo(1), CreativeModeTabs.REDSTONE_BLOCKS, (fabricItemGroupEntries, BetterMinecartItem) -> fabricItemGroupEntries.addAfter(JUKEBOX_COPPER_MINECART, BetterMinecartItem));
    public static final BetterMinecartItem INPUT_COPPER_MINECART = create("input_copper_minecart", settings -> new BetterMinecartItem(RailwaysEntities.INPUT_COPPER_MINECART, settings), new Item.Properties().stacksTo(1), CreativeModeTabs.REDSTONE_BLOCKS, (fabricItemGroupEntries, BetterMinecartItem) -> fabricItemGroupEntries.addAfter(REDSTONE_BLOCK_COPPER_MINECART, BetterMinecartItem));
    public static final Item CART_LINKING_TOOL = create("cart_linking_tool", CartLinkingTool::new, new Item.Properties().stacksTo(1), CreativeModeTabs.OP_BLOCKS);
    private static <T extends Item> T create(String name, Function<Item.Properties, T> constructor, Item.Properties settings, ResourceKey<CreativeModeTab> itemGroup){
        return create(name, constructor, settings, itemGroup, FabricItemGroupEntries::accept);
    }
    private static <T extends Item> T create(String name, Function<Item.Properties, T> constructor, Item.Properties settings, ResourceKey<CreativeModeTab> itemGroup, BiConsumer<FabricItemGroupEntries, T> itemGrouper){
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, EraOfRails.id(name));
        T item = Registry.register(BuiltInRegistries.ITEM, key, constructor.apply(settings));
        ItemGroupEvents.modifyEntriesEvent(itemGroup).register(fabricItemGroupEntries -> itemGrouper.accept(fabricItemGroupEntries, item));
        return item;
    }
    public static void init(){
        Components.init();
        registerBehavior(SPAWNER_MINECART);
        registerBehavior(JUKEBOX_MINECART);
        registerBehavior(REDSTONE_BLOCK_MINECART);
        registerBehavior(INPUT_MINECART);
        registerBehavior(COPPER_MINECART);
        registerBehavior(CHEST_COPPER_MINECART);
        registerBehavior(COMMAND_BLOCK_COPPER_MINECART);
        registerBehavior(FURNACE_COPPER_MINECART);
        registerBehavior(HOPPER_COPPER_MINECART);
        registerBehavior(TNT_COPPER_MINECART);
        registerBehavior(SPAWNER_COPPER_MINECART);
        registerBehavior(JUKEBOX_COPPER_MINECART);
        registerBehavior(REDSTONE_BLOCK_COPPER_MINECART);
        registerBehavior(INPUT_COPPER_MINECART);

    }
    private static void registerBehavior(BetterMinecartItem item){
        DispenserBlock.registerBehavior(item, BetterMinecartItem.DISPENSE_ITEM_BEHAVIOR);
    }
    private final static Map<BlockPos, AbstractMinecart> DISPENSED_CARTS = new HashMap<>();
    private final static Set<BlockPos> DISPENSED_CHAINS = new HashSet<>();

    public static void addDispensedPositionedCart(BlockPos pos, AbstractMinecart minecart){
        if(!DISPENSED_CARTS.containsKey(pos)){
            DISPENSED_CARTS.put(pos, minecart);
        }
    }
    public static Optional<AbstractMinecart> hasCartAndChain(BlockPos pos){
        if(DISPENSED_CHAINS.contains(pos)) {
            return Optional.ofNullable(DISPENSED_CARTS.get(pos));
        }
        return Optional.empty();
    }
    public static void clearAt(BlockPos pos){
        DISPENSED_CARTS.remove(pos);
        DISPENSED_CHAINS.remove(pos);
    }
    public static class Tags {
        public static final TagKey<Item> LINK_CARTS = TagKey.create(Registries.ITEM, EraOfRails.id("link_carts"));
    }
    public static class Components {
        //public static final ComponentType<LazyEntityReference<AbstractMinecartEntity>> LINKING_CHILD = create("linking_child", LazyEntityReference.createCodec(), LazyEntityReference.createPacketCodec());
        public static final DataComponentType<Long> INPUT_CART_SEQUENCE = create("input_cart_sequence", Codec.LONG, ByteBufCodecs.VAR_LONG);
        public static final DataComponentType<Integer> INPUT_CART_LIMIT = create("input_cart_limit", Codec.INT, ByteBufCodecs.VAR_INT);

        private static <T> DataComponentType<T> create(String name, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> packetCodec){
            return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, EraOfRails.id(name), DataComponentType.<T>builder().persistent(codec).networkSynchronized(packetCodec).build());
        }
        public static void init(){}
    }
}
