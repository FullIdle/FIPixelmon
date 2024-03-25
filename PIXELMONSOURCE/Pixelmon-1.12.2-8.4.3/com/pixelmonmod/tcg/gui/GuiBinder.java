package com.pixelmonmod.tcg.gui;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.item.ItemBinder;
import com.pixelmonmod.tcg.item.containers.ContainerBinder;
import com.pixelmonmod.tcg.item.containers.InventoryBinder;
import java.io.IOException;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiBinder extends GuiContainer {
   private static final ResourceLocation iconLocation = new ResourceLocation("tcg", "gui/binder/inventory.png");
   private static final ResourceLocation buttonLeftRaised = new ResourceLocation("tcg", "gui/binder/left_raised.png");
   private static final ResourceLocation buttonRightRaised = new ResourceLocation("tcg", "gui/binder/right_raised.png");
   private static final ResourceLocation buttonLeftPressed = new ResourceLocation("tcg", "gui/binder/left_pressed.png");
   private static final ResourceLocation buttonRightPressed = new ResourceLocation("tcg", "gui/binder/right_pressed.png");
   private final InventoryBinder inventory;

   public GuiBinder(EntityPlayer player, InventoryPlayer i1, InventoryBinder i2) {
      super(new ContainerBinder(player, i1, i2));
      this.inventory = i2;
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      if (this.inventory.getPlayer().func_184614_ca().func_77973_b() instanceof ItemBinder) {
         super.func_73863_a(mouseX, mouseY, partialTicks);
         int count = 0;
         int k = this.inventory.getPage() * this.inventory.getSizePage();

         for(int i = 0; i < 288; ++i) {
            Slot slot = this.field_147002_h.func_75139_a(i);
            if (i >= k && count < 24) {
               if (count < 12) {
                  slot.field_75223_e = 34 + count % 3 * 18;
                  slot.field_75221_f = 5 + (int)Math.floor((double)(count / 3)) * 18;
               } else {
                  slot.field_75223_e = 90 + (count - 9) % 3 * 18;
                  slot.field_75221_f = 5 + (int)Math.floor((double)((count - 12) / 3)) * 18;
               }

               ++count;
            } else {
               slot.field_75223_e = -40000;
               slot.field_75221_f = -40000;
            }
         }

         String odd = Integer.toString((this.inventory.getPage() + 1) * 2 - 1);
         String even = Integer.toString((this.inventory.getPage() + 1) * 2);
         int oddSize = this.field_146289_q.func_78256_a(odd);
         int evenSize = this.field_146289_q.func_78256_a(even);
         this.field_146289_q.func_175065_a(odd, (float)(this.field_146294_l / 2) - 27.5F - (float)(oddSize / 2), (float)(this.field_146295_m / 2) - 87.5F, 12303291, false);
         this.field_146289_q.func_175065_a(even, (float)(this.field_146294_l / 2) + 28.5F - (float)(evenSize / 2), (float)(this.field_146295_m / 2) - 87.5F, 12303291, false);
         GL11.glPushMatrix();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glDisable(2896);
         GlStateManager.func_179141_d();
         if (this.inventory.getPage() != 0) {
            this.field_146297_k.field_71446_o.func_110577_a(buttonLeftRaised);
            GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 77), (double)(this.field_146295_m / 2 - 57), 12.0, 22.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         }

         if (this.inventory.getPage() != 11) {
            this.field_146297_k.field_71446_o.func_110577_a(buttonRightRaised);
            GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 + 65), (double)(this.field_146295_m / 2 - 57), 12.0, 22.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         }

         if (mouseX > this.field_146294_l / 2 - 77 && mouseX < this.field_146294_l / 2 - 63 && mouseY > this.field_146295_m / 2 - 57 && mouseY < this.field_146295_m / 2 - 35 && this.inventory.getPage() != 0) {
            this.field_146297_k.field_71446_o.func_110577_a(buttonLeftPressed);
            GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 77), (double)(this.field_146295_m / 2 - 57), 12.0, 22.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         }

         if (mouseX > this.field_146294_l / 2 + 65 && mouseX < this.field_146294_l / 2 + 77 && mouseY > this.field_146295_m / 2 - 57 && mouseY < this.field_146295_m / 2 - 35 && this.inventory.getPage() != 11) {
            this.field_146297_k.field_71446_o.func_110577_a(buttonRightPressed);
            GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 + 65), (double)(this.field_146295_m / 2 - 57), 12.0, 22.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         }

         this.func_191948_b(mouseX, mouseY);
         GlStateManager.func_179118_c();
         GL11.glEnable(2896);
         GL11.glPopMatrix();
      } else {
         this.inventory.func_174886_c(this.inventory.getPlayer());
         this.inventory.getPlayer().func_71053_j();
      }

   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
      try {
         super.func_73864_a(mouseX, mouseY, mouseButton);
         if (mouseX > this.field_146294_l / 2 - 77 && mouseX < this.field_146294_l / 2 - 63 && mouseY > this.field_146295_m / 2 - 57 && mouseY < this.field_146295_m / 2 - 35) {
            this.inventory.decrementPage();
         }

         if (mouseX > this.field_146294_l / 2 + 65 && mouseX < this.field_146294_l / 2 + 77 && mouseY > this.field_146295_m / 2 - 57 && mouseY < this.field_146295_m / 2 - 35) {
            this.inventory.incrementPage();
         }
      } catch (NullPointerException var5) {
         TCG.logger.error(var5);
         var5.printStackTrace();
         TCG.logger.warn("MouseX: " + mouseX + " MouseY: " + mouseY + " MouseButton " + mouseButton);
      }

   }

   protected void func_146979_b(int par1, int par2) {
      if (this.inventory.func_145818_k_()) {
         this.inventory.func_70005_c_();
      } else {
         this.inventory.func_70005_c_();
      }

   }

   protected void func_146976_a(float par1, int par2, int par3) {
      ScaledResolution sr = new ScaledResolution(this.field_146297_k);
      this.field_146999_f = 176;
      this.field_147000_g = 184;
      this.field_147003_i = sr.func_78326_a() / 2 - 88;
      this.field_147009_r = sr.func_78328_b() / 2 - 83;
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.field_146297_k.func_110434_K().func_110577_a(iconLocation);
      int k = (sr.func_78326_a() - this.field_146999_f) / 2;
      int l = (sr.func_78328_b() - this.field_147000_g) / 2;
      this.func_73729_b(k, l, 0, 0, this.field_146999_f, this.field_147000_g);
   }
}
