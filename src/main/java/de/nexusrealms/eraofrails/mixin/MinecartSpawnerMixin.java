package de.nexusrealms.eraofrails.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import de.nexusrealms.eraofrails.item.RailwaysItems;
import net.minecraft.world.entity.vehicle.MinecartSpawner;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MinecartSpawner.class)
public abstract class MinecartSpawnerMixin {
    @ModifyReturnValue(method = "getPickBlockStack", at = @At("TAIL"))
    public ItemStack getProperPickStack(ItemStack original){
        return new ItemStack(RailwaysItems.SPAWNER_MINECART);
    }
    @ModifyReturnValue(method = "getDropItem", at = @At("TAIL"))
    public Item getProperPickStack(Item original){
        return RailwaysItems.SPAWNER_MINECART;
    }
}
