package de.nexusrealms.eraofrails.client;

import net.minecraft.world.item.JukeboxSong;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class CartJukeboxHandler {
    private final Level world;
    private final Map<Integer, CartJukeboxSoundInstance> soundInstanceMap = new HashMap<>();

    public CartJukeboxHandler(Level world) {
        this.world = world;
    }

    public boolean handleCartJukeboxWorldEvent(Entity source, int data, boolean stop){
        if(source instanceof AbstractMinecart minecart){
            if(stop){
                stopJukeboxSong(minecart);
            } else {
                this.world.registryAccess().registryOrThrow(Registries.JUKEBOX_SONG).getHolder(data).ifPresent(song -> this.playJukeboxSong(song, minecart));
            }
            return true;
        }
        return false;
    }
    private void playJukeboxSong(Holder<JukeboxSong> song, AbstractMinecart entity) {
        this.stopJukeboxSong(entity);
        JukeboxSong jukeboxSong = song.value();
        SoundEvent soundEvent = jukeboxSong.soundEvent().value();
        CartJukeboxSoundInstance soundInstance = CartJukeboxSoundInstance.ofSong(soundEvent, entity);
        this.soundInstanceMap.put(entity.getId(), soundInstance);
        Minecraft.getInstance().getSoundManager().play(soundInstance);
        Minecraft.getInstance().gui.setNowPlaying(jukeboxSong.description());
        //TODO Fix this if anybody notices
        //this.updateEntitiesForSong(this.world, jukeboxPos, true);
    }

    private void stopJukeboxSong(AbstractMinecart entity) {
        SoundInstance soundInstance = this.soundInstanceMap.remove(entity.getId());
        if (soundInstance != null) {
            Minecraft.getInstance().getSoundManager().stop(soundInstance);
        }
    }
}
