package de.nexusrealms.eraofrails.entity;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.UUIDUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class LazyCartReference {
    private final UUID uuid;
    private AbstractMinecart cart = null;
    public static final StreamCodec<ByteBuf, LazyCartReference> PACKET_CODEC = UUIDUtil.STREAM_CODEC.map(LazyCartReference::new, LazyCartReference::getUuid);
    public static final Codec<LazyCartReference> CODEC = UUIDUtil.AUTHLIB_CODEC.xmap(LazyCartReference::new, LazyCartReference::getUuid);
    public LazyCartReference(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public @Nullable AbstractMinecart resolve(ServerLevel world){
        if(cart != null){
            Entity e = world.getEntity(uuid);
            if(e instanceof AbstractMinecart minecart){
                cart = minecart;
            }
        }
        return cart;
    }
    public Optional<AbstractMinecart> resolveOptional(ServerLevel world){
        return Optional.ofNullable(resolve(world));
    }

}
