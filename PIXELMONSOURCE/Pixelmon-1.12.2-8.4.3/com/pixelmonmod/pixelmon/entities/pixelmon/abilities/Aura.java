package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.AuraStatus;
import com.pixelmonmod.pixelmon.battles.status.GlobalStatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.Iterator;

public abstract class Aura extends AbilityBase {
   private EnumType boostType;
   private StatusType auraStatus;

   public Aura(EnumType boostType, StatusType auraStatus) {
      this.boostType = boostType;
      this.auraStatus = auraStatus;
   }

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      GlobalStatusBase current = newPokemon.bc.globalStatusController.getGlobalStatus(this.auraStatus);
      if (current == null) {
         newPokemon.bc.globalStatusController.addGlobalStatus(new AuraStatus(this.boostType, this.auraStatus));
         newPokemon.bc.sendToAll("pixelmon.abilities." + this.auraStatus.toString().toLowerCase(), newPokemon.getNickname());
      }

   }

   public void applySwitchOutEffect(PixelmonWrapper oldPokemon) {
      this.removeAuraStatus(oldPokemon);
   }

   public void onAbilityLost(PixelmonWrapper pokemon) {
      this.removeAuraStatus(pokemon);
   }

   private void removeAuraStatus(PixelmonWrapper pokemon) {
      Iterator var2 = pokemon.bc.getActivePokemon().iterator();

      PixelmonWrapper pw;
      do {
         if (!var2.hasNext()) {
            pokemon.bc.globalStatusController.removeGlobalStatus(this.auraStatus);
            return;
         }

         pw = (PixelmonWrapper)var2.next();
      } while(pw == pokemon || !pw.getBattleAbility().getClass().isAssignableFrom(this.getClass()));

   }
}
