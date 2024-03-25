package com.pixelmonmod.pixelmon.comm.packetHandlers.battles;

import com.pixelmonmod.pixelmon.client.gui.GuiPixelmonOverlay;
import com.pixelmonmod.pixelmon.client.gui.battles.ClientBattleManager;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CloseBattle implements IMessage {
   public void fromBytes(ByteBuf buf) {
   }

   public void toBytes(ByteBuf buf) {
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(CloseBattle message, MessageContext ctx) {
         this.onClient(message, ctx);
         return null;
      }

      @SideOnly(Side.CLIENT)
      public void onClient(CloseBattle message, MessageContext ctx) {
         GuiPixelmonOverlay.isVisible = true;
         Minecraft mc = Minecraft.func_71410_x();
         mc.func_152344_a(() -> {
            if (mc.field_71462_r instanceof GuiBattle) {
               ((GuiBattle)mc.field_71462_r).restoreSettingsAndClose();
            } else {
               GuiBattle.restoreSettingsAndCloseStatic((ClientBattleManager)null);
            }

         });
      }
   }
}
