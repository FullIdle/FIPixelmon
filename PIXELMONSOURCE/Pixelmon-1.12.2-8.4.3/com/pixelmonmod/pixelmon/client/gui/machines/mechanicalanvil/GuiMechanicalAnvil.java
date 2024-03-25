package com.pixelmonmod.pixelmon.client.gui.machines.mechanicalanvil;

import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityMechanicalAnvil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiMechanicalAnvil extends GuiContainer {
   public static final ResourceLocation furnaceGuiTextures = new ResourceLocation("textures/gui/container/furnace.png");
   private TileEntityMechanicalAnvil tileMechanicalAnvil;

   public GuiMechanicalAnvil(InventoryPlayer inventoryPlayer, TileEntityMechanicalAnvil tileEntityFurnace) {
      super(new ContainerMechanicalAnvil(inventoryPlayer, tileEntityFurnace));
      this.tileMechanicalAnvil = tileEntityFurnace;
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.func_146276_q_();
      super.func_73863_a(mouseX, mouseY, partialTicks);
      this.func_191948_b(mouseX, mouseY);
   }

   protected void func_146979_b(int mouseX, int mouseY) {
      this.field_146289_q.func_78276_b(I18n.func_135052_a("container.MechanicalAnvil", new Object[0]), this.field_146999_f / 2 - this.field_146289_q.func_78256_a(I18n.func_135052_a("container.MechanicalAnvil", new Object[0])) / 2, 6, 4210752);
      this.field_146289_q.func_78276_b(I18n.func_135052_a("container.inventory", new Object[0]), 8, this.field_147000_g - 96 + 2, 4210752);
   }

   protected void func_146976_a(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      this.field_146297_k.func_110434_K().func_110577_a(furnaceGuiTextures);
      int k = (this.field_146294_l - this.field_146999_f) / 2;
      int l = (this.field_146295_m - this.field_147000_g) / 2;
      this.func_73729_b(k, l, 0, 0, this.field_146999_f, this.field_147000_g);
      if (this.tileMechanicalAnvil.isRunning()) {
         int i1 = this.getBurnTimeRemainingScaled(13);
         this.func_73729_b(k + 56, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 1);
         i1 = this.getHammerProgressScaled(24);
         this.func_73729_b(k + 79, l + 34, 176, 14, i1 + 1, 16);
      }

   }

   private int getHammerProgressScaled(int pixels) {
      int i = this.tileMechanicalAnvil.func_174887_a_(2);
      int j = 200;
      return j != 0 && i != 0 ? i * pixels / j : 0;
   }

   private int getBurnTimeRemainingScaled(int pixels) {
      int i = this.tileMechanicalAnvil.currentFuelBurnTime;
      if (i == 0) {
         i = 200;
      }

      return this.tileMechanicalAnvil.fuelBurnTime * pixels / i;
   }
}
