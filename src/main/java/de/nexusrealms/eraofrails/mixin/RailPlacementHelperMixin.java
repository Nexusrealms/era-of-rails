package de.nexusrealms.eraofrails.mixin;

import de.nexusrealms.eraofrails.block.SwitchRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.RailPlacementHelper;
import net.minecraft.block.enums.RailShape;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(RailPlacementHelper.class)
public abstract class RailPlacementHelperMixin {
    @Shadow private BlockState state;

    @Shadow @Final private List<BlockPos> neighbors;

    @Shadow @Final private BlockPos pos;

    @Inject(method = "computeNeighbors", at = @At(value = "INVOKE", target = "Ljava/util/List;clear()V", shift = At.Shift.AFTER), cancellable = true)
    public void setSwitchNeighbors(RailShape shape, CallbackInfo ci){
        if(state.getBlock() instanceof SwitchRailBlock switchRailBlock){
            neighbors.addAll(switchRailBlock.getNeighbors(pos, state));
        }
    }
}
