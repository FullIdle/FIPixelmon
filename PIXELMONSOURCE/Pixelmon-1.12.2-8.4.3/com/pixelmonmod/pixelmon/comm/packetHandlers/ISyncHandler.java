package com.pixelmonmod.pixelmon.comm.packetHandlers;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ISyncHandler extends IMessageHandler {
   default IMessage onMessage(IMessage message, MessageContext ctx) {
      if (ctx.side == Side.CLIENT) {
         this.onClientSync(message, ctx);
      } else if (ctx.getServerHandler().field_147369_b != null && ctx.getServerHandler().field_147369_b.func_184102_h() != null) {
         ctx.getServerHandler().field_147369_b.func_184102_h().func_152344_a(() -> {
            this.onSyncMessage(message, ctx);
         });
      }

      return null;
   }

   @SideOnly(Side.CLIENT)
   default void onClientSync(IMessage message, MessageContext ctx) {
      Minecraft.func_71410_x().func_152344_a(() -> {
         this.onSyncMessage(message, ctx);
      });
   }

   void onSyncMessage(IMessage var1, MessageContext var2);
}
