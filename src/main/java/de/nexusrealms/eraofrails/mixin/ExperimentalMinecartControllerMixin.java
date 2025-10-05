package de.nexusrealms.eraofrails.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import de.nexusrealms.eraofrails.block.RailwaysBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.ExperimentalMinecartController;
import net.minecraft.entity.vehicle.MinecartController;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperimentalMinecartController.class)
public abstract class ExperimentalMinecartControllerMixin extends MinecartController {

    protected ExperimentalMinecartControllerMixin(AbstractMinecartEntity minecart) {
        super(minecart);
    }

    @Inject(method = "moveOnRail", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
    public void handleCross(ServerWorld world, CallbackInfo ci, @Local BlockPos pos){
        BlockState blockState = world.getBlockState(pos);
        if(blockState.isIn(RailwaysBlocks.Tags.CROSS_RAIL)) {
            RailShape shape = blockState.get(Properties.STRAIGHT_RAIL_SHAPE);
            Direction direction = Direction.getFacing(getVelocity().getHorizontal());
            if(minecart.getVelocity().horizontalLength() > 0) {
                if(shape == RailShape.NORTH_SOUTH && (direction == Direction.EAST || direction == Direction.WEST)) {
                    getWorld().setBlockState(pos, blockState.with(Properties.STRAIGHT_RAIL_SHAPE, RailShape.EAST_WEST));
                } else if(shape == RailShape.EAST_WEST && (direction == Direction.NORTH || direction == Direction.SOUTH)) {
                    getWorld().setBlockState(pos, blockState.with(Properties.STRAIGHT_RAIL_SHAPE, RailShape.NORTH_SOUTH));
                }
            }
        }
    }
}
