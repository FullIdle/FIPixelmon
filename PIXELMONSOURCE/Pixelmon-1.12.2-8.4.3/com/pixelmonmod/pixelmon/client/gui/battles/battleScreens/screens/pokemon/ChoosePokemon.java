package com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.screens.pokemon;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.BattleMenuGui;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.PigMenuButton;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.SwitchPokemon;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.resources.I18n;

public class ChoosePokemon extends BattleMenuGui {
   public ChoosePokemon(GuiBattle parent, BattleMode mode) {
      super(parent, mode);
   }

   public String getTitle() {
      return I18n.func_135052_a("gui.mainmenu.pokemon", new Object[0]);
   }

   public List generateButtons() {
      Set inBattle = Sets.newHashSet();
      List party = Lists.newArrayList();
      int i;
      if (this.bm.teamPokemon != null) {
         UUID[] var3 = this.bm.teamPokemon;
         i = var3.length;

         for(int var5 = 0; var5 < i; ++var5) {
            UUID uuid = var3[var5];
            Optional pixelmon = this.bm.fullOurPokemon.stream().filter((p) -> {
               return p.pokemonUUID.equals(uuid);
            }).findFirst();
            pixelmon.ifPresent(inBattle::add);
         }
      }

      Iterator var8 = this.bm.fullOurPokemon.iterator();

      while(var8.hasNext()) {
         PixelmonInGui pig = (PixelmonInGui)var8.next();
         if (!inBattle.contains(pig) && pig.health > 0.0F) {
            party.add(pig);
         }
      }

      party.removeIf((pigx) -> {
         return pigx.isSwitchingIn;
      });
      List buttons = Lists.newArrayList();

      for(i = 0; i < party.size(); ++i) {
         PigMenuButton button = new PigMenuButton(i, (PixelmonInGui)party.get(i), this);
         buttons.add(button);
      }

      return buttons;
   }

   public void drawScreen(int width, int height, int mouseX, int mouseY) {
      super.drawScreen(width, height, mouseX, mouseY);
   }

   public void click(int width, int height, int mouseX, int mouseY) {
      if (this.menuElement.isMouseOver(mouseX, mouseY)) {
         if (this.menuElement.isOverReturnButton(mouseX, mouseY)) {
            this.bm.mode = BattleMode.MainMenu;
         } else if (!this.menuElement.handleClickPageTurn(mouseX, mouseY)) {
            List buttons = this.menuElement.getPageButtons();
            Iterator var6 = buttons.iterator();

            while(var6.hasNext()) {
               PigMenuButton button = (PigMenuButton)var6.next();
               if (button.func_146116_c(this.field_146297_k, mouseX, mouseY)) {
                  this.bm.selectedActions.add(new SwitchPokemon(button.getPig().pokemonUUID, this.bm.battleControllerIndex, this.bm.getCurrentPokemon().pokemonUUID, false));
                  button.getPig().isSwitchingIn = true;
                  this.bm.selectedMove();
               }
            }
         }
      }

   }
}
