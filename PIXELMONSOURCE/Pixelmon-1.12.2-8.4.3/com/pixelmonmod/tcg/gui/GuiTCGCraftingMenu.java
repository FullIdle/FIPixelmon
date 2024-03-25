package com.pixelmonmod.tcg.gui;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.tcg.api.card.Energy;
import com.pixelmonmod.tcg.api.card.set.CardSet;
import com.pixelmonmod.tcg.api.registries.CardSetRegistry;
import com.pixelmonmod.tcg.config.TCGConfig;
import com.pixelmonmod.tcg.helper.EssenceHelper;
import com.pixelmonmod.tcg.network.PacketHandler;
import com.pixelmonmod.tcg.network.packets.EssencePurchasePacket;
import com.pixelmonmod.tcg.network.packets.EssenceSyncRequestPacket;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;

public class GuiTCGCraftingMenu extends GuiScreen {
   private GuiButton random;
   private GuiButton baseSet;
   private GuiButton jungle;
   private GuiButton fossil;
   private GuiButton teamRocket;
   private GuiButton craft;
   private final boolean DEBUG;
   private final ResourceLocation backgroundTexture;
   private CardSet selectedSet;

   public GuiTCGCraftingMenu() {
      this.DEBUG = Pixelmon.devEnvironment;
      this.backgroundTexture = new ResourceLocation("tcg", "gui/essence/back.png");
      this.selectedSet = CardSetRegistry.get(0);
   }

   public void func_73866_w_() {
      this.field_146292_n.clear();
      int xOffset = this.field_146294_l / 2 - 142;
      int yOffset = this.field_146295_m / 2 - 78;
      int togglebuttonWidth = 20;
      int togglebuttonHeight = 20;
      int buttonID = 0;
      int spacingXC = 22;
      int spacingYC = 22;
      int spacingX = 22;
      int spacingY = 22;
      this.random = new GuiButton(buttonID++, xOffset + spacingXC * 4, yOffset + spacingYC * 1, togglebuttonWidth, togglebuttonHeight, "");
      this.field_146292_n.add(this.random);
      if (this.selectedSet == null) {
         this.random.field_146124_l = false;
      }

      this.baseSet = new GuiButton(buttonID++, xOffset + spacingXC * 5, yOffset + spacingYC * 1, togglebuttonWidth, togglebuttonHeight, "");
      this.field_146292_n.add(this.baseSet);
      if (this.selectedSet != null && this.selectedSet.getID() == 1) {
         this.baseSet.field_146124_l = false;
      }

      this.jungle = new GuiButton(buttonID++, xOffset + spacingXC * 6, yOffset + spacingYC * 1, togglebuttonWidth, togglebuttonHeight, "");
      this.field_146292_n.add(this.jungle);
      if (this.selectedSet != null && this.selectedSet.getID() == 2) {
         this.jungle.field_146124_l = false;
      }

      this.fossil = new GuiButton(buttonID++, xOffset + spacingXC * 7, yOffset + spacingYC * 1, togglebuttonWidth, togglebuttonHeight, "");
      this.field_146292_n.add(this.fossil);
      if (this.selectedSet != null && this.selectedSet.getID() == 3) {
         this.fossil.field_146124_l = false;
      }

      this.teamRocket = new GuiButton(buttonID++, xOffset + spacingXC * 8, yOffset + spacingYC * 1, togglebuttonWidth, togglebuttonHeight, "");
      this.field_146292_n.add(this.teamRocket);
      if (this.selectedSet != null && this.selectedSet.getID() == 4) {
         this.teamRocket.field_146124_l = false;
      }

      this.craft = new GuiButton(buttonID++, xOffset + spacingX * 5, yOffset + spacingY * 7, togglebuttonWidth * 3 + 4, togglebuttonHeight, "Create");
      this.field_146292_n.add(this.craft);
      this.craft.field_146124_l = false;
      PacketHandler.net.sendToServer(new EssenceSyncRequestPacket());
   }

   public void func_146276_q_() {
      int xOffset = 20;
      int yOffset = -50;
      int dispWidth = xOffset + 230;
      int dispHeight = yOffset + 290;
      this.field_146297_k.field_71446_o.func_110577_a(this.backgroundTexture);
      Gui.func_146110_a(this.field_146294_l / 2 - 125, this.field_146295_m / 2 - 125 + 40, 0.0F, 0.0F, dispWidth, dispHeight, (float)dispWidth, (float)dispHeight);
   }

   public int getLowestEssenceValue() {
      int curLowest = -1;
      Energy[] var2 = Energy.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Energy e = var2[var4];
         if (e != Energy.RAINBOW) {
            int c = EssenceHelper.getPlayerEssenceFromEnergy(this.field_146297_k.field_71439_g, e);
            if (curLowest == -1 || c < curLowest) {
               curLowest = c;
            }
         }
      }

