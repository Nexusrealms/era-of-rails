package de.nexusrealms.eraofrails.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface ReceiverPacket<C> extends CustomPacketPayload {
    void receive(C context);
}
