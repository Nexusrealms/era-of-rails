package de.nexusrealms.eraofrails.block;

import com.mojang.serialization.MapCodec;
import de.nexusrealms.eraofrails.entity.types.InputMinecartEntity;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RailState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Predicate;

public class InputRailBlock extends BaseRailBlock {
    public static final MapCodec<InputRailBlock> CODEC = simpleCodec(InputRailBlock::new);
    public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty LOCKED = BooleanProperty.create("locked");

    private static final int SCHEDULED_TICK_DELAY = 20;

    public MapCodec<InputRailBlock> codec() {
        return CODEC;
    }

    public InputRailBlock(BlockBehaviour.Properties settings) {
        super(true, settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false).setValue(LOCKED, false).setValue(SHAPE, RailShape.NORTH_SOUTH).setValue(WATERLOGGED, false));
    }

    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if (!world.isClientSide()) {
            if (!(Boolean)state.getValue(POWERED)) {
                this.updatePoweredStatus(world, pos, state);
            }
        }
    }

    protected void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if(state.getValue(LOCKED)){
            if(getCarts(world, pos, AbstractMinecart.class, entity -> true).isEmpty()){
                BlockState state1 = state.setValue(LOCKED, false);
                world.setBlockAndUpdate(pos, state1);
                tick(state1, world, pos, random);
            } else {
                world.scheduleTick(pos, this, 20);
            }

        } else if (state.getValue(POWERED)) {
            this.updatePoweredStatus(world, pos, state);
        }
    }

    protected int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    protected int getDirectSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        if (!(Boolean)state.getValue(POWERED)) {
            return 0;
        } else {
            return direction == Direction.UP ? 15 : 0;
        }
    }

    private void updatePoweredStatus(Level world, BlockPos pos, BlockState state) {
        if (this.canSurvive(state, world, pos) && !state.getValue(LOCKED)) {
            boolean bl = state.getValue(POWERED);
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
                BlockState blockState = state.setValue(POWERED, true).setValue(LOCKED, true);
                world.setBlock(pos, blockState, 3);
                this.updateNearbyRails(world, pos, blockState, true);
                world.blockUpdated(pos, this);
                world.blockUpdated(pos.below(), this);
                world.setBlocksDirty(pos, state, blockState);
            } else if ((!bl2 && bl) || shouldLock) {
                BlockState blockState = state.setValue(POWERED, false).setValue(LOCKED, true);
                world.setBlock(pos, blockState, 3);
                this.updateNearbyRails(world, pos, blockState, false);
                world.blockUpdated(pos, this);
                world.blockUpdated(pos.below(), this);
                world.setBlocksDirty(pos, state, blockState);
            }

            world.scheduleTick(pos, this, 20);


            world.updateNeighbourForOutputSignal(pos, this);
        }
    }

    protected void updateNearbyRails(Level world, BlockPos pos, BlockState state, boolean unpowering) {
        RailState railPlacementHelper = new RailState(world, pos, state);

        for(BlockPos blockPos : railPlacementHelper.getConnections()) {
            BlockState blockState = world.getBlockState(blockPos);
            world.neighborChanged(blockState, blockPos, blockState.getBlock(), null, false);
        }

    }

    protected void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!oldState.is(state.getBlock())) {
            BlockState blockState = this.updateState(state, world, pos, notify);
            this.updatePoweredStatus(world, pos, blockState);
        }
    }

    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }


    private <T extends AbstractMinecart> List<T> getCarts(Level world, BlockPos pos, Class<T> entityClass, Predicate<Entity> entityPredicate) {
        return world.getEntitiesOfClass(entityClass, this.getCartDetectionBox(pos), entityPredicate);
    }

    private AABB getCartDetectionBox(BlockPos pos) {
        double d = 0.2;
        return new AABB((double)pos.getX() + 0.2, (double)pos.getY(), (double)pos.getZ() + 0.2, (double)(pos.getX() + 1) - 0.2, (double)(pos.getY() + 1) - 0.2, (double)(pos.getZ() + 1) - 0.2);
    }
/*
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        RailShape railShape = (RailShape)state.get(SHAPE);
        RailShape railShape2 = this.rotate(railShape, rotation);
        return (BlockState)state.with(SHAPE, railShape2);
    }

    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        RailShape railShape = (RailShape)state.get(SHAPE);
        RailShape railShape2 = this.mirrorShape(railShape, mirror);
        return (BlockState)state.with(SHAPE, railShape2);
    }
*/
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, POWERED, WATERLOGGED, LOCKED);
    }


}
