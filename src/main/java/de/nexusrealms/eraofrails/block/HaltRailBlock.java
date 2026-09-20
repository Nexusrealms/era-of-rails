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

public class HaltRailBlock extends BaseRailBlock {
    public static final MapCodec<InputRailBlock> CODEC = simpleCodec(InputRailBlock::new);
    public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty AXIAL = BooleanProperty.create("axial");
    protected HaltRailBlock(Properties settings) {
        super(true, settings);
        registerDefaultState(this.stateDefinition.any().setValue(SHAPE, RailShape.NORTH_SOUTH).setValue(POWERED, false).setValue(WATERLOGGED, false).setValue(AXIAL, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, POWERED, AXIAL, WATERLOGGED);
    }

    @Override
    protected MapCodec<? extends BaseRailBlock> codec() {
        return CODEC;
    }
    protected void updateState(BlockState state, Level world, BlockPos pos, Block neighbor) {
        boolean bl = state.getValue(POWERED);
        boolean bl2 = world.hasNeighborSignal(pos);
        if (bl2 != bl) {
            world.setBlock(pos, state.setValue(POWERED, bl2), 3);
        }

    }
    /*protected BlockState rotate(BlockState state, BlockRotation rotation) {
        RailShape railShape = state.get(SHAPE);
        RailShape railShape2 = this.rotateShape(railShape, rotation);
        return state.with(SHAPE, railShape2);
    }

    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        RailShape railShape = state.get(SHAPE);
        RailShape railShape2 = this.mirrorShape(railShape, mirror);
        return state.with(SHAPE, railShape2);
    }*/
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return super.getStateForPlacement(ctx).setValue(AXIAL, ctx.getHorizontalDirection().getAxisDirection() == Direction.AxisDirection.POSITIVE);
    }
    @Override
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }
    public static Direction getFacingDirection(BlockState state){
        RailShape shape = state.getValue(SHAPE);
        boolean isX = switch (shape) {
            case RailShape.ASCENDING_EAST, RailShape.ASCENDING_WEST, RailShape.EAST_WEST -> true;
            default -> false;
        };
        return state.getValue(AXIAL) ? (isX ? Direction.EAST : Direction.SOUTH) : (isX ? Direction.WEST : Direction.NORTH);
    }
}
