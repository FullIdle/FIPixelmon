package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.client.ClientProxy;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SwitchCamera implements IMessage {
   public void fromBytes(ByteBuf buf) {
   }

   public void toBytes(ByteBuf buf) {
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(SwitchCamera message, MessageContext ctx) {
         if (ClientProxy.camera != null) {
            Minecraft.func_71410_x().field_71474_y.field_74320_O = 1;
         }

         return null;
      }
   }
}
