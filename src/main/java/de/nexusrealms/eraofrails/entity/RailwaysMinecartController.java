package de.nexusrealms.eraofrails.entity;

import de.nexusrealms.eraofrails.EraOfRails;
import de.nexusrealms.eraofrails.block.RailwaysBlocks;
import de.nexusrealms.eraofrails.block.HaltRailBlock;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.ExperimentalMinecartController;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.time.chrono.Era;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class RailwaysMinecartController extends ExperimentalMinecartController implements CartLinker{
    private final Queue<Vec3d> cornerPoints = new LinkedList<>();
    private Vec3d followedCorner = null;
    private BlockPos lastPushedPos = null;

    public RailwaysMinecartController(AbstractMinecartEntity abstractMinecartEntity) {
        super(abstractMinecartEntity);
    }

    @Override
    public void pushCornerPoint(Vec3d point) {
        cornerPoints.add(point);
    }

    @Override
    public void tick() {
        World blockPos = this.getWorld();
        if (blockPos instanceof ServerWorld serverWorld) {
            if(getLinkedParent().isPresent()) {
                AbstractMinecartEntity parent = getLinkedParent().get().asEntity();
                double distance = parent.distanceTo(this.minecart) - 1;
                Vec3d trackedPos = parent.getPos();
                if(followedCorner == null && !cornerPoints.isEmpty()){
                    followedCorner = cornerPoints.poll();
                }
                if(followedCorner != null && BlockPos.ofFloored(followedCorner).equals(minecart.getRailOrMinecartPos())){
                    followedCorner = null;
                }
                if(followedCorner != null) {
                    trackedPos = followedCorner;
                    EraOfRails.LOGGER.info("Tracking pos {}", followedCorner);
                }
                if(distance <= 12) {
                    Vec3d towardsParent = trackedPos.subtract(getPos()).normalize();

                    if(distance > 1 || followedCorner != null) {
                        Vec3d parentVelocity = parent.getVelocity();

                        if(parentVelocity.length() == 0) {
                            setVelocity(towardsParent.multiply(0.4));
                        }
                        else {
                            setVelocity(towardsParent.multiply(parentVelocity.length()).multiply(distance * (distance) > 5 ? 1.5 : 1));
                        }
                    }
                    else if(distance < 0.5 && followedCorner == null) {
                        setVelocity(towardsParent.multiply(-0.1));
                    }
                    else {
                        setVelocity(Vec3d.ZERO);
                    }
                }
                else {
                    if(getWorld() instanceof ServerWorld world){
                        minecart.dropItem(world, Items.CHAIN);
                    }
                    parent.setLinkedChild(null);
                    setLinkedParent(null);
                }
            }
            else {
                followedCorner = null;
                cornerPoints.clear();
            }
            {
                BlockPos pos = this.minecart.getRailOrMinecartPos();
                BlockState blockState = this.getWorld().getBlockState(pos);
                if (this.minecart.isFirstUpdate()) {
                    this.minecart.setOnRail(AbstractRailBlock.isRail(blockState));
                    this.adjustToRail(pos, blockState, true);
                }

                this.minecart.applyGravity();
                this.minecart.moveOnRail(serverWorld);
                getLinkedChild().ifPresent(cartLinker -> {
                    if(AbstractRailBlock.isRail(blockState) && !minecart.getRailOrMinecartPos().equals(lastPushedPos)){
                        if(blockState.contains(Properties.RAIL_SHAPE)){
                            if(isCurved(blockState.get(Properties.RAIL_SHAPE))){
                                lastPushedPos = minecart.getRailOrMinecartPos();
                                  cartLinker.pushCornerPoint(minecart.getRailOrMinecartPos().toBottomCenterPos());
                            }
                        }
                    }
                });

            }
        } else {
            this.tickClient();
            boolean bl = AbstractRailBlock.isRail(this.getWorld().getBlockState(this.minecart.getRailOrMinecartPos()));
            this.minecart.setOnRail(bl);
        }

    }
    private static boolean isCurved(RailShape shape){
        return shape == RailShape.NORTH_EAST || shape == RailShape.NORTH_WEST || shape == RailShape.SOUTH_EAST || shape == RailShape.SOUTH_WEST;
    }


    @Override
    public double getMaxSpeed(ServerWorld world) {
        double d = super.getMaxSpeed(world);
        return d * (isMinecartOnPoweredCopperRail(minecart) ? 4 : 4);
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
    protected Vec3d decelerateFromPoweredRail(Vec3d velocity, BlockState railState) {
        if ((railState.isOf(Blocks.POWERED_RAIL) || railState.isIn(RailwaysBlocks.Tags.POWERED_HIGH_SPEED_RAIL) || railState.isOf(RailwaysBlocks.HALT_RAIL)) && !railState.get(PoweredRailBlock.POWERED) && getLinkedParent().isEmpty()) {
            return velocity.length() < 0.03 ? Vec3d.ZERO : velocity.multiply(0.5);
        } else {
            return velocity;
        }
    }
    @Override
    protected Vec3d accelerateFromPoweredRail(Vec3d velocity, BlockPos railPos, BlockState railState) {
        if ((railState.isOf(Blocks.POWERED_RAIL) || railState.isIn(RailwaysBlocks.Tags.POWERED_HIGH_SPEED_RAIL)) && railState.get(PoweredRailBlock.POWERED)) {
            if (velocity.length() > 0.01) {
                return velocity.normalize().multiply(velocity.length() + 0.06);
            } else {
                Vec3d vec3d = this.minecart.getLaunchDirection(railPos);
                return vec3d.lengthSquared() <= 0.0 ? velocity : vec3d.multiply(velocity.length() + 0.2);
            }
        } else if(railState.isOf(RailwaysBlocks.HALT_RAIL) && railState.get(PoweredRailBlock.POWERED)) {
            if(velocity.length() < 0.01) {
                Direction.Axis axis = shapeToAxis(railState.get(Properties.STRAIGHT_RAIL_SHAPE));
                Vec3d vec3d = (railState.get(HaltRailBlock.AXIAL) ? axis.getPositiveDirection() : axis.getNegativeDirection()).getDoubleVector();
                return vec3d.lengthSquared() <= 0.0 ? velocity : vec3d.multiply(velocity.length() + 0.2);
            }
        }
        return velocity;
    }
    private static Direction.Axis shapeToAxis(RailShape shape){
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
    public AbstractMinecartEntity asEntity() {
        return minecart;
    }
}
