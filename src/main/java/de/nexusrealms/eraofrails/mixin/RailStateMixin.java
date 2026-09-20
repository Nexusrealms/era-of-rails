package de.nexusrealms.eraofrails.mixin;

import de.nexusrealms.eraofrails.block.SwitchRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.RailState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(RailState.class)
public abstract class RailStateMixin {
    @Shadow private BlockState state;

    @Shadow @Final private List<BlockPos> connections;

    @Shadow @Final private BlockPos pos;

    @Inject(method = "updateConnections", at = @At(value = "INVOKE", target = "Ljava/util/List;clear()V", shift = At.Shift.AFTER), cancellable = true)
    public void setSwitchNeighbors(RailShape shape, CallbackInfo ci){
        if(state.getBlock() instanceof SwitchRailBlock switchRailBlock){
            connections.addAll(switchRailBlock.getNeighbors(pos, state));
        }
    }
}
