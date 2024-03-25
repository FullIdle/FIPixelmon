package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import java.util.ArrayList;
import net.minecraft.util.text.TextComponentTranslation;

public class Infatuated extends StatusBase {
   transient PixelmonWrapper originalTarget;

   public Infatuated() {
      super(StatusType.Infatuated);
   }

   public Infatuated(PixelmonWrapper originalTarget) {
      this();
      this.originalTarget = originalTarget;
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      infatuate(user, target, true);
   }

   public static boolean infatuate(PixelmonWrapper user, PixelmonWrapper target, boolean showMessage) {
      if (!user.getGender().isCompatible(target.getGender())) {
         if (showMessage) {
            user.bc.sendToAll("pixelmon.effect.effectfailed");
            user.setAttackFailed();
         }

         return false;
      } else if (target.hasStatus(StatusType.Infatuated)) {
         if (showMessage) {
            user.bc.sendToAll("pixelmon.effect.already", target.getNickname());
            user.setAttackFailed();
         }

         return false;
      } else {
         TextComponentTranslation message = null;
         if (showMessage) {
            message = ChatHandler.getMessage("pixelmon.effect.falleninlove", target.getNickname());
         }

         return target.addStatus(new Infatuated(user), user, message);
      }
   }

   public boolean canAttackThisTurn(PixelmonWrapper user, Attack a) {
      if (!user.bc.isInBattle(this.originalTarget)) {
         user.removeStatus((StatusBase)this);
         return true;
      } else {
         user.bc.sendToAll("pixelmon.status.inlove", user.getNickname(), this.originalTarget.getNickname());
         if (RandomHelper.getRandomChance(50)) {
            user.bc.sendToAll("pixelmon.status.immobilizedbylove", user.getNickname());
            return false;
         } else {
            return true;
         }
      }
   }

   public String getCureMessage() {
      return "pixelmon.status.infatuatedcure";
   }

   public String getCureMessageItem() {
      return "pixelmon.status.infatuatedcureitem";
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!userChoice.hitsAlly()) {
         userChoice.raiseWeight(40.0F);
      }

   }
}
