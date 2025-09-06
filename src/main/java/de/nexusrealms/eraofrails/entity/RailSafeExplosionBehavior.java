package de.nexusrealms.eraofrails.entity;

import net.minecraft.block.BlockState;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;

public class RailSafeExplosionBehavior extends ExplosionBehavior {
    @Override
    public boolean canDestroyBlock(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float power) {
        return super.canDestroyBlock(explosion, world, pos, state, power) && !isRailOrRailSupport(world, pos, state);
    }

    private boolean isRailOrRailSupport(BlockView world, BlockPos pos, BlockState state){
        if(state.isIn(BlockTags.RAILS)){
            return true;
        } else return world.getBlockState(pos.up()).isIn(BlockTags.RAILS);
    }
}
