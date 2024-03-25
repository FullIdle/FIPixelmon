package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.attackModifiers.MultipleHit;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.DragonDarts;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.TripleAxel;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.TripleKick;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Infiltrator;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import java.util.ArrayList;
import java.util.Iterator;

public class Substitute extends StatusBase {
   public transient int health;

   public Substitute() {
      super(StatusType.Substitute);
   }

   public Substitute(int health) {
      super(StatusType.Substitute);
      this.health = health;
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      float health = (float)user.getPercentMaxHealth(25.0F);
      if (!user.hasStatus(this.type) && !((float)user.getHealth() <= health)) {
         if (user.addStatus(new Substitute((int)health), user)) {
            user.doBattleDamage(user, health, DamageTypeEnum.SELF);
            user.bc.sendToAll("pixelmon.effect.createsubstitute", user.getNickname());
         }

      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         user.attack.moveResult.result = AttackResult.failed;
      }
   }

   public boolean stopsStatusChange(StatusType t, PixelmonWrapper target, PixelmonWrapper user) {
      if (user != target && !t.isStatus(StatusType.Cursed, StatusType.Disable, StatusType.Encore, StatusType.FirePledge, StatusType.FutureSight, StatusType.GrassPledge, StatusType.HealBlock, StatusType.Imprison, StatusType.Infatuated, StatusType.Perish, StatusType.Spikes, StatusType.StealthRock, StatusType.StickyWeb, StatusType.Taunt, StatusType.Torment, StatusType.ToxicSpikes, StatusType.WaterPledge) && !this.ignoreSubstitute(user)) {
         if (user.attack != null) {
            if (!user.attack.getMove().getTargetingInfo().hitsAdjacentFoe) {
               return false;
            }

            if (user.attack.getAttackCategory() == AttackCategory.STATUS) {
               if (!user.attack.isAttack("Defog") || !user.attack.isAttack("Parting Shot")) {
                  target.bc.sendToAll("pixelmon.effect.effectfailed");
               }

               user.attack.moveResult.result = AttackResult.failed;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean stopsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user) {
      if (!this.ignoreSubstitute(user) && user.attack.getAttackCategory() == AttackCategory.STATUS && user.attack.getMove().getTargetingInfo().hitsAdjacentFoe && !user.attack.isAttack("Spikes", "Stealth Rock", "Sticky Web", "Toxic Spikes")) {
         if ((!user.attack.isAttack("Defog") || !user.hasStatus(StatusType.Spikes) && !user.hasStatus(StatusType.StealthRock) && !user.hasStatus(StatusType.ToxicSpikes) && !user.hasStatus(StatusType.StickyWeb) && !user.hasStatus(StatusType.ElectricTerrain) && !user.hasStatus(StatusType.GrassyTerrain) && !user.hasStatus(StatusType.MistyTerrain) && !user.hasStatus(StatusType.PsychicTerrain)) && !user.attack.isAttack("Parting Shot")) {
            user.bc.sendToAll("pixelmon.effect.effectfailed");
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean allowsStatChange(PixelmonWrapper pokemon, PixelmonWrapper user, StatsEffect e) {
      return pokemon == user;
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (this.health <= 0) {
         pw.removeStatus((StatusBase)this);
      }

   }

   public void onEndOfAttackersTurn(PixelmonWrapper statusedPokemon, PixelmonWrapper attacker) {
      if (this.health <= 0) {
         statusedPokemon.removeStatus((StatusBase)this);
      }

   }

   public float attackSubstitute(float damage, PixelmonWrapper source, PixelmonWrapper pw) {
      float damageResult = Math.min((float)this.health, damage);
      if (pw.bc.simulateMode) {
         return damageResult;
      } else {
         this.health = (int)((float)this.health - damage);
         pw.bc.sendToAll("pixelmon.status.substitutedamage", pw.getNickname());
         if (this.health <= 0) {
            pw.bc.sendToAll("pixelmon.status.substitutefade", pw.getNickname());
            if (source.attack.isAttack("Counter", "Mirror Coat", "Metal Burst") || source.attack.getActualMove().hasEffect(MultipleHit.class) || source.attack.getActualMove().hasEffect(DragonDarts.class) || source.attack.getActualMove().hasEffect(TripleAxel.class) || source.attack.getActualMove().hasEffect(TripleKick.class)) {
               pw.removeStatus((StatusBase)this);
            }
         }

         return damageResult;
      }
   }

   public boolean ignoreSubstitute(PixelmonWrapper attacker) {
      return attacker.getBattleAbility() instanceof Infiltrator || attacker.attack.isSoundBased() || attacker.attack.getMove().getFlags().authentic;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      boolean canSubstitute = false;
      Iterator var8 = bestOpponentChoices.iterator();

      while(var8.hasNext()) {
         MoveChoice choice = (MoveChoice)var8.next();
         if (choice.isOffensiveMove() && (choice.isMiddleTier() && choice.weight < 25.0F || choice.tier >= 3 && choice.weight <= 75.0F)) {
            canSubstitute = true;
            break;
         }
      }

      if (!canSubstitute) {
         var8 = pw.getOpponentPokemon().iterator();

         while(var8.hasNext()) {
            PixelmonWrapper opponent = (PixelmonWrapper)var8.next();
            if (opponent.hasStatus(StatusType.Bide, StatusType.Freeze, StatusType.MultiTurn, StatusType.PoisonBadly, StatusType.Sleep)) {
               canSubstitute = true;
               break;
            }
         }
      }

      if (canSubstitute) {
         userChoice.raiseWeight(30.0F);
      }

   }
}
