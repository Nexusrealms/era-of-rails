package de.nexusrealms.eraofrails.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.enums.RailShape;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class SwitchRailBlock extends AbstractRailBlock {
    public static final MapCodec<SwitchRailBlock> CODEC = createCodec(SwitchRailBlock::new);

    public static final EnumProperty<RailShape> SHAPE = Properties.RAIL_SHAPE;

    public static final EnumProperty<Direction> ORIENTATION = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty RIGHT_HANDED = BooleanProperty.of("right_handed");
    public static final BooleanProperty SWITCHED = BooleanProperty.of("switched");

    protected SwitchRailBlock(Settings settings) {
        super(true, settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(SHAPE, RailShape.NORTH_SOUTH).with(SWITCHED, false).with(WATERLOGGED, false).with(RIGHT_HANDED, false).with(ORIENTATION, Direction.NORTH));
    }
    public List<BlockPos> getNeighbors(BlockPos pos, BlockState state){
        List<BlockPos> list = new ArrayList<>();
        Direction direction = state.get(ORIENTATION);
        list.add(pos.offset(direction));
        list.add(pos.offset(direction.getOpposite()));
        list.add(pos.offset(state.get(RIGHT_HANDED) ? direction.rotateYClockwise() : direction.rotateYCounterclockwise()));
        return list;
    }
    @Override
    protected MapCodec<? extends AbstractRailBlock> getCodec() {
        return CODEC;
    }

    @Override
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    @Override
    protected void updateBlockState(BlockState state, World world, BlockPos pos, Block neighbor) {
        boolean bl = state.get(SWITCHED);
        boolean bl2 = world.isReceivingRedstonePower(pos);
        if (bl2 != bl) {
            state = state.with(SWITCHED, bl2);
        }
        world.setBlockState(pos, state.with(SHAPE, getShapeFromState(state)), Block.NOTIFY_ALL);
    }

    protected BlockState updateBlockState(World world, BlockPos pos, BlockState state, boolean forceUpdate) {
        return state.with(SHAPE, getShapeFromState(state));
    }
    protected RailShape getShapeFromState(BlockState state){
        boolean switched = state.get(SWITCHED);
        boolean rightHanded = state.get(RIGHT_HANDED);
        Direction direction = state.get(ORIENTATION);
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
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, SWITCHED, RIGHT_HANDED, ORIENTATION, SHAPE);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getHorizontalPlayerFacing();
        float yaw = ctx.getPlayerYaw();
        boolean rightHanded = switch (direction) {
            case SOUTH -> yaw > 0;
            case EAST -> yaw > -90;
            case NORTH -> yaw < 0;
            default -> yaw > 90;
        };
        BlockState state = getDefaultState().with(ORIENTATION, direction).with(RIGHT_HANDED, rightHanded);
        return state.with(SHAPE, getShapeFromState(state));
    }
    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        BlockState state1 = state.with(ORIENTATION, rotation.rotate(state.get(ORIENTATION)));
        return state1.with(SHAPE, getShapeFromState(state1));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        Direction direction = state.get(ORIENTATION);
        BlockState state1 = state.with(ORIENTATION, mirror.getRotation(direction).rotate(direction)).with(RIGHT_HANDED, !state.get(RIGHT_HANDED));
        return state1.with(SHAPE, getShapeFromState(state1));
    }
}
