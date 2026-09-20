package de.nexusrealms.eraofrails.network;

import de.nexusrealms.eraofrails.EraOfRails;
import de.nexusrealms.eraofrails.entity.types.InputMinecartEntity;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.Optional;

public record SetInputCartDataPacket(int entityId, Optional<Long> newSequence, Optional<Integer> newActiveInsts) implements ReceiverPacket<ServerPlayNetworking.Context>{
    public static final Type<SetInputCartDataPacket> ID = new Type<>(EraOfRails.id("set_input_cart_data"));
    public static final StreamCodec<ByteBuf, SetInputCartDataPacket> PACKET_CODEC = StreamCodec.composite(ByteBufCodecs.INT, SetInputCartDataPacket::entityId , ByteBufCodecs.optional(ByteBufCodecs.VAR_LONG), SetInputCartDataPacket::newSequence, ByteBufCodecs.optional(ByteBufCodecs.INT), SetInputCartDataPacket::newActiveInsts, SetInputCartDataPacket::new);
    @Override
    public void receive(ServerPlayNetworking.Context context) {
        Entity entity = context.player().getCommandSenderWorld().getEntity(entityId);
        if(entity instanceof InputMinecartEntity minecart){
            minecart.updateFromPacket(this);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
