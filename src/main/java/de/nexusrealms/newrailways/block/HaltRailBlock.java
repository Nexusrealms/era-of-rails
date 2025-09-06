package de.nexusrealms.newrailways.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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

public class HaltRailBlock extends AbstractRailBlock {
    public static final MapCodec<InputRailBlock> CODEC = createCodec(InputRailBlock::new);
    public static final EnumProperty<RailShape> SHAPE = Properties.STRAIGHT_RAIL_SHAPE;
    public static final BooleanProperty POWERED = Properties.POWERED;
    public static final BooleanProperty AXIAL = BooleanProperty.of("axial");
    protected HaltRailBlock(Settings settings) {
        super(true, settings);
        setDefaultState(this.stateManager.getDefaultState().with(SHAPE, RailShape.NORTH_SOUTH).with(POWERED, false).with(WATERLOGGED, false).with(AXIAL, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, POWERED, AXIAL, WATERLOGGED);
    }

    @Override
    protected MapCodec<? extends AbstractRailBlock> getCodec() {
        return CODEC;
    }
    protected void updateBlockState(BlockState state, World world, BlockPos pos, Block neighbor) {
        boolean bl = state.get(POWERED);
        boolean bl2 = world.isReceivingRedstonePower(pos);
        if (bl2 != bl) {
            world.setBlockState(pos, state.with(POWERED, bl2), 3);
        }

    }
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        RailShape railShape = state.get(SHAPE);
        RailShape railShape2 = this.rotateShape(railShape, rotation);
        return state.with(SHAPE, railShape2);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(AXIAL, ctx.getHorizontalPlayerFacing().getDirection() == Direction.AxisDirection.POSITIVE);
    }

    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        RailShape railShape = state.get(SHAPE);
        RailShape railShape2 = this.mirrorShape(railShape, mirror);
        return state.with(SHAPE, railShape2);
    }
    @Override
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }
}
