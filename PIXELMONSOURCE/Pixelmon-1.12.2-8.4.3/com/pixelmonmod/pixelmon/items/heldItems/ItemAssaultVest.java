package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import java.util.Iterator;

public class ItemAssaultVest extends ItemHeld {
   public ItemAssaultVest() {
      super(EnumHeldItems.assaultVest, "assault_vest");
   }

   public int[] modifyStats(PixelmonWrapper user, int[] stats) {
      int var10001 = StatsType.SpecialDefence.getStatIndex();
      stats[var10001] = (int)((double)stats[var10001] * 1.5);
      return stats;
   }

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      this.applyRepeatedEffect(newPokemon);
   }

   public void applyRepeatedEffect(PixelmonWrapper pokemon) {
      Moveset moveset = pokemon.getMoveset();
      Iterator var3 = moveset.iterator();

      while(var3.hasNext()) {
         Attack currentAttack = (Attack)var3.next();
         if (currentAttack.getAttackCategory() == AttackCategory.STATUS) {
            currentAttack.setDisabled(true, pokemon);
         }
      }

   }
}
