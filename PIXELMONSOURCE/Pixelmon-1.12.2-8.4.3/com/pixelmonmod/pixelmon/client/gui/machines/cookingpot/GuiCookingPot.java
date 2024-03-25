package com.pixelmonmod.pixelmon.client.gui.machines.cookingpot;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityCookingPot;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiCookingPot extends GuiContainer {
   public static final ResourceLocation cookingPotGuiTextures = new ResourceLocation("pixelmon", "textures/gui/cookingpot.png");
   private TileEntityCookingPot tileEntityCookingPot;

   public GuiCookingPot(InventoryPlayer inventoryPlayer, TileEntityCookingPot tileEntityInfuser) {
      super(new ContainerCookingPot(inventoryPlayer, tileEntityInfuser));
      this.tileEntityCookingPot = tileEntityInfuser;
      this.field_146999_f = 180;
      this.field_147000_g = 152;
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.func_146276_q_();
      super.func_73863_a(mouseX, mouseY, partialTicks);
      this.func_191948_b(mouseX, mouseY);
   }

   protected void func_146979_b(int mouseX, int mouseY) {
      this.field_146289_q.func_78276_b(I18n.func_135052_a("container.cookingpot", new Object[0]), this.field_146999_f / 2 - this.field_146289_q.func_78256_a(I18n.func_135052_a("container.cookingpot", new Object[0])) / 2, 6, 4210752);
      this.field_146289_q.func_78276_b(I18n.func_135052_a("container.inventory", new Object[0]), 8, this.field_147000_g - 100 + 2, 4210752);
   }

   protected void func_146976_a(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      this.field_146297_k.func_110434_K().func_110577_a(cookingPotGuiTextures);
      int k = (this.field_146294_l - this.field_146999_f) / 2;
      int l = (this.field_146295_m - this.field_147000_g) / 2;
      this.func_73729_b(k, l, 0, 0, this.field_146999_f, this.field_147000_g);
   }
}
