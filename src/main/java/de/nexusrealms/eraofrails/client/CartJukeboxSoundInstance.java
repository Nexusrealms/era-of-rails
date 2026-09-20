package de.nexusrealms.eraofrails.client;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;

public class CartJukeboxSoundInstance extends AbstractTickableSoundInstance {
    private final AbstractMinecart minecart;

    protected CartJukeboxSoundInstance(SoundEvent soundEvent, SoundSource soundCategory, RandomSource random, AbstractMinecart minecart) {
        super(soundEvent, soundCategory, random);

        this.minecart = minecart;
        this.x = (float) minecart.getX();
        this.y = (float) minecart.getY();
        this.z = (float) minecart.getZ();
    }
    public static CartJukeboxSoundInstance ofSong(SoundEvent soundEvent, AbstractMinecart minecart){
        return new CartJukeboxSoundInstance(soundEvent, SoundSource.RECORDS, minecart.getRandom(), minecart);
    }
    @Override
    public boolean canPlaySound() {
        return !this.minecart.isSilent();
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public void tick() {
        if (this.minecart.isRemoved()) {
            this.stop();
        } else {
            this.x = (float) this.minecart.getX();
            this.y = (float) this.minecart.getY();
            this.z = (float) this.minecart.getZ();
        }
    }
}
