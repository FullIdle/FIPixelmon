package com.pixelmonmod.pixelmon.client.models;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

public class PixelmonModelRenderer extends ModelRenderer {
   private int displayList;
   private boolean compiled = false;
   public ArrayList objs = new ArrayList();
   private boolean isTransparent = false;
   private float transparency;

   public PixelmonModelRenderer(ModelBase par1ModelBase, String par2Str) {
      super(par1ModelBase, par2Str);
   }

   public PixelmonModelRenderer(ModelBase par1ModelBase) {
      super(par1ModelBase);
   }

   public PixelmonModelRenderer(ModelBase par1ModelBase, int par2, int par3) {
      super(par1ModelBase, par2, par3);
   }

   public void addCustomModel(ModelCustomWrapper model) {
      this.objs.add(model);
   }

   public void setTransparent(float transparency) {
      if (transparency > 0.0F) {
         this.isTransparent = true;
         this.transparency = transparency;
      }

   }

   public void func_78785_a(float scale) {
      this.render(scale, 1.0F);
   }

   @SideOnly(Side.CLIENT)
   public void render(float scale, float partialTick) {
      if (!this.field_78807_k && this.field_78806_j) {
         if (!this.compiled) {
            this.compileDisplayList(scale);
         }

         GlStateManager.func_179109_b(this.field_82906_o, this.field_82908_p, this.field_82907_q);
         int var2;
         if (this.field_78795_f == 0.0F && this.field_78796_g == 0.0F && this.field_78808_h == 0.0F) {
            if (this.field_78800_c == 0.0F && this.field_78797_d == 0.0F && this.field_78798_e == 0.0F) {
               GlStateManager.func_179148_o(this.displayList);
               this.renderCustomModels(scale, partialTick);
               if (this.field_78805_m != null) {
                  for(var2 = 0; var2 < this.field_78805_m.size(); ++var2) {
                     ((ModelRenderer)this.field_78805_m.get(var2)).func_78785_a(scale);
                  }
               }
            } else {
               GlStateManager.func_179109_b(this.field_78800_c * scale, this.field_78797_d * scale, this.field_78798_e * scale);
               GlStateManager.func_179148_o(this.displayList);
               this.renderCustomModels(scale, partialTick);
               if (this.field_78805_m != null) {
                  for(var2 = 0; var2 < this.field_78805_m.size(); ++var2) {
                     ((ModelRenderer)this.field_78805_m.get(var2)).func_78785_a(scale);
                  }
               }

               GlStateManager.func_179109_b(-this.field_78800_c * scale, -this.field_78797_d * scale, -this.field_78798_e * scale);
            }
         } else {
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b(this.field_78800_c * scale, this.field_78797_d * scale, this.field_78798_e * scale);
            if (this.field_78796_g != 0.0F) {
               GlStateManager.func_179114_b(this.field_78796_g * 57.295776F, 0.0F, 1.0F, 0.0F);
            }

            if (this.field_78808_h != 0.0F) {
               GlStateManager.func_179114_b(this.field_78808_h * 57.295776F, 0.0F, 0.0F, 1.0F);
            }

            if (this.field_78795_f != 0.0F) {
               GlStateManager.func_179114_b(this.field_78795_f * 57.295776F, 1.0F, 0.0F, 0.0F);
            }

            GlStateManager.func_179148_o(this.displayList);
            this.renderCustomModels(scale, partialTick);
            if (this.field_78805_m != null) {
               for(var2 = 0; var2 < this.field_78805_m.size(); ++var2) {
                  ((ModelRenderer)this.field_78805_m.get(var2)).func_78785_a(scale);
               }
            }

            GlStateManager.func_179121_F();
         }

         GlStateManager.func_179109_b(-this.field_82906_o, -this.field_82908_p, -this.field_82907_q);
      }

   }

   @SideOnly(Side.CLIENT)
   private void compileDisplayList(float par1) {
      this.displayList = GLAllocation.func_74526_a(1);
      GL11.glNewList(this.displayList, 4864);
      Tessellator tessellator = Tessellator.func_178181_a();
      Iterator var3 = this.field_78804_l.iterator();

      while(var3.hasNext()) {
         Object aCubeList = var3.next();
         ((ModelBox)aCubeList).func_178780_a(tessellator.func_178180_c(), par1);
      }

      GL11.glEndList();
      this.compiled = true;
   }

   protected void renderCustomModels(float scale, float partialTick) {
      if (this.isTransparent) {
         GlStateManager.func_179147_l();
         GlStateManager.func_179132_a(false);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, this.transparency);
      }

      Iterator var3 = this.objs.iterator();

      while(var3.hasNext()) {
         ModelCustomWrapper obj = (ModelCustomWrapper)var3.next();
         obj.render(scale, partialTick);
      }

      if (this.isTransparent) {
         GlStateManager.func_179084_k();
         GlStateManager.func_179132_a(true);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      }

      if (OpenGlHelper.func_176075_f()) {
         OpenGlHelper.func_176072_g(OpenGlHelper.field_176089_P, 0);
      }

   }
}
