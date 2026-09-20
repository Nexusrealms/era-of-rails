package de.nexusrealms.eraofrails.entity.types;

import de.nexusrealms.eraofrails.item.RailwaysItems;
import net.minecraft.world.entity.vehicle.AbstractMinecart.Type;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RedstoneBlockMinecartEntity extends AbstractMinecart {
    public RedstoneBlockMinecartEntity(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }
    //TODO Custom cart type enum
    @Override
    public Type getMinecartType() {
        return Type.SPAWNER;
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(getDropItem());
    }

    @Override
    protected Item getDropItem() {
        return RailwaysItems.REDSTONE_BLOCK_MINECART;
    }

    @Override
    public BlockState getDefaultDisplayBlockState() {
        return Blocks.REDSTONE_BLOCK.defaultBlockState();
    }
}
