package de.nexusrealms.eraofrails.entity;

import de.nexusrealms.eraofrails.EraOfRails;
import de.nexusrealms.eraofrails.entity.types.*;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;

import java.util.Optional;

public class RailwaysEntities {

    //Copper variants of vanilla minecarts
    public static final EntityType<CopperMinecartEntity> COPPER_MINECART = create("copper_minecart", EntityType.Builder.of(CopperMinecartEntity::new, MobCategory.MISC)
            .sized(0.98F, 0.7F).passengerAttachments(0.1875F).clientTrackingRange(8));
    public static final EntityType<ChestCopperMinecartEntity> CHEST_COPPER_MINECART = create("chest_copper_minecart", EntityType.Builder.of(ChestCopperMinecartEntity::new, MobCategory.MISC)
            .sized(0.98F, 0.7F).passengerAttachments(0.1875F).clientTrackingRange(8));
    public static final EntityType<CommandBlockCopperMinecartEntity> COMMAND_BLOCK_COPPER_MINECART = create("command_block_copper_minecart", EntityType.Builder.of(CommandBlockCopperMinecartEntity::new, MobCategory.MISC)
            .sized(0.98F, 0.7F).passengerAttachments(0.1875F).clientTrackingRange(8));
    public static final EntityType<FurnaceCopperMinecartEntity> FURNACE_COPPER_MINECART = create("furnace_copper_minecart", EntityType.Builder.of(FurnaceCopperMinecartEntity::new, MobCategory.MISC)
            .sized(0.98F, 0.7F).passengerAttachments(0.1875F).clientTrackingRange(8));
    public static final EntityType<HopperCopperMinecartEntity> HOPPER_COPPER_MINECART = create("hopper_copper_minecart", EntityType.Builder.of(HopperCopperMinecartEntity::new, MobCategory.MISC)
            .sized(0.98F, 0.7F).passengerAttachments(0.1875F).clientTrackingRange(8));
    public static final EntityType<TntCopperMinecartEntity> TNT_COPPER_MINECART = create("tnt_copper_minecart", EntityType.Builder.of(TntCopperMinecartEntity::new, MobCategory.MISC)
            .sized(0.98F, 0.7F).passengerAttachments(0.1875F).clientTrackingRange(8));
    public static final EntityType<SpawnerCopperMinecartEntity> SPAWNER_COPPER_MINECART = create("spawner_copper_minecart", EntityType.Builder.of(SpawnerCopperMinecartEntity::new, MobCategory.MISC)
            .sized(0.98F, 0.7F).passengerAttachments(0.1875F).clientTrackingRange(8));

    //Custom minecarts
    public static final EntityType<JukeboxMinecartEntity> JUKEBOX_MINECART = create("jukebox_minecart", EntityType.Builder.of(JukeboxMinecartEntity::new, MobCategory.MISC)
            .sized(0.98F, 0.7F).passengerAttachments(0.1875F).clientTrackingRange(8));
    public static final EntityType<JukeboxCopperMinecartEntity> JUKEBOX_COPPER_MINECART = create("jukebox_copper_minecart", EntityType.Builder.of(JukeboxCopperMinecartEntity::new, MobCategory.MISC)
            .sized(0.98F, 0.7F).passengerAttachments(0.1875F).clientTrackingRange(8));
    public static final EntityType<RedstoneBlockMinecartEntity> REDSTONE_BLOCK_MINECART = create("redstone_block_minecart", EntityType.Builder.of(RedstoneBlockMinecartEntity::new, MobCategory.MISC)
            .sized(0.98F, 0.7F).passengerAttachments(0.1875F).clientTrackingRange(8));
    public static final EntityType<RedstoneCopperBlockMinecartEntity> REDSTONE_BLOCK_COPPER_MINECART = create("redstone_block_copper_minecart", EntityType.Builder.of(RedstoneCopperBlockMinecartEntity::new, MobCategory.MISC)
            .sized(0.98F, 0.7F).passengerAttachments(0.1875F).clientTrackingRange(8));
    public static final EntityType<InputMinecartEntity> INPUT_MINECART = create("input_minecart", EntityType.Builder.of(InputMinecartEntity::new, MobCategory.MISC)
            .sized(0.98F, 0.7F).passengerAttachments(0.1875F).clientTrackingRange(8));
    public static final EntityType<InputCopperMinecartEntity> INPUT_COPPER_MINECART = create("input_copper_minecart", EntityType.Builder.of(InputCopperMinecartEntity::new, MobCategory.MISC)
            .sized(0.98F, 0.7F).passengerAttachments(0.1875F).clientTrackingRange(8));
    public static <T extends Entity> EntityType<T> create(String name, EntityType.Builder<T> builder){
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, EraOfRails.id(name));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build());
    }
    public static void init(){
    }
    public static class Tags {
        public static final TagKey<EntityType<?>> COPPER_MINECARTS = TagKey.create(Registries.ENTITY_TYPE, EraOfRails.id("copper_minecarts"));
    }
}
