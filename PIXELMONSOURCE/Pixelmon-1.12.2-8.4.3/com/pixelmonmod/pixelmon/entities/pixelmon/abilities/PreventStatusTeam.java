package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;

public class PreventStatusTeam extends AbilityBase {
   protected StatusType[] preventedStatuses;
   protected String immuneText;
   protected String cureText;

   public PreventStatusTeam(String immuneText, String cureText, StatusType... preventedStatuses) {
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

   public boolean allowsStatusTeammate(StatusType status, PixelmonWrapper pokemon, PixelmonWrapper target, PixelmonWrapper user) {
      if (status.isStatus(this.preventedStatuses)) {
         if (user != target && user.attack != null && user.attack.getAttackCategory() == AttackCategory.STATUS) {
            user.bc.sendToAll(this.immuneText, target.getNickname());
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
      pokemon.getTeamPokemon().forEach((tp) -> {
         for(int i = 0; i < tp.getStatusSize(); ++i) {
            StatusBase status = tp.getStatus(i);
            if (status.type.isStatus(this.preventedStatuses)) {
               tp.removeStatus(i);
               tp.bc.sendToAll(this.cureText, tp.getNickname());
               return;
            }
         }

      });
   }
}
