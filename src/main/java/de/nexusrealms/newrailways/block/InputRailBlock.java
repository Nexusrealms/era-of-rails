package de.nexusrealms.newrailways.block;

import com.mojang.serialization.MapCodec;
import de.nexusrealms.newrailways.entity.types.InputMinecartEntity;
import net.minecraft.block.*;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;

import java.util.List;
import java.util.function.Predicate;

public class InputRailBlock extends AbstractRailBlock {
    public static final MapCodec<InputRailBlock> CODEC = createCodec(InputRailBlock::new);
    public static final EnumProperty<RailShape> SHAPE = Properties.STRAIGHT_RAIL_SHAPE;
    public static final BooleanProperty POWERED = Properties.POWERED;
    public static final BooleanProperty LOCKED = BooleanProperty.of("locked");

    private static final int SCHEDULED_TICK_DELAY = 20;

    public MapCodec<InputRailBlock> getCodec() {
        return CODEC;
    }

    public InputRailBlock(AbstractBlock.Settings settings) {
        super(true, settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(POWERED, false).with(LOCKED, false).with(SHAPE, RailShape.NORTH_SOUTH).with(WATERLOGGED, false));
    }

    protected boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler) {
        if (!world.isClient) {
            if (!(Boolean)state.get(POWERED)) {
                this.updatePoweredStatus(world, pos, state);
            }
        }
    }

    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(state.get(LOCKED)){
            if(getCarts(world, pos, AbstractMinecartEntity.class, entity -> true).isEmpty()){
                BlockState state1 = state.with(LOCKED, false);
                world.setBlockState(pos, state1);
                scheduledTick(state1, world, pos, random);
            } else {
                world.scheduleBlockTick(pos, this, 20);
            }

        } else if (state.get(POWERED)) {
            this.updatePoweredStatus(world, pos, state);
        }
    }

    protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(POWERED) ? 15 : 0;
    }

    protected int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        if (!(Boolean)state.get(POWERED)) {
            return 0;
        } else {
            return direction == Direction.UP ? 15 : 0;
        }
    }

    private void updatePoweredStatus(World world, BlockPos pos, BlockState state) {
        if (this.canPlaceAt(state, world, pos) && !state.get(LOCKED)) {
            boolean bl = state.get(POWERED);
            boolean bl2 = false;
            boolean shouldLock = false;
            List<InputMinecartEntity> list = this.getCarts(world, pos, InputMinecartEntity.class, (entity) -> true);
            if (!list.isEmpty()) {
                shouldLock = true;
                if(list.getFirst().getNextInputAndMove()){
                    bl2 = true;
                }

            }

            if (bl2 && !bl) {
                BlockState blockState = state.with(POWERED, true).with(LOCKED, true);
                world.setBlockState(pos, blockState, 3);
                this.updateNearbyRails(world, pos, blockState, true);
                world.updateNeighbors(pos, this);
                world.updateNeighbors(pos.down(), this);
                world.scheduleBlockRerenderIfNeeded(pos, state, blockState);
            } else if ((!bl2 && bl) || shouldLock) {
                BlockState blockState = state.with(POWERED, false).with(LOCKED, true);
                world.setBlockState(pos, blockState, 3);
                this.updateNearbyRails(world, pos, blockState, false);
                world.updateNeighbors(pos, this);
                world.updateNeighbors(pos.down(), this);
                world.scheduleBlockRerenderIfNeeded(pos, state, blockState);
            }

            world.scheduleBlockTick(pos, this, 20);


            world.updateComparators(pos, this);
        }
    }

    protected void updateNearbyRails(World world, BlockPos pos, BlockState state, boolean unpowering) {
        RailPlacementHelper railPlacementHelper = new RailPlacementHelper(world, pos, state);

        for(BlockPos blockPos : railPlacementHelper.getNeighbors()) {
            BlockState blockState = world.getBlockState(blockPos);
            world.updateNeighbor(blockState, blockPos, blockState.getBlock(), (WireOrientation)null, false);
        }

    }

    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!oldState.isOf(state.getBlock())) {
            BlockState blockState = this.updateCurves(state, world, pos, notify);
            this.updatePoweredStatus(world, pos, blockState);
        }
    }

    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }


    private <T extends AbstractMinecartEntity> List<T> getCarts(World world, BlockPos pos, Class<T> entityClass, Predicate<Entity> entityPredicate) {
        return world.getEntitiesByClass(entityClass, this.getCartDetectionBox(pos), entityPredicate);
    }

    private Box getCartDetectionBox(BlockPos pos) {
        double d = 0.2;
        return new Box((double)pos.getX() + 0.2, (double)pos.getY(), (double)pos.getZ() + 0.2, (double)(pos.getX() + 1) - 0.2, (double)(pos.getY() + 1) - 0.2, (double)(pos.getZ() + 1) - 0.2);
    }

    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        RailShape railShape = (RailShape)state.get(SHAPE);
        RailShape railShape2 = this.rotateShape(railShape, rotation);
        return (BlockState)state.with(SHAPE, railShape2);
    }

    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        RailShape railShape = (RailShape)state.get(SHAPE);
        RailShape railShape2 = this.mirrorShape(railShape, mirror);
        return (BlockState)state.with(SHAPE, railShape2);
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, POWERED, WATERLOGGED, LOCKED);
    }


}
