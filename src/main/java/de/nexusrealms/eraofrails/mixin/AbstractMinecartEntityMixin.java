// TODO(Ravel): Failed to fully resolve file: null cannot be cast to non-null type com.intellij.psi.PsiClass
package de.nexusrealms.eraofrails.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.nexusrealms.eraofrails.entity.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.entity.LazyEntityReference;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.entity.vehicle.DefaultMinecartController;
import net.minecraft.entity.vehicle.ExperimentalMinecartController;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.Items;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Function;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin extends VehicleEntity implements CartLinker {
    @Shadow public abstract void tick();

    @Unique
    private static final EntityDataAccessor<Optional<LazyCartReference>> LINKED_PARENT = SynchedEntityData.defineId(AbstractMinecart.class, RailwaysEntities.MINECART_REFERENCE);
    @Unique
    private static final EntityDataAccessor<Optional<LazyCartReference>> LINKED_CHILD = SynchedEntityData.defineId(AbstractMinecart.class, RailwaysEntities.MINECART_REFERENCE);

    public AbstractMinecartMixin(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }
    @WrapOperation(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V", at = @At(value = "NEW", target = "(Lnet/minecraft/world/entity/vehicle/AbstractMinecart;)Lnet/minecraft/entity/vehicle/ExperimentalMinecartController;"))
    public ExperimentalMinecartController useRailwayController(AbstractMinecart abstractMinecartEntity, Operation<DefaultMinecartController> original, EntityType<?> entityType, Level world){
        return entityType.is(RailwaysEntities.Tags.COPPER_MINECARTS) ? new CopperMinecartController(abstractMinecartEntity) : new RailwaysMinecartController(abstractMinecartEntity);
    }
    @ModifyReturnValue(method = "areMinecartImprovementsEnabled", at = @At("TAIL"))
    private static boolean theyAreAlwaysEnabled(boolean original){
        return true;
    }
    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    public void addToDataTracker(SynchedEntityData.Builder builder, CallbackInfo ci){
        builder.define(LINKED_PARENT, Optional.empty());
        builder.define(LINKED_CHILD, Optional.empty());
    }
    @Inject(method = "writeCustomData", at = @At("TAIL"))
    public void writeAdditionalData(WriteView view, CallbackInfo ci){
        view.put("linkedParent", CartLinker.MINECART_REFERENCE_CODEC, entityData.get(LINKED_PARENT));
        view.put("linkedChild", CartLinker.MINECART_REFERENCE_CODEC, entityData.get(LINKED_CHILD));
    }
    @Inject(method = "readCustomData", at = @At("TAIL"))
    public void readAdditionalData(ReadView view, CallbackInfo ci){
        entityData.set(LINKED_PARENT, view.read("linkedParent", CartLinker.MINECART_REFERENCE_CODEC).flatMap(Function.identity()));
        entityData.set(LINKED_CHILD, view.read("linkedChild", CartLinker.MINECART_REFERENCE_CODEC).flatMap(Function.identity()));
    }
    @Inject(method = "tick", at = @At("HEAD"))
    public void updateLinkedCarts(CallbackInfo ci){
        entityData.get(LINKED_PARENT).ifPresent(abstractMinecartEntityLazyEntityReference -> {
            if(abstractMinecartEntityLazyEntityReference.getEntityByClass(getCommandSenderWorld(), AbstractMinecart.class) instanceof AbstractMinecart notNull){
                if(!notNull.isRemoved()){
                    return;
                }
            }
            if(getCommandSenderWorld() instanceof ServerLevel world){
                spawnAtLocation(world, Items.IRON_CHAIN);
            }
            entityData.set(LINKED_PARENT, Optional.empty());
        });
        entityData.get(LINKED_CHILD).ifPresent(abstractMinecartEntityLazyEntityReference -> {
            if(abstractMinecartEntityLazyEntityReference.getEntityByClass(getCommandSenderWorld(), AbstractMinecart.class) instanceof AbstractMinecart notNull){
                if(!notNull.isRemoved()) {
                    return;
                }
            }
            if(getCommandSenderWorld() instanceof ServerLevel world){
                spawnAtLocation(world, Items.IRON_CHAIN);
            }
            entityData.set(LINKED_CHILD, Optional.empty());
        });
    }
    @Override
    public AbstractMinecart asEntity() {
        return (AbstractMinecart) (Object) this;
    }

    public Optional<CartLinker> getLinkedParent() {
        return entityData.get(LINKED_PARENT).map(e -> e.getEntityByClass(getCommandSenderWorld(), AbstractMinecart.class));
    }

    @Override
    public Optional<CartLinker> getLinkedChild() {
        return entityData.get(LINKED_CHILD).map(e -> e.getEntityByClass(getCommandSenderWorld(), AbstractMinecart.class));
    }
    @Override
    public void setLinkedParent(CartLinker cartLinker) {
        if(cartLinker == null){
            entityData.set(LINKED_PARENT, Optional.empty());
        } else {
            entityData.set(LINKED_PARENT, Optional.of(LazyEntityReference.of(cartLinker.asEntity())));
        }
    }
    @Override
    public void setLinkedChild(CartLinker cartLinker) {
        if(cartLinker == null){
            entityData.set(LINKED_CHILD, Optional.empty());
        } else {
            entityData.set(LINKED_CHILD, Optional.of(LazyEntityReference.of(cartLinker.asEntity())));
        }    }


}
