package com.pixelmonmod.pixelmon.client.gui.pokechecker;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.comm.packetHandlers.SetDisplayRibbon;
import com.pixelmonmod.pixelmon.enums.EnumRibbonType;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiScreenPokeCheckerRibbons extends GuiScreenPokeChecker {
   public GuiScreenPokeCheckerRibbons(GuiScreenPokeChecker tab) {
      super(tab);
   }

   public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.summaryRibbons);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      this.func_73729_b((this.field_146294_l - this.xSize) / 2 - 40, (this.field_146295_m - this.ySize) / 2 - 25, 0, 0, 256, 205);
      this.drawPokemonName();
      this.drawArrows(mouseX, mouseY);
   }

   public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
      GL11.glNormal3f(0.0F, -1.0F, 0.0F);
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      if (!this.pokemon.isEgg()) {
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.lvl", new Object[0]) + " " + this.pokemon.getLevel(), 10, -14, 16777215);
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.number", new Object[0]) + " " + this.pokemon.getSpecies().getNationalPokedexNumber(), -30, -14, 16777215);
         int dispC = 0;
         int imgSize = 25;
         int cols = 6;
         Iterator var6 = this.pokemon.getRibbons().iterator();

         while(var6.hasNext()) {
            EnumRibbonType t = (EnumRibbonType)var6.next();
            double y = -18.5 + (double)((imgSize + 5) * (dispC / cols));
            double x = (double)(59 + imgSize * (dispC % cols));
            GuiHelper.drawScaledImage(new ResourceLocation("pixelmon", "textures/gui/ribbons/" + t.toString().toLowerCase() + ".png"), 1.0, x, y, (double)(imgSize - 3), (double)(imgSize - 3), this.field_73735_i);
            if (t == this.pokemon.getDisplayedRibbon()) {
               GuiHelper.drawScaledImage(new ResourceLocation("pixelmon", "textures/gui/ribbons/selected.png"), 1.0, x, y, (double)(imgSize - 3), (double)(imgSize - 3), this.field_73735_i);
            }

            ++dispC;
            if (dispC / cols == 4) {
               break;
            }
         }

         GuiHelper.drawSquashedString(this.field_146297_k.field_71466_p, I18n.func_135052_a("ribbon." + this.pokemon.getDisplayedRibbon().toString().toLowerCase() + ".name", new Object[0]), false, 115.0, -33.0F, 112.0F, 16777215, true);
         String title = I18n.func_135052_a("ribbon." + this.pokemon.getDisplayedRibbon().toString().toLowerCase() + ".title", new Object[]{this.pokemon.getDisplayName()});
         int strWidth = Math.min(115, this.field_146297_k.field_71466_p.func_78256_a(title));
         GuiHelper.drawSquashedString(this.field_146297_k.field_71466_p, title, false, 115.0, (float)(207 - strWidth), 112.0F, 16755200, true);
         this.field_146297_k.field_71466_p.func_78279_b(I18n.func_135052_a("ribbon." + this.pokemon.getDisplayedRibbon().toString().toLowerCase() + ".description", new Object[0]), -30, 128, 235, 16777215);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      } else {
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.lvl", new Object[0]) + " ???", 10, -14, 16777215);
         this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.number", new Object[0]) + " ???", -30, -14, 16777215);
      }

      this.drawBasePokemonInfo();
      GlStateManager.func_179084_k();
   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.func_73864_a(mouseX, mouseY, mouseButton);
      double x = (double)(this.field_146294_l / 2 - mouseX);
      double y = (double)(this.field_146295_m / 2 - mouseY);
      if (x < 30.0 && x > -118.0 && y < 101.0 && y > -9.0) {
         x -= 30.0;
         x *= -1.0;
         y -= 100.0;
         y *= -1.0;
         int index = (int)(Math.floor(x / 24.5) + Math.floor(y / 27.0) * 6.0);
         if (this.pokemon.getRibbons().size() > index) {
            EnumRibbonType sel = (EnumRibbonType)this.pokemon.getRibbons().get(index);
            if (sel == this.pokemon.getDisplayedRibbon()) {
               Pixelmon.network.sendToServer(new SetDisplayRibbon(this.position, this.pokemon.getUUID(), EnumRibbonType.NONE));
            } else {
               Pixelmon.network.sendToServer(new SetDisplayRibbon(this.position, this.pokemon.getUUID(), sel));
            }
         }
      }

   }
}
