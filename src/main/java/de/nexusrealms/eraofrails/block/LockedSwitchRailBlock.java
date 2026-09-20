package de.nexusrealms.eraofrails.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class LockedSwitchRailBlock extends SwitchRailBlock {

    protected LockedSwitchRailBlock(Properties settings) {
        super(settings);
    }
    @Override
    protected void updateState(BlockState state, Level world, BlockPos pos, Block neighbor) {
        boolean bl = state.getValue(SWITCHED);
        BlockState maybeInputRail = world.getBlockState(pos.relative(state.getValue(ORIENTATION).getOpposite()));
        boolean bl2 = maybeInputRail.is(RailwaysBlocks.Tags.INPUT_RAIL) && maybeInputRail.getValue(InputRailBlock.POWERED);
        if (bl2 != bl) {
            state = state.setValue(SWITCHED, bl2);
        }
        world.setBlock(pos, state.setValue(SHAPE, getShapeFromState(state)), Block.UPDATE_ALL);
    }

}
