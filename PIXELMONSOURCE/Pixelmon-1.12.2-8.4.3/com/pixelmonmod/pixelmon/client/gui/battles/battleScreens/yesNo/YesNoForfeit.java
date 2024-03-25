package com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.yesNo;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.Flee;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;
import net.minecraft.client.resources.I18n;

public class YesNoForfeit extends YesNoDialogue {
   public YesNoForfeit(GuiBattle parent) {
      super(parent, BattleMode.YesNoForfeit);
   }

   protected void drawConfirmText(int width, int height) {
      GuiHelper.drawCenteredSplitString(I18n.func_135052_a("battlecontroller.forfeitask", new Object[0]), (float)(width / 2 - 30), (float)(height / 2 - 5), 70, 16777215);
   }

   protected void confirm() {
      Pixelmon.network.sendToServer(new Flee(this.bm.getCurrentPokemon().pokemonUUID));
      this.bm.mode = BattleMode.Waiting;
   }
}
