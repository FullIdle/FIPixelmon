package com.pixelmonmod.tcg.gui;

import com.pixelmonmod.tcg.gui.duel.GuiTCG;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiChatOverlay {
   private Minecraft mc;

   public GuiChatOverlay(Minecraft mc) {
      this.mc = mc;
   }

   @SubscribeEvent(
      priority = EventPriority.NORMAL
   )
   public void eventHandler(RenderGameOverlayEvent.Pre event) {
      if (event.getType() == ElementType.CHAT && this.mc.field_71462_r instanceof GuiTCG) {
         event.setCanceled(true);
      }

   }
}
