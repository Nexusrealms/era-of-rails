package de.nexusrealms.eraofrails.network;

import de.nexusrealms.eraofrails.EraOfRails;
import de.nexusrealms.eraofrails.client.CartJukeboxHandler;
import de.nexusrealms.eraofrails.client.CartJukeboxHandlerProvider;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record CartJukeboxSongPacket(int entity, int song, boolean stop) implements ReceiverPacket<ClientPlayNetworking.Context>{
    public static final Id<CartJukeboxSongPacket> ID = new Id<>(EraOfRails.id("cart_jukebox_song"));
    public static final PacketCodec<ByteBuf, CartJukeboxSongPacket> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, CartJukeboxSongPacket::entity, PacketCodecs.INTEGER, CartJukeboxSongPacket::song, PacketCodecs.BOOLEAN, CartJukeboxSongPacket::stop, CartJukeboxSongPacket::new);
    @Override
    public void receive(ClientPlayNetworking.Context context) {
        CartJukeboxHandler handler = ((CartJukeboxHandlerProvider) context.client().world).getHandler();
        Entity entity1 = context.client().world.getEntityById(entity);
        handler.handleCartJukeboxWorldEvent(entity1, song, stop);

    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
