package de.nexusrealms.eraofrails.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.core.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DispenserBlockEntity.class)
public abstract class DispenserBlockEntityMixin {
    @Shadow private NonNullList<ItemStack> items;

    @ModifyReturnValue(method = "getRandomSlot", at = @At("RETURN"))
    public int chooseFirstSlotIfHasCart(int original){
        if(items.stream().anyMatch(itemStack -> itemStack.getItem() instanceof MinecartItem)){
            for (int i = 0; i < items.size(); i++) {
                if(!items.get(i).isEmpty()){
                    return i;
                }
            }
        }
        return original;
    }

}
