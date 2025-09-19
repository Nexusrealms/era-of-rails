package de.nexusrealms.eraofrails.entity.types;

import de.nexusrealms.eraofrails.client.InputCartScreen;
import de.nexusrealms.eraofrails.item.RailwaysItems;
import de.nexusrealms.eraofrails.network.SetInputCartDataPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.ComponentType;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class InputMinecartEntity extends AbstractMinecartEntity {
    public static final TrackedData<Long> SEQUENCE = DataTracker.registerData(InputMinecartEntity.class, TrackedDataHandlerRegistry.LONG);
    public static final TrackedData<Integer> ACTIVE_INSTS = DataTracker.registerData(InputMinecartEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static final TrackedData<Integer> NEXT_INST = DataTracker.registerData(InputMinecartEntity.class, TrackedDataHandlerRegistry.INTEGER);


    public InputMinecartEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(SEQUENCE, 0L);
        builder.add(ACTIVE_INSTS, 64);
        builder.add(NEXT_INST, 0);
    }

    @Override
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

    public boolean getNextInputAndMove(){
        int next = dataTracker.get(NEXT_INST);
        int active = dataTracker.get(ACTIVE_INSTS);
        if(active == 0){
            return false;
        }
        /*if(next >= active){
            dataTracker.set(NEXT_INST, 0);
            return getNextInputAndMove();
        }*/
        long sequence = dataTracker.get(SEQUENCE);
        boolean bool = ((sequence >> next) & 1) != 0;
        if(next + 1 >= active){
            dataTracker.set(NEXT_INST, 0);
        } else {
            dataTracker.set(NEXT_INST, next + 1);
        }
        return bool;
    }
    @Override
    public void writeData(WriteView view) {
        super.writeData(view);
        view.putLong("sequence", dataTracker.get(SEQUENCE));
        view.putInt("activeInsts", dataTracker.get(ACTIVE_INSTS));
        view.putInt("nextInst", dataTracker.get(NEXT_INST));
    }
    public void killAndDropSelf(ServerWorld world, DamageSource damageSource) {
        this.kill(world);
        if (world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
            this.dropStack(world, getPickBlockStack());
        }
    }
    @Override
    public void readData(ReadView view) {
        super.readData(view);
        dataTracker.set(SEQUENCE, view.getLong("sequence", 0L));
        dataTracker.set(ACTIVE_INSTS, view.getInt("activeInsts", 64));
        dataTracker.set(NEXT_INST, view.getInt("nextInst", 0));

    }
    public long getSequence(){
        return dataTracker.get(SEQUENCE);
    }
    public int getActiveInsts(){
        return dataTracker.get(ACTIVE_INSTS);
    }
    public int getNextInst(){
        return dataTracker.get(NEXT_INST);
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if(getWorld().isClient()){
            openScreen();
        }
        return ActionResult.SUCCESS;
    }
    @Environment(value = EnvType.CLIENT)
    public void openScreen(){
        MinecraftClient.getInstance().setScreen(new InputCartScreen(this));
    }
    @Override
    public ItemStack getPickBlockStack() {
        ItemStack stack = new ItemStack(asItem());
        stack.set(RailwaysItems.Components.INPUT_CART_LIMIT, dataTracker.get(ACTIVE_INSTS));
        stack.set(RailwaysItems.Components.INPUT_CART_SEQUENCE, dataTracker.get(SEQUENCE));
        return stack;
    }

    @Override
    protected Item asItem() {
        return RailwaysItems.INPUT_MINECART;
    }
    public void updateFromPacket(SetInputCartDataPacket inputCartDataPacket){
        inputCartDataPacket.newSequence().ifPresent(aLong -> dataTracker.set(SEQUENCE, aLong));
        inputCartDataPacket.newActiveInsts().ifPresent(aInt -> {
            dataTracker.set(ACTIVE_INSTS, aInt);
            if(aInt <= getNextInst()){
                dataTracker.set(NEXT_INST, 0);
            }
        });
    }
    @Override
    public BlockState getDefaultContainedBlock() {
        return Blocks.OBSERVER.getDefaultState();
    }
}
