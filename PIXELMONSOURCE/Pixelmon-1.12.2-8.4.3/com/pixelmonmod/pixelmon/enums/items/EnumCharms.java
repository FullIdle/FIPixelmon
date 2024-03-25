package com.pixelmonmod.pixelmon.enums.items;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.comm.packetHandlers.SetCharm;
import com.pixelmonmod.pixelmon.enums.EnumFeatureState;
import net.minecraft.util.ResourceLocation;

public enum EnumCharms {
   Shiny("gui.shinycharm.message"),
   Oval("gui.ovalcharm.message"),
   Exp("gui.expcharm.message"),
   Catching("gui.catchingcharm.message"),
   Mark("gui.markcharm.message");

   private final String guiMessage;

   private EnumCharms(String guiMessage) {
      this.guiMessage = guiMessage;
   }

   public String getGuiMessage() {
      return this.guiMessage;
   }

   public ResourceLocation getImage() {
      switch (this) {
         case Oval:
            return GuiResources.ovalCharm;
         case Shiny:
            return GuiResources.shinyCharm;
         case Exp:
            return GuiResources.expCharm;
         case Catching:
            return GuiResources.catchingCharm;
         case Mark:
            return GuiResources.markCharm;
         default:
            return GuiResources.shinyCharm;
      }
   }

   public void sendPacket(EnumFeatureState state) {
      Pixelmon.network.sendToServer(new SetCharm(this, state));
   }
}
