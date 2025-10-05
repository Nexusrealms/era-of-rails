package de.nexusrealms.eraofrails.block;

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

public class CrossRailBlock extends AbstractRailBlock {
    public static final MapCodec<CrossRailBlock> CODEC = createCodec(CrossRailBlock::new);
    public static final EnumProperty<RailShape> SHAPE = Properties.STRAIGHT_RAIL_SHAPE;

    protected CrossRailBlock(Settings settings) {
        super(true, settings);
        setDefaultState(this.stateManager.getDefaultState().with(SHAPE, RailShape.NORTH_SOUTH).with(WATERLOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, WATERLOGGED);
    }

    @Override
    protected MapCodec<? extends AbstractRailBlock> getCodec() {
        return CODEC;
    }


    @Override
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }
}
