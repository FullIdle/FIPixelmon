package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.util.text.TextComponentTranslation;

public class Confusion extends StatusBase {
   private transient int effectTurns = -1;

   public Confusion() {
      super(StatusType.Confusion);
      this.effectTurns = RandomHelper.getRandomNumberBetween(2, 5);
   }

   public static boolean confuse(PixelmonWrapper user, PixelmonWrapper target) {
      return (new Confusion()).addStatus(user, target);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (this.checkChance()) {
         this.addStatus(user, target);
      }

   }

   protected boolean addStatus(PixelmonWrapper user, PixelmonWrapper target) {
      TextComponentTranslation message = ChatHandler.getMessage("pixelmon.effect.becameconfused", target.getNickname());
      if (target.addStatus(new Confusion(), user, message)) {
         return true;
      } else {
         if (user.hasStatus(StatusType.Confusion) && user.attack.getAttackCategory() == AttackCategory.STATUS) {
            user.bc.sendToAll("pixelmon.effect.alreadyconfused", target.getNickname());
            user.attack.moveResult.result = AttackResult.failed;
         }

         return false;
      }
   }

   public boolean canAttackThisTurn(PixelmonWrapper user, Attack a) {
      if (--this.effectTurns <= 0) {
         user.removeStatus((StatusBase)this);
         return true;
      } else {
         user.bc.sendToAll("pixelmon.status.confused", user.getNickname());
         if (RandomHelper.getRandomChance(0.33F)) {
            user.bc.sendToAll("pixelmon.status.confusionhurtself", user.getNickname());
            user.doBattleDamage(user, (float)this.calculateConfusionDamage(user), DamageTypeEnum.STATUS);
            return false;
         } else {
            return true;
         }
      }
   }

   private int calculateConfusionDamage(PixelmonWrapper user) {
      Attack attack = new Attack(new AttackBase(EnumType.Mystery, 40, AttackCategory.PHYSICAL));
      return attack.doDamageCalc(user, user, 1.0);
   }

   public String getCureMessage() {
      return "pixelmon.status.confusionsnap";
   }

   public String getCureMessageItem() {
      return "pixelmon.status.confusioncureitem";
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      boolean hitsAlly = userChoice.hitsAlly();
      boolean offensive = userChoice.isOffensiveMove();
      if (!hitsAlly) {
         Iterator var9 = userChoice.targets.iterator();

         while(true) {
            PixelmonWrapper target;
            do {
               if (!var9.hasNext()) {
                  return;
               }

               target = (PixelmonWrapper)var9.next();
            } while(offensive && hitsAlly);

            if (target.addStatus(new Confusion(), pw)) {
               userChoice.raiseWeight(this.getWeightWithChance(40.0F));
            }
         }
      }
   }
}
