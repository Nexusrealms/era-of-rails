package de.nexusrealms.eraofrails.network;

import de.nexusrealms.eraofrails.EraOfRails;
import de.nexusrealms.eraofrails.client.CartJukeboxHandler;
import de.nexusrealms.eraofrails.client.CartJukeboxHandlerProvider;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record CartJukeboxSongPacket(int entity, int song, boolean stop) implements ReceiverPacket<ClientPlayNetworking.Context>{
    public static final Type<CartJukeboxSongPacket> ID = new Type<>(EraOfRails.id("cart_jukebox_song"));
    public static final StreamCodec<ByteBuf, CartJukeboxSongPacket> PACKET_CODEC = StreamCodec.composite(ByteBufCodecs.INT, CartJukeboxSongPacket::entity, ByteBufCodecs.INT, CartJukeboxSongPacket::song, ByteBufCodecs.BOOLEAN, CartJukeboxSongPacket::stop, CartJukeboxSongPacket::new);
    @Override
    public void receive(ClientPlayNetworking.Context context) {
        CartJukeboxHandler handler = ((CartJukeboxHandlerProvider) context.client().level).getHandler();
        Entity entity1 = context.client().level.getEntity(entity);
        handler.handleCartJukeboxWorldEvent(entity1, song, stop);

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
