package de.nexusrealms.eraofrails.entity.types;

import de.nexusrealms.eraofrails.item.RailwaysItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class InputCopperMinecartEntity extends InputMinecartEntity {

    public InputCopperMinecartEntity(EntityType<? extends InputMinecartEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected Item asItem() {
        return RailwaysItems.INPUT_COPPER_MINECART;
    }
    @Override
    public boolean collidesWith(Entity other) {
        return other instanceof AbstractMinecartEntity;
    }
}
