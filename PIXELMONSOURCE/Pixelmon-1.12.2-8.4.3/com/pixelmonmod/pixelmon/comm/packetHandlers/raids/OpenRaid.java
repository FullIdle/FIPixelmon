package com.pixelmonmod.pixelmon.comm.packetHandlers.raids;

import com.pixelmonmod.pixelmon.battles.raids.RaidData;
import com.pixelmonmod.pixelmon.client.gui.raids.GuiRaidStart;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OpenRaid implements IMessage {
   private RaidData raid;

   public OpenRaid() {
   }

   public OpenRaid(RaidData raid) {
      this.raid = raid;
   }

   public void fromBytes(ByteBuf buf) {
      this.raid = new RaidData(buf);
   }

   public void toBytes(ByteBuf buf) {
      this.raid.writeToByteBuf(buf);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(OpenRaid message, MessageContext ctx) {
         this.onClient(message, ctx);
      }

      @SideOnly(Side.CLIENT)
      public void onClient(OpenRaid message, MessageContext ctx) {
         Minecraft mc = Minecraft.func_71410_x();
         if (mc.field_71462_r instanceof GuiRaidStart) {
            GuiRaidStart gui = (GuiRaidStart)mc.field_71462_r;
            gui.setRaid(message.raid, gui);
         } else {
            mc.func_147108_a(new GuiRaidStart(message.raid));
         }

      }
   }
}
