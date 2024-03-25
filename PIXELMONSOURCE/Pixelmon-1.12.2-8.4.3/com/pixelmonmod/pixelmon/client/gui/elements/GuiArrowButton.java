package com.pixelmonmod.pixelmon.client.gui.elements;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiArrowButton extends GuiButton {
   protected Direction dir;
   private boolean pressed;

   public GuiArrowButton(int buttonId, int x, int y, Direction dir) {
      dir.getClass();
      dir.getClass();
      super(buttonId, x, y, 9, 17, "");
      this.pressed = false;
      this.dir = dir;
   }

   public GuiArrowButton updatePosition(int x, int y) {
      this.field_146128_h = x;
      this.field_146129_i = y;
      return this;
   }

   public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
      if (this.field_146125_m) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         int offsetX = 0;
         int offsetY = 0;
         if (this.field_146124_l) {
            offsetX += this.dir.offsetX;
            this.dir.getClass();
            offsetY += 3;
         }

         mc.func_110434_K().func_110577_a(this.dir.background);
         double var10000 = (double)(this.field_146128_h + offsetX);
         double var10001 = (double)(this.field_146129_i + offsetY);
         this.dir.getClass();
         this.dir.getClass();
         GuiHelper.drawImageQuad(var10000, var10001, 9.0, 17.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         if (this.field_146124_l) {
            mc.func_110434_K().func_110577_a(this.dir.overlay);
            var10000 = (double)this.field_146128_h;
            var10001 = (double)(this.field_146129_i + (this.pressed ? 1 : 0));
            this.dir.getClass();
            this.dir.getClass();
            GuiHelper.drawImageQuad(var10000, var10001, 9.0, 17.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         }
      }

   }

   public boolean func_146116_c(Minecraft mc, int mouseX, int mouseY) {
      this.pressed = super.func_146116_c(mc, mouseX, mouseY);
      return this.pressed;
   }

   public void func_146118_a(int mouseX, int mouseY) {
      this.pressed = false;
   }

   public static enum Direction {
      LEFT(GuiResources.arrowLeft, GuiResources.arrowLeftBackground, 1),
      RIGHT(GuiResources.arrowRight, GuiResources.arrowRightBackground, -1);

      public final ResourceLocation overlay;
      public final ResourceLocation background;
      public final int width = 9;
      public final int height = 17;
      public final int offsetX;
      public final int offsetY = 3;

      private Direction(ResourceLocation overlay, ResourceLocation background, int offsetX) {
         this.overlay = overlay;
         this.background = background;
         this.offsetX = offsetX;
      }
   }
}
