package de.nexusrealms.newrailways.entity;

import de.nexusrealms.newrailways.item.RailwaysItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.ChestMinecartEntity;
import net.minecraft.entity.vehicle.FurnaceMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ChestCopperMinecartEntity extends ChestMinecartEntity {

    public ChestCopperMinecartEntity(EntityType<? extends ChestMinecartEntity> entityType, World world) {
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
    @Override
    protected Item asItem() {
        return RailwaysItems.CHEST_COPPER_MINECART;
    }
}
