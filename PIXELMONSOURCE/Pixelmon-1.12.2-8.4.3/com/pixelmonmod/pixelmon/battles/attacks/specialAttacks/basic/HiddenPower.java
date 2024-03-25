package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class HiddenPower extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      user.attack.overrideType(getHiddenPowerType(user.getStats().ivs));
      return AttackResult.proceed;
   }

   public static EnumType getHiddenPowerType(IVStore ivs) {
      int a = ivs.hp % 2;
      int b = ivs.attack % 2;
      int c = ivs.defence % 2;
      int d = ivs.speed % 2;
      int e = ivs.specialAttack % 2;
      int f = ivs.specialDefence % 2;
      double fedbca = (double)(32 * f + 16 * e + 8 * d + 4 * c + 2 * b + a);
      int type = (int)Math.floor(fedbca * 15.0 / 63.0);
      if (type == 0) {
         return EnumType.Fighting;
      } else if (type == 1) {
         return EnumType.Flying;
      } else if (type == 2) {
         return EnumType.Poison;
      } else if (type == 3) {
         return EnumType.Ground;
      } else if (type == 4) {
         return EnumType.Rock;
      } else if (type == 5) {
         return EnumType.Bug;
      } else if (type == 6) {
         return EnumType.Ghost;
      } else if (type == 7) {
         return EnumType.Steel;
      } else if (type == 8) {
         return EnumType.Fire;
      } else if (type == 9) {
         return EnumType.Water;
      } else if (type == 10) {
         return EnumType.Grass;
      } else if (type == 11) {
         return EnumType.Electric;
      } else if (type == 12) {
         return EnumType.Psychic;
      } else if (type == 13) {
         return EnumType.Ice;
      } else {
         return type == 14 ? EnumType.Dragon : EnumType.Dark;
      }
   }

   public static IVStore getOptimalIVs(EnumType type) {
      IVStore ivs = new IVStore();
      ivs.hp = 31;
      ivs.attack = 31;
      ivs.defence = 31;
      ivs.specialAttack = 31;
      ivs.specialDefence = 31;
      ivs.speed = 31;
      switch (type) {
         case Bug:
            ivs.attack = 30;
            ivs.defence = 30;
            ivs.specialDefence = 30;
         case Dark:
         default:
            break;
         case Dragon:
            ivs.attack = 30;
            break;
         case Electric:
            ivs.specialAttack = 30;
            break;
         case Fighting:
            ivs.defence = 30;
            ivs.specialAttack = 30;
            ivs.specialDefence = 30;
            ivs.speed = 30;
            break;
         case Fire:
            ivs.attack = 30;
            ivs.specialAttack = 30;
            ivs.speed = 30;
            break;
         case Flying:
            ivs.hp = 30;
            ivs.attack = 30;
            ivs.defence = 30;
            ivs.specialAttack = 30;
            ivs.specialDefence = 30;
            break;
         case Ghost:
            ivs.attack = 30;
            ivs.specialDefence = 30;
            break;
         case Grass:
            ivs.attack = 30;
            ivs.specialAttack = 30;
            break;
         case Ground:
            ivs.specialAttack = 30;
            ivs.specialDefence = 30;
            break;
         case Ice:
            ivs.attack = 30;
            ivs.defence = 30;
            break;
         case Poison:
            ivs.defence = 30;
            ivs.specialAttack = 30;
            ivs.specialDefence = 30;
            break;
         case Psychic:
            ivs.attack = 30;
            ivs.speed = 30;
            break;
         case Rock:
            ivs.defence = 30;
            ivs.specialDefence = 30;
            ivs.speed = 30;
            break;
         case Steel:
            ivs.specialDefence = 30;
            break;
         case Water:
            ivs.attack = 30;
            ivs.defence = 30;
            ivs.specialAttack = 30;
      }

      return ivs;
   }
}
