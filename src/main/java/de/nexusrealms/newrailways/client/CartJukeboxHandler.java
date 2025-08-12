package de.nexusrealms.newrailways.client;

import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MovingMinecartSoundInstance;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CartJukeboxHandler {
    private final World world;
    private final Map<Integer, CartJukeboxSoundInstance> soundInstanceMap = new HashMap<>();

    public CartJukeboxHandler(World world) {
        this.world = world;
    }

    public boolean handleCartJukeboxWorldEvent(Entity source, int data, boolean stop){
        if(source instanceof AbstractMinecartEntity minecart){
            if(stop){
                stopJukeboxSong(minecart);
            } else {
                this.world.getRegistryManager().getOrThrow(RegistryKeys.JUKEBOX_SONG).getEntry(data).ifPresent(song -> this.playJukeboxSong(song, minecart));
            }
            return true;
        }
        return false;
    }
    private void playJukeboxSong(RegistryEntry<JukeboxSong> song, AbstractMinecartEntity entity) {
        this.stopJukeboxSong(entity);
        JukeboxSong jukeboxSong = song.value();
        SoundEvent soundEvent = jukeboxSong.soundEvent().value();
        CartJukeboxSoundInstance soundInstance = CartJukeboxSoundInstance.ofSong(soundEvent, entity);
        this.soundInstanceMap.put(entity.getId(), soundInstance);
        MinecraftClient.getInstance().getSoundManager().play(soundInstance);
        MinecraftClient.getInstance().inGameHud.setRecordPlayingOverlay(jukeboxSong.description());
        //TODO Fix this if anybody notices
        //this.updateEntitiesForSong(this.world, jukeboxPos, true);
    }

    private void stopJukeboxSong(AbstractMinecartEntity entity) {
        SoundInstance soundInstance = this.soundInstanceMap.remove(entity.getId());
        if (soundInstance != null) {
            MinecraftClient.getInstance().getSoundManager().stop(soundInstance);
        }
    }
}
