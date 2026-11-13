package de.nexusrealms.eraofrails.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.notunanancyowen.minecart.MinecartController;
import com.notunanancyowen.minecart.dataholders.ImprovedMinecart;
import de.nexusrealms.eraofrails.block.HaltRailBlock;
import de.nexusrealms.eraofrails.block.RailwaysBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecartController.class)
public abstract class MinecartControllerMixin<T extends AbstractMinecart & ImprovedMinecart> {
    @Shadow
    public T minecart;

    @Shadow
    public abstract Level getWorld();

    @Shadow
    public abstract Vec3 getVelocity();

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/notunanancyowen/minecart/dataholders/ImprovedMinecart;isFirstUpdate()Z"))
    public void runCross(CallbackInfo ci){
        BlockPos pos = this.minecart.getRailOrMinecartPos();
        BlockState blockState = getWorld().getBlockState(pos);
        if(blockState.is(RailwaysBlocks.Tags.CROSS_RAIL)) {
            RailShape shape = blockState.getValue(BlockStateProperties.RAIL_SHAPE_STRAIGHT);
            Direction direction = Direction.getNearest(getVelocity());
            if(getVelocity().horizontalDistance() > 0) {
                if(shape == RailShape.NORTH_SOUTH && (direction == Direction.EAST || direction == Direction.WEST)) {
                    getWorld().setBlockAndUpdate(pos, blockState.setValue(BlockStateProperties.RAIL_SHAPE_STRAIGHT, RailShape.EAST_WEST));
                } else if(shape == RailShape.EAST_WEST && (direction == Direction.NORTH || direction == Direction.SOUTH)) {
                    getWorld().setBlockAndUpdate(pos, blockState.setValue(BlockStateProperties.RAIL_SHAPE_STRAIGHT, RailShape.NORTH_SOUTH));
                }
            }
        }
    }

    @Inject(method = "moveOnRail", at = @At(value = "INVOKE", target="Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    public void handleCross(ServerLevel world, CallbackInfo ci, @Local(name = "blockPos") BlockPos pos){
        BlockState blockState = world.getBlockState(pos);
        if(blockState.is(RailwaysBlocks.Tags.CROSS_RAIL)) {
            RailShape shape = blockState.getValue(BlockStateProperties.RAIL_SHAPE_STRAIGHT);
            Direction direction = Direction.getNearest(getVelocity());
            if(getVelocity().horizontalDistance() > 0) {
                if(shape == RailShape.NORTH_SOUTH && (direction == Direction.EAST || direction == Direction.WEST)) {
                    getWorld().setBlockAndUpdate(pos, blockState.setValue(BlockStateProperties.RAIL_SHAPE_STRAIGHT, RailShape.EAST_WEST));
                } else if(shape == RailShape.EAST_WEST && (direction == Direction.NORTH || direction == Direction.SOUTH)) {
                    getWorld().setBlockAndUpdate(pos, blockState.setValue(BlockStateProperties.RAIL_SHAPE_STRAIGHT, RailShape.NORTH_SOUTH));
                }
            }
        }
    }
    @ModifyReturnValue(method = "getMaxSpeed", at = @At("RETURN"))
    public double modifyMaxSpeed(double original){
        return original * (isMinecartOnPoweredCopperRail(minecart) ? 4 : 1);
    }
    @ModifyReturnValue(method = "getSpeedRetention", at = @At("RETURN"))
    public double modifySpeedRetention(double original){
        return isMinecartOnPoweredCopperRail(minecart) ? 1 : original;
    }
    @ModifyReturnValue(method = "accelerateFromPoweredRail", at = @At("RETURN"))
    public Vec3 accelerateThings(Vec3 original, Vec3 velocity, BlockPos railPos, BlockState railState){
        if (railState.is(RailwaysBlocks.Tags.POWERED_HIGH_SPEED_RAIL) && railState.getValue(PoweredRailBlock.POWERED)) {
            if (velocity.length() > 0.01) {
                return velocity.normalize().scale(velocity.length() + 0.06);
            } else {
                Vec3 vec3d = this.minecart.getLaunchDirection(railPos);
                return vec3d.lengthSqr() <= 0.0 ? velocity : vec3d.scale(velocity.length() + 0.2);
            }
        } else if(railState.is(RailwaysBlocks.HALT_RAIL) && railState.getValue(PoweredRailBlock.POWERED)) {
            if(velocity.length() < 0.01) {
                Vec3 vec3d = new Vec3(HaltRailBlock.getFacingDirection(railState).step());
                return vec3d.lengthSqr() <= 0.0 ? velocity : vec3d.scale(velocity.length() + 0.2);
            }
        }
        return original;
    }
    @ModifyReturnValue(method = "decelerateFromPoweredRail", at = @At("RETURN"))
    protected Vec3 decelerateFromPoweredRail(Vec3 original, Vec3 velocity, BlockState railState) {
        if ((railState.is(RailwaysBlocks.Tags.POWERED_HIGH_SPEED_RAIL) || railState.is(RailwaysBlocks.HALT_RAIL)) && !railState.getValue(PoweredRailBlock.POWERED)) {
            return velocity.length() < 0.03 ? Vec3.ZERO : velocity.scale(0.5);
        } else {
            return original;
        }
    }
    @Unique
    private static <T extends AbstractMinecart & ImprovedMinecart> boolean isMinecartOnPoweredCopperRail(T minecart){
        BlockPos blockPos = minecart.getRailOrMinecartPos();
        BlockState blockState = minecart.level().getBlockState(blockPos);
        return blockState.is(RailwaysBlocks.Tags.POWERED_HIGH_SPEED_RAIL) && blockState.getValue(BlockStateProperties.POWERED);
    }
}
