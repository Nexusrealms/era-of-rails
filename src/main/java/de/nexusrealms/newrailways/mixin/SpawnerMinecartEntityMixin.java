package de.nexusrealms.newrailways.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import de.nexusrealms.newrailways.item.RailwaysItems;
import net.minecraft.entity.vehicle.SpawnerMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SpawnerMinecartEntity.class)
public abstract class SpawnerMinecartEntityMixin {
    @ModifyReturnValue(method = "getPickBlockStack", at = @At("TAIL"))
    public ItemStack getProperPickStack(ItemStack original){
        return new ItemStack(RailwaysItems.SPAWNER_MINECART);
    }
    @ModifyReturnValue(method = "asItem", at = @At("TAIL"))
    public Item getProperPickStack(Item original){
        return RailwaysItems.SPAWNER_MINECART;
    }
}
