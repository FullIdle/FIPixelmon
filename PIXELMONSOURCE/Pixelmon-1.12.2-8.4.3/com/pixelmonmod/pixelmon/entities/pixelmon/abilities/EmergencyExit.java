package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.ParticipantType;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.battles.tasks.EnforcedSwitchTask;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleEndCause;

public class EmergencyExit extends AbilityBase {
   public void tookDamageTargetAfterMove(PixelmonWrapper user, PixelmonWrapper target, Attack a, float damage) {
      if (user != target) {
         if (!user.bc.simulateMode) {
            if (target.getHealthPercent((float)target.lastHP) >= 50.0F && target.getHealthPercent() <= 50.0F && !target.isFainted()) {
               if (target.getParticipant() instanceof WildPixelmonParticipant) {
                  user.willTryFlee = true;
                  target.bc.endBattle(EnumBattleEndCause.FLEE);
               } else {
                  BattleParticipant targetParticipant = target.getParticipant();
                  ParticipantType targetType = targetParticipant.getType();
                  if (!targetParticipant.hasMorePokemonReserve()) {
                     return;
                  }

                  target.bc.sendToAll("pixelmon.effect.escape", target.getNickname());
                  target.setUpSwitchMove();
                  if (targetType == ParticipantType.Player) {
                     Pixelmon.network.sendTo(new EnforcedSwitchTask(target.bc.getPositionOfPokemon(target, targetParticipant)), target.getPlayerOwner());
                  } else {
                     target.bc.switchPokemon(target.getPokemonUUID(), target.getBattleAI().getNextSwitch(target), true);
                  }
               }
            }

         }
      }
   }
}
