package com.pixelmonmod.pixelmon.client.gui;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.BattleGuiClosed;
import com.pixelmonmod.pixelmon.comm.packetHandlers.itemDrops.ItemDropMode;
import com.pixelmonmod.pixelmon.comm.packetHandlers.itemDrops.ItemDropPacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.itemDrops.ServerItemDropPacket;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity1Base;
import com.pixelmonmod.pixelmon.storage.ClientData;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import org.lwjgl.util.vector.Vector4f;

public class GuiItemDrops extends GuiScreen {
   String s;
   ItemDropPacket drops;
   int mouseOverIndex = -1;
   boolean isFirst = true;

   public GuiItemDrops() {
      this.drops = ServerStorageDisplay.bossDrops;
      ServerStorageDisplay.bossDrops = null;
   }

   public void func_73866_w_() {
      super.func_73866_w_();
   }

   public void func_146278_c(int par1) {
   }

   public void func_146276_q_() {
   }

   public void func_146281_b() {
      super.func_146281_b();
      Pixelmon.network.sendToServer(new BattleGuiClosed());
   }

   public void func_73876_c() {
      super.func_73876_c();
   }

   protected void func_73869_a(char par1, int par2) throws IOException {
   }

   protected void func_73864_a(int i, int j, int par3) {
      int xPos = (this.field_146294_l - 280) / 2 + 180;
      int yPos = (this.field_146295_m - 182) / 2 + 150;
      int buttonWidth = 90;
      int buttonHeight = 20;
      if (i >= xPos && i <= xPos + buttonWidth && j >= yPos && j <= yPos + buttonHeight) {
         Pixelmon.network.sendToServer(new ServerItemDropPacket(ServerItemDropPacket.PacketMode.TakeAllItems));
         this.closeScreen();
      } else {
         xPos = (this.field_146294_l - 280) / 2 + 10;
         yPos = (this.field_146295_m - 182) / 2 + 150;
         if (i >= xPos && i <= xPos + buttonWidth && j >= yPos && j <= yPos + buttonHeight) {
            Pixelmon.network.sendToServer(new ServerItemDropPacket(ServerItemDropPacket.PacketMode.DropAllItems));
            this.closeScreen();
         } else {
            if (this.mouseOverIndex != -1) {
               Pixelmon.network.sendToServer(new ServerItemDropPacket(this.drops.items[this.mouseOverIndex].id));
               this.drops.items[this.mouseOverIndex] = null;
               this.mouseOverIndex = -1;
               int count = 0;

               for(int ind = 0; ind < this.drops.items.length; ++ind) {
                  if (this.drops.items[ind] != null) {
                     ++count;
                  }
               }

               if (count == 0) {
                  this.closeScreen();
                  return;
               }
            }

         }
      }
   }

   private void closeScreen() {
      Minecraft mc;
      if (!ClientProxy.battleManager.evolveList.isEmpty()) {
         mc = Minecraft.func_71410_x();
         mc.func_147108_a(new GuiEvolve());
      } else if (ClientData.openMegaItemGui >= 0) {
         mc = Minecraft.func_71410_x();
         mc.func_147108_a(new GuiMegaItem(ClientData.openMegaItemGui > 0));
      } else {
         GuiHelper.closeScreen();
      }

   }

