package com.pixelmonmod.pixelmon.battles.rules.clauses;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.util.helpers.ArrayHelper;

public class ItemPreventClause extends BattleClause {
   private EnumHeldItems[] heldItems;

   public ItemPreventClause(String id, EnumHeldItems... heldItems) {
      super(id);
      this.heldItems = heldItems;
      ArrayHelper.validateArrayNonNull(heldItems);
   }

   public boolean validateSingle(Pokemon pokemon) {
      EnumHeldItems[] var2 = this.heldItems;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumHeldItems heldItem = var2[var4];
         if (pokemon.getHeldItemAsItemHeld().getHeldItemType() == heldItem) {
            return false;
         }
      }

      return true;
   }
}
