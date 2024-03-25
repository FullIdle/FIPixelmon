package com.pixelmonmod.pixelmon.client.gui.machines.infuser;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityInfuser;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiInfuser extends GuiContainer {
   public static final ResourceLocation infuserGuiTextures = new ResourceLocation("pixelmon", "textures/gui/infuser.png");
   private TileEntityInfuser tileInfuser;

   public GuiInfuser(InventoryPlayer inventoryPlayer, TileEntityInfuser tileEntityInfuser) {
      super(new ContainerInfuser(inventoryPlayer, tileEntityInfuser));
      this.tileInfuser = tileEntityInfuser;
      this.field_146999_f = 180;
      this.field_147000_g = 152;
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.func_146276_q_();
      super.func_73863_a(mouseX, mouseY, partialTicks);
      this.func_191948_b(mouseX, mouseY);
   }

   protected void func_146979_b(int mouseX, int mouseY) {
      this.field_146289_q.func_78276_b(I18n.func_135052_a("container.infuser", new Object[0]), this.field_146999_f / 2 - this.field_146289_q.func_78256_a(I18n.func_135052_a("container.infuser", new Object[0])) / 2, 6, 4210752);
      this.field_146289_q.func_78276_b(I18n.func_135052_a("container.inventory", new Object[0]), 8, this.field_147000_g - 100 + 2, 4210752);
   }

   protected void func_146976_a(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      this.field_146297_k.func_110434_K().func_110577_a(infuserGuiTextures);
      int k = (this.field_146294_l - this.field_146999_f) / 2;
      int l = (this.field_146295_m - this.field_147000_g) / 2;
      this.func_73729_b(k, l, 0, 0, this.field_146999_f, this.field_147000_g);
      if (this.tileInfuser.isRunning()) {
         int i1 = this.tileInfuser.getBurnTimeRemainingScaled(14);
         this.func_73729_b(k + 29, l + 26 - i1, 180, 12 - i1, 14, i1 + 1);
         i1 = this.tileInfuser.getInfusionProgressScaled(15);
         this.func_73729_b(k + 108, l + 32, 180, 14, i1 + 1, 13);
      }

   }
}
