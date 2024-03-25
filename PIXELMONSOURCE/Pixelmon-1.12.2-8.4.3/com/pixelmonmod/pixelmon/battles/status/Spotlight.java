package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.PropellerTail;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Stalwart;

public class Spotlight extends StatusBase {
   public Spotlight() {
      super(StatusType.FollowMe);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      user.bc.sendToAll("pixelmon.status.followme", target.getNickname());
      target.addStatus(new Spotlight(), user);
   }

   public boolean redirectAttack(PixelmonWrapper user, PixelmonWrapper targetAlly, Attack attack) {
      return user.isOpponent(targetAlly) && attack.getMove().getTargetingInfo().hitsAdjacentFoe && !attack.isAttack("Bide", "Bounce", "Dig", "Dive", "Fly", "Freeze Shock", "Ice Burn", "Phantom Force", "Razor Wind", "Shadow Force", "Skull Bash", "Sky attack", "Sky Drop", "SolarBeam", "Snipe Shot") && !user.getBattleAbility().isAbility(PropellerTail.class) && !user.getBattleAbility().isAbility(Stalwart.class);
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      pw.removeStatus((StatusBase)this);
   }
}
