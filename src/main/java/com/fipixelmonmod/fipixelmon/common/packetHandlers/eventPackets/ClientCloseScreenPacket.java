package com.fipixelmonmod.fipixelmon.common.packetHandlers.eventPackets;

import com.fipixelmonmod.fipixelmon.common.api.events.CloseScreenEvent;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientCloseScreenPacket implements IMessage {
    private EnumGuiScreen gui;

    public ClientCloseScreenPacket() {
    }

    public ClientCloseScreenPacket(EnumGuiScreen gui){
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

    public static class Handler implements ISyncHandler<ClientCloseScreenPacket>{
        @Override
        public void onSyncMessage(ClientCloseScreenPacket packet, MessageContext ct) {
            Pixelmon.EVENT_BUS.post(new CloseScreenEvent(ct.getServerHandler().player,packet.gui));
        }
    }
}
