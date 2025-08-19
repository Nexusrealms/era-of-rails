package de.nexusrealms.newrailways.entity.types;

import de.nexusrealms.newrailways.item.RailwaysItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.FurnaceMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class FurnaceCopperMinecartEntity extends FurnaceMinecartEntity {

    public FurnaceCopperMinecartEntity(EntityType<? extends FurnaceMinecartEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public ItemStack getPickBlockStack() {
        return new ItemStack(asItem());
    }

    @Override
    protected Item asItem() {
        return RailwaysItems.FURNACE_COPPER_MINECART;
    }
    @Override
    public boolean collidesWith(Entity other) {
        return other instanceof AbstractMinecartEntity;
    }
}
