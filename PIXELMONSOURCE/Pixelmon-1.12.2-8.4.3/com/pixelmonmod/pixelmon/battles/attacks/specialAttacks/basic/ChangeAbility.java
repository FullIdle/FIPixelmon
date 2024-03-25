package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Multitype;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.StanceChange;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Truant;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.util.text.TextComponentTranslation;

public class ChangeAbility extends SpecialAttackBase {
   String ability = null;

   public ChangeAbility(String ability) {
      this.ability = ability;
   }

   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      AbilityBase targetAbility = target.getBattleAbility(false);
      if (!targetAbility.getClass().equals(this.ability) && !(targetAbility instanceof Multitype) && !(targetAbility instanceof StanceChange) && !(targetAbility instanceof Truant)) {
         user.bc.sendToAll("pixelmon.effect.entrainment", target.getNickname(), targetAbility.getTranslatedName(), new TextComponentTranslation("ability." + this.ability + ".name", new Object[0]));
         target.setTempAbility((AbilityBase)AbilityBase.getAbility(this.ability).get());
         return AttackResult.succeeded;
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      ArrayList allies = pw.getTeamPokemon();
      Iterator var8 = userChoice.targets.iterator();

      while(true) {
         while(var8.hasNext()) {
            PixelmonWrapper target = (PixelmonWrapper)var8.next();
            AbilityBase targetAbility = target.getBattleAbility(false);
            boolean negativeAbility = targetAbility.isNegativeAbility();
            boolean allied = allies.contains(target);
            if (allied && negativeAbility) {
               userChoice.raiseWeight(40.0F);
            } else if (!allied && !negativeAbility) {
               userChoice.raiseWeight(25.0F);
            }
         }

         return;
      }
   }
}
