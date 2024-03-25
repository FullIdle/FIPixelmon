package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.Effectiveness;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Foresight extends StatusBase {
   transient EnumType ignoredType;

   public Foresight() {
      super(StatusType.Foresight);
   }

   public Foresight(EnumType ignoredType) {
      super(StatusType.Foresight);
      this.ignoredType = ignoredType;
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (!target.hasStatus(StatusType.Foresight)) {
         target.bc.sendToAll("pixelmon.status.foresight", target.getNickname());
         EnumType type = this.getIgnoredType(user.attack);
         target.addStatus(new Foresight(type), user);
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
      }

   }

   private EnumType getIgnoredType(Attack attack) {
      return attack.isAttack("Miracle Eye") ? EnumType.Dark : EnumType.Ghost;
   }

   public List getEffectiveTypes(PixelmonWrapper user, PixelmonWrapper target) {
      return user.attack != null && EnumType.getEffectiveness(user.attack.getType(), this.ignoredType) == Effectiveness.None.value ? EnumType.ignoreType(target.type, this.ignoredType) : target.type;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!userChoice.hitsAlly()) {
         Iterator var7 = userChoice.targets.iterator();

         while(true) {
            EnumType ignoredType;
            Moveset moveset;
            do {
               PixelmonWrapper target;
               do {
                  if (!var7.hasNext()) {
                     return;
                  }

                  target = (PixelmonWrapper)var7.next();
                  int evasion = target.getBattleStats().getStage(StatsType.Evasion);
                  if (evasion > 0) {
                     userChoice.raiseWeight((float)(10 * evasion));
                  }

                  ignoredType = this.getIgnoredType(userChoice.attack);
               } while(!target.hasType(ignoredType));

               moveset = pw.getMoveset();
            } while((ignoredType != EnumType.Ghost || !moveset.hasOffensiveAttackType(EnumType.Normal, EnumType.Fighting)) && (ignoredType != EnumType.Dark || !moveset.hasOffensiveAttackType(EnumType.Psychic)));

            userChoice.raiseWeight(25.0F);
         }
      }
   }
}
