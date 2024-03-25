package com.pixelmonmod.tcg.gui;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.helper.LogicHelper;
import com.pixelmonmod.tcg.item.ItemDeck;
import com.pixelmonmod.tcg.item.containers.ContainerDeck;
import com.pixelmonmod.tcg.item.containers.InventoryDeck;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiDeck extends GuiContainer {
   private static final ResourceLocation iconLocation = new ResourceLocation("tcg", "gui/deck/inventory.png");
   private final InventoryDeck inventory;

   public GuiDeck(EntityPlayer player, InventoryPlayer i1, InventoryDeck i2) {
      super(new ContainerDeck(player, i1, i2));
      this.inventory = i2;
   }

   public void func_73863_a(int par1, int par2, float par3) {
      if (this.inventory.getPlayer().func_184614_ca().func_77973_b() instanceof ItemDeck) {
         super.func_73863_a(par1, par2, par3);
         GL11.glPushMatrix();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glDisable(2896);
         GlStateManager.func_179141_d();
         this.func_191948_b(par1, par2);
         GlStateManager.func_179118_c();
         GL11.glEnable(2896);
         GL11.glPopMatrix();
      } else {
         this.inventory.func_174886_c(this.inventory.getPlayer());
         this.inventory.getPlayer().func_71053_j();
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
      this.field_146999_f = 226;
      this.field_147000_g = 202;
      this.field_147003_i = sr.func_78326_a() / 2 - 88;
      this.field_147009_r = sr.func_78328_b() / 2 - 83;
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.field_146297_k.func_110434_K().func_110577_a(iconLocation);
      int k = (this.field_146294_l - this.field_146999_f) / 2 - 6;
      int l = (this.field_146295_m - this.field_147000_g) / 2;
      this.func_73729_b(k, l, 0, 0, this.field_146999_f, this.field_147000_g);
      List cards = new ArrayList();

      for(int i = 0; i < 60; ++i) {
         ItemStack s = this.inventory.func_70301_a(i);
         if (s != null && s.func_77973_b() == TCG.itemCard) {
            cards.add(s);
         }
      }

      List cards2 = LogicHelper.getCards(cards, this.field_146297_k.field_71439_g);
      this.field_146297_k.func_110434_K().func_110577_a(LogicHelper.validateDeckInHolder(cards2));
      GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 + 92), (double)(this.field_146295_m / 2 - 48), 10.0, 16.0F, 0.0, 0.0, 1.0, 1.0, 1.0F);
   }
}
