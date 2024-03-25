package com.pixelmonmod.pixelmon.client.gui.curryDex;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.enums.EnumBerryFlavor;
import com.pixelmonmod.pixelmon.enums.EnumCurryKey;
import com.pixelmonmod.pixelmon.enums.EnumCurryRating;
import com.pixelmonmod.pixelmon.util.helpers.CommonHelper;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.translation.I18n;
import org.lwjgl.opengl.GL11;

public class GuiCurryDex extends GuiScreen {
   private int centerX;
   private int centerY;
   private float guiHeight;
   private float guiWidth;
   private BiMap buttons = HashBiMap.create();
   private byte[][] data = new byte[26][6];
   private boolean[] anySeen = new boolean[EnumCurryKey.values().length];
   private int page = 0;

   public GuiCurryDex(int[] data) {
      EnumCurryKey[] var2 = EnumCurryKey.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumCurryKey key = var2[var4];
         this.anySeen[key.ordinal()] = data[key.ordinal()] > 0;
         this.data[key.ordinal()] = CommonHelper.decodeInteger(data[key.ordinal()], 3);
      }

   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.centerX = this.field_146294_l / 2;
      this.centerY = this.field_146295_m / 2;
      this.guiHeight = 220.0F;
      this.guiWidth = this.guiHeight * 1.23333F;

