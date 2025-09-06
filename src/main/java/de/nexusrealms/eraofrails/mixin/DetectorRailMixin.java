package de.nexusrealms.eraofrails.mixin;

import de.nexusrealms.eraofrails.entity.ComparatorOutputtingMinecart;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.DetectorRailBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Predicate;

@Mixin(DetectorRailBlock.class)
public abstract class DetectorRailMixin extends AbstractRailBlock {
    @Shadow protected abstract <T extends AbstractMinecartEntity> List<T> getCarts(World world, BlockPos pos, Class<T> entityClass, Predicate<Entity> entityPredicate);

    protected DetectorRailMixin(boolean forbidCurves, Settings settings) {
        super(forbidCurves, settings);
    }

    @Inject(method = "getComparatorOutput", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/DetectorRailBlock;getCarts(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Ljava/lang/Class;Ljava/util/function/Predicate;)Ljava/util/List;", ordinal = 0), cancellable = true)
    public void addComparatorOutput(BlockState state, World world, BlockPos pos, CallbackInfoReturnable<Integer> cir){
        List<AbstractMinecartEntity> list = getCarts(world, pos, AbstractMinecartEntity.class, cart -> cart instanceof ComparatorOutputtingMinecart);
        if(!list.isEmpty() && list.get(0) instanceof ComparatorOutputtingMinecart comparatorOutputtingMinecart){
            cir.setReturnValue(comparatorOutputtingMinecart.getComparatorOutput());
        }
    }
}
