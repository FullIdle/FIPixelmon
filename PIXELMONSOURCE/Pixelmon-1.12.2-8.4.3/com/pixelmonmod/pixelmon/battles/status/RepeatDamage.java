package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.GlobalStatusController;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.MagicGuard;
import java.util.ArrayList;
import java.util.Iterator;

public class RepeatDamage extends GlobalStatusBase {
   public transient AttackBase variant;
   private transient int turnsToGo;
   private transient PixelmonWrapper cause;
   private transient int targetLocation;

   public RepeatDamage() {
      super(StatusType.GMaxRepeatDamage);
   }

   public RepeatDamage(AttackBase variant, PixelmonWrapper cause, int targetLocation) {
      super(StatusType.GMaxRepeatDamage);
      this.variant = variant;
      this.cause = cause;
      this.turnsToGo = 4;
      this.targetLocation = targetLocation;
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      AttackBase variant = user.attack.getMove();
      ArrayList targets = user.bc.getAdjacentPokemonAndSelf(target);
      Iterator var5 = targets.iterator();

      while(var5.hasNext()) {
         PixelmonWrapper pw = (PixelmonWrapper)var5.next();
         if (!pw.isSameTeam(user)) {
            user.bc.globalStatusController.addGlobalStatus(new RepeatDamage(variant, user, pw.bc.getPositionOfPokemon(pw)));
         }
      }

      if (variant.isAttack("G-Max Wildfire")) {
         user.bc.sendToPlayer(user.getPlayerOwner(), "pixelmon.effect.wildfire.user");
         user.bc.sendToPlayer(target.getPlayerOwner(), "pixelmon.effect.wildfire.target");
      } else if (variant.isAttack("G-Max Vine Lash")) {
         user.bc.sendToPlayer(user.getPlayerOwner(), "pixelmon.effect.vinelash.user");
         user.bc.sendToPlayer(target.getPlayerOwner(), "pixelmon.effect.vinelash.target");
      } else if (variant.isAttack("G-Max Cannonade")) {
         user.bc.sendToPlayer(user.getPlayerOwner(), "pixelmon.effect.cannonade.user");
         user.bc.sendToPlayer(target.getPlayerOwner(), "pixelmon.effect.cannonade.target");
      } else if (variant.isAttack("G-Max Volcalith")) {
         user.bc.sendToPlayer(user.getPlayerOwner(), "pixelmon.effect.volcalith.user");
         user.bc.sendToPlayer(target.getPlayerOwner(), "pixelmon.effect.volcalith.target");
      }

   }

   public void applyRepeatedEffect(GlobalStatusController gsc) {
      if (--this.turnsToGo < 0) {
         gsc.removeGlobalStatus((GlobalStatusBase)this);
      } else {
         PixelmonWrapper pw = gsc.bc.getPokemonAtPosition(this.targetLocation);
         if (pw != null && !(pw.getBattleAbility() instanceof MagicGuard) && !pw.type.contains(this.variant.getAttackType()) && !pw.isFainted()) {
            pw.doBattleDamage(pw, (float)pw.getPercentMaxHealth(16.666666F), DamageTypeEnum.STATUS);
            if (this.variant.isAttack("G-Max Wildfire")) {
               pw.bc.sendToAll("pixelmon.status.wildfire", pw.getNickname());
            } else if (this.variant.isAttack("G-Max Vine Lash")) {
               pw.bc.sendToAll("pixelmon.status.vinelash", pw.getNickname());
            } else if (this.variant.isAttack("G-Max Cannonade")) {
               pw.bc.sendToAll("pixelmon.status.cannonade", pw.getNickname());
            } else if (this.variant.isAttack("G-Max Volcalith")) {
               pw.bc.sendToAll("pixelmon.status.volcalith", pw.getNickname());
            }

         }
      }
   }

   public int getRemainingTurns() {
      return this.turnsToGo;
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!userChoice.hitsAlly()) {
         float weight;
         for(Iterator var7 = userChoice.targets.iterator(); var7.hasNext(); userChoice.raiseWeight(weight)) {
            PixelmonWrapper target = (PixelmonWrapper)var7.next();
            weight = 15.0F;
            if (target.getBattleAbility() instanceof MagicGuard) {
               weight = 8.0F;
            }
         }

      }
   }
}
