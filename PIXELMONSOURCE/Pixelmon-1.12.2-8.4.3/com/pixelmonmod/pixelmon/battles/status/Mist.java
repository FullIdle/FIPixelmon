package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.EffectBase;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.modifiers.ModifierBase;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.modifiers.ModifierType;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Infiltrator;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import java.util.ArrayList;
import java.util.Iterator;

public class Mist extends StatusBase {
   public transient int effectTurns = 0;

   public Mist() {
      super(StatusType.Mist);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.targetIndex == 0 || user.bc.simulateMode) {
         if (user.hasStatus(StatusType.Mist)) {
            user.bc.sendToAll("pixelmon.effect.alreadymist", user.getNickname());
            user.attack.moveResult.result = AttackResult.failed;
            return;
         }

         if (user.addTeamStatus(new Mist(), user)) {
            user.bc.sendToAll("pixelmon.effect.usemist", user.getNickname());
         }
      }

   }

   public boolean isTeamStatus() {
      return true;
   }

   public boolean stopsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user) {
      if (user.attack.getAttackCategory() == AttackCategory.STATUS && !(user.getBattleAbility() instanceof Infiltrator) && this.hasTargetStatsEffect(user.attack)) {
         user.bc.sendToAll("pixelmon.status.mistprotect", pokemon.getNickname());
         return true;
      } else {
         return false;
      }
   }

   private boolean hasTargetStatsEffect(Attack attack) {
      Iterator var2 = attack.getMove().effects.iterator();

      EffectBase e;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         e = (EffectBase)var2.next();
      } while(!(e instanceof StatsEffect));

      Iterator var4 = ((StatsEffect)e).modifiers.iterator();

      ModifierBase m;
      do {
         if (!var4.hasNext()) {
            return true;
         }

         m = (ModifierBase)var4.next();
      } while(m.type != ModifierType.User);

      return false;
   }

   public boolean allowsStatChange(PixelmonWrapper pokemon, PixelmonWrapper user, StatsEffect e) {
      return e.getUser() || e.amount > 0 || user.getBattleAbility() instanceof Infiltrator;
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (++this.effectTurns >= 5) {
         pw.bc.sendToAll("pixelmon.status.mistoff", pw.getNickname());
         pw.removeTeamStatus((StatusBase)this);
      }

   }

   public StatusBase copy() {
      Mist copy = new Mist();
      copy.effectTurns = this.effectTurns;
      return copy;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      boolean hasTargetStatsEffect = false;
      Iterator var8 = pw.getOpponentPokemon().iterator();

      while(true) {
         while(true) {
            PixelmonWrapper opponent;
            do {
               if (!var8.hasNext()) {
                  if (hasTargetStatsEffect) {
                     userChoice.raiseWeight(15.0F);
                  }

                  return;
               }

               opponent = (PixelmonWrapper)var8.next();
            } while(opponent.getBattleAbility() instanceof Infiltrator);

            Iterator var10 = pw.getBattleAI().getMoveset(opponent).iterator();

            while(var10.hasNext()) {
               Attack attack = (Attack)var10.next();
               if (this.hasTargetStatsEffect(attack)) {
                  hasTargetStatsEffect = true;
                  break;
               }
            }
         }
      }
   }
}
