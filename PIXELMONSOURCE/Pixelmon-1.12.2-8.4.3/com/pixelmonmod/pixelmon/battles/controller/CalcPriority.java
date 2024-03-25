package com.pixelmonmod.pixelmon.battles.controller;

import com.pixelmonmod.pixelmon.battles.attacks.EffectBase;
import com.pixelmonmod.pixelmon.battles.attacks.MaxMoveConverter;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.ParticipantType;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import com.pixelmonmod.pixelmon.tools.MutableBoolean;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class CalcPriority {
   public static SpeedPriorityComparator speedPriorityComparator = new SpeedPriorityComparator();
   public static SpeedComparator speedComparator = new SpeedComparator();

   public static void checkMoveSpeed(BattleControllerBase bc) {
      ArrayList fullParticipants = new ArrayList(6);
      Iterator var2 = bc.participants.iterator();

      while(var2.hasNext()) {
         BattleParticipant p = (BattleParticipant)var2.next();
         fullParticipants.addAll(p.controlledPokemon);
      }

      ArrayList filteredParticipants = new ArrayList(fullParticipants.size());
      filteredParticipants.addAll((Collection)fullParticipants.stream().filter((px) -> {
         return px.isAlive();
      }).collect(Collectors.toList()));
      sortByMoveSpeed(bc, filteredParticipants);
      bc.turnList = filteredParticipants;
   }

   private static void sortByMoveSpeed(BattleControllerBase bc, List participants) {
      participants.stream().forEach((p) -> {
         if (!bc.simulateMode) {
            p.priority = calculatePriority(p);
         }

      });
      participants.sort(speedPriorityComparator);
      if (!bc.simulateMode) {
         participants.stream().filter((p) -> {
            return p.attack != null;
         }).forEach((p) -> {
            Iterator var1 = p.attack.getMove().effects.iterator();

            while(var1.hasNext()) {
               EffectBase e = (EffectBase)var1.next();
               e.applyEarlyEffect(p);
            }

         });
      }

   }

   public static void recalculateMoveSpeed(BattleControllerBase bc, int turn) {
      List toMove = new ArrayList();

      while(bc.turnList.size() > turn) {
         toMove.add(bc.turnList.remove(turn));
      }

      sortByMoveSpeed(bc, toMove);
      bc.turnList.addAll((Collection)toMove.stream().filter((p) -> {
         return p.isAlive();
      }).collect(Collectors.toList()));
   }

   static List getDefaultTurnOrder(BattleControllerBase bc) {
      ArrayList turnOrder = new ArrayList(6);
      Iterator var2 = bc.participants.iterator();

      while(var2.hasNext()) {
         BattleParticipant p = (BattleParticipant)var2.next();
         turnOrder.addAll(p.controlledPokemon);
      }

      turnOrder.sort(speedComparator);
      return turnOrder;
   }

   public static List getTurnOrder(List pokemon) {
      ArrayList turnOrder = new ArrayList(6);
      turnOrder.addAll(pokemon);
      turnOrder.sort(speedComparator);
      return turnOrder;
   }

   public static float calculatePriority(PixelmonWrapper p) {
      float priority = 0.0F;
      if (p.willTryFlee && p.getParticipant().getType() != ParticipantType.WildPokemon) {
         priority = 8.0F;
      } else if (p.willUseItemInStack == null && !p.isSwitching) {
         if (p.attack != null) {
            if (!p.usingZ || p.attack.getMove().getAttackCategory() == AttackCategory.STATUS) {
               if (p.isDynamax == 1) {
                  priority = (float)MaxMoveConverter.getMaxMoveFromAttack(p.attack, p).getMove().getPriority(p);
               } else if (p.isDynamax == 2) {
                  priority = (float)MaxMoveConverter.getGMaxMoveFromAttack(p.attack, p, p.getSpecies(), p.getFormEnum()).getMove().getPriority(p);
               } else {
                  priority = (float)p.attack.getMove().getPriority(p);
               }
            }

            MutableBoolean triggered = new MutableBoolean(false);
            priority = p.getBattleAbility().modifyPriority(p, priority, triggered);
            priority = p.getUsableHeldItem().modifyPriority(p, priority, triggered);
            if (p.attack.isAttack("Pursuit") && !p.usingZ) {
               Iterator var3 = p.targets.iterator();

               while(var3.hasNext()) {
                  PixelmonWrapper target = (PixelmonWrapper)var3.next();
                  if (target.isSwitching && target.isAlive()) {
                     priority = 7.0F;
                     break;
                  }
               }
            }
         }
      } else {
         priority = 6.0F;
      }

      return priority;
   }
}
