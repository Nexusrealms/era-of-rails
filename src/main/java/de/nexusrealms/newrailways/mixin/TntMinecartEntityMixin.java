package de.nexusrealms.newrailways.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.nexusrealms.newrailways.entity.RailSafeExplosionBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TntMinecartEntity.class)
public abstract class TntMinecartEntityMixin extends AbstractMinecartEntity {
    protected TntMinecartEntityMixin(EntityType<?> entityType, World world) {
        super(entityType, world);
    }
    @WrapOperation(method = "explode(Lnet/minecraft/entity/damage/DamageSource;D)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;createExplosion(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/world/explosion/ExplosionBehavior;DDDFZLnet/minecraft/world/World$ExplosionSourceType;)V"))
    public void createSafeExplosion(ServerWorld instance, Entity entity, DamageSource damageSource, ExplosionBehavior explosionBehavior, double x, double y, double z, float power, boolean b, World.ExplosionSourceType explosionSourceType, Operation<Void> original){
        original.call(instance, entity, damageSource, new RailSafeExplosionBehavior(), x, y, z, power, b, explosionSourceType);
    }
}
