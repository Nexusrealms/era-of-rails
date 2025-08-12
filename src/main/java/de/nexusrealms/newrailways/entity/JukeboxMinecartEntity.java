package de.nexusrealms.newrailways.entity;

import com.google.common.annotations.VisibleForTesting;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class JukeboxMinecartEntity extends AbstractMinecartEntity implements ComparatorOutputtingMinecart {
    protected JukeboxMinecartEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
    }
    private ItemStack recordStack = ItemStack.EMPTY;
    private final CartJukeboxManager manager = new CartJukeboxManager();

    private CartJukeboxManager getManager() {
        return this.manager;
    }
    @Override
    public BlockState getDefaultContainedBlock() {
        return Blocks.JUKEBOX.getDefaultState();
    }
    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        boolean dropped = dropRecord();
        if(isValid(stack)){
            setStack(stack);
            return ActionResult.SUCCESS;
        }
        return dropped ? ActionResult.SUCCESS : ActionResult.FAIL;
    }
    @Override
    public void killAndDropSelf(ServerWorld world, DamageSource damageSource) {
        super.killAndDropSelf(world, damageSource);
        this.dropRecord();
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        manager.stopPlaying();
    }

    @Override
    public void kill(ServerWorld world) {
        super.kill(world);
        manager.stopPlaying();
    }

    @Override
    public void onActivatorRail(int x, int y, int z, boolean powered) {
        if(powered){
           dropRecord();
        }
    }

    public boolean dropRecord() {
        if (!this.getWorld().isClient()) {
            BlockPos blockPos = this.getBlockPos();
            ItemStack itemStack = this.getRecordStack();
            if (!itemStack.isEmpty()) {
                this.setStack(ItemStack.EMPTY);
                Vec3d vec3d = Vec3d.add(blockPos, 0.5, 1.01, 0.5).addRandom(this.getWorld().getRandom(), 0.7F);
                ItemStack itemStack2 = itemStack.copy();
                ItemEntity itemEntity = new ItemEntity(this.getWorld(), vec3d.getX(), vec3d.getY(), vec3d.getZ(), itemStack2);
                itemEntity.setToDefaultPickupDelay();
                this.getWorld().spawnEntity(itemEntity);
                return true;
            }
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        manager.tick();
    }

    public int getComparatorOutput() {
        return JukeboxSong.getSongEntryFromStack(this.getWorld().getRegistryManager(), this.recordStack)
                .map(RegistryEntry::value)
                .map(JukeboxSong::comparatorOutput)
                .orElse(0);
    }

    @Override
    public void readData(ReadView view) {
        super.readData(view);
        ItemStack itemStack = view.read("recordItem", ItemStack.CODEC).orElse(ItemStack.EMPTY);
        if (!this.recordStack.isEmpty() && !ItemStack.areItemsAndComponentsEqual(itemStack, this.recordStack)) {
            this.manager.stopPlaying();
        }

        this.recordStack = itemStack;
        view.getOptionalLong("songTicks")
                .ifPresent(
                        ticksSinceSongStarted -> JukeboxSong.getSongEntryFromStack(view.getRegistries(), this.recordStack)
                                .ifPresent(song -> this.manager.setValues(song, ticksSinceSongStarted))
                );
    }

    @Override
    public void writeData(WriteView view) {
        super.writeData(view);
        if (!this.getRecordStack().isEmpty()) {
            view.put("recordItem", ItemStack.CODEC, this.getRecordStack());
        }

        if (this.manager.getSong() != null) {
            view.putLong("songTicks", this.manager.getTicksSinceSongStarted());
        }
    }

    public ItemStack getRecordStack() {
        return recordStack;
    }

    public void setStack(ItemStack stack) {
        this.recordStack = stack;
        boolean bl = !this.recordStack.isEmpty();
        Optional<RegistryEntry<JukeboxSong>> optional = JukeboxSong.getSongEntryFromStack(this.getWorld().getRegistryManager(), this.recordStack);
        //this.onRecordStackChanged(bl);
        if (bl && optional.isPresent()) {
            this.manager.startPlaying(optional.get());
        } else {
            this.manager.stopPlaying();
        }
    }

    public boolean isValid(ItemStack stack) {
        return stack.contains(DataComponentTypes.JUKEBOX_PLAYABLE) && this.getRecordStack().isEmpty();
    }


    @VisibleForTesting
    public void setDisc(ItemStack stack) {
        this.recordStack = stack;
        JukeboxSong.getSongEntryFromStack(this.getWorld().getRegistryManager(), stack).ifPresent(song -> this.manager.setValues(song, 0L));
    }

    @VisibleForTesting
    public void reloadDisc() {
        JukeboxSong.getSongEntryFromStack(this.getWorld().getRegistryManager(), this.getRecordStack()).ifPresent(this.manager::startPlaying);
    }
    @Override
    public ItemStack getPickBlockStack() {
        return null;
    }

    @Override
    protected Item asItem() {
        return null;
    }


    private class CartJukeboxManager {
        public static final int TICKS_PER_SECOND = 20;
        private long songTicks;
        @Nullable
        private RegistryEntry<JukeboxSong> song;
        @Nullable
        private BlockPos startedPlayingPos;

        public boolean isPlaying() {
            return this.song != null;
        }

        @Nullable
        public JukeboxSong getSong() {
            return this.song == null ? null : this.song.value();
        }

        public long getTicksSinceSongStarted() {
            return this.songTicks;
        }

        public void setValues(RegistryEntry<JukeboxSong> song, long ticksPlaying) {
            if (!song.value().shouldStopPlaying(ticksPlaying)) {
                this.song = song;
                this.songTicks = ticksPlaying;
            }
        }

        public void startPlaying(RegistryEntry<JukeboxSong> song) {
            this.song = song;
            startedPlayingPos = JukeboxMinecartEntity.super.getBlockPos();
            this.songTicks = 0L;
            int i = JukeboxMinecartEntity.super.getRegistryManager().getOrThrow(RegistryKeys.JUKEBOX_SONG).getRawId(this.song.value());
            getWorld().syncWorldEvent(null, WorldEvents.JUKEBOX_STARTS_PLAYING, startedPlayingPos, i);
            //JukeboxMinecartEntity.super.not;
        }

        public void stopPlaying() {
            if (this.song != null) {
                this.song = null;
                this.songTicks = 0L;
                getWorld().emitGameEvent(JukeboxMinecartEntity.this, GameEvent.JUKEBOX_STOP_PLAY, getPos());
                JukeboxMinecartEntity.super.getWorld().syncWorldEvent(WorldEvents.JUKEBOX_STOPS_PLAYING, startedPlayingPos, 0);
                //this.changeNotifier.notifyChange();
            }
        }

        public void tick() {
            if (this.song != null) {
                if (this.song.value().shouldStopPlaying(this.songTicks)) {
                    this.stopPlaying();
                } else {
                    if (this.hasSecondPassed()) {
                        getWorld().emitGameEvent(JukeboxMinecartEntity.this, GameEvent.JUKEBOX_PLAY, JukeboxMinecartEntity.super.getPos());
                        spawnNoteParticles(getWorld(), JukeboxMinecartEntity.super.getPos());
                    }

                    this.songTicks++;
                }
            }
        }
        private World getWorld(){
            return JukeboxMinecartEntity.super.getWorld();
        }
        private boolean hasSecondPassed() {
            return this.songTicks % 20L == 0L;
        }

        private static void spawnNoteParticles(WorldAccess world, Vec3d pos) {
            if (world instanceof ServerWorld serverWorld) {
                Vec3d vec3d = pos.add(0.0, 1.2F, 0.0);
                float f = world.getRandom().nextInt(4) / 24.0F;
                serverWorld.spawnParticles(ParticleTypes.NOTE, vec3d.getX(), vec3d.getY(), vec3d.getZ(), 0, f, 0.0, 0.0, 1.0);
            }
        }
    }
}
