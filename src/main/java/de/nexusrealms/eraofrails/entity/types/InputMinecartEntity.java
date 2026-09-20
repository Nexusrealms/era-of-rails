package de.nexusrealms.eraofrails.entity.types;

import de.nexusrealms.eraofrails.client.InputCartScreen;
import de.nexusrealms.eraofrails.item.RailwaysItems;
import de.nexusrealms.eraofrails.network.SetInputCartDataPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.vehicle.AbstractMinecart.Type;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

public class InputMinecartEntity extends AbstractMinecart {
    public static final EntityDataAccessor<Long> SEQUENCE = SynchedEntityData.defineId(InputMinecartEntity.class, EntityDataSerializers.LONG);
    public static final EntityDataAccessor<Integer> ACTIVE_INSTS = SynchedEntityData.defineId(InputMinecartEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> NEXT_INST = SynchedEntityData.defineId(InputMinecartEntity.class, EntityDataSerializers.INT);


    public InputMinecartEntity(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }
    public Item getDropItem(){
        return RailwaysItems.INPUT_MINECART;
    }
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SEQUENCE, 0L);
        builder.define(ACTIVE_INSTS, 64);
        builder.define(NEXT_INST, 0);
    }
    //TODO Custom cart type enum
    @Override
    public Type getMinecartType() {
        return Type.RIDEABLE;
    }
   /* @Override
    protected void copyComponentsFrom(ComponentsAccess from) {
        super.copyComponentsFrom(from);
        copyComponentFrom(from, RailwaysItems.Components.INPUT_CART_SEQUENCE);
        copyComponentFrom(from, RailwaysItems.Components.INPUT_CART_LIMIT);

    }

    @Override
    protected <T> boolean setApplicableComponent(ComponentType<T> type, T value) {
        if(type == RailwaysItems.Components.INPUT_CART_SEQUENCE){
            dataTracker.set(SEQUENCE, castComponentValue(RailwaysItems.Components.INPUT_CART_SEQUENCE, value));
            return true;
        }
        if(type == RailwaysItems.Components.INPUT_CART_LIMIT){
            dataTracker.set(ACTIVE_INSTS, castComponentValue(RailwaysItems.Components.INPUT_CART_LIMIT, value));
            return true;
        }
        return super.setApplicableComponent(type, value);
    }
*/
    public boolean getNextInputAndMove(){
        int next = entityData.get(NEXT_INST);
        int active = entityData.get(ACTIVE_INSTS);
        if(active == 0){
            return false;
        }
        /*if(next >= active){
            dataTracker.set(NEXT_INST, 0);
            return getNextInputAndMove();
        }*/
        long sequence = entityData.get(SEQUENCE);
        boolean bool = ((sequence >> next) & 1) != 0;
        if(next + 1 >= active){
            entityData.set(NEXT_INST, 0);
        } else {
            entityData.set(NEXT_INST, next + 1);
        }
        return bool;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag view) {
        super.addAdditionalSaveData(view);
        view.putLong("sequence", entityData.get(SEQUENCE));
        view.putInt("activeInsts", entityData.get(ACTIVE_INSTS));
        view.putInt("nextInst", entityData.get(NEXT_INST));
    }

    public void destroy(DamageSource damageSource) {
        this.kill();
        if (getCommandSenderWorld().getGameRules().getRule(GameRules.RULE_DOENTITYDROPS).get()) {
            this.spawnAtLocation(getPickResult());
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag view) {
        super.readAdditionalSaveData(view);
        entityData.set(SEQUENCE, view.getLong("sequence"));
        entityData.set(ACTIVE_INSTS, view.getInt("activeInsts"));
        entityData.set(NEXT_INST, view.getInt("nextInst"));
    }
    public long getSequence(){
        return entityData.get(SEQUENCE);
    }
    public int getActiveInsts(){
        return entityData.get(ACTIVE_INSTS);
    }
    public int getNextInst(){
        return entityData.get(NEXT_INST);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if(getCommandSenderWorld().isClientSide()){
            openScreen();
        }
        return InteractionResult.SUCCESS;
    }
    @Environment(value = EnvType.CLIENT)
    public void openScreen(){
        Minecraft.getInstance().setScreen(new InputCartScreen(this));
    }
    @Override
    public ItemStack getPickResult() {
        ItemStack stack = new ItemStack(getDropItem());
        stack.set(RailwaysItems.Components.INPUT_CART_LIMIT, entityData.get(ACTIVE_INSTS));
        stack.set(RailwaysItems.Components.INPUT_CART_SEQUENCE, entityData.get(SEQUENCE));
        return stack;
    }

    public void updateFromPacket(SetInputCartDataPacket inputCartDataPacket){
        inputCartDataPacket.newSequence().ifPresent(aLong -> entityData.set(SEQUENCE, aLong));
        inputCartDataPacket.newActiveInsts().ifPresent(aInt -> {
            entityData.set(ACTIVE_INSTS, aInt);
            if(aInt <= getNextInst()){
                entityData.set(NEXT_INST, 0);
            }
        });
    }
    @Override
    public BlockState getDefaultDisplayBlockState() {
        return Blocks.OBSERVER.defaultBlockState();
    }
}
