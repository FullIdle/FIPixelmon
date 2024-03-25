package com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.screens;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.MaxMoveConverter;
import com.pixelmonmod.pixelmon.battles.attacks.ZMove;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.BattleGui;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.ChooseAttack;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;
import java.io.IOException;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class ChooseTargets extends BattleGui {
   public ChooseTargets(GuiBattle parent, BattleMode mode) {
      super(parent, mode);
   }

   public void func_73866_w_() {
      super.func_73866_w_();
   }

   public void drawScreen(int width, int height, int mouseX, int mouseY) {
      PixelmonInGui data = this.bm.getCurrentPokemon();
      if (data != null) {
         Attack attack = data.moveset.get(this.bm.selectedAttack);
         String moveName;
         if (this.bm.showZMoves) {
            ZMove zMove = attack.getMove().getZMove(data.getBaseStats().getSpecies(), data.form, data.heldItem, false);
            moveName = zMove.getLocalizedName();
         } else if (!this.bm.dynamaxing && !data.pokemonUUID.equals(this.bm.dynamax)) {
            moveName = attack.getMove().getLocalizedName();
         } else {
            Attack maxMove;
            if (data.gmaxFactor) {
               maxMove = MaxMoveConverter.getGMaxMoveFromAttack(attack, (PixelmonWrapper)null, data.species, data.species.getFormEnum(data.form));
            } else {
               maxMove = MaxMoveConverter.getMaxMoveFromAttack(attack, (PixelmonWrapper)null);
            }

            moveName = maxMove.getMove().getLocalizedName();
         }

         boolean hovering = false;
         int tempInt;
         if ((tempInt = this.parent.pokemonOverlay.mouseOverUserPokemon(width, height, this.parent.getGuiWidth(), this.parent.getGuiHeight(), mouseX, mouseY)) != -1) {
            hovering = this.parent.setTargeting(data, data.moveset.get(this.bm.selectedAttack), -1, tempInt);
         }

         if ((tempInt = this.parent.pokemonOverlay.mouseOverEnemyPokemon(this.parent.getGuiWidth(), this.parent.getGuiHeight(), mouseX, mouseY)) != -1) {
            hovering = this.parent.setTargeting(data, data.moveset.get(this.bm.selectedAttack), tempInt, -1);
         }

         GuiHelper.drawBattleCursor(hovering, (float)mouseX, (float)mouseY, this.field_73735_i);
         this.drawBackground(width, height, mouseX, mouseY);
         this.drawButtons(mouseX, mouseY);

         try {
            this.parent.battleLog.setActiveMessage(I18n.func_135052_a("battlecontroller.targets", new Object[]{moveName}));
            this.parent.battleLog.drawElement(40, height - 80, Math.min(this.field_146294_l - 80, 260), 80, true);
         } catch (Exception var11) {
            var11.printStackTrace();
         }

         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      }
   }

   public void click(int width, int height, int mouseX, int mouseY) {
      if (!this.parent.battleLog.processUpDown(mouseX, mouseY)) {
         PixelmonInGui data = this.bm.getCurrentPokemon();
         if (mouseX > width / 2 + 137 && mouseX < width / 2 + 148 && mouseY > height - 11 && mouseY < height - 1) {
            this.bm.mode = BattleMode.ChooseAttack;
         } else {
            int tempInt;
            if ((tempInt = this.parent.pokemonOverlay.mouseOverUserPokemon(width, height, this.parent.getGuiWidth(), this.parent.getGuiHeight(), mouseX, mouseY)) != -1 && this.bm.targetted[0][tempInt]) {
               this.bm.selectedActions.add(new ChooseAttack(data.pokemonUUID, this.bm.targetted, this.bm.selectedAttack + (this.bm.showZMoves ? 4 : 0), this.bm.battleControllerIndex, this.bm.megaEvolving, this.bm.dynamaxing));
               this.bm.selectedMove();
            }

            if ((tempInt = this.parent.pokemonOverlay.mouseOverEnemyPokemon(this.parent.getGuiWidth(), this.parent.getGuiHeight(), mouseX, mouseY)) != -1 && this.bm.targetted[1][tempInt]) {
               this.bm.selectedActions.add(new ChooseAttack(data.pokemonUUID, this.bm.targetted, this.bm.selectedAttack + (this.bm.showZMoves ? 4 : 0), this.bm.battleControllerIndex, this.bm.megaEvolving, this.bm.dynamaxing));
               if (!this.bm.usedZMove) {
                  this.bm.usedZMove = this.bm.showZMoves;
               }

               this.bm.selectedMove();
            }

         }
      }
   }

   protected void func_73869_a(char typedChar, int keyCode) throws IOException {
   }
}
