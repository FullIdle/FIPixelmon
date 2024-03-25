package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemAirBalloon extends ItemHeld {
   public ItemAirBalloon() {
      super(EnumHeldItems.airBalloon, "air_balloon");
   }

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      newPokemon.bc.sendToAll("pixelmon.helditems.airballoon", newPokemon.getNickname());
   }

   public boolean allowsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
      if (a.getType().equals(EnumType.Ground) && a.getAttackCategory() != AttackCategory.STATUS && !pokemon.isGrounded() && !a.isAttack("Thousand Arrows")) {
         user.bc.sendToAll("pixelmon.battletext.noeffect", pokemon.getNickname());
         return false;
      } else {
         return true;
      }
   }

   public void postProcessDamagingAttackTarget(PixelmonWrapper attacker, PixelmonWrapper holder, Attack attack, float damage) {
      if (damage > 0.0F && attacker != holder) {
         holder.removeHeldItem();
         attacker.bc.sendToAll("pixelmon.helditems.airballoonpop", holder.getNickname());
      }

   }
}
