package de.nexusrealms.newrailways.entity;

import de.nexusrealms.newrailways.item.RailwaysItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class CopperMinecartEntity extends MinecartEntity {
    protected CopperMinecartEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public ItemStack getPickBlockStack() {
        return new ItemStack(asItem());
    }

    @Override
    public boolean collidesWith(Entity other) {
        return other instanceof AbstractMinecartEntity;
    }
    public static boolean launches(AbstractMinecartEntity minecart, Entity other) {
        return AbstractBoatEntity.canCollide(minecart, other);
    }

    @Override
    protected Item asItem() {
        return RailwaysItems.COPPER_MINECART;
    }
}
