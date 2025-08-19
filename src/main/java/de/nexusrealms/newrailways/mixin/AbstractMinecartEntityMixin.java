package de.nexusrealms.newrailways.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.nexusrealms.newrailways.entity.CartLinker;
import de.nexusrealms.newrailways.entity.CopperMinecartController;
import de.nexusrealms.newrailways.entity.RailwaysEntities;
import de.nexusrealms.newrailways.entity.RailwaysMinecartController;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LazyEntityReference;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.DefaultMinecartController;
import net.minecraft.entity.vehicle.ExperimentalMinecartController;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Function;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin extends VehicleEntity implements CartLinker {
    @Shadow public abstract void tick();

    @Unique
    private static final TrackedData<Optional<LazyEntityReference<AbstractMinecartEntity>>> LINKED_PARENT = DataTracker.registerData(AbstractMinecartEntity.class, RailwaysEntities.MINECART_REFERENCE);
    @Unique
    private static final TrackedData<Optional<LazyEntityReference<AbstractMinecartEntity>>> LINKED_CHILD = DataTracker.registerData(AbstractMinecartEntity.class, RailwaysEntities.MINECART_REFERENCE);

    public AbstractMinecartEntityMixin(EntityType<?> entityType, World world) {
        super(entityType, world);
    }
    @WrapOperation(method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V", at = @At(value = "NEW", target = "(Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;)Lnet/minecraft/entity/vehicle/ExperimentalMinecartController;"))
    public ExperimentalMinecartController useRailwayController(AbstractMinecartEntity abstractMinecartEntity, Operation<DefaultMinecartController> original, EntityType<?> entityType, World world){
        return entityType.isIn(RailwaysEntities.Tags.COPPER_MINECARTS) ? new CopperMinecartController(abstractMinecartEntity) : new RailwaysMinecartController(abstractMinecartEntity);
    }
    @ModifyReturnValue(method = "areMinecartImprovementsEnabled", at = @At("TAIL"))
    private static boolean theyAreAlwaysEnabled(boolean original){
        return true;
    }
    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void addToDataTracker(DataTracker.Builder builder, CallbackInfo ci){
        builder.add(LINKED_PARENT, Optional.empty());
        builder.add(LINKED_CHILD, Optional.empty());
    }
    @Inject(method = "writeCustomData", at = @At("TAIL"))
    public void writeAdditionalData(WriteView view, CallbackInfo ci){
        view.put("linkedParent", CartLinker.MINECART_REFERENCE_CODEC, dataTracker.get(LINKED_PARENT));
        view.put("linkedChild", CartLinker.MINECART_REFERENCE_CODEC, dataTracker.get(LINKED_CHILD));
    }
    @Inject(method = "readCustomData", at = @At("TAIL"))
    public void readAdditionalData(ReadView view, CallbackInfo ci){
        dataTracker.set(LINKED_PARENT, view.read("linkedParent", CartLinker.MINECART_REFERENCE_CODEC).flatMap(Function.identity()));
        dataTracker.set(LINKED_CHILD, view.read("linkedChild", CartLinker.MINECART_REFERENCE_CODEC).flatMap(Function.identity()));
    }
    @Inject(method = "tick", at = @At("HEAD"))
    public void updateLinkedCarts(CallbackInfo ci){
        dataTracker.get(LINKED_PARENT).ifPresent(abstractMinecartEntityLazyEntityReference -> {
            if(abstractMinecartEntityLazyEntityReference.resolve(getWorld(), AbstractMinecartEntity.class) instanceof AbstractMinecartEntity notNull){
                if(!notNull.isRemoved()){
                    return;
                }
            }
            dataTracker.set(LINKED_PARENT, Optional.empty());
        });
        dataTracker.get(LINKED_CHILD).ifPresent(abstractMinecartEntityLazyEntityReference -> {
            if(abstractMinecartEntityLazyEntityReference.resolve(getWorld(), AbstractMinecartEntity.class) instanceof AbstractMinecartEntity notNull){
                if(!notNull.isRemoved()){
                    return;
                }
            }
            dataTracker.set(LINKED_CHILD, Optional.empty());
        });
    }
    @Override
    public AbstractMinecartEntity asEntity() {
        return (AbstractMinecartEntity) (Object) this;
    }

    public Optional<CartLinker> getLinkedParent() {
        return dataTracker.get(LINKED_PARENT).map(e -> e.resolve(getWorld(), AbstractMinecartEntity.class));
    }

    @Override
    public Optional<CartLinker> getLinkedChild() {
        return dataTracker.get(LINKED_CHILD).map(e -> e.resolve(getWorld(), AbstractMinecartEntity.class));
    }
    @Override
    public void setLinkedParent(CartLinker cartLinker) {
        if(cartLinker == null){
            dataTracker.set(LINKED_PARENT, Optional.empty());
        } else {
            dataTracker.set(LINKED_PARENT, Optional.of(new LazyEntityReference<>(cartLinker.asEntity())));
        }
    }
    @Override
    public void setLinkedChild(CartLinker cartLinker) {
        if(cartLinker == null){
            dataTracker.set(LINKED_CHILD, Optional.empty());
        } else {
            dataTracker.set(LINKED_CHILD, Optional.of(new LazyEntityReference<>(cartLinker.asEntity())));
        }    }


}
