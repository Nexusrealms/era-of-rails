package de.nexusrealms.eraofrails.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
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
