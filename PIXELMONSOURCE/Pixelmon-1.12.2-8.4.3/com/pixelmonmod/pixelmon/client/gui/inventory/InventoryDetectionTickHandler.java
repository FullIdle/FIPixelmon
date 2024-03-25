package com.pixelmonmod.pixelmon.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class InventoryDetectionTickHandler {
   @SubscribeEvent
   public void onGuiOpen(GuiOpenEvent event) {
      if (event.getGui() instanceof GuiInventory && !(event.getGui() instanceof GuiInventoryPixelmonExtended)) {
         event.setGui(new GuiInventoryPixelmonExtended(Minecraft.func_71410_x().field_71439_g));
      } else if (event.getGui() instanceof GuiContainerCreative && !(event.getGui() instanceof GuiCreativeInventoryExtended)) {
         event.setGui(new GuiCreativeInventoryExtended(Minecraft.func_71410_x().field_71439_g));
      }

   }
}
