package com.pixelmonmod.pixelmon.client.gui.custom.overlays;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CustomOverlay {
   @SubscribeEvent
   public void onDrawOverlay(RenderGameOverlayEvent.Pre event) {
      try {
         if (event.getType() == ElementType.ALL) {
            if (Minecraft.func_71410_x().field_71474_y.field_74319_N) {
               return;
            }

            if (CustomNoticeOverlay.isEnabled() && Minecraft.func_71410_x().field_71456_v.func_184046_j().field_184060_g.isEmpty() && !Minecraft.func_71410_x().field_71474_y.field_74321_H.func_151470_d()) {
               CustomNoticeOverlay.draw(event.getResolution());
            }

            if (CustomScoreboardOverlay.isEnabled()) {
               CustomScoreboardOverlay.draw(event.getResolution());
            }
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
