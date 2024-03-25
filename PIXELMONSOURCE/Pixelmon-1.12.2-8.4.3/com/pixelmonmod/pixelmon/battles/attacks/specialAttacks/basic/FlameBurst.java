package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.MagicGuard;

public class FlameBurst extends SpecialAttackBase {
   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.bc.rules.battleType.numPokemon > 1) {
         BattleParticipant targetParticipant = target.getParticipant();
         user.bc.getTeamPokemon(targetParticipant).stream().filter((pw) -> {
            return (Math.abs(pw.battlePosition - target.battlePosition) == 1 || pw.getParticipant() != targetParticipant) && !(pw.getBattleAbility() instanceof MagicGuard);
         }).forEach((pw) -> {
            pw.doBattleDamage(user, (float)(pw.getMaxHealth() / 16), DamageTypeEnum.STATUS);
            user.bc.sendToAll("pixelmon.effect.hurt", pw.getNickname());
         });
      }

   }
}
