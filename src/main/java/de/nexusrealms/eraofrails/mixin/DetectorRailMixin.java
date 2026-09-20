package de.nexusrealms.eraofrails.mixin;

import de.nexusrealms.eraofrails.entity.ComparatorOutputtingMinecart;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.DetectorRailBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Predicate;

@Mixin(DetectorRailBlock.class)
public abstract class DetectorRailMixin extends BaseRailBlock {
    @Shadow protected abstract <T extends AbstractMinecart> List<T> getInteractingMinecartOfType(Level world, BlockPos pos, Class<T> entityClass, Predicate<Entity> entityPredicate);

    protected DetectorRailMixin(boolean forbidCurves, Properties settings) {
        super(forbidCurves, settings);
    }

    @Inject(method = "getAnalogOutputSignal", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/DetectorRailBlock;getInteractingMinecartOfType(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Ljava/lang/Class;Ljava/util/function/Predicate;)Ljava/util/List;", ordinal = 0), cancellable = true)
    public void addComparatorOutput(BlockState state, Level world, BlockPos pos, Direction direction, CallbackInfoReturnable<Integer> cir){
        List<AbstractMinecart> list = getInteractingMinecartOfType(world, pos, AbstractMinecart.class, cart -> cart instanceof ComparatorOutputtingMinecart);
        if(!list.isEmpty() && list.get(0) instanceof ComparatorOutputtingMinecart comparatorOutputtingMinecart){
            cir.setReturnValue(comparatorOutputtingMinecart.getComparatorOutput());
        }
    }
}
