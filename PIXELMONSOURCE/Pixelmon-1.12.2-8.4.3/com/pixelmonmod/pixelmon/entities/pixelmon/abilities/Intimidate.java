package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.CalcPriority;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import java.util.Iterator;
import java.util.List;

public class Intimidate extends AbilityBase {
   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      boolean sentMessage = false;
      List opponents = newPokemon.bc.getOpponentPokemon(newPokemon);
      Iterator var4 = CalcPriority.getTurnOrder(opponents).iterator();

      while(var4.hasNext()) {
         PixelmonWrapper pw = (PixelmonWrapper)var4.next();
         if (!sentMessage) {
            this.sendActivatedMessage(newPokemon);
            sentMessage = true;
         }

         if (!(pw.getBattleAbility() instanceof InnerFocus) && !(pw.getAbility() instanceof Oblivious) && !(pw.getAbility() instanceof Scrappy) && !(pw.getAbility() instanceof OwnTempo)) {
            pw.getBattleStats().modifyStat(-1, StatsType.Attack, newPokemon, false);
            if (pw.getBattleAbility().isAbility(Rattled.class)) {
               ((Rattled)pw.getBattleAbility()).activate(pw);
            }

            if (pw.getUsableHeldItem().getHeldItemType() == EnumHeldItems.adrenalineOrb) {
               pw.getBattleStats().modifyStat(1, StatsType.Speed, pw, false);
               pw.consumeItem();
               pw.bc.sendToAll("pixelmon.helditems.adrenalineorb", pw.getNickname());
            }
         }
      }

   }
}
