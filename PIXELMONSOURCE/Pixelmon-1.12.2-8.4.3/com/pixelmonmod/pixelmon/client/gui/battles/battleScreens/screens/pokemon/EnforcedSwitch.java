package com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.screens.pokemon;

import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.PigMenuButton;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.SwitchPokemon;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import net.minecraft.item.ItemStack;

public class EnforcedSwitch extends ChoosePokemon {
   public EnforcedSwitch(GuiBattle parent, BattleMode mode) {
      super(parent, mode);
   }

   public String getTitle() {
      return "battlecontroller.switch";
   }

   public List generateButtons() {
      if (!this.bm.enforcedFleeFailed) {
         this.menuElement.setReuseMessage("gui.fainterChoice.run", (ItemStack)null);
      } else {
         this.menuElement.setReuseMessage("", (ItemStack)null);
      }

      return super.generateButtons();
   }

   public void click(int width, int height, int mouseX, int mouseY) {
      Optional opt = this.bm.fullOurPokemon.stream().filter((it) -> {
         return it != null && it.pokemonUUID.equals(this.bm.teamPokemon[this.bm.currentPokemon]);
      }).findFirst();
      if (this.menuElement.isOverReuseButton(mouseX, mouseY)) {
         if (opt.isPresent()) {
            this.bm.selectRunAction(((PixelmonInGui)opt.get()).pokemonUUID);
         }
      } else if (!this.menuElement.handleClickPageTurn(mouseX, mouseY)) {
         List buttons = this.menuElement.getPageButtons();
         Iterator var7 = buttons.iterator();

         while(var7.hasNext()) {
            PigMenuButton button = (PigMenuButton)var7.next();
            if (button.func_146116_c(this.field_146297_k, mouseX, mouseY) && opt.isPresent()) {
               this.bm.selectedActions.add(new SwitchPokemon(button.getPig().pokemonUUID, this.bm.battleControllerIndex, ((PixelmonInGui)opt.get()).pokemonUUID, true));
               this.bm.selectedMove(true);
            }
         }
      }

   }
}
