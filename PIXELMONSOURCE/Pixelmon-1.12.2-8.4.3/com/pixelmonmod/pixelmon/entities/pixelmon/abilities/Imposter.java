package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.Transformed;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import java.util.Iterator;
import net.minecraft.network.datasync.EntityDataManager;

public class Imposter extends AbilityBase {
   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      if (!newPokemon.bc.simulateMode) {
         PixelmonWrapper target = newPokemon.bc.getOppositePokemon(newPokemon);
         if ((!(target.getBattleAbility() instanceof Illusion) || ((Illusion)target.getBattleAbility()).disguisedPokemon == null) && !target.hasStatus(StatusType.Substitute, StatusType.Transformed)) {
            if (!target.isFainted()) {
               newPokemon.bc.sendToAll("pixelmon.abilities.imposter", newPokemon.getNickname(), target.getNickname());
               EntityDataManager dataManager = newPokemon.entity.func_184212_Q();
               dataManager.func_187227_b(EntityPixelmon.dwTransformation, -1);
               newPokemon.addStatus(new Transformed(newPokemon, target), target);
               Moveset tempMoveset = (new Moveset()).withPokemon(newPokemon.pokemon);
               Iterator var5 = target.getMoveset().iterator();

               while(var5.hasNext()) {
                  Attack a = (Attack)var5.next();
                  if (a != null) {
                     Attack copy = a.copy();
                     copy.pp = 5;
                     copy.overridePPMax(5);
                     tempMoveset.add(copy);
                  }
               }

               newPokemon.setTemporaryMoveset(tempMoveset);
            }
         }
      }
   }

   public void applyFoeSwitchInEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (!user.hasStatus(StatusType.Transformed)) {
         this.applySwitchInEffect(user);
      }

   }
}
