package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.client.gui.GuiPixelmonOverlay;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PlayerDeath implements IMessage {
   public void fromBytes(ByteBuf buf) {
   }

   public void toBytes(ByteBuf buf) {
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(PlayerDeath message, MessageContext ctx) {
         this.onClient(message, ctx);
         return null;
      }

      @SideOnly(Side.CLIENT)
      public void onClient(PlayerDeath message, MessageContext ctx) {
         GuiPixelmonOverlay.isVisible = true;
         Minecraft.func_71410_x().field_71474_y.field_74320_O = 0;
         Minecraft.func_71410_x().field_71474_y.field_74319_N = false;
         Minecraft.func_71410_x().func_175607_a(Minecraft.func_71410_x().field_71439_g);
         Minecraft.func_71410_x().field_71462_r = null;
      }
   }
}
