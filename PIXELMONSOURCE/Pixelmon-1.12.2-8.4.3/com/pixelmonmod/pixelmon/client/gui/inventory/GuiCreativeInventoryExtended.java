package com.pixelmonmod.pixelmon.client.gui.inventory;

import com.google.common.collect.Ordering;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class GuiCreativeInventoryExtended extends GuiContainerCreative implements IInventoryPixelmon {
   private InventoryPixelmon inventory = new InventoryPixelmon(this, 53);

   public GuiCreativeInventoryExtended(EntityPlayer player) {
      super(player);
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.inventory.initGui();
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.inventory.drawScreen(mouseX, mouseY, partialTicks);
      if (this.field_147045_u) {
         this.func_147044_g();
      }

   }

   public void superDrawScreen(int mouseX, int mouseY, float partialTicks) {
      super.func_73863_a(mouseX, mouseY, partialTicks);
   }

   protected void func_175378_g() {
      boolean hasVisibleEffect = false;
      Iterator var2 = this.field_146297_k.field_71439_g.func_70651_bq().iterator();

      while(var2.hasNext()) {
         PotionEffect potioneffect = (PotionEffect)var2.next();
         Potion potion = potioneffect.func_188419_a();
         if (potion.shouldRender(potioneffect)) {
            hasVisibleEffect = true;
            break;
         }
      }

      if (!this.field_146297_k.field_71439_g.func_70651_bq().isEmpty() && hasVisibleEffect) {
         this.field_147045_u = true;
      } else {
         this.field_147003_i = (this.field_146294_l - this.field_146999_f) / 2;
         this.field_147045_u = false;
      }

   }

   public void func_147044_g() {
      int i = this.field_147003_i + 196;
      int j = this.field_147009_r + 1;
      Collection collection = this.field_146297_k.field_71439_g.func_70651_bq();
      if (!collection.isEmpty()) {
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.func_179140_f();
         int l = 33;
         if (collection.size() > 5) {
            l = 132 / (collection.size() - 1);
         }

         Iterator var5 = Ordering.natural().sortedCopy(collection).iterator();

         while(var5.hasNext()) {
            PotionEffect potioneffect = (PotionEffect)var5.next();
            Potion potion = potioneffect.func_188419_a();
            if (potion.shouldRender(potioneffect)) {
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               this.field_146297_k.func_110434_K().func_110577_a(field_147001_a);
               this.func_73729_b(i, j, 0, 166, 109, 32);
               this.func_73729_b(i + 109, j, 115, 166, 6, 32);
               if (potion.func_76400_d()) {
                  int i1 = potion.func_76392_e();
                  this.func_73729_b(i + 6, j + 7, i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
               }

               potion.renderInventoryEffect(i, j, potioneffect, this.field_146297_k);
               if (!potion.shouldRenderInvText(potioneffect)) {
                  j += l;
               } else {
                  String s1 = I18n.func_135052_a(potion.func_76393_a(), new Object[0]);
                  if (potioneffect.func_76458_c() == 1) {
                     s1 = s1 + " " + I18n.func_135052_a("enchantment.level.2", new Object[0]);
                  } else if (potioneffect.func_76458_c() == 2) {
                     s1 = s1 + " " + I18n.func_135052_a("enchantment.level.3", new Object[0]);
                  } else if (potioneffect.func_76458_c() == 3) {
                     s1 = s1 + " " + I18n.func_135052_a("enchantment.level.4", new Object[0]);
                  }

                  this.field_146289_q.func_175063_a(s1, (float)(i + 10 + 18), (float)(j + 6), 16777215);
                  String s = Potion.func_188410_a(potioneffect, 1.0F);
                  this.field_146289_q.func_175063_a(s, (float)(i + 10 + 18), (float)(j + 6 + 10), 8355711);
                  j += l;
               }
            }
         }
      }

   }

   public float getZLevel() {
      return this.field_73735_i;
   }

   public int getGUILeft() {
      return this.field_147003_i;
   }

   public void offsetGUILeft(int offset) {
      this.field_147003_i += offset;
   }

   protected void func_146976_a(float partialTicks, int mouseX, int mouseY) {
      super.func_146976_a(partialTicks, mouseX, mouseY);
      GlStateManager.func_179091_B();
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.pixelmonCreativeInventory);
      this.func_73729_b(this.field_147003_i - 91, this.field_146295_m / 2 - 83, 0, 0, 92, 170);
      this.inventory.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
   }

   public void subDrawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
      super.func_73733_a(left, top, right, bottom, startColor, endColor);
   }

   public List getButtonList() {
      return this.field_146292_n;
   }

   protected void func_73864_a(int x, int y, int mouseButton) throws IOException {
      if (this.inventory.mouseClicked(x, y, mouseButton) && this.isOutsideParty(x, y)) {
         try {
            super.func_73864_a(x, y, mouseButton);
         } catch (IndexOutOfBoundsException var5) {
         }
      }

   }

   public void renderToolTipPublic(ItemStack stack, int mouseX, int mouseY) {
      super.func_146285_a(stack, mouseX, mouseY);
   }

   protected void func_146286_b(int mouseX, int mouseY, int state) {
      if (this.isOutsideParty(mouseX, mouseY)) {
         super.func_146286_b(mouseX, mouseY, state);
      }

   }

   private boolean isOutsideParty(int x, int y) {
      return !this.isWithinParty(x, y) && !this.isWithinLoreBox(x, y);
   }

   private boolean isWithinParty(int x, int y) {
      int centerW = this.field_146294_l / 2;
      int centerH = this.field_146295_m / 2;
      return x > centerW + -148 && x < centerW + -100 && y > centerH + -84 && y < centerH + 60;
   }

   private boolean isWithinLoreBox(int x, int y) {
      int centerW = this.field_146294_l / 2;
      int centerH = this.field_146295_m / 2;
      return x > centerW + -188 && x < centerW + -148 && y > centerH + -84 && y < centerH + -42;
   }

   public void func_73876_c() {
      super.func_73876_c();
      this.inventory.tick();
   }
}
