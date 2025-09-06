package de.nexusrealms.eraofrails.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MinecartItem;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DispenserBlockEntity.class)
public abstract class DispenserBlockEntityMixin {
    @Shadow private DefaultedList<ItemStack> inventory;

    @ModifyReturnValue(method = "chooseNonEmptySlot", at = @At("RETURN"))
    public int chooseFirstSlotIfHasCart(int original){
        if(inventory.stream().anyMatch(itemStack -> itemStack.getItem() instanceof MinecartItem)){
            for (int i = 0; i < inventory.size(); i++) {
                if(!inventory.get(i).isEmpty()){
                    return i;
                }
            }
        }
        return original;
    }

}
