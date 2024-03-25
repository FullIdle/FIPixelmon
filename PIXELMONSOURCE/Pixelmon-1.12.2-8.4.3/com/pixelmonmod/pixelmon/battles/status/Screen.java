package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.ai.BattleAIBase;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Infiltrator;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Screen extends StatusBase {
   StatsType stat;
   transient int effectTurns;
   String langStart;
   String langFail;
   String langEnd;

   public Screen(StatusType type, StatsType stat, int effectTurns, String langStart, String langFail, String langEnd) {
      super(type);
      this.stat = stat;
      this.effectTurns = effectTurns;
      this.langStart = langStart;
      this.langFail = langFail;
      this.langEnd = langEnd;
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.targetIndex == 0 || user.bc.simulateMode) {
         this.apply(user, true);
      }

   }

   public boolean apply(PixelmonWrapper user, boolean failureMessages) {
      if (user.hasStatus(this.type)) {
         if (failureMessages) {
            user.bc.sendToAll(this.langFail, user.getNickname());
         }

         user.attack.moveResult.result = AttackResult.failed;
         return false;
      } else {
         int turns = user.getUsableHeldItem().getHeldItemType() == EnumHeldItems.lightClay ? 8 : 5;
         user.addTeamStatus(this.getNewInstance(turns), user);
         user.bc.sendToAll(this.langStart, user.getNickname());
         return true;
      }
   }

   protected abstract Screen getNewInstance(int var1);

   public int modifyDamageIncludeFixed(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack a, DamageTypeEnum damageType) {
      if (damageType != DamageTypeEnum.ATTACK) {
         return damage;
      } else if (a.isAttack("Brick Break", "Psychic Fangs")) {
         return damage;
      } else if (user.getAbility().isAbility(Infiltrator.class)) {
         return damage;
      } else {
         return this.shouldReduce(a) ? (int)Math.ceil((double)((float)damage * this.getDamageMultiplier(user, target))) : damage;
      }
   }

   public float getDamageMultiplier(PixelmonWrapper user, PixelmonWrapper target) {
      return 0.5F;
   }

   public boolean shouldReduce(Attack a) {
      return a.getAttackCategory() == AttackCategory.PHYSICAL && this.stat == StatsType.Defence || a.getAttackCategory() == AttackCategory.SPECIAL && this.stat == StatsType.SpecialDefence;
   }

   public boolean ignoreStatus(PixelmonWrapper user, PixelmonWrapper target) {
      return user.getBattleAbility() instanceof Infiltrator;
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (--this.effectTurns <= 0) {
         pw.bc.sendToAll(this.langEnd, pw.getNickname());
         pw.removeTeamStatus((StatusBase)this);
      }

   }

   public boolean isTeamStatus() {
      return true;
   }

   public StatusBase copy() {
      return this.getNewInstance(this.effectTurns);
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      ArrayList opponents = pw.getOpponentPokemon();
      if (!opponents.isEmpty()) {
         Iterator var8 = opponents.iterator();

         PixelmonWrapper opponent;
         do {
            if (!var8.hasNext()) {
               BattleAIBase ai = pw.getBattleAI();

               try {
                  pw.bc.simulateMode = false;
                  pw.addTeamStatus(this.getNewInstance(5), pw);
                  pw.bc.simulateMode = true;
                  pw.bc.modifyStats();
                  ArrayList bestOpponentChoicesAfter = ai.getBestAttackChoices(opponents);
                  ai.weightFromOpponentOptions(pw, userChoice, MoveChoice.splitChoices(opponents, bestOpponentChoices), bestOpponentChoicesAfter);
               } finally {
                  pw.bc.simulateMode = false;
                  pw.removeTeamStatus((StatusBase)this);
                  pw.bc.simulateMode = true;
                  pw.bc.modifyStats();
                  pw.bc.modifyStatsCancellable(pw);
               }

               if (userChoice.weight == 0.0F) {
                  userChoice.raiseWeight(10.0F);
               }

               return;
            }

            opponent = (PixelmonWrapper)var8.next();
         } while(!this.ignoreStatus(opponent, pw));

      }
   }
}
