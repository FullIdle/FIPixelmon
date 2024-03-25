package com.pixelmonmod.pixelmon.client.gui;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.SetMegaItem;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.BattleGuiClosed;
import com.pixelmonmod.pixelmon.enums.EnumMegaItem;
import com.pixelmonmod.pixelmon.storage.ClientData;
import com.pixelmonmod.pixelmon.storage.extras.PixelExtrasData;
import com.pixelmonmod.pixelmon.storage.extras.PlayerExtraDataStore;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.vector.Vector4f;

public class GuiMegaItem extends GuiScreen {
   private static final ResourceLocation DYNAMAX_BAND = new ResourceLocation("pixelmon", "textures/gui/megaItems/dynamax_band.png");
   private static final ResourceLocation MEGA_BRACELET_ORAS = new ResourceLocation("pixelmon", "textures/gui/megaItems/megabraceletoras.png");
   private static final ResourceLocation MEGA_NECKLESS = new ResourceLocation("pixelmon", "textures/gui/megaItems/meganeckless.png");
   private static final ResourceLocation MEGA_GLASSES = new ResourceLocation("pixelmon", "textures/gui/megaItems/mega_glasses.png");
   private static final ResourceLocation MEGA_ANCHOR = new ResourceLocation("pixelmon", "textures/gui/megaItems/mega_anchor.png");
   private static final ResourceLocation MEGA_TIARA = new ResourceLocation("pixelmon", "textures/gui/megaItems/mega_tiara.png");
   private final boolean isDynamax;

   public GuiMegaItem(boolean isDynamax) {
      this.isDynamax = isDynamax;
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      GuiHelper.drawGradientRect(0, 0, this.field_73735_i, this.field_146294_l, this.field_146295_m, new Vector4f(0.0F, 0.0F, 0.0F, 0.4F), new Vector4f(0.0F, 0.0F, 0.0F, 0.4F), true);
      int centerX = this.field_146294_l / 2;
      int centerY = this.field_146295_m / 2;
      String s = I18n.func_135052_a(this.isDynamax ? "gui.dynamaxitem.message" : "gui.megaitem.message", new Object[0]);
      this.func_73732_a(this.field_146297_k.field_71466_p, s, centerX, centerY - 75, 16777215);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      if (this.isDynamax) {
         GuiHelper.bindTexture(DYNAMAX_BAND);
         this.drawIcon(50, -10, mouseX, mouseY);
         this.field_146297_k.field_71446_o.func_110577_a(GuiResources.noItem);
         this.drawIcon(-40, -10, mouseX, mouseY);
      } else {
         this.field_146297_k.field_71446_o.func_110577_a(MEGA_BRACELET_ORAS);
         this.drawIcon(-80, -10, mouseX, mouseY);
         this.field_146297_k.field_71446_o.func_110577_a(MEGA_GLASSES);
         this.drawIcon(5, -10, mouseX, mouseY);
         this.field_146297_k.field_71446_o.func_110577_a(MEGA_ANCHOR);
         this.drawIcon(90, -10, mouseX, mouseY);
         PixelExtrasData extras = PlayerExtraDataStore.get((EntityPlayer)Minecraft.func_71410_x().field_71439_g);
         if (!extras.hasBoostedNecklace()) {
            this.field_146297_k.field_71446_o.func_110577_a(MEGA_TIARA);
            this.drawIcon(-40, 70, mouseX, mouseY);
            this.field_146297_k.field_71446_o.func_110577_a(GuiResources.noItem);
            this.drawIcon(50, 70, mouseX, mouseY);
         } else {
            this.field_146297_k.field_71446_o.func_110577_a(MEGA_TIARA);
            this.drawIcon(-80, 70, mouseX, mouseY);
            this.field_146297_k.field_71446_o.func_110577_a(MEGA_NECKLESS);
            this.drawIcon(5, 70, mouseX, mouseY);
            this.field_146297_k.field_71446_o.func_110577_a(GuiResources.noItem);
            this.drawIcon(90, 70, mouseX, mouseY);
         }
      }

   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
      if (this.isDynamax) {
         this.checkIconClick(50, -10, mouseX, mouseY, EnumMegaItem.DynamaxBand);
         this.checkIconClick(-40, -10, mouseX, mouseY, EnumMegaItem.None);
      } else {
         this.checkIconClick(-80, -10, mouseX, mouseY, EnumMegaItem.BraceletORAS);
         this.checkIconClick(5, -10, mouseX, mouseY, EnumMegaItem.MegaGlasses);
         this.checkIconClick(90, -10, mouseX, mouseY, EnumMegaItem.MegaAnchor);
         PixelExtrasData extras = PlayerExtraDataStore.get((EntityPlayer)Minecraft.func_71410_x().field_71439_g);
         if (!extras.hasBoostedNecklace()) {
            this.checkIconClick(-40, 70, mouseX, mouseY, EnumMegaItem.MegaTiara);
            this.checkIconClick(50, 70, mouseX, mouseY, EnumMegaItem.None);
         } else {
            this.checkIconClick(-80, 70, mouseX, mouseY, EnumMegaItem.MegaTiara);
            this.checkIconClick(5, 70, mouseX, mouseY, EnumMegaItem.BoostNecklace);
            this.checkIconClick(90, 70, mouseX, mouseY, EnumMegaItem.None);
         }
      }

   }

