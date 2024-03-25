package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.GlobalStatusController;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.ArrayList;
import net.minecraft.util.text.TextComponentTranslation;

public abstract class Sport extends GlobalStatusBase {
   protected transient PixelmonWrapper user;
   protected EnumType affectedType;
   private transient int turnsLeft;
   private String moveName;

   public Sport(PixelmonWrapper user, StatusType status, EnumType affectedType, String moveName) {
      super(status);
      this.user = user;
      this.affectedType = affectedType;
      this.turnsLeft = 5;
      this.moveName = moveName;
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.bc.globalStatusController.hasStatus(this.type)) {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         user.attack.moveResult.result = AttackResult.failed;
      } else {
         user.bc.sendToAll("pixelmon.status.sport", this.affectedType.getLocalizedName());
         user.bc.globalStatusController.addGlobalStatus(this.getNewInstance(user));
      }

   }

   protected abstract Sport getNewInstance(PixelmonWrapper var1);

   public int[] modifyPowerAndAccuracyTarget(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a) {
      if (a.getType() == this.affectedType) {
         power /= 3;
      }

      return new int[]{power, accuracy};
   }

   public void applyRepeatedEffect(GlobalStatusController gsc) {
      if (--this.turnsLeft <= 0) {
         gsc.removeGlobalStatus((GlobalStatusBase)this);
         gsc.bc.sendToAll("pixelmon.status.sportfade", new TextComponentTranslation("attack." + this.moveName.toLowerCase().replace(" ", "_") + ".name", new Object[0]));
      }

   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (MoveChoice.hasOffensiveAttackType(bestOpponentChoices, this.affectedType)) {
         userChoice.raiseWeight(20.0F);
      }

      if (MoveChoice.hasOffensiveAttackType(bestUserChoices, this.affectedType)) {
         userChoice.raiseWeight(-20.0F);
      }

   }
}
