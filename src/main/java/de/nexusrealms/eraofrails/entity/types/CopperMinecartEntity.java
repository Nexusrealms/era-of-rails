package de.nexusrealms.eraofrails.entity.types;

import de.nexusrealms.eraofrails.entity.ComparatorOutputtingMinecart;
import de.nexusrealms.eraofrails.item.RailwaysItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.entity.vehicle.ContainerEntity;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;

public class CopperMinecartEntity extends Minecart implements ComparatorOutputtingMinecart {
    public CopperMinecartEntity(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(getDropItem());
    }

    @Override
    public boolean canCollideWith(Entity other) {
        return other instanceof AbstractMinecart;
    }
    public static boolean launches(AbstractMinecart minecart, Entity other) {
        return Boat.canVehicleCollide(minecart, other);
    }

    @Override
    protected Item getDropItem() {
        return RailwaysItems.COPPER_MINECART;
    }

    @Override
    public int getComparatorOutput() {
        Container inventory = null;
        Entity passenger = getFirstPassenger();
        if(passenger instanceof Player player) inventory = player.getInventory();
        if(passenger instanceof AbstractHorse horseEntity) inventory = horseEntity.inventory;
        if(passenger instanceof ContainerEntity vehicleInventory) inventory = vehicleInventory;
        return inventory != null ? AbstractContainerMenu.getRedstoneSignalFromContainer(inventory) : 0;
    }
}
