package com.pixelmonmod.pixelmon.enums;

import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import net.minecraft.util.ResourceLocation;

public enum EnumTrainerCardColor {
   BLUE("trainercard_blue.png", 0.0F, 0.891F, 0.955F),
   PINK("trainercard_pink.png", 0.955F, 0.705F, 0.78F),
   WHITE("trainercard_white.png", 1.0F, 1.0F, 1.0F),
   GOLD("trainercard_gold.png", 0.955F, 0.915F, 0.0F);

   public final ResourceLocation resource;
   public final float red;
   public final float green;
   public final float blue;

   private EnumTrainerCardColor(String file, float red, float green, float blue) {
      this.resource = new ResourceLocation(GuiResources.prefix + "gui/trainercards/" + file);
      this.red = red;
      this.green = green;
      this.blue = blue;
   }
}
