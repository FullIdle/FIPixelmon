package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CourtChange extends SpecialAttackBase {
   private static final transient List transferableStatuses;

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      if (user != target) {
         return AttackResult.proceed;
      } else {
         BattleControllerBase bc = user.bc;
         ArrayList opponents = user.getOpponentPokemon();
         HashMap toTransferToUser = new HashMap();
         HashMap toTransferToTarget = new HashMap();
         Iterator var7 = user.getTeamPokemon().iterator();

         PixelmonWrapper pw;
         ArrayList transfer;
         Iterator iterator;
         StatusBase status;
         while(var7.hasNext()) {
            pw = (PixelmonWrapper)var7.next();
            transfer = new ArrayList();
            iterator = pw.getStatuses().iterator();

            while(iterator.hasNext()) {
               status = (StatusBase)iterator.next();
               if (transferableStatuses.contains(status.type)) {
                  transfer.add(status);
                  iterator.remove();
               }
            }

            toTransferToTarget.put(bc.getOppositePokemon(pw), transfer);
         }

         var7 = opponents.iterator();

         while(var7.hasNext()) {
            pw = (PixelmonWrapper)var7.next();
            transfer = new ArrayList();
            iterator = pw.getStatuses().iterator();

            while(iterator.hasNext()) {
               status = (StatusBase)iterator.next();
               if (transferableStatuses.contains(status.type)) {
                  transfer.add(status);
                  iterator.remove();
               }
            }

            toTransferToUser.put(bc.getOppositePokemon(pw), transfer);
         }

         var7 = toTransferToTarget.entrySet().iterator();

         Map.Entry transfer;
         PixelmonWrapper pw;
         while(var7.hasNext()) {
            transfer = (Map.Entry)var7.next();
            pw = (PixelmonWrapper)transfer.getKey();
            iterator = ((List)transfer.getValue()).iterator();

            while(iterator.hasNext()) {
               status = (StatusBase)iterator.next();
               pw.addStatus(status, bc.getOppositePokemon(pw));
            }
         }

         var7 = toTransferToUser.entrySet().iterator();

         while(var7.hasNext()) {
            transfer = (Map.Entry)var7.next();
            pw = (PixelmonWrapper)transfer.getKey();
            iterator = ((List)transfer.getValue()).iterator();

            while(iterator.hasNext()) {
               status = (StatusBase)iterator.next();
               pw.addStatus(status, bc.getOppositePokemon(pw));
            }
         }

         return AttackResult.proceed;
      }
   }

   public void applyAfterEffect(PixelmonWrapper user) {
      user.bc.sendToAll("pixelmon.effect.courtchange", user.getNickname());
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
   }

   static {
      transferableStatuses = Arrays.asList(StatusType.Reflect, StatusType.LightScreen, StatusType.AuroraVeil, StatusType.StealthRock, StatusType.Spikes, StatusType.StickyWeb, StatusType.ToxicSpikes, StatusType.Tailwind, StatusType.Steelsurge, StatusType.FirePledge, StatusType.GrassPledge, StatusType.WaterPledge, StatusType.Mist, StatusType.SafeGuard, StatusType.GMaxRepeatDamage);
   }
}
