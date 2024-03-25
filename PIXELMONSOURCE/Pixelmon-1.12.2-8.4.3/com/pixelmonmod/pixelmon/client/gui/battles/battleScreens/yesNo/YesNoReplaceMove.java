package com.pixelmonmod.pixelmon.client.gui.battles.battleScreens.yesNo;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.client.gui.battles.AttackData;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.enums.battle.BattleMode;
import net.minecraft.client.resources.I18n;

public class YesNoReplaceMove extends YesNoDialogue {
   public YesNoReplaceMove(GuiBattle parent) {
      super(parent, BattleMode.YesNoReplaceMove);
   }

   protected void drawConfirmText(int width, int height) {
      if (this.bm.newAttackList.isEmpty()) {
         this.parent.restoreSettingsAndClose();
         this.bm.mode = this.bm.oldMode;
      } else {
         Pokemon pokemonToLearnAttack = ClientStorageManager.party.find(((AttackData)this.bm.newAttackList.get(0)).pokemonUUID);

         Attack newAttack;
         try {
            newAttack = ((AttackData)this.bm.newAttackList.get(0)).attack;
         } catch (Exception var10) {
            Pixelmon.LOGGER.warn("User is clicking too fast.");
            Pixelmon.LOGGER.warn("New attack list is " + var10.getMessage());
            this.bm.mode = this.bm.oldMode;
            return;
         }

         float textAreaWidth = 170.0F;
         String text;
         if (this.bm.selectedAttack == -1) {
            text = newAttack.getActualMove().getLocalizedName();
            String text = I18n.func_135052_a("gui.yesno.yousure", new Object[]{text});
            float textWidth = (float)this.field_146297_k.field_71466_p.func_78256_a(text);
            int numLines = (int)(textWidth / textAreaWidth) + 1;
            this.field_146297_k.field_71466_p.func_78279_b(text, width / 2 - 109, height / 2 + 1 - numLines * 10 / 2, (int)textAreaWidth, 0);
            this.field_146297_k.field_71466_p.func_78279_b(text, width / 2 - 110, height / 2 - numLines * 10 / 2, (int)textAreaWidth, 16777215);
         } else {
            if (pokemonToLearnAttack == null) {
               return;
            }

            text = I18n.func_135052_a("gui.yesno.replace", new Object[]{pokemonToLearnAttack.getMoveset().get(this.bm.selectedAttack).getMove().getLocalizedName(), newAttack.getMove().getLocalizedName()});
            float textWidth = (float)this.field_146297_k.field_71466_p.func_78256_a(text);
            int numLines = (int)(textWidth / textAreaWidth) + 1;
            this.field_146297_k.field_71466_p.func_78279_b(text, width / 2 - 109, height / 2 + 1 - numLines * 10 / 2, (int)textAreaWidth, 0);
            this.field_146297_k.field_71466_p.func_78279_b(text, width / 2 - 110, height / 2 - numLines * 10 / 2, (int)textAreaWidth, 16777215);
         }

      }
   }

   protected void confirm() {
      if (this.bm.newAttackList.isEmpty()) {
         this.closeBattleScreen();
      } else {
         this.bm.newAttackList.remove(0);
         if (this.bm.sendPacket != null) {
            Pixelmon.network.sendToServer(this.bm.sendPacket);
         }

         if (this.bm.battleControllerIndex != -1) {
            if (!this.bm.hasNewAttacks() && !this.bm.hasLevelUps()) {
               if (this.bm.battleEnded) {
                  this.closeBattleScreen();
                  return;
               }

               this.bm.mode = BattleMode.MainMenu;
               this.bm.checkClearedMessages();
            } else {
               this.bm.mode = this.bm.yesNoOrigin;
            }
         } else {
            if (!this.bm.hasNewAttacks() && !this.bm.hasLevelUps()) {
               this.closeBattleScreen();
               return;
            }

            this.bm.mode = this.bm.yesNoOrigin;
         }

      }
   }

   private void closeBattleScreen() {
      this.parent.restoreSettingsAndClose();
      this.bm.mode = this.bm.oldMode;
   }
}
