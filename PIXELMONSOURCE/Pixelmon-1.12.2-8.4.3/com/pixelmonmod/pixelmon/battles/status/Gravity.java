package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.StatsEffect;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.SmackDown;
import com.pixelmonmod.pixelmon.battles.controller.GlobalStatusController;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Levitate;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Gravity extends GlobalStatusBase {
   private transient int duration = 5;

   public Gravity() {
      super(StatusType.Gravity);
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.bc.globalStatusController.hasStatus(StatusType.Gravity)) {
         user.bc.sendToAll("pixelmon.effect.effectfailed");
         user.attack.moveResult.result = AttackResult.failed;
      } else {
         user.bc.sendToAll("pixelmon.status.gravity");
         user.bc.globalStatusController.addGlobalStatus(new Gravity());
         Iterator var3 = user.bc.getActiveUnfaintedPokemon().iterator();

         while(true) {
            PixelmonWrapper pokemon;
            do {
               if (!var3.hasNext()) {
                  return;
               }

               pokemon = (PixelmonWrapper)var3.next();
               if (pokemon.hasStatus(StatusType.Flying)) {
                  pokemon.canAttack = false;
               }
            } while(!pokemon.removeStatuses(StatusType.Flying, StatusType.MagnetRise, StatusType.Telekinesis, StatusType.SkyDropped, StatusType.SkyDropping) && !pokemon.hasType(EnumType.Flying) && !(pokemon.getBattleAbility() instanceof Levitate) && pokemon.getUsableHeldItem().getHeldItemType() != EnumHeldItems.airBalloon);

            if (!pokemon.hasStatus(StatusType.SmackedDown)) {
               user.bc.sendToAll("pixelmon.status.gravityaffected", pokemon.getNickname());
            }
         }
      }
   }

   public void applyEffectOnSwitch(PixelmonWrapper pw) {
      if (pw.hasType(EnumType.Flying) || pw.getBattleAbility() instanceof Levitate || pw.getUsableHeldItem().getHeldItemType() == EnumHeldItems.airBalloon) {
         pw.bc.sendToAll("pixelmon.status.gravityaffected", pw.getNickname());
      }

   }

   public boolean stopsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user) {
      return user.attack.isAttack("Bounce", "Fly", "Flying Press", "High Jump Kick", "Jump Kick", "Magnet Rise", "Sky Drop", "Splash", "Telekinesis");
   }

   public void stopsIncomingAttackMessage(PixelmonWrapper pokemon, PixelmonWrapper user) {
      user.bc.sendToAll("pixelmon.effect.effectfailed");
   }

   public List getEffectiveTypes(PixelmonWrapper user, PixelmonWrapper target) {
      if (user.attack != null && user.attack.getType() == EnumType.Ground) {
         ArrayList types = (ArrayList)target.type.stream().filter((type) -> {
            return type != EnumType.Flying;
         }).collect(Collectors.toList());
         if (types.size() == 0) {
            types.add(EnumType.Normal);
         }

         return types;
      } else {
         return target.type;
      }
   }

   public void applyRepeatedEffect(GlobalStatusController globalStatusController) {
      --this.duration;
      if (this.duration <= 0) {
         globalStatusController.bc.sendToAll("pixelmon.status.gravityend");
         globalStatusController.removeGlobalStatus((GlobalStatusBase)this);
      }

   }

   public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, ArrayList userChoices, ArrayList bestUserChoices, ArrayList opponentChoices, ArrayList bestOpponentChoices) {
      SmackDown smackDown = new SmackDown();
      smackDown.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
      StatsEffect statsEffect = new StatsEffect(StatsType.Evasion, -2, false);
      statsEffect.weightEffect(pw, userChoice, userChoices, bestUserChoices, opponentChoices, bestOpponentChoices);
   }
}
