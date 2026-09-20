package de.nexusrealms.eraofrails.entity;

import de.nexusrealms.eraofrails.block.RailwaysBlocks;
import de.nexusrealms.eraofrails.block.HaltRailBlock;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.entity.vehicle.ExperimentalMinecartController;
import net.minecraft.world.item.Items;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class RailwaysMinecartController extends ExperimentalMinecartController implements CartLinker{


    public RailwaysMinecartController(AbstractMinecart abstractMinecartEntity) {
        super(abstractMinecartEntity);
    }
    Vec3 prevVel = Vec3.ZERO;

    @Override
    public void tick() {
        Level blockPos = this.getWorld();
        if (blockPos instanceof ServerLevel serverWorld) {
            if(getLinkedParent().isPresent()) {
                AbstractMinecart parent = getLinkedParent().get().asEntity();
                double distance = parent.distanceTo(this.minecart) - 1;

                if(distance <= 6) {
                    Vec3 towardsParent = parent.getEntityPos().subtract(getPos()).normalize();

                    if(distance > 0.3) {
                        Vec3 parentVelocity = parent.getDeltaMovement();

                        if(parentVelocity.length() == 0) {
                            setVelocity(towardsParent.scale(0.1));
                        }
                        else {
                            setVelocity(towardsParent.scale(parentVelocity.length()).scale(distance * (distance) > 5 ? 3 : 1));
                        }
                    }
                    else if(distance < 0.2) {
                        setVelocity(towardsParent.scale(-0.1));
                    }
                    else {
                        setVelocity(Vec3.ZERO);
                    }
                }
                else {
                    if(getWorld() instanceof ServerLevel world){
                        minecart.dropItem(world, Items.IRON_CHAIN);
                    }
                    parent.setLinkedChild(null);
                    setLinkedParent(null);
                }
            }
            {
                BlockPos pos = this.minecart.getRailOrMinecartPos();
                BlockState blockState = this.getWorld().getBlockState(pos);
                if(blockState.is(RailwaysBlocks.Tags.CROSS_RAIL)) {
                    RailShape shape = blockState.getValue(BlockStateProperties.RAIL_SHAPE_STRAIGHT);
                    Direction direction = Direction.getNearest(getVelocity().getHorizontal());
                    if(getVelocity().horizontalLength() > 0) {
                        if(shape == RailShape.NORTH_SOUTH && (direction == Direction.EAST || direction == Direction.WEST)) {
                            getWorld().setBlockState(pos, blockState.setValue(BlockStateProperties.RAIL_SHAPE_STRAIGHT, RailShape.EAST_WEST));
                        } else if(shape == RailShape.EAST_WEST && (direction == Direction.NORTH || direction == Direction.SOUTH)) {
                            getWorld().setBlockState(pos, blockState.setValue(BlockStateProperties.RAIL_SHAPE_STRAIGHT, RailShape.NORTH_SOUTH));
                        }
                    }
                }
                prevVel = getVelocity();
                if (this.minecart.isFirstUpdate()) {
                    this.minecart.setOnRail(BaseRailBlock.isRail(blockState));
                    this.adjustToRail(pos, blockState, true);
                }

                this.minecart.applyGravity();
                this.minecart.moveOnRail(serverWorld);
            }
        } else {
            this.tickClient();
            boolean bl = BaseRailBlock.isRail(this.getWorld().getBlockState(this.minecart.getRailOrMinecartPos()));
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
    public double getMaxSpeed(ServerLevel world) {
        double d = super.getMaxSpeed(world);
        return d * (isMinecartOnPoweredCopperRail(minecart) ? 4 : 1);
    }

    @Override
    public double getSpeedRetention() {
        return isMinecartOnPoweredCopperRail(minecart) ? 1 : super.getSpeedRetention();
    }
    public static boolean isMinecartOnPoweredCopperRail(AbstractMinecart minecart){
        BlockPos blockPos = minecart.getRailOrMinecartPos();
        BlockState blockState = minecart.getCommandSenderWorld().getBlockState(blockPos);
        return blockState.is(RailwaysBlocks.Tags.POWERED_HIGH_SPEED_RAIL) && blockState.getValue(BlockStateProperties.POWERED);
    }
    protected Vec3 decelerateFromPoweredRail(Vec3 velocity, BlockState railState) {
        if ((railState.is(Blocks.POWERED_RAIL) || railState.is(RailwaysBlocks.Tags.POWERED_HIGH_SPEED_RAIL) || railState.is(RailwaysBlocks.HALT_RAIL)) && !railState.getValue(PoweredRailBlock.POWERED) && getLinkedParent().isEmpty()) {
            return velocity.length() < 0.03 ? Vec3.ZERO : velocity.scale(0.5);
        } else {
            return velocity;
        }
    }
    @Override
    protected Vec3 accelerateFromPoweredRail(Vec3 velocity, BlockPos railPos, BlockState railState) {
        if ((railState.is(Blocks.POWERED_RAIL) || railState.is(RailwaysBlocks.Tags.POWERED_HIGH_SPEED_RAIL)) && railState.getValue(PoweredRailBlock.POWERED)) {
            if (velocity.length() > 0.01) {
                return velocity.normalize().scale(velocity.length() + 0.06);
            } else {
                Vec3 vec3d = this.minecart.getLaunchDirection(railPos);
                return vec3d.lengthSqr() <= 0.0 ? velocity : vec3d.scale(velocity.length() + 0.2);
            }
        } else if(railState.is(RailwaysBlocks.HALT_RAIL) && railState.getValue(PoweredRailBlock.POWERED)) {
            if(velocity.length() < 0.01) {
                Vec3 vec3d = HaltRailBlock.getFacingDirection(railState).getDoubleVector();
                return vec3d.lengthSqr() <= 0.0 ? velocity : vec3d.scale(velocity.length() + 0.2);
            }
        }
        return velocity;
    }
    public static Direction.Axis shapeToAxis(RailShape shape){
        if(shape == RailShape.ASCENDING_EAST || shape == RailShape.ASCENDING_WEST || shape == RailShape.EAST_WEST){
            return Direction.Axis.X;
        }
        return Direction.Axis.Z;
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
    public AbstractMinecart asEntity() {
        return minecart;
    }
}
