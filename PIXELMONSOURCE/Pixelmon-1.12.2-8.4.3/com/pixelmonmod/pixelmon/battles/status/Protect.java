package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.DrySkin;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.IceBody;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.MagicGuard;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Moody;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.PoisonHeal;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.RainDish;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.SlowStart;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.SolarPower;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.SpeedBoost;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Truant;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import java.util.ArrayList;
import java.util.Iterator;

public class Protect extends ProtectVariation {
   public Protect() {
      this(StatusType.Protect);
   }

   public Protect(StatusType type) {
      super(type);
   }

   protected boolean addStatus(PixelmonWrapper user) {
      return user.addStatus(new Protect(), user);
   }

   protected void displayMessage(PixelmonWrapper user) {
      user.bc.sendToAll("pixelmon.effect.redaying", user.getNickname());
   }

   public boolean stopsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user) {
      return super.stopsIncomingAttack(pokemon, user) && !user.attack.isAttack("Acupressure", "Aromatic Mist", "Bestow", "Block", "Confide", "Conversion 2", "Curse", "Decorate", "Doom Desire", "Feint", "Flower Shield", "Future Sight", "Hold Hands", "Hyperspace Fury", "Hyperspace Hole", "Mean Look", "Nightmare", "Perish Song", "Phantom Force", "Play Nice", "Psych Up", "Roar", "Role Play", "Rototiller", "Shadow Force", "Sketch", "Spider Web", "Tearful Look", "Transform", "Whirlwind", "Spikes", "Stealth Rock", "Sticky Web", "Toxic Spikes");
   }

   public void stopsIncomingAttackMessage(PixelmonWrapper pokemon, PixelmonWrapper user) {
      user.bc.sendToAll("pixelmon.effect.redaying", pokemon.getNickname());
   }

   public void applyRepeatedEffect(PixelmonWrapper pw) {
      pw.removeStatus((StatusBase)this);
   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      if (MoveChoice.canBreakProtect(pw.getOpponentPokemon(), bestOpponentChoices)) {
         userChoice.raiseWeight(-1.0F);
      } else {
         int numResidualUser = this.countStallBenefit(pw);
         int numResidualOpponent = 0;

         PixelmonWrapper opponent;
         for(Iterator var9 = pw.getOpponentPokemon().iterator(); var9.hasNext(); numResidualOpponent += this.countStallBenefit(opponent)) {
            opponent = (PixelmonWrapper)var9.next();
            if (opponent.hasStatus(StatusType.Vanish)) {
               userChoice.raiseWeight(-1.0F);
               return;
            }
         }

         if (numResidualUser < numResidualOpponent) {
            userChoice.raiseWeight(50.0F);
         }

         ArrayList targetedChoices = MoveChoice.getTargetedChoices(pw, bestOpponentChoices);
         if (MoveChoice.hasSuccessfulAttackChoice(targetedChoices, "Explosion", "Fake Out", "Jump Kick", "Hi Jump Kick", "Selfdestruct")) {
            userChoice.raiseWeight(50.0F);
         }

         if (pw.bc.rules.battleType.numPokemon > 1 && targetedChoices.size() > 0) {
            userChoice.raiseWeight(50.0F);
         }

         userChoice.weight /= (float)(1 << pw.protectsInARow);
      }
   }

   private int countStallBenefit(PixelmonWrapper pw) {
      AbilityBase ability = pw.getBattleAbility();
      boolean hasMagicGuard = ability instanceof MagicGuard;
      EnumHeldItems heldItem = pw.getUsableHeldItem().getHeldItemType();
      GlobalStatusBase weather = pw.bc.globalStatusController.getWeather();
      boolean fullHealth = pw.hasFullHealth();
      int numResidual = 0;
      numResidual += pw.countStatuses(StatusType.FutureSight, StatusType.LightScreen, StatusType.LuckyChant, StatusType.MagnetRise, StatusType.MultiTurn, StatusType.Mist, StatusType.Nightmare, StatusType.Perish, StatusType.Reflect, StatusType.SafeGuard, StatusType.Tailwind, StatusType.UnderGround, StatusType.Yawn);
      numResidual -= pw.countStatuses(StatusType.AquaRing, StatusType.Disable, StatusType.Embargo, StatusType.Encore, StatusType.Freeze, StatusType.GrassPledge, StatusType.HealBlock, StatusType.Ingrain, StatusType.Sleep, StatusType.Taunt, StatusType.Telekinesis, StatusType.WaterPledge, StatusType.Wish);
      if (!hasMagicGuard) {
         numResidual += pw.countStatuses(StatusType.Burn, StatusType.Cursed, StatusType.FirePledge, StatusType.Leech, StatusType.PartialTrap);
         int numPoison = pw.countStatuses(StatusType.Poison, StatusType.PoisonBadly);
         if (ability instanceof PoisonHeal) {
            if (!fullHealth) {
               numResidual -= numPoison;
            }
         } else {
            numResidual += numPoison;
         }
      }

      if (!fullHealth) {
         numResidual -= pw.countStatuses(StatusType.AquaRing, StatusType.Ingrain, StatusType.Wish);
      }

      if (!fullHealth && heldItem == EnumHeldItems.leftovers || heldItem == EnumHeldItems.blackSludge && pw.hasType(EnumType.Poison)) {
         --numResidual;
      } else if (!hasMagicGuard && (heldItem == EnumHeldItems.stickyBarb || heldItem == EnumHeldItems.blackSludge)) {
         ++numResidual;
      }

      if (weather instanceof Rainy) {
         if (!fullHealth && (ability instanceof DrySkin || ability instanceof RainDish)) {
            --numResidual;
         }
      } else if (weather instanceof Sunny) {
         if (ability instanceof DrySkin || ability instanceof SolarPower) {
            ++numResidual;
         }
      } else if (weather instanceof Sandstorm) {
         if (!weather.isImmune(pw)) {
            ++numResidual;
         }
      } else if (weather instanceof Hail) {
         if (!fullHealth && ability instanceof IceBody) {
            --numResidual;
         } else if (!weather.isImmune(pw)) {
            ++numResidual;
         }
      }

      if (!(ability instanceof Moody) && !(ability instanceof SpeedBoost) && !(ability instanceof SlowStart)) {
         if (ability instanceof Truant && ((Truant)ability).canMove) {
            ++numResidual;
         }
      } else {
         --numResidual;
      }

      return numResidual;
   }
}
