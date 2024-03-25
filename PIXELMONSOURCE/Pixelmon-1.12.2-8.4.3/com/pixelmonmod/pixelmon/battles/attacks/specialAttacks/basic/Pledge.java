package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.FirePledge;
import com.pixelmonmod.pixelmon.battles.status.GrassPledge;
import com.pixelmonmod.pixelmon.battles.status.WaterPledge;
import java.util.ArrayList;

public class Pledge extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      if (this.getPreviousPledge(user) != null) {
         user.attack.getMove().setBasePower(user.attack.getMove().getBasePower() * 2);
      }

      return AttackResult.proceed;
   }

   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      Attack beforePledge = this.getPreviousPledge(user);
      if (beforePledge != null) {
         if (!beforePledge.isAttack("Fire Pledge") && !user.attack.isAttack("Fire Pledge")) {
            target.bc.sendToAll("pixelmon.status.grasspledge", target.getNickname());
            target.addTeamStatus(new GrassPledge(), user);
         } else if (!beforePledge.isAttack("Grass Pledge") && !user.attack.isAttack("Grass Pledge")) {
            user.bc.sendToAll("pixelmon.status.waterpledge", user.getNickname());
            user.addTeamStatus(new WaterPledge(), user);
         } else if (!beforePledge.isAttack("Water Pledge") && !user.attack.isAttack("Water Pledge")) {
            target.bc.sendToAll("pixelmon.status.firepledge", target.getNickname());
            target.addTeamStatus(new FirePledge(), user);
         }
      }

   }

   private Attack getPreviousPledge(PixelmonWrapper user) {
      Attack beforePledge = null;
      ArrayList team = user.bc.getTeamPokemon(user.getParticipant());

      for(int i = 0; i < user.bc.turn; ++i) {
         PixelmonWrapper currentPokemon = (PixelmonWrapper)user.bc.turnList.get(i);
         if (currentPokemon.canAttack && team.contains(currentPokemon)) {
            Attack currentAttack = ((PixelmonWrapper)user.bc.turnList.get(i)).attack;
            if (currentAttack != null && currentAttack.getMove().getAttackName().contains("pledge") && !user.attack.equals(currentAttack)) {
               beforePledge = currentAttack;
            }
         }
      }

      return beforePledge;
   }
}
