package com.pixelmonmod.pixelmon.comm.packetHandlers.raids;

import com.pixelmonmod.pixelmon.client.gui.raids.GuiRaidCatch;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UpdateRaidCatch implements IMessage {
   private int shakes;
   private boolean sentToPC;

   public UpdateRaidCatch() {
   }

   public UpdateRaidCatch(int shakes, boolean sentToPC) {
      this.shakes = shakes;
      this.sentToPC = sentToPC;
   }

   public void fromBytes(ByteBuf buf) {
      this.shakes = buf.readInt();
      this.sentToPC = buf.readBoolean();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.shakes);
      buf.writeBoolean(this.sentToPC);
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(UpdateRaidCatch message, MessageContext ctx) {
         this.onClient(message, ctx);
      }

      @SideOnly(Side.CLIENT)
      public void onClient(UpdateRaidCatch message, MessageContext ctx) {
         Minecraft mc = Minecraft.func_71410_x();
         if (mc.field_71462_r instanceof GuiRaidCatch) {
            GuiRaidCatch gui = (GuiRaidCatch)mc.field_71462_r;
            gui.setShakes(message.shakes, message.sentToPC);
         }

      }
   }
}
