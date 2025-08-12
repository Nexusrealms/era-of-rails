package de.nexusrealms.newrailways.network;

import de.nexusrealms.newrailways.NewRailways;
import de.nexusrealms.newrailways.client.CartJukeboxHandler;
import de.nexusrealms.newrailways.client.CartJukeboxHandlerProvider;
import de.nexusrealms.newrailways.datagen.LootTableGen;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;

public record CartJukeboxSongPacket(int entity, int song, boolean stop) implements ReceiverPacket<ClientPlayNetworking.Context>{
    public static final Id<CartJukeboxSongPacket> ID = new Id<>(NewRailways.id("cart_jukebox_song"));
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
