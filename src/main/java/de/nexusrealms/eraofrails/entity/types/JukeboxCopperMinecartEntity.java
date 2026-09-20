package de.nexusrealms.eraofrails.entity.types;

import de.nexusrealms.eraofrails.item.RailwaysItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class JukeboxCopperMinecartEntity extends JukeboxMinecartEntity {

    public JukeboxCopperMinecartEntity(EntityType<? extends JukeboxMinecartEntity> entityType, Level world) {
        super(entityType, world);

    }
    @Override
    public boolean canCollideWith(Entity other) {
        return other instanceof AbstractMinecart;
    }
    @Override
    public ItemStack getPickResult() {
        return new ItemStack(getDropItem());
    }

    @Override
    protected Item getDropItem() {
        return RailwaysItems.CHEST_COPPER_MINECART;
    }
}
