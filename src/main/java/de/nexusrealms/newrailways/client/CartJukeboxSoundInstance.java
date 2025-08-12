package de.nexusrealms.newrailways.client;

import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.ExperimentalMinecartController;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

public class CartJukeboxSoundInstance extends MovingSoundInstance {
    private final AbstractMinecartEntity minecart;

    protected CartJukeboxSoundInstance(SoundEvent soundEvent, SoundCategory soundCategory, Random random, AbstractMinecartEntity minecart) {
        super(soundEvent, soundCategory, random);

        this.minecart = minecart;
        this.x = (float) minecart.getX();
        this.y = (float) minecart.getY();
        this.z = (float) minecart.getZ();
    }
    public static CartJukeboxSoundInstance ofSong(SoundEvent soundEvent, AbstractMinecartEntity minecart){
        return new CartJukeboxSoundInstance(soundEvent, SoundCategory.RECORDS, minecart.getRandom(), minecart);
    }
    @Override
    public boolean canPlay() {
        return !this.minecart.isSilent();
    }

    @Override
    public boolean shouldAlwaysPlay() {
        return true;
    }

    @Override
    public void tick() {
        if (this.minecart.isRemoved()) {
            this.setDone();
        } else {
            this.x = (float) this.minecart.getX();
            this.y = (float) this.minecart.getY();
            this.z = (float) this.minecart.getZ();
        }
    }
}
