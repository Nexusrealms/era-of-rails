package de.nexusrealms.eraofrails.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class SwitchRailBlock extends BaseRailBlock {
    public static final MapCodec<SwitchRailBlock> CODEC = simpleCodec(SwitchRailBlock::new);

    public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE;

    public static final EnumProperty<Direction> ORIENTATION = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty RIGHT_HANDED = BooleanProperty.create("right_handed");
    public static final BooleanProperty SWITCHED = BooleanProperty.create("switched");

    protected SwitchRailBlock(Properties settings) {
        super(true, settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(SHAPE, RailShape.NORTH_SOUTH).setValue(SWITCHED, false).setValue(WATERLOGGED, false).setValue(RIGHT_HANDED, false).setValue(ORIENTATION, Direction.NORTH));
    }
    public List<BlockPos> getNeighbors(BlockPos pos, BlockState state){
        List<BlockPos> list = new ArrayList<>();
        Direction direction = state.getValue(ORIENTATION);
        list.add(pos.relative(direction));
        list.add(pos.relative(direction.getOpposite()));
        list.add(pos.relative(state.getValue(RIGHT_HANDED) ? direction.getClockWise() : direction.getCounterClockWise()));
        return list;
    }
    @Override
    protected MapCodec<? extends BaseRailBlock> codec() {
        return CODEC;
    }

    @Override
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    @Override
    protected void updateState(BlockState state, Level world, BlockPos pos, Block neighbor) {
        boolean bl = state.getValue(SWITCHED);
        boolean bl2 = world.hasNeighborSignal(pos);
        if (bl2 != bl) {
            state = state.setValue(SWITCHED, bl2);
        }
        world.setBlock(pos, state.setValue(SHAPE, getShapeFromState(state)), Block.UPDATE_ALL);
    }

    protected BlockState updateDir(Level world, BlockPos pos, BlockState state, boolean forceUpdate) {
        return state.setValue(SHAPE, getShapeFromState(state));
    }
    protected RailShape getShapeFromState(BlockState state){
        boolean switched = state.getValue(SWITCHED);
        boolean rightHanded = state.getValue(RIGHT_HANDED);
        Direction direction = state.getValue(ORIENTATION);
        if(!switched){
            return direction.getAxis() == Direction.Axis.X ? RailShape.EAST_WEST : RailShape.NORTH_SOUTH;
        }
        return switch (direction){
            case WEST -> rightHanded ? RailShape.NORTH_EAST : RailShape.SOUTH_EAST;
            case EAST -> rightHanded ? RailShape.SOUTH_WEST : RailShape.NORTH_WEST;
            case NORTH -> rightHanded ? RailShape.SOUTH_EAST : RailShape.SOUTH_WEST;
            default -> rightHanded ? RailShape.NORTH_WEST : RailShape.NORTH_EAST;
        };
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, SWITCHED, RIGHT_HANDED, ORIENTATION, SHAPE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction direction = ctx.getHorizontalDirection();
        float yaw = ctx.getRotation();
        boolean rightHanded = switch (direction) {
            case SOUTH -> yaw > 0;
            case EAST -> yaw > -90;
            case NORTH -> yaw < 0;
            default -> yaw > 90;
        };
        BlockState state = defaultBlockState().setValue(ORIENTATION, direction).setValue(RIGHT_HANDED, rightHanded);
        return state.setValue(SHAPE, getShapeFromState(state));
    }
    public static Direction getSidewaysDirection(BlockState state){
        boolean rightHanded = state.getValue(RIGHT_HANDED);
        Direction direction = state.getValue(ORIENTATION);
        return switch (direction){
            case WEST -> rightHanded ? Direction.NORTH : Direction.SOUTH;
            case EAST -> rightHanded ? Direction.SOUTH : Direction.NORTH;
            case NORTH -> rightHanded ? Direction.EAST : Direction.WEST;
            default -> rightHanded ? Direction.WEST : Direction.EAST;
        };
    }
    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        BlockState state1 = state.setValue(ORIENTATION, rotation.rotate(state.getValue(ORIENTATION)));
        return state1.setValue(SHAPE, getShapeFromState(state1));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        Direction direction = state.getValue(ORIENTATION);
        BlockState state1 = state.setValue(ORIENTATION, mirror.getRotation(direction).rotate(direction)).setValue(RIGHT_HANDED, !state.getValue(RIGHT_HANDED));
        return state1.setValue(SHAPE, getShapeFromState(state1));
    }
}
