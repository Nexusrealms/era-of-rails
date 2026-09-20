package de.nexusrealms.eraofrails.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import de.nexusrealms.eraofrails.block.RailwaysBlocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.entity.vehicle.ExperimentalMinecartController;
import net.minecraft.entity.vehicle.MinecartController;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// TODO(Ravel): can not resolve target class ExperimentalMinecartController
// TODO(Ravel): can not resolve target class ExperimentalMinecartController
@Mixin(ExperimentalMinecartController.class)
public abstract class ExperimentalMinecartControllerMixin extends MinecartController {

    protected ExperimentalMinecartControllerMixin(AbstractMinecart minecart) {
        super(minecart);
    }

    // TODO(Ravel): no target class
// TODO(Ravel): no target class
    @Inject(method = "moveOnRail", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    public void handleCross(ServerLevel world, CallbackInfo ci, @Local BlockPos pos){
        BlockState blockState = world.getBlockState(pos);
        if(blockState.is(RailwaysBlocks.Tags.CROSS_RAIL)) {
            RailShape shape = blockState.getValue(BlockStateProperties.RAIL_SHAPE_STRAIGHT);
            Direction direction = Direction.getNearest(getVelocity().getHorizontal());
            if(minecart.getVelocity().horizontalLength() > 0) {
                if(shape == RailShape.NORTH_SOUTH && (direction == Direction.EAST || direction == Direction.WEST)) {
                    getWorld().setBlockState(pos, blockState.setValue(BlockStateProperties.RAIL_SHAPE_STRAIGHT, RailShape.EAST_WEST));
                } else if(shape == RailShape.EAST_WEST && (direction == Direction.NORTH || direction == Direction.SOUTH)) {
                    getWorld().setBlockState(pos, blockState.setValue(BlockStateProperties.RAIL_SHAPE_STRAIGHT, RailShape.NORTH_SOUTH));
                }
            }
        }
    }
}
