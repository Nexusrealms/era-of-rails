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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class LockedSwitchRailBlock extends SwitchRailBlock {

    protected LockedSwitchRailBlock(Settings settings) {
        super(settings);
    }
    @Override
    protected void updateBlockState(BlockState state, World world, BlockPos pos, Block neighbor) {
        boolean bl = state.get(SWITCHED);
        BlockState maybeInputRail = world.getBlockState(pos.offset(state.get(ORIENTATION).getOpposite()));
        boolean bl2 = maybeInputRail.isIn(RailwaysBlocks.Tags.INPUT_RAIL) && maybeInputRail.get(InputRailBlock.POWERED);
        if (bl2 != bl) {
            state = state.with(SWITCHED, bl2);
        }
        world.setBlockState(pos, state.with(SHAPE, getShapeFromState(state)), Block.NOTIFY_ALL);
    }

}
