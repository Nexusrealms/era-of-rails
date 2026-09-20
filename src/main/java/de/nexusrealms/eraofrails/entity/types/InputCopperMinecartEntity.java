package de.nexusrealms.eraofrails.entity.types;

import de.nexusrealms.eraofrails.item.RailwaysItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class InputCopperMinecartEntity extends InputMinecartEntity {

    public InputCopperMinecartEntity(EntityType<? extends InputMinecartEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public Item getDropItem() {
        return RailwaysItems.INPUT_COPPER_MINECART;
    }
    @Override
    public boolean canCollideWith(Entity other) {
        return other instanceof AbstractMinecart;
    }
}
