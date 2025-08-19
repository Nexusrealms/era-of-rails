package de.nexusrealms.newrailways.network;

import de.nexusrealms.newrailways.NewRailways;
import de.nexusrealms.newrailways.entity.types.InputMinecartEntity;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

import java.util.Optional;

public record SetInputCartDataPacket(int entityId, Optional<Long> newSequence, Optional<Integer> newActiveInsts) implements ReceiverPacket<ServerPlayNetworking.Context>{
    public static final Id<SetInputCartDataPacket> ID = new Id<>(NewRailways.id("set_input_cart_data"));
    public static final PacketCodec<ByteBuf, SetInputCartDataPacket> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, SetInputCartDataPacket::entityId ,PacketCodecs.optional(PacketCodecs.LONG), SetInputCartDataPacket::newSequence, PacketCodecs.optional(PacketCodecs.INTEGER), SetInputCartDataPacket::newActiveInsts, SetInputCartDataPacket::new);
    @Override
    public void receive(ServerPlayNetworking.Context context) {
        Entity entity = context.player().getWorld().getEntityById(entityId);
        if(entity instanceof InputMinecartEntity minecart){
            minecart.updateFromPacket(this);
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
