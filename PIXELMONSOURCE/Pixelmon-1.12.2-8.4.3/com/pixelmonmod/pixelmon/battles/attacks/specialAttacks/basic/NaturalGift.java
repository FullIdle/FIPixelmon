package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.items.DecreaseEV;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.items.heldItems.ItemBerryRestoreHP;
import com.pixelmonmod.pixelmon.items.heldItems.ItemBerryStatIncrease;
import com.pixelmonmod.pixelmon.items.heldItems.ItemBerryStatus;
import com.pixelmonmod.pixelmon.items.heldItems.ItemBerryTypeReducing;

public class NaturalGift extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      ItemHeld userItem = user.getHeldItem();
      if (user.hasHeldItem() && ItemHeld.canEatBerry(user)) {
         label65:
         switch (userItem.getHeldItemType()) {
            case berryStatIncrease:
               user.attack.getMove().setBasePower(100);
               switch (((ItemBerryStatIncrease)userItem).berryType) {
                  case liechiBerry:
                     user.attack.overrideType(EnumType.Grass);
                     break label65;
                  case ganlonBerry:
                     user.attack.overrideType(EnumType.Ice);
                     break label65;
                  case petayaBerry:
                     user.attack.overrideType(EnumType.Poison);
                     break label65;
                  case apicotBerry:
                     user.attack.overrideType(EnumType.Ground);
                     break label65;
                  case salacBerry:
                     user.attack.overrideType(EnumType.Fighting);
                     break label65;
                  case lansatBerry:
                     user.attack.overrideType(EnumType.Flying);
                     break label65;
                  case starfBerry:
                     user.attack.overrideType(EnumType.Psychic);
                  default:
                     break label65;
               }
            case berryCustap:
               user.attack.overrideType(EnumType.Ghost);
               user.attack.getMove().setBasePower(100);
               break;
            case berryMicle:
               user.attack.overrideType(EnumType.Rock);
               user.attack.getMove().setBasePower(100);
            case berryEnigma:
            case ginemaBerry:
               user.attack.overrideType(EnumType.Bug);
               user.attack.getMove().setBasePower(100);
               break;
            case jabocaBerry:
               user.attack.overrideType(EnumType.Dragon);
               user.attack.getMove().setBasePower(100);
               break;
            case rowapBerry:
               user.attack.overrideType(EnumType.Dark);
               user.attack.getMove().setBasePower(100);
               break;
            case berryEVReducing:
               user.attack.getMove().setBasePower(90);
               switch (((DecreaseEV)userItem).type) {
                  case PomegBerry:
                     user.attack.overrideType(EnumType.Ice);
                     break label65;
                  case KelpsyBerry:
                     user.attack.overrideType(EnumType.Fighting);
                     break label65;
                  case QualotBerry:
                     user.attack.overrideType(EnumType.Poison);
                     break label65;
                  case HondewBerry:
                     user.attack.overrideType(EnumType.Ground);
                     break label65;
                  case GrepaBerry:
                     user.attack.overrideType(EnumType.Flying);
                     break label65;
                  case TamatoBerry:
                     user.attack.overrideType(EnumType.Psychic);
                  default:
                     break label65;
               }
            case berryTypeReducing:
               user.attack.getMove().setBasePower(80);
               user.attack.overrideType(((ItemBerryTypeReducing)userItem).typeReduced);
               break;
            case berryStatus:
               switch (((ItemBerryStatus)userItem).berryType) {
                  case pumkinBerry:
                  case drashBerry:
                  case eggantBerry:
                  case yagoBerry:
                  case tougaBerry:
                     user.attack.overrideType(EnumType.Bug);
                     user.attack.getMove().setBasePower(100);
                     break label65;
                  case cheriBerry:
                     user.attack.overrideType(EnumType.Fire);
                     user.attack.getMove().setBasePower(80);
                     break label65;
                  case chestoBerry:
                     user.attack.overrideType(EnumType.Water);
                     user.attack.getMove().setBasePower(80);
                     break label65;
                  case pechaBerry:
                     user.attack.overrideType(EnumType.Electric);
                     user.attack.getMove().setBasePower(80);
                     break label65;
                  case rawstBerry:
                     user.attack.overrideType(EnumType.Grass);
                     user.attack.getMove().setBasePower(80);
                     break label65;
                  case aspearBerry:
                     user.attack.overrideType(EnumType.Ice);
                     user.attack.getMove().setBasePower(80);
                     break label65;
                  case persimBerry:
                     user.attack.overrideType(EnumType.Ground);
                     user.attack.getMove().setBasePower(80);
                     break label65;
                  case lumBerry:
                     user.attack.overrideType(EnumType.Flying);
                     user.attack.getMove().setBasePower(80);
                  default:
                     break label65;
               }
            case berryRestoreHP:
               user.attack.getMove().setBasePower(80);
               switch (((ItemBerryRestoreHP)userItem).berryType) {
                  case oranBerry:
                     user.attack.overrideType(EnumType.Poison);
                     break label65;
                  case sitrusBerry:
                     user.attack.overrideType(EnumType.Psychic);
                     break label65;
                  case figyBerry:
                     user.attack.overrideType(EnumType.Bug);
                     break label65;
                  case wikiBerry:
                     user.attack.overrideType(EnumType.Rock);
                     break label65;
                  case magoBerry:
                     user.attack.overrideType(EnumType.Ghost);
                     break label65;
                  case aguavBerry:
                     user.attack.overrideType(EnumType.Dragon);
                     break label65;
                  case iapapaBerry:
                     user.attack.overrideType(EnumType.Dark);
                     break label65;
                  default:
                     user.bc.sendToAll("pixelmon.effect.effectfailed");
                     return AttackResult.failed;
               }
            case leppa:
               user.attack.overrideType(EnumType.Fighting);
               user.attack.getMove().setBasePower(80);
               break;
            case keeBerry:
               user.attack.overrideType(EnumType.Fairy);
               user.attack.getMove().setBasePower(100);
               break;
            case marangaBerry:
               user.attack.overrideType(EnumType.Dark);
               user.attack.getMove().setBasePower(100);
               break;
            default:
               user.bc.sendToAll("pixelmon.effect.effectfailed");
               return AttackResult.failed;
         }

         user.consumeItem();
         return AttackResult.proceed;
      } else {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         return AttackResult.failed;
      }
   }
}
