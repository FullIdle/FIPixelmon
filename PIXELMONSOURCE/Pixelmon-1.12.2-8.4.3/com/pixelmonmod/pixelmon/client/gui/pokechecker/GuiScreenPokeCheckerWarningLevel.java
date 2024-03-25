package com.pixelmonmod.pixelmon.client.gui.pokechecker;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.GuiWarning;
import com.pixelmonmod.pixelmon.comm.packetHandlers.StopStartLevelling;
import net.minecraft.client.resources.I18n;

public class GuiScreenPokeCheckerWarningLevel extends GuiWarning {
   public GuiScreenPokeCheckerWarningLevel(GuiScreenPokeChecker previousScreen) {
      super(previousScreen);
   }

   protected void confirmAction() {
      Pixelmon.network.sendToServer(new StopStartLevelling(((GuiScreenPokeChecker)this.previousScreen).position, ((GuiScreenPokeChecker)this.previousScreen).pokemon.getUUID()));
   }

   protected void drawWarningText() {
      String text = I18n.func_135052_a("gui.screenpokechecker." + (((GuiScreenPokeChecker)this.previousScreen).pokemon.doesLevel() ? "disable" : "enable"), new Object[0]);
      this.func_73732_a(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.forget1", new Object[0]), 60, 73, 16777215);
      this.func_73732_a(this.field_146297_k.field_71466_p, I18n.func_135052_a("gui.screenpokechecker.move", new Object[]{text}), 60, 83, 16777215);
   }
}
