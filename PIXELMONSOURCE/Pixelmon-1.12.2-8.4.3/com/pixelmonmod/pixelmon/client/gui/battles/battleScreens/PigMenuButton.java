package com.pixelmonmod.pixelmon.client.gui.battles.battleScreens;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class PigMenuButton extends BattleMenuElement.MenuListButton {
   private final PixelmonInGui pig;

   public PigMenuButton(int buttonId, PixelmonInGui pig, GuiScreen parent) {
      super(buttonId, parent);
      this.pig = pig;
   }

   public PixelmonInGui getPig() {
      return this.pig;
   }

   public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
      super.func_191745_a(mc, mouseX, mouseY, partialTicks);
      GuiHelper.bindPokemonSprite(this.pig, mc);
      GuiHelper.drawImage((double)(this.field_146128_h + 4), (double)this.field_146129_i, (double)(this.field_146121_g - 6), (double)(this.field_146121_g - 6), this.field_73735_i);
      String displayName = this.pig.getDisplayName();
      GuiHelper.drawScaledString(displayName, (float)(this.field_146128_h + 36), (float)(this.field_146129_i + 4), -16777216, 16.0F);
      int lvlX = Math.max(122, this.field_146128_h + 36 + GuiHelper.getStringWidth(displayName) + 4);
      GuiHelper.drawScaledString("Lv." + this.pig.level, (float)lvlX, (float)(this.field_146129_i + 4), -16777216, 12.0F);
      Color color = this.pig.getHealthColor();
      GuiHelper.drawBar((double)(this.field_146128_h + 36), (double)(this.field_146129_i + 16), 110.0, 8.0, this.pig.health / (float)this.pig.maxHealth, color);
      GuiHelper.drawScaledCenteredString((int)this.pig.health + "/" + this.pig.maxHealth, (float)(this.field_146128_h + 36) + 55.0F, (float)(this.field_146129_i + 17), 15790320, 12.0F);
   }
}
