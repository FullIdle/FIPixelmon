package com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.old_gui.chooseMove;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.battles.AttackData;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.BattleScreen;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ReplaceMove;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.translation.I18n;

public class ReplaceAttack extends BattleScreen {
   public ReplaceAttack(GuiBattle parent) {
      super(parent, BattleMode.ReplaceAttack);
   }

   public void drawScreen(int width, int height, int mouseX, int mouseY) {
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.chooseMove);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GuiHelper.drawImageQuad((double)(width / 2 - 128), (double)(height / 2 - 102), 256.0, 205.0F, 0.0, 0.0, 1.0, 0.80078125, this.field_73735_i);
      if (!this.bm.newAttackList.isEmpty()) {
         Pokemon pokemonToLearnAttack = ClientStorageManager.party.find(((AttackData)this.bm.newAttackList.get(0)).pokemonUUID);
         if (pokemonToLearnAttack == null) {
            this.bm.newAttackList.clear();
            this.bm.mode = this.bm.oldMode;
         } else {
            GuiHelper.drawMoveset(pokemonToLearnAttack.getMoveset(), width, height, this.field_73735_i);

            for(int i = 0; i < pokemonToLearnAttack.getMoveset().size(); ++i) {
               if (this.mouseOverMove(i, width, height, mouseX, mouseY)) {
                  this.field_146297_k.field_71446_o.func_110577_a(GuiResources.chooseMove);
                  GuiHelper.drawImageQuad((double)(width / 2 - 30), (double)(height / 2 - 94 + 22 * i), 152.0, 24.0F, 0.37890625, 0.81640625, 0.97265625, 0.9140625, this.field_73735_i);
                  GuiHelper.drawAttackInfoMoveset(pokemonToLearnAttack.getMoveset().get(i), height / 2 + 53, width, height);
               }
            }

            Attack newAttack = ((AttackData)this.bm.newAttackList.get(0)).attack;
            this.func_73731_b(this.field_146297_k.field_71466_p, newAttack.getMove().getLocalizedName(), width / 2 + 11, height / 2 - 78 + 88, 16777215);
            this.func_73731_b(this.field_146297_k.field_71466_p, newAttack.pp + "/" + newAttack.getMaxPP(), width / 2 + 90, height / 2 - 76 + 88, 16777215);
            float x3 = newAttack.getMove().getAttackType().textureX;
            float y3 = newAttack.getMove().getAttackType().textureY;
            this.field_146297_k.field_71446_o.func_110577_a(GuiResources.types);
            GuiHelper.drawImageQuad((double)(width / 2 - 23), (double)(height / 2 + 3), 21.0, 21.0F, (double)(x3 / 1792.0F), (double)(y3 / 768.0F), (double)((x3 + 240.0F) / 1792.0F), (double)((y3 + 240.0F) / 768.0F), this.field_73735_i);
            if (mouseX > width / 2 - 30 && mouseX < width / 2 + 120 && mouseY > height / 2 + 3 && mouseY < height / 2 + 25) {
               this.field_146297_k.field_71446_o.func_110577_a(GuiResources.chooseMove);
               GuiHelper.drawImageQuad((double)(width / 2 - 30), (double)(height / 2 + 1), 152.0, 24.0F, 0.37890625, 0.81640625, 0.97265625, 0.9140625, this.field_73735_i);
               GuiHelper.drawAttackInfoMoveset(newAttack, height / 2 + 53, width, height);
            }

            this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_74838_a("gui.replaceattack.effect"), width / 2 - 96, height / 2 + 38, 16777215);
            this.func_73731_b(this.field_146297_k.field_71466_p, I18n.func_74838_a("gui.description.text"), width / 2 - 20, height / 2 + 38, 16777215);
            GuiHelper.drawPokemonInfoChooseMove(new PixelmonInGui(pokemonToLearnAttack), width, height, this.field_73735_i);
         }
      }
   }

   public void click(int width, int height, int mouseX, int mouseY) {
      if (this.bm.newAttackList.isEmpty()) {
         try {
            Thread.sleep(100L);
         } catch (InterruptedException var9) {
            Thread.currentThread().interrupt();
         }
      }

      if (!this.bm.newAttackList.isEmpty()) {
         AttackData newAttackData = (AttackData)this.bm.newAttackList.get(0);
         Attack newAttack = newAttackData.attack;
         Pokemon pokemonToLearnAttack = ClientStorageManager.party.find(newAttackData.pokemonUUID);
         if (pokemonToLearnAttack != null) {
            for(int i = 0; i < pokemonToLearnAttack.getMoveset().size(); ++i) {
               if (this.mouseOverMove(i, width, height, mouseX, mouseY)) {
                  this.showConfirm(pokemonToLearnAttack, newAttack, i);
                  return;
               }
            }

            if (mouseX > width / 2 - 30 && mouseX < width / 2 + 120 && mouseY > height / 2 + 3 && mouseY < height / 2 + 25) {
               this.showConfirm(pokemonToLearnAttack, newAttack, -1);
            }

         }
      }
   }

   private void showConfirm(Pokemon pokemonToLearnAttack, Attack newAttack, int attackIndex) {
      boolean checkEvo = ((AttackData)this.bm.newAttackList.get(0)).checkEvo;
      this.bm.sendPacket = new ReplaceMove(pokemonToLearnAttack.getUUID(), newAttack.getMove().getAttackId(), attackIndex, checkEvo);
      this.bm.yesNoOrigin = this.bm.mode;
      this.bm.mode = BattleMode.YesNoReplaceMove;
      this.bm.selectedAttack = attackIndex;
   }

   private boolean mouseOverMove(int i, int width, int height, int mouseX, int mouseY) {
      return mouseX > width / 2 - 30 && mouseX < width / 2 + 120 && mouseY > height / 2 - 94 + 22 * i && mouseY < height / 2 - 94 + 22 * (i + 1);
   }

   public boolean isScreen() {
      return super.isScreen() && this.bm.hasNewAttacks();
   }
}
