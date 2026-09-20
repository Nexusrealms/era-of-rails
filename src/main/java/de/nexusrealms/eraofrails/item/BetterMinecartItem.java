package de.nexusrealms.eraofrails.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class BetterMinecartItem extends Item {
    public static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
        private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

        public ItemStack execute(BlockSource blockSource, ItemStack itemStack) {
            Direction direction = (Direction)blockSource.state().getValue(DispenserBlock.FACING);
            ServerLevel serverLevel = blockSource.level();
            Vec3 vec3 = blockSource.center();
            double d = vec3.x() + (double)direction.getStepX() * (double)1.125F;
            double e = Math.floor(vec3.y()) + (double)direction.getStepY();
            double f = vec3.z() + (double)direction.getStepZ() * (double)1.125F;
            BlockPos blockPos = blockSource.pos().relative(direction);
            BlockState blockState = serverLevel.getBlockState(blockPos);
            RailShape railShape = blockState.getBlock() instanceof BaseRailBlock ? (RailShape)blockState.getValue(((BaseRailBlock)blockState.getBlock()).getShapeProperty()) : RailShape.NORTH_SOUTH;
            double g;
            if (blockState.is(BlockTags.RAILS)) {
                if (railShape.isAscending()) {
                    g = 0.6;
                } else {
                    g = 0.1;
                }
            } else {
                if (!blockState.isAir() || !serverLevel.getBlockState(blockPos.below()).is(BlockTags.RAILS)) {
                    return this.defaultDispenseItemBehavior.dispense(blockSource, itemStack);
                }

                BlockState blockState2 = serverLevel.getBlockState(blockPos.below());
                RailShape railShape2 = blockState2.getBlock() instanceof BaseRailBlock ? (RailShape)blockState2.getValue(((BaseRailBlock)blockState2.getBlock()).getShapeProperty()) : RailShape.NORTH_SOUTH;
                if (direction != Direction.DOWN && railShape2.isAscending()) {
                    g = -0.4;
                } else {
                    g = -0.9;
                }
            }

            AbstractMinecart abstractMinecart = createMinecart(serverLevel, d, e + g, f, ((BetterMinecartItem)itemStack.getItem()).factory, itemStack, null);
            serverLevel.addFreshEntity(abstractMinecart);
            itemStack.shrink(1);
            return itemStack;
        }

        protected void playSound(BlockSource blockSource) {
            blockSource.level().levelEvent(1000, blockSource.pos(), 0);
        }
    };
    final CartFactory factory;

    public BetterMinecartItem(CartFactory factory, Item.Properties properties) {
        super(properties);
        this.factory = factory;
        DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
    }
    public BetterMinecartItem(EntityType<? extends AbstractMinecart> type, Item.Properties properties) {
        super(properties);
        this.factory = (level, x, y, z) -> {
            AbstractMinecart abstractMinecart = type.create(level);
            abstractMinecart.setPos(x, y, z);
            abstractMinecart.xo = x;
            abstractMinecart.yo = y;
            abstractMinecart.zo = z;
            return abstractMinecart;
        };
        DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
    }
    public static AbstractMinecart createMinecart(ServerLevel serverLevel, double d, double e, double f, CartFactory factory, ItemStack itemStack, @Nullable Player player) {

        AbstractMinecart abstractMinecart = factory.create(serverLevel, d, e, f);
        EntityType.createDefaultStackConfig(serverLevel, itemStack, player).accept(abstractMinecart);
        return abstractMinecart;
    }
    public InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        BlockPos blockPos = useOnContext.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        if (!blockState.is(BlockTags.RAILS)) {
            return InteractionResult.FAIL;
        } else {
            ItemStack itemStack = useOnContext.getItemInHand();
            if (level instanceof ServerLevel) {
                ServerLevel serverLevel = (ServerLevel)level;
                RailShape railShape = blockState.getBlock() instanceof BaseRailBlock ? (RailShape)blockState.getValue(((BaseRailBlock)blockState.getBlock()).getShapeProperty()) : RailShape.NORTH_SOUTH;
                double d = (double)0.0F;
                if (railShape.isAscending()) {
                    d = (double)0.5F;
                }

                AbstractMinecart abstractMinecart = createMinecart(serverLevel, (double)blockPos.getX() + (double)0.5F, (double)blockPos.getY() + (double)0.0625F + d, (double)blockPos.getZ() + (double)0.5F, this.factory, itemStack, useOnContext.getPlayer());
                serverLevel.addFreshEntity(abstractMinecart);
                serverLevel.gameEvent(GameEvent.ENTITY_PLACE, blockPos, GameEvent.Context.of(useOnContext.getPlayer(), serverLevel.getBlockState(blockPos.below())));
            }

            itemStack.shrink(1);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }
    @FunctionalInterface
    public interface CartFactory {
        AbstractMinecart create(ServerLevel level, double x, double y, double z);
    }
}
