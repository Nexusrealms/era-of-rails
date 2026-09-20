package de.nexusrealms.eraofrails.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import de.nexusrealms.eraofrails.item.RailwaysItems;
import net.minecraft.block.dispenser.MinecartDispenserBehavior;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.dispenser.BlockSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

// TODO(Ravel): can not resolve target class MinecartDispenserBehavior
// TODO(Ravel): can not resolve target class MinecartDispenserBehavior
@Mixin(MinecartDispenserBehavior.class)
public abstract class MinecartDispenserBehaviorMixin {
    // TODO(Ravel): no target class
// TODO(Ravel): no target class
    @Inject(method = "dispenseSilently", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"))
    public void dispenseAndTryLink(BlockSource pointer, ItemStack stack, CallbackInfoReturnable<ItemStack> cir, @Local AbstractMinecart minecart){
        Optional<AbstractMinecart> maybeParent = RailwaysItems.hasCartAndChain(pointer.pos());
        if(maybeParent.isPresent()){
            maybeParent.get().setLinkedChild(minecart);
            minecart.setLinkedParent(maybeParent.get());
            RailwaysItems.clearAt(pointer.pos());
        }
        RailwaysItems.addDispensedPositionedCart(pointer.pos(), minecart);
    }
}
