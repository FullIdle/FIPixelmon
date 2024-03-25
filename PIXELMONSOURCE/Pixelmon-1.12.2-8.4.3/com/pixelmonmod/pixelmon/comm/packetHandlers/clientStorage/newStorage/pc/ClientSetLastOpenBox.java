package com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.pc;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientSetLastOpenBox implements IMessage {
   private int lastBox = 0;

   public ClientSetLastOpenBox() {
   }

   public ClientSetLastOpenBox(EntityPlayerMP player, int lastBox) {
      PCStorage pc = Pixelmon.storageManager.getPCForPlayer(player);
      if (lastBox >= 0 && lastBox < pc.getBoxes().length) {
         this.lastBox = lastBox;
      } else {
         this.lastBox = 0;
         Pixelmon.LOGGER.error("ClientSetLastOpenBox out of bounds! Expected value between 0 and " + pc.getBoxes().length + ", got " + lastBox);
      }

   }

   public void fromBytes(ByteBuf buf) {
      this.lastBox = buf.readShort();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeShort(this.lastBox);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(ClientSetLastOpenBox message, MessageContext ctx) {
         Minecraft.func_71410_x().func_152344_a(() -> {
            ClientStorageManager.openPC.setLastBox(message.lastBox);
         });
         return null;
      }
   }
}
