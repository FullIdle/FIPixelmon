package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.log.MoveResults;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.ParticipantType;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.battles.status.EntryHazard;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.SuctionCups;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleEndCause;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Flee extends SpecialAttackBase {
   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (!user.inParentalBond) {
         boolean isGhost = user.hasType(EnumType.Ghost);
         MoveResults results;
         boolean doesDamage;
         if (user.attack == null) {
            results = new MoveResults(target);
            doesDamage = false;
         } else {
            results = user.attack.moveResult;
            doesDamage = user.attack.getMove().getBasePower() > 0;
         }

         if (target.isDynamax()) {
            target.bc.sendToAll("pixelmon.effect.effectfailed");
            results.result = AttackResult.failed;
         } else {
            if (doesDamage) {
               if (target.getHealth() == 0 || target.hasStatus(StatusType.Ingrain, StatusType.Substitute)) {
                  return;
               }

               if (target.getBattleAbility() instanceof SuctionCups && !isGhost) {
                  target.bc.sendToAll("pixelmon.ability.suctioncups", target.getNickname());
                  return;
               }
            }

            if (target.hasStatus(StatusType.Ingrain) && !isGhost) {
               target.bc.sendToAll("pixelmon.effect.effectfailed");
               results.result = AttackResult.failed;
            } else if (target.getBattleAbility() instanceof SuctionCups && !isGhost) {
               target.bc.sendToAll("pixelmon.abilities.suctioncups", target.getNickname());
               results.result = AttackResult.failed;
            } else {
               BattleParticipant targetParticipant = target.getParticipant();
               if (targetParticipant.getType() != ParticipantType.WildPokemon && user.getParticipant().getType() != ParticipantType.WildPokemon) {
                  PixelmonWrapper randomPokemon = targetParticipant.getRandomPartyPokemon();
                  if (randomPokemon != null) {
                     if (user.bc.simulateMode) {
                        return;
                     }

                     target.bc.sendToAll("pixelmon.effect.flee", target.getNickname());
                     target.forceRandomSwitch(randomPokemon.getPokemonUUID());
                  } else if (!doesDamage) {
                     target.bc.sendToAll("pixelmon.effect.effectfailed");
                     results.result = AttackResult.failed;
                  }

               } else {
                  if (target.getLevelNum() > user.getLevelNum()) {
                     if (!doesDamage) {
                        target.bc.sendToAll("pixelmon.effect.effectfailed");
                        results.result = AttackResult.failed;
                     }
                  } else if (!user.bc.simulateMode) {
                     targetParticipant.isDefeated = true;
                     if (!target.bc.isTeamDefeated(targetParticipant)) {
                        target.entity.func_70106_y();
                        target.bc.updateRemovedPokemon(target);
                        target.bc.sendDamagePacket(target, target.getHealth());
                        target.entity.func_70606_j(0.0F);
                     } else {
                        target.bc.sendToAll("pixelmon.effect.flee", target.getNickname());
                        target.bc.endBattle(EnumBattleEndCause.FLEE);
                     }
                  }

               }
            }
         }
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      BattleParticipant part = pw.getParticipant();
      if (!userChoice.hitsAlly() && !(part instanceof WildPixelmonParticipant)) {
         Iterator var8 = userChoice.targets.iterator();

         while(var8.hasNext()) {
            PixelmonWrapper target = (PixelmonWrapper)var8.next();
            if (pw.attack.getMove().getBasePower() > 0 && (userChoice.tier >= 3 || target.hasStatus(StatusType.Substitute, StatusType.Ingrain) || target.getBattleAbility() instanceof SuctionCups || !part.hasMorePokemonReserve()) || target.isDynamax > 0) {
               return;
            }

            userChoice.raiseWeight((float)(target.getBattleStats().getSumStages() * 20));
            Iterator var10 = ((List)target.getEntryHazards().stream().filter((hazardx) -> {
               return hazardx.type != StatusType.StickyWeb;
            }).collect(Collectors.toList())).iterator();

            while(var10.hasNext()) {
               EntryHazard hazard = (EntryHazard)var10.next();
               userChoice.raiseWeight((float)(hazard.getAIWeight() * hazard.getNumLayers()));
            }
         }

      }
   }
}
