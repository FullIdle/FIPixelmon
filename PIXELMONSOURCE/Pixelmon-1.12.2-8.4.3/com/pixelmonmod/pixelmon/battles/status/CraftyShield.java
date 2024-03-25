package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;

public class CraftyShield extends ProtectVariationTeam {
   public CraftyShield() {
      super(StatusType.CraftyShield);
   }

   public ProtectVariationTeam getNewInstance() {
      return new CraftyShield();
   }

   protected void displayMessage(PixelmonWrapper user) {
      user.bc.sendToAll("pixelmon.status.craftyshield", user.getNickname());
   }

   public boolean stopsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user) {
      return super.stopsIncomingAttack(pokemon, user) && user.attack.getAttackCategory() == AttackCategory.STATUS;
   }

   public void stopsIncomingAttackMessage(PixelmonWrapper pokemon, PixelmonWrapper user) {
      user.bc.sendToAll("pixelmon.status.craftyshieldprotect", pokemon.getNickname());
   }
}
