package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.PixelmonMethods;
import com.pixelmonmod.pixelmon.client.gui.selectMove.SelectMoveScreen;
import com.pixelmonmod.pixelmon.items.ItemPPUp;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SelectMovePacket implements IMessage {
   private int mode;
   private UUID pokemon;
   private int moveIndex;

   public SelectMovePacket() {
   }

   public SelectMovePacket(int mode, UUID pokemon, int moveIndex) {
      this.mode = mode;
      this.pokemon = pokemon;
      this.moveIndex = moveIndex;
   }

   public void fromBytes(ByteBuf buf) {
      this.mode = buf.readByte();
      this.pokemon = PixelmonMethods.fromBytesUUID(buf);
      this.moveIndex = buf.readByte();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeByte(this.mode);
      PixelmonMethods.toBytesUUID(buf, this.pokemon);
      buf.writeByte(this.moveIndex);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(SelectMovePacket message, MessageContext ctx) {
         if (message.mode == SelectMoveScreen.Mode.PP_UP.ordinal()) {
            ItemPPUp.handleMoveSelect(ctx.getServerHandler().field_147369_b, message.pokemon, message.moveIndex);
         }

      }
   }
}
