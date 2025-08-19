package de.nexusrealms.newrailways.entity;

import de.nexusrealms.newrailways.NewRailways;
import de.nexusrealms.newrailways.entity.types.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class RailwaysEntities {
    //Copper variants of vanilla minecarts
    public static final EntityType<CopperMinecartEntity> COPPER_MINECART = create("copper_minecart", EntityType.Builder.create(CopperMinecartEntity::new, SpawnGroup.MISC)
            .dropsNothing().dimensions(0.98F, 0.7F).passengerAttachments(0.1875F).maxTrackingRange(8));
    public static final EntityType<ChestCopperMinecartEntity> CHEST_COPPER_MINECART = create("chest_copper_minecart", EntityType.Builder.create(ChestCopperMinecartEntity::new, SpawnGroup.MISC)
            .dropsNothing().dimensions(0.98F, 0.7F).passengerAttachments(0.1875F).maxTrackingRange(8));
    public static final EntityType<CommandBlockCopperMinecartEntity> COMMAND_BLOCK_COPPER_MINECART = create("command_block_copper_minecart", EntityType.Builder.create(CommandBlockCopperMinecartEntity::new, SpawnGroup.MISC)
            .dropsNothing().dimensions(0.98F, 0.7F).passengerAttachments(0.1875F).maxTrackingRange(8));
    public static final EntityType<FurnaceCopperMinecartEntity> FURNACE_COPPER_MINECART = create("furnace_copper_minecart", EntityType.Builder.create(FurnaceCopperMinecartEntity::new, SpawnGroup.MISC)
            .dropsNothing().dimensions(0.98F, 0.7F).passengerAttachments(0.1875F).maxTrackingRange(8));
    public static final EntityType<HopperCopperMinecartEntity> HOPPER_COPPER_MINECART = create("hopper_copper_minecart", EntityType.Builder.create(HopperCopperMinecartEntity::new, SpawnGroup.MISC)
            .dropsNothing().dimensions(0.98F, 0.7F).passengerAttachments(0.1875F).maxTrackingRange(8));
    public static final EntityType<TntCopperMinecartEntity> TNT_COPPER_MINECART = create("tnt_copper_minecart", EntityType.Builder.create(TntCopperMinecartEntity::new, SpawnGroup.MISC)
            .dropsNothing().dimensions(0.98F, 0.7F).passengerAttachments(0.1875F).maxTrackingRange(8));
    public static final EntityType<SpawnerCopperMinecartEntity> SPAWNER_COPPER_MINECART = create("spawner_copper_minecart", EntityType.Builder.create(SpawnerCopperMinecartEntity::new, SpawnGroup.MISC)
            .dropsNothing().dimensions(0.98F, 0.7F).passengerAttachments(0.1875F).maxTrackingRange(8));

    //Custom minecarts
    public static final EntityType<JukeboxMinecartEntity> JUKEBOX_MINECART = create("jukebox_minecart", EntityType.Builder.create(JukeboxMinecartEntity::new, SpawnGroup.MISC)
            .dropsNothing().dimensions(0.98F, 0.7F).passengerAttachments(0.1875F).maxTrackingRange(8));
    public static final EntityType<JukeboxCopperMinecartEntity> JUKEBOX_COPPER_MINECART = create("jukebox_copper_minecart", EntityType.Builder.create(JukeboxCopperMinecartEntity::new, SpawnGroup.MISC)
            .dropsNothing().dimensions(0.98F, 0.7F).passengerAttachments(0.1875F).maxTrackingRange(8));
    public static final EntityType<RedstoneBlockMinecartEntity> REDSTONE_BLOCK_MINECART = create("redstone_block_minecart", EntityType.Builder.create(RedstoneBlockMinecartEntity::new, SpawnGroup.MISC)
            .dropsNothing().dimensions(0.98F, 0.7F).passengerAttachments(0.1875F).maxTrackingRange(8));
    public static final EntityType<RedstoneCopperBlockMinecartEntity> REDSTONE_BLOCK_COPPER_MINECART = create("redstone_block_copper_minecart", EntityType.Builder.create(RedstoneCopperBlockMinecartEntity::new, SpawnGroup.MISC)
            .dropsNothing().dimensions(0.98F, 0.7F).passengerAttachments(0.1875F).maxTrackingRange(8));
    public static final EntityType<InputMinecartEntity> INPUT_MINECART = create("input_minecart", EntityType.Builder.create(InputMinecartEntity::new, SpawnGroup.MISC)
            .dropsNothing().dimensions(0.98F, 0.7F).passengerAttachments(0.1875F).maxTrackingRange(8));
    public static final EntityType<InputCopperMinecartEntity> INPUT_COPPER_MINECART = create("input_copper_minecart", EntityType.Builder.create(InputCopperMinecartEntity::new, SpawnGroup.MISC)
            .dropsNothing().dimensions(0.98F, 0.7F).passengerAttachments(0.1875F).maxTrackingRange(8));
    public static <T extends Entity> EntityType<T> create(String name, EntityType.Builder<T> builder){
        RegistryKey<EntityType<?>> key = RegistryKey.of(RegistryKeys.ENTITY_TYPE, NewRailways.id(name));
        return Registry.register(Registries.ENTITY_TYPE, key, builder.build(key));
    }
    public static void init(){}
    public static class Tags {
        public static final TagKey<EntityType<?>> COPPER_MINECARTS = TagKey.of(RegistryKeys.ENTITY_TYPE, NewRailways.id("copper_minecarts"));
    }
}
