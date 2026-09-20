package de.nexusrealms.eraofrails.entity;

import de.nexusrealms.eraofrails.EraOfRails;
import de.nexusrealms.eraofrails.entity.types.CopperMinecartEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.phys.Vec3;

public class CopperMinecartController extends RailwaysMinecartController{
    public CopperMinecartController(AbstractMinecart abstractMinecartEntity) {
        super(abstractMinecartEntity);
    }

    @Override
    public void tick() {
        super.tick();
        for(Entity other : getWorld().getOtherEntities(minecart, minecart.getBoundingBox().expand(0.5), entity -> CopperMinecartEntity.launches(minecart, entity))) {
            if(!getWorld().isClient() && other instanceof LivingEntity living && living.isAlive() && !living.isPassenger() && minecart.getVelocity().length() > 1.5) {
                Vec3 knockback = minecart.getVelocity().add(living.getEntityPos().subtract(minecart.getEntityPos()).normalize()).add(0, minecart.getVelocity().length() / 2, 0);
                living.push(knockback);
            }
        }
    }
}
