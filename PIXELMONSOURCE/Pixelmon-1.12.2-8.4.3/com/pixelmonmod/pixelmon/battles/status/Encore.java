package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.util.text.TextComponentTranslation;

public class Encore extends StatusBase {
   transient Attack attack;
   transient int turns;

   public Encore() {
      super(StatusType.Encore);
   }

   public Encore(Attack attack) {
      super(StatusType.Encore);
      this.attack = attack;
      this.turns = 3;
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      Attack lastAttack = target.lastAttack;
      if (target.isDynamax()) {
         target.bc.sendToAll("pixelmon.effect.effectfailed");
         user.attack.moveResult.result = AttackResult.failed;
      } else {
         TextComponentTranslation message = ChatHandler.getMessage("pixelmon.status.encore", target.getNickname());
         if (lastAttack == null || lastAttack.pp <= 0 || lastAttack.isAttack("Encore", "Mimic", "Mirror Move", "Sketch", "Struggle", "Transform") || !target.addStatus(new Encore(lastAttack), target, message)) {
            if (target.hasStatus(StatusType.Disable)) {
               user.attack.moveResult.result = AttackResult.failed;
            }

            target.bc.sendToAll("pixelmon.effect.effectfailed");
         }

      }
   }

   public boolean canAttackThisTurn(PixelmonWrapper user, Attack a) {
      if (!user.attack.equals(this.attack) && !a.isAttack("Struggle")) {
         user.attack = this.attack;
         user.targets = user.getTargets(this.attack);
      }

      return true;
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      if (this.turns <= 0) {
         pw.bc.sendToAll("pixelmon.status.encoreend", pw.getNickname());
         pw.removeStatus((StatusBase)this);
      } else {
         Iterator var2 = pw.getMoveset().iterator();

         while(var2.hasNext()) {
            Attack attack = (Attack)var2.next();
            if (attack != null && !attack.equals(this.attack)) {
               attack.setDisabled(true, pw);
            }
         }

      }
   }

   public void onEndOfTurn(PixelmonWrapper pw) {
      --this.turns;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      Iterator var7 = userChoice.targets.iterator();

      while(var7.hasNext()) {
         PixelmonWrapper target = (PixelmonWrapper)var7.next();
         if (target.isDynamax == 0 && !target.hasStatus(StatusType.Encore) && !MoveChoice.canOutspeed(bestOpponentChoices, pw, userChoice.createList()) && target.lastAttack != null && target.lastAttack.getAttackCategory() == AttackCategory.STATUS) {
            userChoice.raiseWeight(100.0F);
         }
      }

   }
}