   public boolean func_73868_f() {
      return false;
   }

   public void func_146281_b() {
      ClientData.openMegaItemGui = -1;
      super.func_146281_b();
      Pixelmon.network.sendToServer(new BattleGuiClosed());
   }

   private void drawIcon(int x, int y, int mouseX, int mouseY) {
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      int centerX = this.field_146294_l / 2;
      int centerY = this.field_146295_m / 2;
      int buttonSize = 70;
      int buttonLeft = centerX - buttonSize / 2 + x;
      int buttonTop = centerY - buttonSize / 2 + y;
      if (mouseX >= buttonLeft && mouseX <= buttonLeft + buttonSize && mouseY >= buttonTop && mouseY <= buttonTop + buttonSize) {
         GuiHelper.drawGradientRect(buttonLeft, buttonTop, this.field_73735_i, buttonLeft + buttonSize, buttonTop + buttonSize, new Vector4f(0.8F, 0.8F, 0.8F, 1.0F), new Vector4f(1.0F, 1.0F, 1.0F, 1.0F), true);
      } else {
         GuiHelper.drawGradientRect(buttonLeft, buttonTop, this.field_73735_i, buttonLeft + buttonSize, buttonTop + buttonSize, new Vector4f(0.2F, 0.2F, 0.2F, 1.0F), new Vector4f(0.0F, 0.0F, 0.0F, 1.0F), true);
      }

      GuiHelper.drawImageQuad((double)(buttonLeft + 3), (double)(buttonTop + 3), (double)(buttonSize - 6), (float)(buttonSize - 6), 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
   }

   private void checkIconClick(int x, int y, int mouseX, int mouseY, EnumMegaItem item) {
      int centerX = this.field_146294_l / 2;
      int centerY = this.field_146295_m / 2;
      int buttonSize = 70;
      int buttonLeft = centerX - buttonSize / 2 + x;
      int buttonTop = centerY - buttonSize / 2 + y;
      if (mouseX >= buttonLeft && mouseX <= buttonLeft + buttonSize && mouseY >= buttonTop && mouseY <= buttonTop + buttonSize) {
         if (item == EnumMegaItem.BoostNecklace) {
            PixelExtrasData extras = PlayerExtraDataStore.get((EntityPlayer)Minecraft.func_71410_x().field_71439_g);
            if (!extras.hasBoostedNecklace()) {
               this.field_146297_k.field_71439_g.func_71053_j();
               return;
            }
         }

         Pixelmon.network.sendToServer(new SetMegaItem(item));
         this.field_146297_k.field_71439_g.func_71053_j();
      }

   }
}
