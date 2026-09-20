package de.nexusrealms.eraofrails.entity.types;

import com.google.common.annotations.VisibleForTesting;
import de.nexusrealms.eraofrails.entity.ComparatorOutputtingMinecart;
import de.nexusrealms.eraofrails.item.RailwaysItems;
import de.nexusrealms.eraofrails.network.CartJukeboxSongPacket;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.vehicle.AbstractMinecart.Type;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class JukeboxMinecartEntity extends AbstractMinecart implements ComparatorOutputtingMinecart {
    public JukeboxMinecartEntity(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }
    private ItemStack recordStack = ItemStack.EMPTY;
    private final CartJukeboxManager manager = new CartJukeboxManager();

    private CartJukeboxManager getManager() {
        return this.manager;
    }
    @Override
    public BlockState getDefaultDisplayBlockState() {
        return Blocks.JUKEBOX.defaultBlockState();
    }
    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if(hand == InteractionHand.MAIN_HAND){
            ItemStack stack = player.getItemInHand(hand);
            boolean dropped = dropRecord();
            if(isValid(stack)){
                setStack(stack);
                player.setItemInHand(hand, ItemStack.EMPTY);
                return InteractionResult.SUCCESS;
            }
            return dropped ? InteractionResult.SUCCESS : InteractionResult.FAIL;
        }
        return InteractionResult.PASS;
    }
    @Override
    public void destroy(DamageSource damageSource) {
        super.destroy(damageSource);
        this.dropRecord();
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        manager.stopPlaying();
    }

    @Override
    public void kill() {
        super.kill();
        manager.stopPlaying();
    }

    @Override
    public void activateMinecart(int x, int y, int z, boolean powered) {
        if(powered){
            dropRecord();
        }    }


    public boolean dropRecord() {
        if (!this.getCommandSenderWorld().isClientSide()) {
            BlockPos blockPos = this.blockPosition();
            ItemStack itemStack = this.getRecordStack();
            if (!itemStack.isEmpty()) {
                this.setStack(ItemStack.EMPTY);
                Vec3 vec3d = Vec3.atLowerCornerWithOffset(blockPos, 0.5, 1.01, 0.5).offsetRandom(this.getCommandSenderWorld().getRandom(), 0.7F);
                ItemStack itemStack2 = itemStack.copy();
                ItemEntity itemEntity = new ItemEntity(this.getCommandSenderWorld(), vec3d.x(), vec3d.y(), vec3d.z(), itemStack2);
                itemEntity.setDefaultPickUpDelay();
                this.getCommandSenderWorld().addFreshEntity(itemEntity);
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
        return JukeboxSong.fromStack(this.getCommandSenderWorld().registryAccess(), this.recordStack)
                .map(Holder::value)
                .map(JukeboxSong::comparatorOutput)
                .orElse(0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("RecordItem", Tag.TAG_COMPOUND)) {
            this.recordStack = (ItemStack) ItemStack.parse(registryAccess(), nbt.getCompound("RecordItem")).orElse(ItemStack.EMPTY);
        } else {
            this.recordStack = ItemStack.EMPTY;
        }

        if (nbt.contains("ticks_since_song_started", Tag.TAG_LONG)) {
            JukeboxSong.fromStack(registryAccess(), this.recordStack).ifPresent(song -> this.manager.setValues(song, nbt.getLong("ticks_since_song_started")));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        if (!recordStack.isEmpty()) {
            nbt.put("RecordItem", this.recordStack.save(registryAccess()));
        }

        if (this.manager.getSong() != null) {
            nbt.putLong("ticks_since_song_started", this.manager.getTicksSinceSongStarted());
        }
    }
    //TODO Custom cart type enum
    @Override
    public Type getMinecartType() {
        return Type.SPAWNER;
    }

    public ItemStack getRecordStack() {
        return recordStack;
    }

    public void setStack(ItemStack stack) {
        this.recordStack = stack;
        boolean bl = !this.recordStack.isEmpty();
        Optional<Holder<JukeboxSong>> optional = JukeboxSong.fromStack(this.getCommandSenderWorld().registryAccess(), this.recordStack);
        //this.onRecordStackChanged(bl);
        if (bl && optional.isPresent()) {
            this.manager.startPlaying(optional.get());
        } else {
            this.manager.stopPlaying();
        }
    }

    public boolean isValid(ItemStack stack) {
        return stack.has(DataComponents.JUKEBOX_PLAYABLE) && this.getRecordStack().isEmpty();
    }


    @VisibleForTesting
    public void setDisc(ItemStack stack) {
        this.recordStack = stack;
        JukeboxSong.fromStack(this.getCommandSenderWorld().registryAccess(), stack).ifPresent(song -> this.manager.setValues(song, 0L));
    }

    @VisibleForTesting
    public void reloadDisc() {
        JukeboxSong.fromStack(this.getCommandSenderWorld().registryAccess(), this.getRecordStack()).ifPresent(this.manager::startPlaying);
    }
    @Override
    public ItemStack getPickResult() {
        return new ItemStack(getDropItem());
    }

    @Override
    protected Item getDropItem() {
        return RailwaysItems.JUKEBOX_MINECART;
    }


    private class CartJukeboxManager {
        public static final int TICKS_PER_SECOND = 20;
        private long songTicks;
        @Nullable
        private Holder<JukeboxSong> song;
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

        public void setValues(Holder<JukeboxSong> song, long ticksPlaying) {
            if (!song.value().hasFinished(ticksPlaying)) {
                this.song = song;
                this.songTicks = ticksPlaying;
            }
        }

        public void startPlaying(Holder<JukeboxSong> song) {
            this.song = song;
            startedPlayingPos = JukeboxMinecartEntity.super.blockPosition();
            this.songTicks = 0L;
            int i = JukeboxMinecartEntity.super.registryAccess().registryOrThrow(Registries.JUKEBOX_SONG).getId(this.song.value());
            if(!getWorld().isClientSide()) getWorld().getServer().getPlayerList().broadcast(null, getX(), getY(), getZ(), 64f, getWorld().dimension(), new ClientboundCustomPayloadPacket(new CartJukeboxSongPacket(getId(), i, false)));
            //JukeboxMinecartEntity.super.not;
        }

        public void stopPlaying() {
            if (this.song != null) {
                this.song = null;
                this.songTicks = 0L;
                getWorld().gameEvent(JukeboxMinecartEntity.this, GameEvent.JUKEBOX_STOP_PLAY, position());
                if(!getWorld().isClientSide()){
                    getWorld().getServer().getPlayerList().broadcast(null, getX(), getY(), getZ(), 64f, getWorld().dimension(), new ClientboundCustomPayloadPacket(new CartJukeboxSongPacket(getId(), 0, true)));
                }
                //this.changeNotifier.notifyChange();
            }
        }

        public void tick() {
            if (this.song != null) {
                if (this.song.value().hasFinished(this.songTicks)) {
                    this.stopPlaying();
                } else {
                    if (this.hasSecondPassed()) {
                        getWorld().gameEvent(JukeboxMinecartEntity.this, GameEvent.JUKEBOX_PLAY, JukeboxMinecartEntity.super.position());
                        spawnNoteParticles(getWorld(), JukeboxMinecartEntity.super.position());
                    }

                    this.songTicks++;
                }
            }
        }
        private Level getWorld(){
            return JukeboxMinecartEntity.super.getCommandSenderWorld();
        }
        private boolean hasSecondPassed() {
            return this.songTicks % 20L == 0L;
        }

        private static void spawnNoteParticles(LevelAccessor world, Vec3 pos) {
            if (world instanceof ServerLevel serverWorld) {
                Vec3 vec3d = pos.add(0.0, 1.2F, 0.0);
                float f = world.getRandom().nextInt(4) / 24.0F;
                serverWorld.sendParticles(ParticleTypes.NOTE, vec3d.x(), vec3d.y(), vec3d.z(), 0, f, 0.0, 0.0, 1.0);
            }
        }
    }
}
