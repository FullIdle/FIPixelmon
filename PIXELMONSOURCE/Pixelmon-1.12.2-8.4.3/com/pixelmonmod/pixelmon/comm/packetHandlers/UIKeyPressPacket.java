package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UIKeyPressPacket implements IMessage {
   private int code;
   private char key;

   public UIKeyPressPacket() {
   }

   public UIKeyPressPacket(int code, char key) {
      this.code = code;
      this.key = key;
   }

   public void fromBytes(ByteBuf buf) {
      this.code = buf.readInt();
      this.key = buf.readChar();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.code);
      buf.writeChar(this.key);
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(UIKeyPressPacket message, MessageContext ctx) {
         FMLCommonHandler.instance().getMinecraftServerInstance().func_152344_a(() -> {
            if (ctx.getServerHandler().field_147369_b != null) {
               PlayerPartyStorage pps = Pixelmon.storageManager.getParty(ctx.getServerHandler().field_147369_b);
               pps.receiveKeyInput(message.code, message.key);
            }

         });
         return null;
      }
   }
}
