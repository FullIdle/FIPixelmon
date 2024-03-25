package com.pixelmonmod.pixelmon.comm;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

public class PixelmonPotion extends Potion {
   public PixelmonPotion(String name, boolean isBadEffect, EnumDyeColor color) {
      super(isBadEffect, (new Color(color.func_193349_f()[0], color.func_193349_f()[1], color.func_193349_f()[2])).getRGB());
      this.func_76390_b("potion." + name);
      this.setRegistryName("pixelmon", name);
   }

   public PotionEffect getEffect(int seconds) {
      return new PotionEffect(this, seconds * 20);
   }

   public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
      mc.field_71446_o.func_110577_a(new ResourceLocation("pixelmon", "textures/potions/" + this.getRegistryName().func_110623_a() + ".png"));
      GuiHelper.drawImageQuad((double)x, (double)y, 20.0, 20.0F, 0.0, 0.0, 1.0, 1.0, 0.0F);
   }
}
