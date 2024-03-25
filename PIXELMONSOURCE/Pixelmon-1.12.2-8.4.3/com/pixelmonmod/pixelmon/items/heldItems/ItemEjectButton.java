package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.ParticipantType;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.tasks.EnforcedSwitchTask;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.SheerForce;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;

public class ItemEjectButton extends ItemHeld {
   public ItemEjectButton() {
      super(EnumHeldItems.ejectButton, "eject_button");
   }

   public void postProcessDamagingAttackTarget(PixelmonWrapper attacker, PixelmonWrapper target, Attack attack, float damage) {
      if (!attacker.bc.simulateMode && damage > 0.0F && target.isAlive()) {
         if (attacker.getBattleAbility() instanceof SheerForce && attack.getMove().hasSecondaryEffect()) {
            return;
         }

         BattleParticipant targetParticipant = target.getParticipant();
         ParticipantType targetType = targetParticipant.getType();
         if (targetType == ParticipantType.WildPokemon || !targetParticipant.hasMorePokemonReserve()) {
            return;
         }

         this.setUpSwitch(target);
         if (targetType == ParticipantType.Player) {
            Pixelmon.network.sendTo(new EnforcedSwitchTask(target.bc.getPositionOfPokemon(target, targetParticipant)), target.getPlayerOwner());
         } else {
            target.bc.switchPokemon(target.getPokemonUUID(), target.getBattleAI().getNextSwitch(target), true);
         }
      }

   }

   private void setUpSwitch(PixelmonWrapper target) {
      target.bc.sendToAll("pixelmon.helditems.ejectbutton", target.getNickname());
      target.consumeItem();
      target.setUpSwitchMove();
   }
}
