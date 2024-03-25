package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.ParticipantType;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.Spectator;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.SwapPosition;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.PropellerTail;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Stalwart;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class AllySwitch extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.bc.simulateMode) {
         return AttackResult.failed;
      } else {
         ArrayList team = user.bc.getTeamPokemon(user.getParticipant());
         if (team.size() > 1 && user.bc.getTeam(user.getParticipant()).size() == 1) {
            PixelmonWrapper switchPokemon = null;
            Iterator var5;
            PixelmonWrapper pw;
            if (user.bc.rules.battleType == EnumBattleType.Double) {
               var5 = team.iterator();

               while(var5.hasNext()) {
                  pw = (PixelmonWrapper)var5.next();
                  if (pw != user) {
                     switchPokemon = pw;
                  }
               }
            } else if (user.bc.rules.battleType == EnumBattleType.Triple) {
               if (user.battlePosition == 1) {
                  user.bc.sendToAll("pixelmon.effect.effectfailed");
                  return AttackResult.failed;
               }

               var5 = team.iterator();

               while(var5.hasNext()) {
                  pw = (PixelmonWrapper)var5.next();
                  if (pw.battlePosition != 1 && pw != user) {
                     switchPokemon = pw;
                  }
               }
            }

            if (switchPokemon != null && !switchPokemon.isFainted()) {
               user.bc.sendToAll("pixelmon.effect.allyswitch", user.getNickname(), switchPokemon.getNickname());
               List userStatuses = user.getStatuses();
               List allyStatuses = switchPokemon.getStatuses();
               List userPositionStatuses = this.removePositionStatuses(userStatuses);
               List allyPositionStatuses = this.removePositionStatuses(allyStatuses);
               allyStatuses.addAll((Collection)userPositionStatuses.stream().collect(Collectors.toList()));
               userStatuses.addAll((Collection)allyPositionStatuses.stream().collect(Collectors.toList()));
               int userPosition = user.battlePosition;
               user.battlePosition = switchPokemon.battlePosition;
               switchPokemon.battlePosition = userPosition;
               if (!user.hasStatus(StatusType.FollowMe) && !switchPokemon.hasStatus(StatusType.FollowMe)) {
                  for(int i = user.bc.turn + 1; i < user.bc.turnList.size(); ++i) {
                     PixelmonWrapper current = (PixelmonWrapper)user.bc.turnList.get(i);
                     if (current != user && current != switchPokemon && current.attack != null && current.targets != null && !current.getBattleAbility().isAbility(PropellerTail.class) && !current.getBattleAbility().isAbility(Stalwart.class)) {
                        for(int j = 0; j < current.targets.size(); ++j) {
                           if (current.targets.get(j) == user) {
                              current.targets.set(j, switchPokemon);
                           } else if (current.targets.get(j) == switchPokemon) {
                              current.targets.set(j, user);
                           }
                        }
                     }
                  }
               }

               Iterator var15 = user.bc.participants.iterator();

               while(var15.hasNext()) {
                  BattleParticipant participant = (BattleParticipant)var15.next();
                  if (participant.getType() == ParticipantType.Player) {
                     Pixelmon.network.sendTo(new SwapPosition(user, switchPokemon), ((PlayerParticipant)participant).player);
                  }
               }

               var15 = user.bc.spectators.iterator();

               while(var15.hasNext()) {
                  Spectator spectator = (Spectator)var15.next();
                  spectator.sendMessage(new SwapPosition(user, switchPokemon));
               }

               return AttackResult.succeeded;
            } else {
               user.bc.sendToAll("pixelmon.effect.effectfailed");
               return AttackResult.failed;
            }
         } else {
            user.bc.sendToAll("pixelmon.effect.effectfailed");
            return AttackResult.failed;
         }
      }
   }

   private List removePositionStatuses(List statuses) {
      List positionStatuses = new ArrayList(statuses.size());

      for(int i = 0; i < statuses.size(); ++i) {
         StatusBase status = (StatusBase)statuses.get(i);
         if (status.isTeamStatus() && !status.isWholeTeamStatus()) {
            statuses.remove(status);
            positionStatuses.add(status);
         }
      }

      return positionStatuses;
   }
}
