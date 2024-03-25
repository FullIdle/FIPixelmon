package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.EffectBase;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.Drain;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.Recover;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.Synthesis;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.PoisonHeal;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.VoltAbsorb;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.WaterAbsorb;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HealBlock extends StatusBase {
   transient int turnsLeft = 5;

   public HealBlock() {
      super(StatusType.HealBlock);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.hasStatus(StatusType.HealBlock)) {
         user.bc.sendToAll("pixelmon.status.healblockalready", target.getNickname());
         user.attack.moveResult.result = AttackResult.failed;
      } else if (target.addTeamStatus(new HealBlock(), user)) {
         user.bc.sendToAll("pixelmon.status.healblock", target.getNickname());
      }

   }

   public boolean isTeamStatus() {
      return true;
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (--this.turnsLeft <= 0) {
         pw.bc.sendToAll("pixelmon.status.healblockend", pw.getNickname());
         pw.removeTeamStatus((StatusBase)this);
      }

   }

   public StatusBase copy() {
      HealBlock copy = new HealBlock();
      copy.turnsLeft = this.turnsLeft;
      return copy;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      boolean hasHeal = false;
      Iterator var8 = pw.getOpponentPokemon().iterator();

      while(var8.hasNext()) {
         PixelmonWrapper target = (PixelmonWrapper)var8.next();
         AbilityBase ability = target.getBattleAbility();
         hasHeal = hasHeal || ability instanceof PoisonHeal || ability instanceof VoltAbsorb || ability instanceof WaterAbsorb;
         EnumHeldItems heldItem = target.getUsableHeldItem().getHeldItemType();
         hasHeal = hasHeal || heldItem == EnumHeldItems.leftovers || heldItem == EnumHeldItems.shellBell;
         List moveset = pw.getBattleAI().getMoveset(target);
         hasHeal = hasHeal || Attack.hasAttack(moveset, "Aqua Ring", "Ingrain", "Leech Seed", "Rest", "Swallow", "Wish");
         Iterator var13 = moveset.iterator();

         while(var13.hasNext()) {
            Attack attack = (Attack)var13.next();

            EffectBase effect;
            for(Iterator var15 = attack.getMove().effects.iterator(); var15.hasNext(); hasHeal = hasHeal || effect instanceof Drain || effect instanceof Recover || effect instanceof Synthesis) {
               effect = (EffectBase)var15.next();
            }
         }
      }

      if (hasHeal) {
         userChoice.raiseWeight(25.0F);
      }

   }
}
