package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class BanefulBunker extends Protect {
   public BanefulBunker() {
      super(StatusType.KingsShield);
   }

   protected boolean addStatus(PixelmonWrapper user) {
      return user.addStatus(new BanefulBunker(), user);
   }

   public void stopsIncomingAttackMessage(PixelmonWrapper pokemon, PixelmonWrapper user) {
      super.stopsIncomingAttackMessage(pokemon, user);
      if (user.attack.getMove().getMakesContact()) {
         user.addStatus(new Poison(), user);
      }

   }
}
