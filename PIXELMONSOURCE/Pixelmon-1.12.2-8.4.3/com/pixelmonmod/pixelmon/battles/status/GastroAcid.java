package com.pixelmonmod.pixelmon.battles.status;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.ChangeAbility;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import java.util.ArrayList;
import java.util.Set;

public class GastroAcid extends StatusBase {
   public static Set noAbilityChange = Sets.newHashSet(new String[]{"BattleBond", "Comatose", "Disguise", "GulpMissile", "IceFace", "MagicBounce", "Multitype", "PowerConstruct", "RKSSystem", "Schooling", "StanceChange", "ShieldsDown", "ZenMode", "AsOne"});

   public GastroAcid() {
      super(StatusType.GastroAcid);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      AbilityBase targetAbility = target.getBattleAbility(false);
      if (!target.hasStatus(StatusType.GastroAcid) && !noAbilityChange.contains(targetAbility.getName())) {
         if (target.addStatus(new GastroAcid(), target)) {
            target.bc.sendToAll("pixelmon.status.gastroacid", target.getNickname());
            targetAbility.onAbilityLost(target);
         }
      } else {
         target.bc.sendToAll("pixelmon.effect.effectfailed");
         user.attack.moveResult.result = AttackResult.failed;
      }

   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      ChangeAbility changeAbility = new ChangeAbility((String)null);
      changeAbility.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
   }
}
