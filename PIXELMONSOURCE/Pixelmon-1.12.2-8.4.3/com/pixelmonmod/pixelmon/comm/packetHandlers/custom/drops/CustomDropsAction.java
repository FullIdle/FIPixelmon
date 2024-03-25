package com.pixelmonmod.pixelmon.comm.packetHandlers.custom.drops;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.enums.EnumPositionTriState;
import com.pixelmonmod.pixelmon.api.events.drops.CustomDropsEvent;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CustomDropsAction implements IMessage {
   EnumPositionTriState position;
   int index;

   public CustomDropsAction() {
   }

   public CustomDropsAction(EnumPositionTriState position) {
      this.position = position;
   }

   public CustomDropsAction(int index) {
      this.index = index;
   }

   public void fromBytes(ByteBuf buf) {
      if (buf.readBoolean()) {
         this.position = EnumPositionTriState.values()[buf.readByte()];
      } else {
         this.index = buf.readByte();
      }

   }

   public void toBytes(ByteBuf buf) {
      if (this.position != null) {
         buf.writeBoolean(true);
         buf.writeByte(this.position.ordinal());
      } else {
         buf.writeBoolean(false);
         buf.writeByte(this.index);
      }

   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(CustomDropsAction message, MessageContext ctx) {
         if (message.position != null) {
            Pixelmon.EVENT_BUS.post(new CustomDropsEvent.ClickButton(ctx.getServerHandler().field_147369_b, message.position));
         } else {
            Pixelmon.EVENT_BUS.post(new CustomDropsEvent.ClickDrop(ctx.getServerHandler().field_147369_b, message.index));
         }

      }
   }
}
