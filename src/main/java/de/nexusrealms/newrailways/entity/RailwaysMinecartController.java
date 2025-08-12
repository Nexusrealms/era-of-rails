package de.nexusrealms.newrailways.entity;

import de.nexusrealms.newrailways.block.RailwaysBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.ExperimentalMinecartController;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class RailwaysMinecartController extends ExperimentalMinecartController {


    public RailwaysMinecartController(AbstractMinecartEntity abstractMinecartEntity) {
        super(abstractMinecartEntity);
    }



    @Override
    public double getMaxSpeed(ServerWorld world) {
        double d = super.getMaxSpeed(world);
        return d * (isMinecartOnPoweredCopperRail(minecart) ? 4 : 1);
    }

    @Override
    public double getSpeedRetention() {
        return isMinecartOnPoweredCopperRail(minecart) ? 1 : super.getSpeedRetention();
    }
    public static boolean isMinecartOnPoweredCopperRail(AbstractMinecartEntity minecart){
        BlockPos blockPos = minecart.getRailOrMinecartPos();
        BlockState blockState = minecart.getWorld().getBlockState(blockPos);
        return blockState.isIn(RailwaysBlocks.Tags.POWERED_HIGH_SPEED_RAIL) && blockState.get(Properties.POWERED);
    }

    @Override
    protected Vec3d accelerateFromPoweredRail(Vec3d velocity, BlockPos railPos, BlockState railState) {
        if ((railState.isOf(Blocks.POWERED_RAIL) || railState.isIn(RailwaysBlocks.Tags.POWERED_HIGH_SPEED_RAIL)) && (Boolean)railState.get(PoweredRailBlock.POWERED)) {
            if (velocity.length() > 0.01) {
                return velocity.normalize().multiply(velocity.length() + 0.06);
            } else {
                Vec3d vec3d = this.minecart.getLaunchDirection(railPos);
                return vec3d.lengthSquared() <= 0.0 ? velocity : vec3d.multiply(velocity.length() + 0.2);
            }
        } else {
            return velocity;
        }
    }
}
