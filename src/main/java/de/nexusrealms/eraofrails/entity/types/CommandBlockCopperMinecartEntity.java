package de.nexusrealms.eraofrails.entity.types;

import de.nexusrealms.eraofrails.item.RailwaysItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.MinecartCommandBlock;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CommandBlockCopperMinecartEntity extends MinecartCommandBlock {

    public CommandBlockCopperMinecartEntity(EntityType<? extends MinecartCommandBlock> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(getDropItem());
    }

    @Override
    protected Item getDropItem() {
        return RailwaysItems.COMMAND_BLOCK_COPPER_MINECART;
    }
    @Override
    public boolean canCollideWith(Entity other) {
        return other instanceof AbstractMinecart;
    }
}
