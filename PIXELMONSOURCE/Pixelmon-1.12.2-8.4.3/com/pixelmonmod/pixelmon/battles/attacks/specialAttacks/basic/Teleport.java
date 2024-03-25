package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.AI.AITeleportAway;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleEndCause;
import java.util.Iterator;

public class Teleport extends SwitchOut {
   public void applyEffect(PixelmonWrapper userWrapper, PixelmonWrapper targetWrapper) {
   }

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      boolean willSucceed = false;
      Iterator var4 = user.bc.participants.iterator();

      while(var4.hasNext()) {
         BattleParticipant participant = (BattleParticipant)var4.next();
         if (participant instanceof WildPixelmonParticipant) {
            willSucceed = true;
            break;
         }
      }

      if (willSucceed && BattleParticipant.canSwitch(user)[1] && user.isWildPokemon()) {
         if (!user.bc.simulateMode) {
            user.bc.sendToAll("teleport.away", user.getNickname());
            user.getParticipant().isDefeated = true;
            if (!user.bc.isTeamDefeated(user.getParticipant())) {
               user.entity.func_70106_y();
               user.bc.updateRemovedPokemon(user);
               user.bc.sendDamagePacket(target, target.getHealth());
               user.entity.func_70606_j(0.0F);
            } else {
               user.willTryFlee = true;
               user.bc.endBattle(EnumBattleEndCause.FLEE);
            }

            while(true) {
               if (!AITeleportAway.teleportRandomly(user.entity, RandomHelper.rand)) {
                  continue;
               }
            }
         }

         return AttackResult.succeeded;
      } else {
         if (!user.bc.battleEnded) {
            super.applyEffect(user, target);
         }

         return AttackResult.succeeded;
      }
   }
}