      return curLowest;
   }

   public void func_73863_a(int mouseX, int mouseY, float f) {
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      this.func_146276_q_();
      int xOffset = this.field_146294_l / 2 - 125 + 13;
      int yOffset = this.field_146295_m / 2 - 100 + 13;
      super.func_73863_a(mouseX, mouseY, f);
      if (this.DEBUG) {
         this.field_146289_q.func_78276_b("X: " + (mouseX - xOffset) + " Y: " + (mouseY - yOffset), mouseX, mouseY, Color.red.getRGB());
      }

      this.field_146289_q.func_78276_b("(c) Pixelmon Reforged", -800, -800, Color.white.getRGB());
      String essexchange = "Essence Card Pack Exchange";
      int essexchangeSize = this.field_146289_q.func_78256_a(essexchange);
      this.field_146289_q.func_175063_a(essexchange, (float)(this.field_146294_l / 2 - essexchangeSize / 2), (float)(yOffset + 10), 16777215);
      this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg", "gui/logo.png"));
      GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - 58), 2.5, 116.0, 61.0F, 0.0, 0.0, 1.0, 1.0, 3.0F);
      this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg", "gui/cards/icons/sets/0.png"));
      GuiHelper.drawImageQuad((double)(this.random.field_146128_h + 2), (double)(this.random.field_146129_i + 2), 16.0, 16.0F, 0.0, 0.0, 1.0, 1.0, 3.0F);
      this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg", "gui/cards/icons/sets/1.png"));
      GuiHelper.drawImageQuad((double)(this.baseSet.field_146128_h + 2), (double)(this.baseSet.field_146129_i + 2), 16.0, 16.0F, 0.0, 0.0, 1.0, 1.0, 3.0F);
      this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg", "gui/cards/icons/sets/2.png"));
      GuiHelper.drawImageQuad((double)(this.jungle.field_146128_h + 2), (double)(this.jungle.field_146129_i + 2), 16.0, 16.0F, 0.0, 0.0, 1.0, 1.0, 3.0F);
      this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg", "gui/cards/icons/sets/3.png"));
      GuiHelper.drawImageQuad((double)(this.fossil.field_146128_h + 2), (double)(this.fossil.field_146129_i + 2), 16.0, 16.0F, 0.0, 0.0, 1.0, 1.0, 3.0F);
      this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg", "gui/cards/icons/sets/4.png"));
      GuiHelper.drawImageQuad((double)(this.teamRocket.field_146128_h + 2), (double)(this.teamRocket.field_146129_i + 2), 16.0, 16.0F, 0.0, 0.0, 1.0, 1.0, 3.0F);
      int dX = 0;
      Energy[] var9 = Energy.values();
      int stringSizeA = var9.length;

      for(int var11 = 0; var11 < stringSizeA; ++var11) {
         Energy e = var9[var11];
         if (e != Energy.RAINBOW) {
            int xS = -8;
            int yS = -43;
            int xpos = xOffset + dX + 2 + xS;
            int ypos = yOffset + 96 + 2 + yS;
            int width = 16;
            int height = 16;
            if (mouseX >= xpos && mouseX <= xpos + width && mouseY >= ypos && mouseY <= ypos + height) {
               String text = StringUtils.capitalize(e.getUnlocalizedName()) + " Essence";
               GlStateManager.func_179094_E();
               GL11.glTranslatef(0.0F, 0.0F, 10.0F);
               GuiHelper.drawString(text, (float)(mouseX - this.field_146289_q.func_78256_a(text) / 2), (float)(mouseY - 15), Color.black.getRGB());
               GlStateManager.func_179121_F();
            }

            GlStateManager.func_179124_c(255.0F, 255.0F, 255.0F);
            this.field_146297_k.field_71446_o.func_110577_a(e.getHighResTexture());
            GuiHelper.drawImageQuad((double)xpos, (double)ypos, (double)width, (float)height, 0.0, 0.0, 1.0, 1.0, 3.0F);
            int amount = EssenceHelper.getPlayerEssenceFromEnergy(this.field_146297_k.field_71439_g, e);
            String amountStr;
            if (amount >= 1000000) {
               amountStr = amount / 1000000 + "m";
            } else if (amount >= 1000) {
               amountStr = amount / 1000 + "k";
            } else {
               amountStr = Integer.toString(amount);
            }

            int stringSize = this.field_146289_q.func_78256_a(amountStr);
            int color = Color.red.getRGB();
            if (this.selectedSet == null && amount >= TCGConfig.getInstance().randomPackEssenceCost) {
               color = Color.white.getRGB();
            }

            if (this.selectedSet != null && amount >= TCGConfig.getInstance().specificPackEssenceCost) {
               color = Color.white.getRGB();
            }

            this.field_146289_q.func_175063_a(amountStr, (float)(xOffset + xS + 10 + dX - stringSize / 2), (float)(yOffset + 96 + 20 + yS), color);
            dX += 22;
         }
      }

      yOffset += 10;
      this.field_146289_q.func_78276_b("(c) Pixelmon Reforged", -800, -800, Color.white.getRGB());
      GlStateManager.func_179084_k();
      this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg", "textures/items/pack.png"));
      GuiHelper.drawImageQuad((double)(xOffset + 88), (double)(yOffset + 74), 48.0, 48.0F, 0.0, 0.0, 1.0, 1.0, 3.0F);
      String stringA = this.selectedSet == null ? "Random Booster Pack" : I18n.func_135052_a(this.selectedSet.getUnlocalizedName().toLowerCase(), new Object[0]) + " Booster Pack";
      stringSizeA = this.field_146289_q.func_78256_a(stringA);
      this.field_146289_q.func_175063_a(stringA, (float)(this.field_146294_l / 2 - stringSizeA / 2), (float)(yOffset + 122), 16777215);
      String stringB = this.selectedSet == null ? "Costs      " + TCGConfig.getInstance().randomPackEssenceCost + " of every essence" : "Costs      " + TCGConfig.getInstance().specificPackEssenceCost + " of every essence";
      int stringSizeB = this.field_146289_q.func_78256_a(stringB);
      this.field_146289_q.func_175063_a(stringB, (float)(this.field_146294_l / 2 - stringSizeB / 2), (float)(yOffset + 138), 16777215);
      GL11.glColor3d(255.0, 255.0, 255.0);
      this.field_146297_k.field_71446_o.func_110577_a(new ResourceLocation("tcg", "gui/cards/icons/rainbow.png"));
      GuiHelper.drawImageQuad((double)(this.field_146294_l / 2 - stringSizeB / 2 + 31), (double)(yOffset + 135), 16.0, 16.0F, 0.0, 0.0, 1.0, 1.0, 3.0F);
      String exchOne = "Exchange the above amount of essence of";
      String exchTwo = "every type to receive the displayed card pack";
      this.field_146289_q.func_78276_b(exchOne, this.field_146294_l / 2 - this.field_146289_q.func_78256_a(exchOne) / 2, yOffset + 180, 139810);
      this.field_146289_q.func_78276_b(exchTwo, this.field_146294_l / 2 - this.field_146289_q.func_78256_a(exchTwo) / 2, yOffset + 190, 139810);
      if (this.selectedSet == null && this.getLowestEssenceValue() >= TCGConfig.getInstance().randomPackEssenceCost) {
         this.craft.field_146124_l = true;
      } else if (this.selectedSet != null && this.getLowestEssenceValue() >= TCGConfig.getInstance().specificPackEssenceCost) {
         this.craft.field_146124_l = true;
      } else {
         this.craft.field_146124_l = false;
      }

   }

   public boolean func_73868_f() {
      return false;
   }

   public void func_73876_c() {
   }

   protected void func_146284_a(GuiButton guibutton) {
      switch (guibutton.field_146127_k) {
         case 0:
            this.selectedSet = null;
            guibutton.field_146124_l = false;
            this.baseSet.field_146124_l = true;
            this.fossil.field_146124_l = true;
            this.jungle.field_146124_l = true;
            this.teamRocket.field_146124_l = true;
            break;
         case 1:
            this.selectedSet = CardSetRegistry.get(1);
            guibutton.field_146124_l = false;
            this.jungle.field_146124_l = true;
            this.fossil.field_146124_l = true;
            this.random.field_146124_l = true;
            this.teamRocket.field_146124_l = true;
            break;
         case 2:
            this.selectedSet = CardSetRegistry.get(2);
            guibutton.field_146124_l = false;
            this.fossil.field_146124_l = true;
            this.baseSet.field_146124_l = true;
            this.random.field_146124_l = true;
            this.teamRocket.field_146124_l = true;
            break;
         case 3:
            this.selectedSet = CardSetRegistry.get(3);
            guibutton.field_146124_l = false;
            this.jungle.field_146124_l = true;
            this.random.field_146124_l = true;
            this.baseSet.field_146124_l = true;
            this.teamRocket.field_146124_l = true;
            break;
         case 4:
            this.selectedSet = CardSetRegistry.get(4);
            guibutton.field_146124_l = false;
            this.jungle.field_146124_l = true;
            this.fossil.field_146124_l = true;
            this.random.field_146124_l = true;
            this.baseSet.field_146124_l = true;
            break;
         case 5:
            PacketHandler.net.sendToServer(new EssencePurchasePacket(this.selectedSet == null ? 0 : this.selectedSet.getID()));
      }

      PacketHandler.net.sendToServer(new EssenceSyncRequestPacket());
   }
}
