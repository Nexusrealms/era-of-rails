package de.nexusrealms.eraofrails.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.nexusrealms.eraofrails.entity.RailSafeExplosionBehavior;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ExplosionDamageCalculator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MinecartTNT.class)
public abstract class MinecartTNTMixin extends AbstractMinecart {
    protected MinecartTNTMixin(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }
    @WrapOperation(method = "explode(Lnet/minecraft/world/damagesource/DamageSource;D)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;createExplosion(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/level/ExplosionDamageCalculator;DDDFZLnet/minecraft/world/level/Level$ExplosionInteraction;)V"))
    public void createSafeExplosion(ServerLevel instance, Entity entity, DamageSource damageSource, ExplosionDamageCalculator explosionBehavior, double x, double y, double z, float power, boolean b, Level.ExplosionInteraction explosionSourceType, Operation<Void> original){
        original.call(instance, entity, damageSource, new RailSafeExplosionBehavior(), x, y, z, power, b, explosionSourceType);
    }
}
