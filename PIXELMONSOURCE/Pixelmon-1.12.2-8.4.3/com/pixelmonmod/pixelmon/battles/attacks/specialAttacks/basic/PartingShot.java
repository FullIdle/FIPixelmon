package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.tasks.EnforcedSwitchTask;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.ClearBody;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Contrary;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.FlowerVeil;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.FullMetalBody;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.MoldBreaker;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Teravolt;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Turboblaze;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.WhiteSmoke;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.ArrayList;
import java.util.Iterator;

public class PartingShot extends SpecialAttackBase {
   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      boolean flowerVeil = false;
      ArrayList enemyTeam = target.getTeamPokemon();
      Iterator var5 = enemyTeam.iterator();

      while(var5.hasNext()) {
         PixelmonWrapper pokemon = (PixelmonWrapper)var5.next();
         if (pokemon.getBattleAbility().isAbility(FlowerVeil.class)) {
            flowerVeil = true;
         }
      }

      if (!target.hasStatus(StatusType.Mist) && !target.getBattleAbility().isAbility(FullMetalBody.class) && (user.getBattleAbility().isAbility(MoldBreaker.class) || user.getBattleAbility().isAbility(Teravolt.class) || user.getBattleAbility().isAbility(Turboblaze.class) || !target.getBattleAbility().isAbility(ClearBody.class) && !target.getBattleAbility().isAbility(WhiteSmoke.class) && (!target.hasType(EnumType.Grass) || !flowerVeil)) && (!target.getBattleAbility().isAbility(Contrary.class) && (target.getBattleStats().statCanBeLowered(StatsType.Attack) || target.getBattleStats().statCanBeLowered(StatsType.SpecialAttack)) || target.getBattleAbility().isAbility(Contrary.class) && (target.getBattleStats().statCanBeRaised(StatsType.Attack) || target.getBattleStats().statCanBeRaised(StatsType.SpecialAttack)))) {
         BattleParticipant userParticipant = user.getParticipant();
         if (user.bc.simulateMode) {
            return;
         }

         user.nextSwitchIsMove = true;
         if (userParticipant instanceof TrainerParticipant) {
            user.bc.switchPokemon(user.getPokemonUUID(), user.getBattleAI().getNextSwitch(user), true);
         } else if (userParticipant instanceof PlayerParticipant) {
            if (userParticipant.hasMorePokemonReserve()) {
               boolean pursuitAttacker = false;
               int i = user.bc.turnList.size() - 1;

               for(int j = i; i >= 0; --i) {
                  PixelmonWrapper queuedPokemon = (PixelmonWrapper)user.bc.turnList.get(j);
                  int queuedIndex = user.bc.turnList.indexOf(queuedPokemon);
                  int userIndex = user.bc.turnList.indexOf(user);
                  if (userIndex == i) {
                     break;
                  }

                  if (queuedPokemon.attack.isAttack("Pursuit") && queuedPokemon.targets.contains(user) && userIndex < queuedIndex) {
                     queuedPokemon.bc.turnList.remove(queuedPokemon);
                     queuedPokemon.bc.turnList.add(user.bc.turn + 1, queuedPokemon);
                     pursuitAttacker = true;
                     ++j;
                  }

                  --j;
               }

               if (!pursuitAttacker) {
                  user.wait = true;
                  Pixelmon.network.sendTo(new EnforcedSwitchTask(user.bc.getPositionOfPokemon(user, user.getParticipant())), user.getPlayerOwner());
               }
            } else {
               user.nextSwitchIsMove = false;
            }
         } else {
            user.nextSwitchIsMove = false;
         }
      }

   }
}
