package de.nexusrealms.newrailways.entity.types;

import de.nexusrealms.newrailways.item.RailwaysItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RedstoneBlockMinecartEntity extends AbstractMinecartEntity {
    public RedstoneBlockMinecartEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public ItemStack getPickBlockStack() {
        return new ItemStack(asItem());
    }

    @Override
    protected Item asItem() {
        return RailwaysItems.REDSTONE_BLOCK_MINECART;
    }

    @Override
    public BlockState getDefaultContainedBlock() {
        return Blocks.REDSTONE_BLOCK.getDefaultState();
    }
}