   public void func_73863_a(int i, int j, float f) {
      int itemSpacingX = 40;
      int itemSpacingY = 30;
      int itemWidth = 24;
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.func_179147_l();
      Vector4f colour = new Vector4f(0.1F, 0.1F, 0.1F, 0.6F);
      GuiHelper.drawGradientRect((this.field_146294_l - 280) / 2, (this.field_146295_m - 182) / 2, this.field_73735_i, (this.field_146294_l - 280) / 2 + 280, (this.field_146295_m - 182) / 2 + 182, colour, colour, false);
      colour = new Vector4f(0.1F, 0.2F, 0.23F, 0.6F);
      Vector4f colour2 = new Vector4f(0.1F, 0.14F, 0.2F, 0.6F);
      GuiHelper.drawGradientRect((this.field_146294_l - 280) / 2, (this.field_146295_m - 182) / 2, this.field_73735_i, (this.field_146294_l - 280) / 2 + 280, (this.field_146295_m - 182) / 2 + 30, colour, colour2, false);
      if (this.drops.hasCustomTitle) {
         this.s = this.drops.customTitle.func_150260_c();
      } else if (this.drops.mode == ItemDropMode.Boss) {
         this.s = I18n.func_135052_a("gui.guiItemDrops.beatBossPixelmon1", new Object[]{Entity1Base.getLocalizedName(ClientProxy.battleManager.displayedEnemyPokemon[0].species.name)});
      } else if (this.drops.mode == ItemDropMode.NormalTrainer) {
         this.s = I18n.func_135052_a("gui.guiItemDrops.beatTrainer1", new Object[0]);
      }

      this.field_146297_k.field_71466_p.func_78276_b(this.s, (this.field_146294_l - 280) / 2 + 10, (this.field_146295_m - 182) / 2 + 15, 16777215);
      int xPos = (this.field_146294_l - 280) / 2 + 180;
      int yPos = (this.field_146295_m - 182) / 2 + 150;
      int buttonWidth = 90;
      int buttonHeight = 20;
      if (i >= xPos && i <= xPos + buttonWidth && j >= yPos && j <= yPos + buttonWidth) {
         colour = new Vector4f(1.0F, 1.0F, 1.0F, 1.4F);
      } else {
         colour = new Vector4f(0.0F, 0.0F, 0.0F, 0.4F);
      }

      GuiHelper.drawGradientRect(xPos, yPos, this.field_73735_i, xPos + buttonWidth, yPos + buttonHeight, colour, colour, false);
      this.s = I18n.func_135052_a("gui.guiItemDrops.takeAll", new Object[0]);
      GuiHelper.drawCenteredString(this.s, (float)(xPos + buttonWidth / 2), (float)(yPos + 7), 16777215);
      xPos = (this.field_146294_l - 280) / 2 + 10;
      yPos = (this.field_146295_m - 182) / 2 + 150;
      if (i >= xPos && i <= xPos + buttonWidth && j >= yPos && j <= yPos + buttonWidth) {
         colour = new Vector4f(1.0F, 1.0F, 1.0F, 1.4F);
      } else {
         colour = new Vector4f(0.0F, 0.0F, 0.0F, 0.4F);
      }

      GuiHelper.drawGradientRect(xPos, yPos, this.field_73735_i, xPos + buttonWidth, yPos + buttonHeight, colour, colour, false);
      this.s = I18n.func_135052_a("gui.guiItemDrops.drop", new Object[0]);
      GuiHelper.drawCenteredString(this.s, (float)(xPos + buttonWidth / 2), (float)(yPos + 7), 16777215);
      int x = false;
      int y = false;
      colour = new Vector4f(0.1F, 0.1F, 0.1F, 0.3F);
      int x = (this.field_146294_l - 280) / 2 + 5;
      int y = (this.field_146295_m - 182) / 2 + 35;
      GuiHelper.drawGradientRect(x, y, this.field_73735_i, x + 165, y + 105, colour, colour, false);
      y = 0;
      x = 0;
      this.mouseOverIndex = -1;

      int d;
      for(d = 0; d < this.drops.items.length; ++d) {
         xPos = (this.field_146294_l - 280) / 2 + 15 + x * itemSpacingX;
         yPos = (this.field_146295_m - 182) / 2 + 45 + y * itemSpacingY;
         if (i >= xPos && i <= xPos + itemWidth && j >= yPos && j <= yPos + itemWidth) {
            if (this.drops.items[d] != null) {
               colour = new Vector4f(1.0F, 1.0F, 1.0F, 1.4F);
               this.mouseOverIndex = d;
            }
         } else {
            colour = new Vector4f(0.0F, 0.0F, 0.0F, 0.4F);
         }

         GuiHelper.drawGradientRect(xPos, yPos, this.field_73735_i, xPos + itemWidth, yPos + itemWidth, colour, colour, false);
         ++x;
         if (x > 3) {
            x = 0;
            ++y;
         }
      }

      RenderHelper.func_74520_c();
      colour = new Vector4f(0.1F, 0.1F, 0.1F, 0.7F);
      d = (this.field_146294_l - 280) / 2 + 175;
      int itemYPos = (this.field_146295_m - 182) / 2 + 35;
      GuiHelper.drawGradientRect(d, itemYPos, this.field_73735_i, d + 100, itemYPos + 105, colour, colour, false);
      if (this.mouseOverIndex != -1) {
         this.s = this.drops.items[this.mouseOverIndex].itemStack.func_82833_r();
         GuiHelper.drawCenteredString(this.s, (float)((this.field_146294_l - 280) / 2 + 225), (float)(itemYPos + 5), 16777215);
         GuiHelper.drawCenteredString(I18n.func_135052_a("gui.guiItemDrops.click", new Object[0]), (float)((this.field_146294_l - 280) / 2 + 225), (float)(itemYPos + 90), 16777215);
         d = (this.field_146294_l / 3 - 93) / 2 + 69;
         itemYPos = (this.field_146295_m / 3 - 60) / 2 + 21;
         GlStateManager.func_179152_a(3.0F, 3.0F, 3.0F);
         this.field_146296_j.func_180450_b(this.drops.items[this.mouseOverIndex].itemStack, d, itemYPos);
         this.field_146296_j.func_180453_a(this.field_146297_k.field_71466_p, this.drops.items[this.mouseOverIndex].itemStack, d, itemYPos, (String)null);
         GlStateManager.func_179152_a(0.33333334F, 0.33333334F, 0.33333334F);
         this.isFirst = false;
      } else if (this.isFirst) {
         this.s = I18n.func_135052_a("gui.guiItemDrops.mouse", new Object[0]);
         this.field_146297_k.field_71466_p.func_78276_b(this.s, (this.field_146294_l - 280) / 2 + 228 - this.field_146297_k.field_71466_p.func_78256_a(this.s) / 2, (this.field_146295_m - 182) / 2 + 100, 8947848);
         this.s = I18n.func_135052_a("gui.guiItemDrops.see", new Object[0]);
         this.field_146297_k.field_71466_p.func_78276_b(this.s, (this.field_146294_l - 280) / 2 + 228 - this.field_146297_k.field_71466_p.func_78256_a(this.s) / 2, (this.field_146295_m - 182) / 2 + 114, 8947848);
         this.s = I18n.func_135052_a("gui.guiItemDrops.details", new Object[0]);
         this.field_146297_k.field_71466_p.func_78276_b(this.s, (this.field_146294_l - 280) / 2 + 228 - this.field_146297_k.field_71466_p.func_78256_a(this.s) / 2, (this.field_146295_m - 182) / 2 + 128, 8947848);
      }

      y = 0;
      x = 0;

      for(int d = 0; d < this.drops.items.length; ++d) {
         if (this.drops.items[d] != null) {
            xPos = (this.field_146294_l - 280) / 2 + 15 + x * itemSpacingX;
            yPos = (this.field_146295_m - 182) / 2 + 45 + y * itemSpacingY;
            this.field_146296_j.func_180450_b(this.drops.items[d].itemStack, xPos + 4, yPos + 3);
            this.field_146296_j.func_180453_a(this.field_146297_k.field_71466_p, this.drops.items[d].itemStack, xPos + 4, yPos + 3, (String)null);
         }

         ++x;
         if (x > 3) {
            x = 0;
            ++y;
         }
      }

   }
}
