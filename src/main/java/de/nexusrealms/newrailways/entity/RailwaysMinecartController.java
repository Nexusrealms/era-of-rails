package de.nexusrealms.newrailways.entity;

import de.nexusrealms.newrailways.block.RailwaysBlocks;
import net.minecraft.block.AbstractRailBlock;
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
import net.minecraft.world.World;

import java.util.Optional;

public class RailwaysMinecartController extends ExperimentalMinecartController implements CartLinker{


    public RailwaysMinecartController(AbstractMinecartEntity abstractMinecartEntity) {
        super(abstractMinecartEntity);
    }


    @Override
    public void tick() {
        World blockPos = this.getWorld();
        if (blockPos instanceof ServerWorld serverWorld) {
            if(getLinkedParent().isPresent()) {
                AbstractMinecartEntity parent = getLinkedParent().get().asEntity();
                double distance = parent.distanceTo(this.minecart) - 1;

                if(distance <= 6) {
                    Vec3d towardsParent = parent.getPos().subtract(getPos()).normalize();

                    if(distance > 1) {
                        Vec3d parentVelocity = parent.getVelocity();

                        if(parentVelocity.length() == 0) {
                            setVelocity(towardsParent.multiply(0.1));
                        }
                        else {
                            setVelocity(towardsParent.multiply(parentVelocity.length()).multiply(distance * (distance) > 4 ? 3 : 1));
                        }
                    }
                    else if(distance < 0.5) {
                        setVelocity(towardsParent.multiply(-0.1));
                    }
                    else {
                        setVelocity(Vec3d.ZERO);
                    }
                }
                else {
                    parent.setLinkedChild(null);
                    setLinkedParent(null);
                }
            }
            {
                BlockPos var5 = this.minecart.getRailOrMinecartPos();
                BlockState blockState = this.getWorld().getBlockState(var5);
                if (this.minecart.isFirstUpdate()) {
                    this.minecart.setOnRail(AbstractRailBlock.isRail(blockState));
                    this.adjustToRail(var5, blockState, true);
                }

                this.minecart.applyGravity();
                this.minecart.moveOnRail(serverWorld);
            }
        } else {
            this.tickClient();
            boolean bl = AbstractRailBlock.isRail(this.getWorld().getBlockState(this.minecart.getRailOrMinecartPos()));
            this.minecart.setOnRail(bl);
        }

    }

    /*@Override
    public boolean pushAwayFromEntities(Box box) {
        if(getLinkedParent().isPresent()){
            return false;
        }
        return super.pushAwayFromEntities(box);
    }*/

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

    @Override
    public Optional<CartLinker> getLinkedParent() {
        return minecart.getLinkedParent();
    }

    @Override
    public Optional<CartLinker> getLinkedChild() {
        return minecart.getLinkedChild();
    }

    @Override
    public void setLinkedParent(CartLinker cartLinker) {
        minecart.setLinkedParent(cartLinker);

    }

    @Override
    public void setLinkedChild(CartLinker cartLinker) {
        minecart.setLinkedChild(cartLinker);
    }

    @Override
    public AbstractMinecartEntity asEntity() {
        return minecart;
    }
}
