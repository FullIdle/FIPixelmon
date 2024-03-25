package com.pixelmonmod.pixelmon.client.models.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

public class ModelScroll extends ModelBase {
   public ModelRenderer scrollSlate;
   public ModelRenderer scrollStand;
   public ModelRenderer scrollTop;
   private static final ResourceLocation base = new ResourceLocation("pixelmon:textures/entity/scroll/scroll_base.png");

   public ModelScroll() {
      this.field_78090_t = 128;
      this.field_78089_u = 128;
      this.scrollSlate = new ModelRenderer(this, 0, 0);
      this.scrollSlate.func_78790_a(-16.0F, -2.0F, -2.0F, 32, 63, 1, 0.0F);
      this.scrollStand = new ModelRenderer(this, 0, 0);
      this.scrollStand.func_78790_a(-1.0F, -52.0F, -1.0F, 2, 64, 2, 0.0F);
      this.scrollTop = new ModelRenderer(this, 8, 0);
      this.scrollTop.func_78790_a(-16.0F, -54.0F, -1.0F, 32, 2, 2, 0.0F);
   }

   public void debugUpdate() {
   }

   public void renderScroll() {
      this.debugUpdate();
      this.scrollSlate.field_78797_d = -52.0F;
      this.scrollSlate.func_78785_a(0.0625F);
      Minecraft.func_71410_x().field_71446_o.func_110577_a(base);
      this.scrollStand.func_78785_a(0.0625F);
      this.scrollTop.func_78785_a(0.0625F);
   }
}
