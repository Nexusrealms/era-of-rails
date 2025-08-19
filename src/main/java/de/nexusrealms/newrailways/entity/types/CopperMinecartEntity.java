package de.nexusrealms.newrailways.entity.types;

import de.nexusrealms.newrailways.entity.ComparatorOutputtingMinecart;
import de.nexusrealms.newrailways.item.RailwaysItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.entity.vehicle.VehicleInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.world.World;

public class CopperMinecartEntity extends MinecartEntity implements ComparatorOutputtingMinecart {
    public CopperMinecartEntity(EntityType<?> entityType, World world) {
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
    public static boolean launches(AbstractMinecartEntity minecart, Entity other) {
        return AbstractBoatEntity.canCollide(minecart, other);
    }

    @Override
    protected Item asItem() {
        return RailwaysItems.COPPER_MINECART;
    }

    @Override
    public int getComparatorOutput() {
        Inventory inventory = null;
        Entity passenger = getFirstPassenger();
        if(passenger instanceof PlayerEntity player) inventory = player.getInventory();
        if(passenger instanceof AbstractHorseEntity horseEntity) inventory = horseEntity.items;
        if(passenger instanceof VehicleInventory vehicleInventory) inventory = vehicleInventory;
        return inventory != null ? ScreenHandler.calculateComparatorOutput(inventory) : 0;
    }
}
