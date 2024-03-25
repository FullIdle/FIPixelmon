package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.TargetingInfo;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class MaxGuard extends Protect {
   private static final String[] NON_PROTECTED_MOVES = new String[]{"Feint", "Mean Look", "Role Play", "Perish Song", "Decorate", "G-Max One Blow", "G-Max Rapid Flow", "Spikes", "Stealth Rock", "Sticky Web", "Toxic Spikes"};

   public MaxGuard() {
      super(StatusType.MaxGuard);
   }

   protected boolean addStatus(PixelmonWrapper user) {
      return user.addStatus(new MaxGuard(), user);
   }

   public boolean stopsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user) {
      TargetingInfo targetingInfo = user.attack.getMove().getTargetingInfo();
      if (pokemon.isSameTeam(user) && (targetingInfo.hitsAdjacentAlly || targetingInfo.hitsExtendedAlly) && !targetingInfo.hitsOppositeFoe && !targetingInfo.hitsAdjacentFoe && !targetingInfo.hitsExtendedFoe) {
         return false;
      } else {
         return !user.attack.isAttack(NON_PROTECTED_MOVES);
      }
   }
}
