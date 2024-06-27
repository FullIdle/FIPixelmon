package com.fipixelmonmod.fipixelmon.common.packetHandlers.eventPackets;

import com.fipixelmonmod.fipixelmon.common.api.events.OpenScreenEvent;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientOpenScreenPacket implements IMessage {
    private EnumGuiScreen gui;

    public ClientOpenScreenPacket() {
    }

    public ClientOpenScreenPacket(EnumGuiScreen gui){
        this.gui = gui;
    }

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        this.gui = EnumGuiScreen.getFromOrdinal(byteBuf.readByte());
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeByte(this.gui.getIndex());
    }

    public static class Handler implements ISyncHandler<ClientOpenScreenPacket>{
        @Override
        public void onSyncMessage(ClientOpenScreenPacket packet, MessageContext ct) {
            Pixelmon.EVENT_BUS.post(new OpenScreenEvent(ct.getServerHandler().player,packet.gui));
        }
    }
}
