package com.pixelmonmod.pixelmon.comm.packetHandlers.raids;

import com.pixelmonmod.pixelmon.client.gui.raids.GuiRaidStart;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CloseRaid implements IMessage {
   public void fromBytes(ByteBuf buf) {
   }

   public void toBytes(ByteBuf buf) {
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(CloseRaid message, MessageContext ctx) {
         this.onClient(message, ctx);
      }

      @SideOnly(Side.CLIENT)
      public void onClient(CloseRaid message, MessageContext ctx) {
         Minecraft mc = Minecraft.func_71410_x();
         if (mc.field_71462_r instanceof GuiRaidStart) {
            mc.field_71439_g.func_71053_j();
         }

      }
   }
}
