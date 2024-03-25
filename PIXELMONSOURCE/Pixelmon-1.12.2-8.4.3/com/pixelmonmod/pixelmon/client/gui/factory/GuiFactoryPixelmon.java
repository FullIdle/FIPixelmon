package com.pixelmonmod.pixelmon.client.gui.factory;

import com.pixelmonmod.pixelmon.client.gui.factory.config.PixelmonConfigGui;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class GuiFactoryPixelmon implements IModGuiFactory {
   public void initialize(Minecraft minecraftInstance) {
   }

   public boolean hasConfigGui() {
      return true;
   }

   public GuiScreen createConfigGui(GuiScreen parentScreen) {
      return new PixelmonConfigGui(parentScreen);
   }

   public Set runtimeGuiCategories() {
      return null;
   }
}
