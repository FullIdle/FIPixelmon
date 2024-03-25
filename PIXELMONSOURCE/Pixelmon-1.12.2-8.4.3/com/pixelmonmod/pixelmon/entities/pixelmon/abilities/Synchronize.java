package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import net.minecraft.entity.EntityLivingBase;

public class Synchronize extends AbilityBase {
   public void onStatusAdded(StatusBase status, PixelmonWrapper user, PixelmonWrapper opponent) {
      if (user != opponent && StatusType.isPrimaryStatus(status.type) && status.type != StatusType.Freeze && status.type != StatusType.Sleep && !opponent.cannotHaveStatus(status, opponent) && !status.isImmune(opponent) && opponent.addStatus(status.copy(), opponent)) {
         opponent.bc.sendToAll("pixelmon.entities.syncpassed", opponent.getNickname());
      }

   }

   public void applyStartOfBattleHeadOfPartyEffect(PixelmonWrapper user, PixelmonWrapper opponent) {
      if (user.getParticipant() instanceof PlayerParticipant && opponent.getParticipant() instanceof WildPixelmonParticipant) {
         EntityLivingBase entity = opponent.getParticipant().getEntity();
         if (entity == null || !entity.getEntityData().func_74767_n("SynchronizeAttempt")) {
            if (RandomHelper.getRandomChance(PixelmonConfig.synchronizeChance)) {
               opponent.pokemon.setNature(user.getBaseNature());
               opponent.entity.getPokemonData().setNature(user.getBaseNature());
               opponent.entity.updateStats();
            }

            if (entity != null) {
               entity.getEntityData().func_74757_a("SynchronizeAttempt", true);
            }
         }
      }

   }
}
