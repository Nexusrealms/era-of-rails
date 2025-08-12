package de.nexusrealms.newrailways.entity;

import de.nexusrealms.newrailways.item.RailwaysItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RedstoneCopperBlockMinecart extends RedstoneBlockMinecart {
    protected RedstoneCopperBlockMinecart(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public ItemStack getPickBlockStack() {
        return new ItemStack(asItem());
    }

    @Override
    protected Item asItem() {
        return RailwaysItems.REDSTONE_BLOCK_COPPER_MINECART;
    }
    @Override
    public boolean collidesWith(Entity other) {
        return other instanceof AbstractMinecartEntity;
    }
}
