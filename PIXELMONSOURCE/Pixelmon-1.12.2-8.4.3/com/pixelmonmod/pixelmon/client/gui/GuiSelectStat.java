package com.pixelmonmod.pixelmon.client.gui;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.SelectStatPacket;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;

public class GuiSelectStat extends GuiScreen {
   private int[] data;
   private GuiButton[] buttons;
   private Pokemon pokemon;

   public GuiSelectStat(int[] data) {
      this.data = data;
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      int centerW = this.field_146294_l / 2;
      int centerH = this.field_146295_m / 2;
      GuiButton hp = this.func_189646_b(new GuiButton(0, centerW - 152, centerH - 10, 100, 20, StatsType.HP.getLocalizedName()));
      GuiButton att = this.func_189646_b(new GuiButton(1, centerW - 50, centerH - 10, 100, 20, StatsType.Attack.getLocalizedName()));
      GuiButton def = this.func_189646_b(new GuiButton(2, centerW + 52, centerH - 10, 100, 20, StatsType.Defence.getLocalizedName()));
      GuiButton spa = this.func_189646_b(new GuiButton(3, centerW - 152, centerH + 10, 100, 20, StatsType.SpecialAttack.getLocalizedName()));
      GuiButton spd = this.func_189646_b(new GuiButton(4, centerW - 50, centerH + 10, 100, 20, StatsType.SpecialDefence.getLocalizedName()));
      GuiButton spe = this.func_189646_b(new GuiButton(5, centerW + 52, centerH + 10, 100, 20, StatsType.Speed.getLocalizedName()));
      this.buttons = new GuiButton[]{hp, att, def, spa, spd, spe};

      for(int i = 0; i < this.buttons.length; ++i) {
         if (this.data[i] == 0) {
            this.buttons[i].field_146124_l = false;
         }
      }

      Entity entity = Minecraft.func_71410_x().field_71441_e.func_73045_a(this.data[6]);
      if (entity instanceof EntityPixelmon) {
         this.pokemon = ((EntityPixelmon)entity).getStoragePokemonData();
      }

   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.func_146276_q_();
      super.func_73863_a(mouseX, mouseY, partialTicks);
      int centerW = this.field_146294_l / 2;
      int centerH = this.field_146295_m / 2;
      GuiHelper.drawCenteredString(I18n.func_135052_a("pixelmon.gui.selectstat", new Object[0]), (float)centerW, (float)(centerH - 40), 16777215);
      if (this.pokemon != null) {
         for(int i = 0; i < this.buttons.length; ++i) {
            if (this.buttons[i].func_146115_a() && this.buttons[i].field_146124_l) {
               StatsType[] types = StatsType.getStatValues();
               this.func_146279_a(types[i].getLocalizedName() + " " + this.pokemon.getStat(types[i]) + " -> " + this.data[i], mouseX, mouseY);
            }
         }
      }

   }

   protected void func_146284_a(GuiButton button) throws IOException {
      if (button != null) {
         Pixelmon.network.sendToServer(new SelectStatPacket(this.data[6], StatsType.getStatValues()[button.field_146127_k]));
         GuiHelper.closeScreen();
      }

   }

   public boolean func_73868_f() {
      return false;
   }
}
