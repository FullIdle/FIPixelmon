package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.Transformed;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Illusion;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.network.datasync.EntityDataManager;

public class Transform extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      AbilityBase targetAbility = target.getBattleAbility();
      if ((!(targetAbility instanceof Illusion) || ((Illusion)targetAbility).disguisedPokemon == null) && !target.hasStatus(StatusType.Substitute, StatusType.Transformed)) {
         if (!user.bc.simulateMode) {
            EntityDataManager dataManager = user.entity.func_184212_Q();
            if (user.removeStatus(StatusType.Transformed)) {
               dataManager.func_187227_b(EntityPixelmon.dwTransformation, 0);
            }

            user.bc.sendToAll("pixelmon.status.transform", user.getNickname(), target.getNickname());
            dataManager.func_187227_b(EntityPixelmon.dwTransformation, -1);
            user.addStatus(new Transformed(user, target), target);
            Moveset tempMoveset = (new Moveset()).withPokemon(user.pokemon);
            Iterator var6 = target.getMoveset().iterator();

            while(var6.hasNext()) {
               Attack a = (Attack)var6.next();
               if (a != null) {
                  Attack copy = a.copy();
                  copy.pp = 5;
                  copy.overridePPMax(5);
                  tempMoveset.add(copy);
               }
            }

            user.setTemporaryMoveset(tempMoveset);
         }

         return AttackResult.succeeded;
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      userChoice.raiseWeight(40.0F);
   }
}
