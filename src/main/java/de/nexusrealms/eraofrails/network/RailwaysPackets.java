package de.nexusrealms.eraofrails.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class RailwaysPackets {
    public static void init(){
        initC2S();
        if(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT){
            initS2C();
        }
    }
    private static void initS2C(){
        registerClientReceiverPacket(CartJukeboxSongPacket.ID, CartJukeboxSongPacket.PACKET_CODEC);
    }
    private static void initC2S(){
        registerServerReceiverPacket(SetInputCartDataPacket.ID, SetInputCartDataPacket.PACKET_CODEC);
    }


    private static <T extends ReceiverPacket<ClientPlayNetworking.Context>> void registerClientReceiverPacket(CustomPacketPayload.Type<T> packetId, StreamCodec<? super RegistryFriendlyByteBuf, T> packetCodec){
        PayloadTypeRegistry.playS2C().register(packetId, packetCodec);
        ClientPlayNetworking.registerGlobalReceiver(packetId, ReceiverPacket::receive);
    }
    private static <T extends ReceiverPacket<ServerPlayNetworking.Context>> void registerServerReceiverPacket(CustomPacketPayload.Type<T> packetId, StreamCodec<? super RegistryFriendlyByteBuf, T> packetCodec){
        PayloadTypeRegistry.playC2S().register(packetId, packetCodec);
        ServerPlayNetworking.registerGlobalReceiver(packetId, ReceiverPacket::receive);
    }
}
