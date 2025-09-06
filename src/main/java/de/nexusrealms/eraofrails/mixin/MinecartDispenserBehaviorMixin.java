package de.nexusrealms.eraofrails.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import de.nexusrealms.eraofrails.item.RailwaysItems;
import net.minecraft.block.dispenser.MinecartDispenserBehavior;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(MinecartDispenserBehavior.class)
public abstract class MinecartDispenserBehaviorMixin {
    @Inject(method = "dispenseSilently", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V"))
    public void dispenseAndTryLink(BlockPointer pointer, ItemStack stack, CallbackInfoReturnable<ItemStack> cir, @Local AbstractMinecartEntity minecart){
        Optional<AbstractMinecartEntity> maybeParent = RailwaysItems.hasCartAndChain(pointer.pos());
        if(maybeParent.isPresent()){
            maybeParent.get().setLinkedChild(minecart);
            minecart.setLinkedParent(maybeParent.get());
            RailwaysItems.clearAt(pointer.pos());
        }
        RailwaysItems.addDispensedPositionedCart(pointer.pos(), minecart);
    }
}
