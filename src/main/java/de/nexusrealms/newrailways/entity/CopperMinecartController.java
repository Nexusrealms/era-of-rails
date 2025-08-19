package de.nexusrealms.newrailways.entity;

import de.nexusrealms.newrailways.NewRailways;
import de.nexusrealms.newrailways.entity.types.CopperMinecartEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.math.Vec3d;

public class CopperMinecartController extends RailwaysMinecartController{
    public CopperMinecartController(AbstractMinecartEntity abstractMinecartEntity) {
        super(abstractMinecartEntity);
    }

    @Override
    public void tick() {
        super.tick();
        for(Entity other : getWorld().getOtherEntities(minecart, minecart.getBoundingBox().expand(0.5), entity -> CopperMinecartEntity.launches(minecart, entity))) {
            if(!getWorld().isClient() && other instanceof LivingEntity living && living.isAlive() && !living.hasVehicle() && minecart.getVelocity().length() > 1.5) {
                Vec3d knockback = minecart.getVelocity().add(living.getPos().subtract(minecart.getPos()).normalize()).add(0, minecart.getVelocity().length() / 2, 0);
                living.addVelocity(knockback);
                living.velocityModified = true;
                NewRailways.LOGGER.info("knockback " + knockback);
            }
        }
    }
}
