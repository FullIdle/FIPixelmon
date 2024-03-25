package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.ParticipantType;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.SheerForce;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.StickyHold;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.SuctionCups;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemRedCard extends ItemHeld {
   public ItemRedCard() {
      super(EnumHeldItems.redCard, "red_card");
   }

   public void postProcessDamagingAttackTarget(PixelmonWrapper attacker, PixelmonWrapper target, Attack attack, float damage) {
      if (!attacker.bc.simulateMode && attacker.getParticipant().getType() != ParticipantType.WildPokemon && !(attacker.getBattleAbility() instanceof SuctionCups)) {
         if (!attacker.isFainted()) {
            if (!(attacker.getBattleAbility() instanceof SheerForce) || !attack.getMove().hasSecondaryEffect()) {
               if (!attack.isAttack("Knock Off") || target.getBattleAbility().isAbility(StickyHold.class)) {
                  if (damage > 0.0F && !attacker.hasStatus(StatusType.Ingrain)) {
                     PixelmonWrapper randomPokemon = attacker.getParticipant().getRandomPartyPokemon();
                     if (randomPokemon != null) {
                        target.consumeItem();
                        attacker.bc.sendToAll("pixelmon.helditems.redcard", target.getNickname());
                        if (attacker.isDynamax > 0) {
                           attacker.bc.sendToAll("pixelmon.helditems.noeffect");
                        } else {
                           attacker.bc.sendToAll("pixelmon.effect.flee", attacker.getNickname());
                           attacker.forceRandomSwitch(randomPokemon.getPokemonUUID());
                        }
                     }
                  }

               }
            }
         }
      }
   }
}