      for(byte i = 0; i < 13; ++i) {
         this.buttons.put(i, this.func_189646_b(new GuiButton(i, 0, (int)((float)this.centerY - this.guiHeight / 2.0F + (float)(i * 16)) + 7, 10, 14, "")));
      }

   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      GuiHelper.bindFontRenderer("pixelmon:textures/gui/pokemonfont.png", true);
      this.drawScreenBackground();
      GL11.glPushMatrix();
      this.drawScreenForeground(mouseX, mouseY);
      GL11.glPopMatrix();
      GuiHelper.resetFontRenderer();
   }

   private void drawScreenForeground(int mouseX, int mouseY) {
      float x = (float)this.centerX - this.guiWidth / 2.0F + 14.5F;
      EnumCurryKey right = EnumCurryKey.values()[this.page * 2];
      int var7;
      if (right != null) {
         EnumBerryFlavor[] var5 = EnumBerryFlavor.values();
         int var6 = var5.length;

         for(var7 = 0; var7 < var6; ++var7) {
            EnumBerryFlavor flavor = var5[var7];
            this.field_146297_k.field_71446_o.func_110577_a(right.getDishTexture());
            GuiHelper.drawImageQuad((double)(x + 177.0F), (double)((int)((float)this.centerY - this.guiHeight / 2.0F - 5.0F) + 13), 20.0, 20.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
            if (this.data[right.ordinal()][flavor.ordinal()] > 0) {
               switch (this.data[right.ordinal()][flavor.ordinal()]) {
                  case 1:
                     this.field_146297_k.field_71446_o.func_110577_a(GuiResources.curryClassKoffing);
                     break;
                  case 2:
                     this.field_146297_k.field_71446_o.func_110577_a(GuiResources.curryClassWobbuffet);
                     break;
                  case 3:
                     this.field_146297_k.field_71446_o.func_110577_a(GuiResources.curryClassCopperajah);
                     break;
                  case 4:
                     this.field_146297_k.field_71446_o.func_110577_a(GuiResources.curryClassMilcery);
                     break;
                  case 5:
                     this.field_146297_k.field_71446_o.func_110577_a(GuiResources.curryClassCharizard);
                     break;
                  default:
                     this.field_146297_k.field_71446_o.func_110577_a(GuiResources.curryClassBlank);
               }

               GuiHelper.drawImageQuad((double)(x + 130.0F), (double)((int)((float)this.centerY - this.guiHeight / 2.0F - 5.0F) + 38 + 30 * flavor.ordinal()), 20.0, 20.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
               GuiHelper.drawScaledString(I18n.func_74837_a("item.dish_curry_" + right.toString().toLowerCase() + ".name", new Object[]{I18n.func_74838_a("berry.flavor." + flavor.name().toLowerCase() + ".name")}), x + 155.0F, (float)((int)((float)this.centerY - this.guiHeight / 2.0F - 5.0F) + 40 + 30 * flavor.ordinal()), -7845363, 8.0F);
               String[] split = GuiHelper.splitStringToFit(I18n.func_74837_a("gui.currydex." + right.toString().toLowerCase() + "." + flavor.name().toLowerCase(), new Object[0]), 6, 70).split("\n");

               for(int i = 0; i < split.length; ++i) {
                  GuiHelper.drawScaledString(split[i], x + 155.0F, (float)((int)((float)this.centerY - this.guiHeight / 2.0F - 5.0F) + 45 + 30 * flavor.ordinal()) + (float)i * 4.5F, -7845363, 6.0F);
               }
            } else {
               this.field_146297_k.field_71446_o.func_110577_a(GuiResources.curryClassBlank);
               GuiHelper.drawImageQuad((double)(x + 130.0F), (double)((int)((float)this.centerY - this.guiHeight / 2.0F - 5.0F) + 38 + 30 * flavor.ordinal()), 20.0, 20.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
               GuiHelper.drawScaledString("???", x + 155.0F, (float)((int)((float)this.centerY - this.guiHeight / 2.0F - 5.0F) + 46 + 30 * flavor.ordinal()), -7845363, 8.0F);
            }
         }
      }

      EnumCurryKey left = this.page > 0 ? EnumCurryKey.values()[this.page * 2 - 1] : null;
      int i;
      int var16;
      String[] split;
      if (left != null) {
         EnumBerryFlavor[] var13 = EnumBerryFlavor.values();
         var7 = var13.length;

         for(var16 = 0; var16 < var7; ++var16) {
            EnumBerryFlavor flavor = var13[var16];
            this.field_146297_k.field_71446_o.func_110577_a(left.getDishTexture());
            GuiHelper.drawImageQuad((double)(x + 47.0F), (double)((int)((float)this.centerY - this.guiHeight / 2.0F - 5.0F) + 13), 20.0, 20.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
            if (this.data[left.ordinal()][flavor.ordinal()] > 0) {
               switch (this.data[left.ordinal()][flavor.ordinal()]) {
                  case 1:
                     this.field_146297_k.field_71446_o.func_110577_a(GuiResources.curryClassKoffing);
                     break;
                  case 2:
                     this.field_146297_k.field_71446_o.func_110577_a(GuiResources.curryClassWobbuffet);
                     break;
                  case 3:
                     this.field_146297_k.field_71446_o.func_110577_a(GuiResources.curryClassCopperajah);
                     break;
                  case 4:
                     this.field_146297_k.field_71446_o.func_110577_a(GuiResources.curryClassMilcery);
                     break;
                  case 5:
                     this.field_146297_k.field_71446_o.func_110577_a(GuiResources.curryClassCharizard);
                     break;
                  default:
                     this.field_146297_k.field_71446_o.func_110577_a(GuiResources.curryClassBlank);
               }

               GuiHelper.drawImageQuad((double)x, (double)((int)((float)this.centerY - this.guiHeight / 2.0F - 5.0F) + 38 + 30 * flavor.ordinal()), 20.0, 20.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
               GuiHelper.drawScaledString(I18n.func_74837_a("item.dish_curry_" + left.toString().toLowerCase() + ".name", new Object[]{I18n.func_74838_a("berry.flavor." + flavor.name().toLowerCase() + ".name")}), x + 25.0F, (float)((int)((float)this.centerY - this.guiHeight / 2.0F - 5.0F) + 40 + 30 * flavor.ordinal()), -7845363, 8.0F);
               split = GuiHelper.splitStringToFit(I18n.func_74837_a("gui.currydex." + left.toString().toLowerCase() + "." + flavor.name().toLowerCase(), new Object[0]), 6, 70).split("\n");

               for(i = 0; i < split.length; ++i) {
                  GuiHelper.drawScaledString(split[i], x + 25.0F, (float)((int)((float)this.centerY - this.guiHeight / 2.0F - 5.0F) + 45 + 30 * flavor.ordinal()) + (float)i * 4.5F, -7845363, 6.0F);
               }
            } else {
               this.field_146297_k.field_71446_o.func_110577_a(GuiResources.curryClassBlank);
               GuiHelper.drawImageQuad((double)x, (double)((int)((float)this.centerY - this.guiHeight / 2.0F - 5.0F) + 38 + 30 * flavor.ordinal()), 20.0, 20.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
               GuiHelper.drawScaledString("???", x + 25.0F, (float)((int)((float)this.centerY - this.guiHeight / 2.0F - 5.0F) + 46 + 30 * flavor.ordinal()), -7845363, 8.0F);
            }
         }
      } else {
         EnumCurryRating[] var14 = EnumCurryRating.values();
         var7 = var14.length;

         for(var16 = 0; var16 < var7; ++var16) {
            EnumCurryRating rating = var14[var16];
            switch (rating.ordinal()) {
               case 1:
                  this.field_146297_k.field_71446_o.func_110577_a(GuiResources.curryClassWobbuffet);
                  break;
               case 2:
                  this.field_146297_k.field_71446_o.func_110577_a(GuiResources.curryClassMilcery);
                  break;
               case 3:
                  this.field_146297_k.field_71446_o.func_110577_a(GuiResources.curryClassCopperajah);
                  break;
               case 4:
                  this.field_146297_k.field_71446_o.func_110577_a(GuiResources.curryClassCharizard);
                  break;
               default:
                  this.field_146297_k.field_71446_o.func_110577_a(GuiResources.curryClassKoffing);
            }

            GuiHelper.drawImageQuad((double)x, (double)((int)((float)this.centerY - this.guiHeight / 2.0F - 5.0F) + 18 + 40 * rating.ordinal()), 20.0, 20.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
            GuiHelper.drawScaledString(I18n.func_74837_a("gui.currydex." + rating.name().toLowerCase() + ".name", new Object[0]), x + 25.0F, (float)((int)((float)this.centerY - this.guiHeight / 2.0F - 5.0F) + 20 + 40 * rating.ordinal()), -7845363, 8.0F);
            split = I18n.func_74837_a("gui.currydex." + rating.name().toLowerCase() + ".desc", new Object[0]).split("\\\\n");

            for(i = 0; i < split.length; ++i) {
               GuiHelper.drawScaledString(split[i], x + 25.0F, (float)((int)((float)this.centerY - this.guiHeight / 2.0F - 5.0F) + 25 + 40 * rating.ordinal() + 5 * i), -7845363, 8.0F);
            }
         }
      }

      Iterator var15 = this.buttons.values().iterator();

      while(var15.hasNext()) {
         GuiButton button = (GuiButton)var15.next();
         if (button.field_146127_k < this.page) {
            button.field_146128_h = (int)x - 20;
            this.field_146297_k.field_71446_o.func_110577_a(GuiResources.curryDexTabL);
         } else {
            button.field_146128_h = (int)(x + this.guiWidth - 18.0F);
            this.field_146297_k.field_71446_o.func_110577_a(GuiResources.curryDexTabR);
         }

         if (button.field_146127_k == this.page) {
            this.field_146297_k.field_71446_o.func_110577_a(GuiResources.curryDexTab);
         }

         GuiHelper.drawImageQuad((double)button.field_146128_h, (double)button.field_146129_i, (double)button.field_146120_f, (float)button.field_146121_g, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         if (button.field_146127_k > 0) {
            this.field_146297_k.field_71446_o.func_110577_a(EnumCurryKey.values()[button.field_146127_k * 2 - 1].getDishTexture());
            GuiHelper.drawImageQuad((double)(button.field_146128_h + 2), (double)button.field_146129_i, (double)(button.field_146121_g / 2), (float)(button.field_146121_g / 2), 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
         }

         this.field_146297_k.field_71446_o.func_110577_a(EnumCurryKey.values()[button.field_146127_k * 2].getDishTexture());
         GuiHelper.drawImageQuad((double)(button.field_146128_h + 2), (double)(button.field_146129_i + button.field_146121_g / 2), (double)(button.field_146121_g / 2), (float)(button.field_146121_g / 2), 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      }

   }

   public void drawScreenBackground() {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.curryDexBackground);
      GuiHelper.drawImageQuad((double)((float)this.centerX - this.guiWidth / 2.0F), (double)((float)this.centerY - this.guiHeight / 2.0F), (double)this.guiWidth, this.guiHeight, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
   }

   public void func_146274_d() throws IOException {
      super.func_146274_d();
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      this.page = (Byte)this.buttons.inverse().get(button);
   }
}
