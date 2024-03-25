package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Overcoat;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.PropellerTail;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Stalwart;
import java.util.ArrayList;
import java.util.Iterator;

public class FollowMe extends StatusBase {
   private transient boolean isPowder;
   transient PixelmonWrapper centerOfAttention;

   public FollowMe() {
      this(false, (PixelmonWrapper)null);
   }

   public FollowMe(boolean isPowder, PixelmonWrapper centerOfAttention) {
      super(StatusType.FollowMe);
      this.isPowder = isPowder;
      this.centerOfAttention = centerOfAttention;
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      user.bc.sendToAll("pixelmon.status.followme", user.getNickname());
      user.addStatus(new FollowMe(Overcoat.isPowderMove(user.attack), user), user);
   }

   public boolean redirectAttack(PixelmonWrapper user, PixelmonWrapper targetAlly, Attack attack) {
      return attack.getMove().getTargetingInfo().hitsAdjacentFoe && (!this.isPowder || !user.isImmuneToPowder()) && !attack.isAttack("Bide", "Bounce", "Dig", "Dive", "Fly", "Freeze Shock", "Ice Burn", "Phantom Force", "Razor Wind", "Shadow Force", "Skull Bash", "Sky attack", "Sky Drop", "SolarBeam", "Snipe Shot", "Doom Desire", "Future Sight") && !user.getBattleAbility().isAbility(PropellerTail.class) && !user.getBattleAbility().isAbility(Stalwart.class) && !this.centerOfAttention.isFainted();
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      pw.removeStatus((StatusBase)this);
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (pw.bc.rules.battleType.numPokemon != 1) {
         Iterator var7 = pw.getTeamPokemonExcludeSelf().iterator();

         while(var7.hasNext()) {
            PixelmonWrapper ally = (PixelmonWrapper)var7.next();
            if (!MoveChoice.hasSpreadMove(bestOpponentChoices) && !MoveChoice.getTargetedChoices(ally, bestOpponentChoices).isEmpty()) {
               userChoice.raiseWeight(25.0F);
               break;
            }
         }

      }
   }
}
