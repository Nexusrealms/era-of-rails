package de.nexusrealms.eraofrails.entity;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;

public class RailSafeExplosionBehavior extends ExplosionDamageCalculator {
    @Override
    public boolean shouldBlockExplode(Explosion explosion, BlockGetter world, BlockPos pos, BlockState state, float power) {
        return super.shouldBlockExplode(explosion, world, pos, state, power) && !isRailOrRailSupport(world, pos, state);
    }

    private boolean isRailOrRailSupport(BlockGetter world, BlockPos pos, BlockState state){
        if(state.is(BlockTags.RAILS)){
            return true;
        } else return world.getBlockState(pos.above()).is(BlockTags.RAILS);
    }
}
