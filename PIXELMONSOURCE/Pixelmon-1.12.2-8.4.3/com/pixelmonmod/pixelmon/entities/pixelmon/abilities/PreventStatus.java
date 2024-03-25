package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;

public class PreventStatus extends AbilityBase {
   protected StatusType[] preventedStatuses;
   protected String immuneText;
   protected String cureText;

   public PreventStatus(String immuneText, String cureText, StatusType... preventedStatuses) {
      this.preventedStatuses = preventedStatuses;
      this.immuneText = immuneText;
      this.cureText = cureText;
   }

   public boolean allowsStatus(StatusType status, PixelmonWrapper pokemon, PixelmonWrapper user) {
      if (status.isStatus(this.preventedStatuses)) {
         if (user != pokemon && user.attack != null && user.attack.getAttackCategory() == AttackCategory.STATUS) {
            user.bc.sendToAll(this.immuneText, pokemon.getNickname());
         }

         return false;
      } else {
         return true;
      }
   }

   public void onStatusAdded(StatusBase status, PixelmonWrapper user, PixelmonWrapper opponent) {
      if (status.type.isStatus(this.preventedStatuses)) {
         user.removeStatus(status, false);
         user.bc.sendToAll(this.cureText, user.getNickname());
      }
   }

   public void applySwitchInEffect(PixelmonWrapper pokemon) {
      for(int i = 0; i < pokemon.getStatusSize(); ++i) {
         StatusBase status = pokemon.getStatus(i);
         if (status.type.isStatus(this.preventedStatuses)) {
            pokemon.removeStatus(i);
            pokemon.bc.sendToAll(this.cureText, pokemon.getNickname());
            return;
         }
      }

   }
}
