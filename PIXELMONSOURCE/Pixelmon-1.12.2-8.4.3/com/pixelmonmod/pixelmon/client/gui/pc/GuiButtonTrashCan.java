package com.pixelmonmod.pixelmon.client.gui.pc;

import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonTrashCan extends GuiButton {
   private GuiPC parent;

   public GuiButtonTrashCan(GuiPC parent, int buttonId, int x, int y) {
      super(buttonId, x, y, 26, 35, "");
      this.parent = parent;
   }

   public boolean func_146116_c(Minecraft mc, int mouseX, int mouseY) {
      return this.parent.selected != null && this.parent.selectedPokemon != null && (this.parent.selectedPokemon.isEgg() || this.parent.selected.box != -1 || ClientStorageManager.party.countPokemon() > 1) && super.func_146116_c(mc, mouseX, mouseY);
   }

   public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
      if (this.field_146125_m) {
         this.field_146123_n = this.func_146116_c(mc, mouseX, mouseY);
         mc.func_110434_K().func_110577_a(GuiResources.pcResources);
         this.func_73729_b(this.field_146128_h, this.field_146129_i, this.field_146123_n ? this.field_146120_f : 0, 85, this.field_146120_f, this.field_146121_g);
      }

   }
}
