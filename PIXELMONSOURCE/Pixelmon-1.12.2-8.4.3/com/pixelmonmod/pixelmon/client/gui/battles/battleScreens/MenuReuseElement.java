package com.pixelmonmod.pixelmon.client.gui.battles.battleScreens;

import com.pixelmonmod.pixelmon.client.gui.GuiElement;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class MenuReuseElement extends GuiElement {
   protected static final ResourceLocation MENU_REUSE_BUTTON = new ResourceLocation("pixelmon", "textures/gui/battle/menu_reuse_button.png");
   protected String message;
   @Nullable
   protected ItemStack item;

   public MenuReuseElement(String message, @Nullable ItemStack item) {
      this.message = message;
      this.item = item;
   }

   public void drawElement(float scale) {
      GuiHelper.drawImage(MENU_REUSE_BUTTON, (double)this.x, (double)this.y, 110.0, 18.0, this.zLevel);
      float x = (float)this.x + 55.0F;
      if (this.item != null) {
         x += 8.0F;
      }

      GuiHelper.drawScaledCenteredString(I18n.func_135052_a(this.message, new Object[0]), x, (float)(this.y + 6), -16777216, 14.0F);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      if (this.item != null) {
         Minecraft.func_71410_x().func_175599_af().func_180450_b(this.item, this.x + 14, this.y + 1);
      }

      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
   }
}
