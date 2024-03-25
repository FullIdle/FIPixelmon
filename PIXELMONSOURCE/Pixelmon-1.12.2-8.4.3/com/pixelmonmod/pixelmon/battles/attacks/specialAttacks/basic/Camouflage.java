package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.ArrayList;

public class Camouflage extends SpecialAttackBase {
   public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
      EnumType newType = this.getTypeChange(user);
      user.setTempType(newType);
      user.bc.sendToAll("pixelmon.effect.changetype", user.getNickname(), newType.getLocalizedName());
      return AttackResult.succeeded;
   }

   private EnumType getTypeChange(PixelmonWrapper user) {
      TerrainExamine.TerrainType terrainType = TerrainExamine.getTerrain(user);
      switch (terrainType) {
         case Cave:
            return EnumType.Rock;
         case Sand:
            return EnumType.Ground;
         case Water:
            return EnumType.Water;
         case SnowIce:
            return EnumType.Ice;
         case Volcano:
            return EnumType.Fire;
         case Grass:
            return EnumType.Grass;
         case Misty:
            return EnumType.Fairy;
         case Electric:
            return EnumType.Electric;
         case Psychic:
         case UltraSpace:
            return EnumType.Psychic;
         default:
            return EnumType.Normal;
      }
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      pw.getBattleAI().weightTypeChange(pw, userChoice, this.getTypeChange(pw).makeTypeList(), bestUserChoices, bestOpponentChoices);
   }
}
