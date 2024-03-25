package com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.pc;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerSetLastOpenBox implements IMessage {
   private int lastBox = 0;

   public ServerSetLastOpenBox() {
   }

   public ServerSetLastOpenBox(int lastBox) {
      this.lastBox = lastBox;
   }

   public void fromBytes(ByteBuf buf) {
      this.lastBox = buf.readShort();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeShort(this.lastBox);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(ServerSetLastOpenBox message, MessageContext ctx) {
         PCStorage pc = Pixelmon.storageManager.getPCForPlayer(ctx.getServerHandler().field_147369_b);
         if (message.lastBox >= 0 && message.lastBox < pc.getBoxes().length) {
            pc.setLastBox(message.lastBox);
         } else {
            Pixelmon.LOGGER.error("Likely hacker trying to use the lastBox exploit: " + ctx.getServerHandler().field_147369_b.func_70005_c_());
         }
      }
   }
}
