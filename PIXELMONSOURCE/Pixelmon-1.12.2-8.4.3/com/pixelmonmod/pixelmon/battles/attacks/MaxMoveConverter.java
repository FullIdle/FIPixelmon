package com.pixelmonmod.pixelmon.battles.attacks;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumGigantamaxPokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import java.util.Iterator;

public class MaxMoveConverter {
   public static Attack getMaxMoveFromAttack(Attack a, PixelmonWrapper pw) {
      AttackBase base = a.getActualMove();
      Attack newAttack;
      if (base.getAttackCategory() == AttackCategory.STATUS) {
         newAttack = new Attack("Max Guard");
         newAttack.overrideType(EnumType.Normal);
      } else {
         if (a.isAttack("Weather Ball", "Judgment", "Hidden Power", "Multi-Attack") && pw != null) {
            Iterator var4 = base.effects.iterator();

            while(var4.hasNext()) {
               EffectBase effect = (EffectBase)var4.next();
               effect.applyEffectStart(pw, (PixelmonWrapper)null);
            }
         }

         newAttack = new Attack(getMaxMoveForType(a.getType()));
         newAttack.overrideAttackCategory(a.getAttackCategory());
         newAttack.overridePower = getMaxMovePowerForMove(a);
         newAttack.overridePPMax(10);
         newAttack.setDisabled(a.getDisabled(), pw);
      }

      newAttack.isMax = true;
      newAttack.originalMove = a;
      return newAttack;
   }

   public static Attack getGMaxMoveFromAttack(Attack a, PixelmonWrapper pw, EnumSpecies species, IEnumForm form) {
      AttackBase base = a.getActualMove();
      if (base.getAttackCategory() != AttackCategory.STATUS) {
         String gmaxMove = getGMaxMoveForType(a.getType(), species, form);
         if (gmaxMove != null) {
            Attack newAttack = new Attack(gmaxMove);
            newAttack.overrideAttackCategory(a.getAttackCategory());
            if (species != EnumSpecies.Rillaboom && species != EnumSpecies.Cinderace && species != EnumSpecies.Inteleon) {
               newAttack.overridePower = getMaxMovePowerForMove(a);
            } else {
               newAttack.overridePower = 160;
            }

            newAttack.overridePPMax(10);
            newAttack.setDisabled(a.getDisabled(), pw);
            newAttack.isMax = true;
            newAttack.originalMove = a;
            return newAttack;
         }
      }

      return getMaxMoveFromAttack(a, pw);
   }

   private static String getMaxMoveForType(EnumType type) {
      switch (type) {
         case Ground:
            return "Max Quake";
         case Water:
            return "Max Geyser";
         case Ice:
            return "Max Hailstorm";
         case Bug:
            return "Max Flutterby";
         case Dark:
            return "Max Darkness";
         case Fire:
            return "Max Flare";
         case Rock:
            return "Max Rockfall";
         case Fairy:
            return "Max Starfall";
         case Ghost:
            return "Max Phantasm";
         case Grass:
            return "Max Overgrowth";
         case Steel:
            return "Max Steelspike";
         case Dragon:
            return "Max Wyrmwind";
         case Flying:
            return "Max Airstream";
         case Normal:
            return "Max Strike";
         case Poison:
            return "Max Ooze";
         case Electric:
            return "Max Lightning";
         case Psychic:
            return "Max Mindstorm";
         case Fighting:
            return "Max Knuckle";
         default:
            return "Max Strike";
      }
   }

   private static String getGMaxMoveForType(EnumType type, EnumSpecies species, IEnumForm form) {
      EnumGigantamaxPokemon egp = EnumGigantamaxPokemon.getGigantamax(species);
      if (egp != null) {
         String gmaxMove;
         if (species == EnumSpecies.Urshifu) {
            gmaxMove = egp.getGmaxMoveIfPossible(type, form.getForm() > 1 ? form.getForm() - 2 : form.getForm());
         } else {
            gmaxMove = egp.getGmaxMoveIfPossible(type, 0);
         }

         return gmaxMove;
      } else {
         return null;
      }
   }

   private static int getMaxMovePowerForMove(Attack a) {
      if (a.getType() != EnumType.Fighting && a.getType() != EnumType.Poison) {
         if (a.isAttack("Dual Wingbeat")) {
            return 130;
         } else if (a.movePower >= 150) {
            return 150;
         } else if (a.movePower >= 110) {
            return 140;
         } else if (a.movePower >= 75) {
            return 130;
         } else if (a.movePower >= 65) {
            return 120;
         } else if (a.movePower >= 55) {
            return 110;
         } else {
            return a.movePower >= 45 ? 100 : 90;
         }
      } else if (a.movePower >= 150) {
         return 100;
      } else if (a.movePower >= 110) {
         return 95;
      } else if (a.movePower >= 75) {
         return 90;
      } else if (a.movePower >= 65) {
         return 85;
      } else if (a.movePower >= 55) {
         return 80;
      } else {
         return a.movePower >= 45 ? 75 : 70;
      }
   }
}
