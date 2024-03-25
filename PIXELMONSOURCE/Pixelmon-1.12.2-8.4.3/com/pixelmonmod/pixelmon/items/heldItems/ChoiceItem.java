package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumChoiceItems;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ChoiceItem extends ItemHeld {
   private EnumChoiceItems choiceItemType;

   public ChoiceItem(EnumChoiceItems choiceItemType, String itemName) {
      super(EnumHeldItems.choiceItem, itemName);
      this.choiceItemType = choiceItemType;
   }

   public void onAttackUsed(PixelmonWrapper user, Attack attack) {
      if (!user.bc.simulateMode && user.isDynamax <= 0 && !attack.getActualMove().getAttackName().equals("Transform") && !attack.isAttack("Struggle")) {
         user.choiceLocked = attack;
      }

   }

   public void applySwitchInEffect(PixelmonWrapper pw) {
      if (!pw.bc.simulateMode && !pw.choiceSwapped) {
         pw.choiceLocked = null;
      }

      pw.choiceSwapped = false;
   }

   public void applySwitchOutEffect(PixelmonWrapper pw) {
      if (!pw.bc.simulateMode && !pw.choiceSwapped) {
         pw.choiceLocked = null;
      }

   }

   public int[] modifyStats(PixelmonWrapper user, int[] stats) {
      if (user.isDynamax <= 0) {
         int var10001 = this.choiceItemType.effectType.getStatIndex();
         stats[var10001] = (int)((double)stats[var10001] * 1.5);
      }

      return stats;
   }

   public void applyRepeatedEffect(PixelmonWrapper pokemon) {
      Moveset moveset = pokemon.getMoveset();
      if (!pokemon.bc.simulateMode && !pokemon.choiceSwapped && pokemon.isFainted()) {
         pokemon.choiceLocked = null;
      }

      if (!pokemon.isDynamax()) {
         for(int i = 0; i < moveset.size(); ++i) {
            Attack currentMove = moveset.get(i);
            if (pokemon.choiceLocked != null && !currentMove.equals(pokemon.choiceLocked)) {
               currentMove.setDisabled(true, pokemon);
            }
         }

      }
   }
}
