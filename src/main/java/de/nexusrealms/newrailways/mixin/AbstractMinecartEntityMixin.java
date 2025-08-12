package de.nexusrealms.newrailways.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.nexusrealms.newrailways.entity.CopperMinecartController;
import de.nexusrealms.newrailways.entity.RailwaysEntities;
import de.nexusrealms.newrailways.entity.RailwaysMinecartController;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.DefaultMinecartController;
import net.minecraft.entity.vehicle.ExperimentalMinecartController;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin extends VehicleEntity {

    public AbstractMinecartEntityMixin(EntityType<?> entityType, World world) {
        super(entityType, world);
    }
    @WrapOperation(method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V", at = @At(value = "NEW", target = "(Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;)Lnet/minecraft/entity/vehicle/ExperimentalMinecartController;"))
    public ExperimentalMinecartController useRailwayController(AbstractMinecartEntity abstractMinecartEntity, Operation<DefaultMinecartController> original, EntityType<?> entityType, World world){
        return entityType.isIn(RailwaysEntities.Tags.COPPER_MINECARTS) ? new CopperMinecartController(abstractMinecartEntity) : new RailwaysMinecartController(abstractMinecartEntity);
    }

}
