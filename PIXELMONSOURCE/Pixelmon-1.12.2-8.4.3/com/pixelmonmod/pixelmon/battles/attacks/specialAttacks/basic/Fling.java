package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Burn;
import com.pixelmonmod.pixelmon.battles.status.Flinch;
import com.pixelmonmod.pixelmon.battles.status.Paralysis;
import com.pixelmonmod.pixelmon.battles.status.Poison;
import com.pixelmonmod.pixelmon.battles.status.PoisonBadly;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumEvAdjustingItems;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumTypeEnhancingItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.items.heldItems.EVAdjusting;
import com.pixelmonmod.pixelmon.items.heldItems.ItemBerry;
import com.pixelmonmod.pixelmon.items.heldItems.ItemMentalHerb;
import com.pixelmonmod.pixelmon.items.heldItems.ItemWhiteHerb;
import com.pixelmonmod.pixelmon.items.heldItems.NoItem;
import com.pixelmonmod.pixelmon.items.heldItems.TypeEnhancingItems;
import java.util.ArrayList;

public class Fling extends SpecialAttackBase {
   private transient ItemHeld heldItem;

   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      user.attack.getMove().setBasePower(0);
      ItemHeld item = user.getUsableHeldItem();
      if (user.hasHeldItem() && user.isItemRemovable(user)) {
         label35:
         switch (item.getHeldItemType()) {
            case ironBall:
               user.attack.getMove().setBasePower(130);
               break;
            case typeEnhancer:
               switch (((TypeEnhancingItems)item).enhanceType) {
                  case hardStone:
                     user.attack.getMove().setBasePower(100);
                     break label35;
                  case plate:
                     user.attack.getMove().setBasePower(90);
                     break label35;
                  case poisonBarb:
                  case dragonFang:
                     user.attack.getMove().setBasePower(70);
                     break label35;
                  case sharpBeak:
                     user.attack.getMove().setBasePower(50);
                     break label35;
                  case blackBelt:
                  case blackGlasses:
                  case charcoal:
                  case magnet:
                  case miracleSeed:
                  case mysticWater:
                  case neverMeltIce:
                  case spellTag:
                  case twistedSpoon:
                  case fairyWand:
                     user.attack.getMove().setBasePower(30);
                     break label35;
                  default:
                     user.attack.getMove().setBasePower(10);
                     break label35;
               }
            case deepSeaTooth:
            case gripClaw:
            case thickClub:
               user.attack.getMove().setBasePower(90);
               break;
            case assaultVest:
            case electirizer:
            case magmarizer:
            case protector:
            case quickClaw:
            case razorClaw:
            case stickyBarb:
               user.attack.getMove().setBasePower(80);
               break;
            case evAdjusting:
               user.attack.getMove().setBasePower(((EVAdjusting)item).type == EnumEvAdjustingItems.MachoBrace ? 60 : 70);
               break;
            case dampRock:
            case heatRock:
            case rockyHelmet:
            case leek:
               user.attack.getMove().setBasePower(60);
               break;
            case dubiousDisc:
               user.attack.getMove().setBasePower(50);
               break;
            case eviolite:
            case icyRock:
            case luckyPunch:
               user.attack.getMove().setBasePower(40);
               break;
            case absorbbulb:
            case berryJuice:
            case bindingBand:
            case blackSludge:
            case cellbattery:
            case deepSeaScale:
            case dragonScale:
            case ejectButton:
            case throatSpray:
            case everStone:
            case expShare:
            case flameOrb:
            case floatStone:
            case kingsRock:
            case lifeorb:
            case lightBall:
            case lightClay:
            case luckyEgg:
            case metalCoat:
            case metalPowder:
            case metronome:
            case prismScale:
            case quickPowder:
            case razorFang:
            case scopeLens:
            case shellBell:
            case smokeBall:
            case soulDew:
            case toxicOrb:
            case upGrade:
               user.attack.getMove().setBasePower(30);
               break;
            case gems:
            case mail:
            case other:
               user.attack.getMove().setBasePower(0);
               break;
            default:
               user.attack.getMove().setBasePower(10);
         }
      }

      if (user.attack.getOverridePower() == 0) {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         this.heldItem = NoItem.noItem;
         return AttackResult.failed;
      } else {
         user.bc.sendToAll("pixelmon.effect.fling", user.getNickname(), item.getLocalizedName());
         this.heldItem = user.getHeldItem();
         user.consumeItem();
         return AttackResult.proceed;
      }
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (target.isAlive() && this.heldItem != NoItem.noItem) {
         EnumHeldItems type = this.heldItem.getHeldItemType();
         if (type != EnumHeldItems.kingsRock && type != EnumHeldItems.razorFang) {
            if (type == EnumHeldItems.lightBall) {
               Paralysis.paralyze(user, target, user.attack, true);
            } else if (type == EnumHeldItems.flameOrb) {
               Burn.burn(user, target, user.attack, true);
            } else if (type == EnumHeldItems.toxicOrb) {
               PoisonBadly.poisonBadly(user, target, user.attack, true);
            } else if (type == EnumHeldItems.typeEnhancer && ((TypeEnhancingItems)this.heldItem).enhanceType == EnumTypeEnhancingItems.poisonBarb) {
               Poison.poison(user, target, user.attack, true);
            } else if (this.heldItem.isBerry() && ItemBerry.canEatBerry(target)) {
               ItemHeld tempItem = target.getHeldItem();
               ItemHeld tempConsumedItem = target.getConsumedItem();
               target.setHeldItem(this.heldItem);
               target.getHeldItem().eatBerry(target);
               target.setHeldItem(tempItem);
               target.setConsumedItem(tempConsumedItem);
            } else if (type == EnumHeldItems.whiteHerb) {
               ItemWhiteHerb.healStats(target);
            } else if (type == EnumHeldItems.mentalHerb) {
               ((ItemMentalHerb)this.heldItem).healStatus(target);
            }
         } else {
            Flinch.flinch(user, target);
         }
      }

   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (!userChoice.hitsAlly()) {
         for(int i = 0; i < userChoice.targets.size(); ++i) {
            if (userChoice.tier < 3) {
               ItemHeld item = pw.getHeldItem();
               EnumHeldItems type = item.getHeldItemType();
               if (type != EnumHeldItems.kingsRock && type != EnumHeldItems.razorFang) {
                  if (type == EnumHeldItems.lightBall) {
                     Paralysis paralysis = new Paralysis();
                     paralysis.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
                  } else if (type == EnumHeldItems.flameOrb) {
                     Burn burn = new Burn();
                     burn.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
                  } else if (type == EnumHeldItems.toxicOrb) {
                     PoisonBadly poisonBadly = new PoisonBadly();
                     poisonBadly.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
                  } else if (type == EnumHeldItems.typeEnhancer && ((TypeEnhancingItems)item).enhanceType == EnumTypeEnhancingItems.poisonBarb) {
                     Poison poison = new Poison();
                     poison.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
                  }
               } else {
                  Flinch flinch = new Flinch();
                  flinch.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
               }
            }
         }

      }
   }
}
