package com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills;

import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.SpawnLocationType;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.tools.LineCalc;
import java.util.Iterator;
import java.util.List;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

public class GroundBirds {
   public static boolean affectOwnedPokemon = false;

   public static MoveSkill createMoveSkill() {
      MoveSkill moveSkill = (new MoveSkill("ground")).setName("pixelmon.moveskill.ground.name").describe("pixelmon.moveskill.ground.description1", "pixelmon.moveskill.ground.description2").setAnyMoves("Gravity", "Smack Down").setUsePP(true).setIcon(new ResourceLocation("pixelmon", "textures/gui/overlay/externalMoves/groundbirds.png")).setDefaultCooldownTicks(900);
      moveSkill.setBehaviourNoTarget((usingPixelmon) -> {
         List nearby = usingPixelmon.field_70170_p.func_175644_a(EntityPixelmon.class, (pixelmonx) -> {
            if (!affectOwnedPokemon && pixelmonx.hasOwner()) {
               return false;
            } else if (pixelmonx.getSpawnLocation() != SpawnLocationType.Air && pixelmonx.getSpawnLocation() != SpawnLocationType.AirPersistent && pixelmonx.getFlyingParameters() == null) {
               return false;
            } else if (pixelmonx == usingPixelmon) {
               return false;
            } else {
               Pokemon pokemon = pixelmonx.getPokemonData();
               float specialAttack = (float)pokemon.getStat(StatsType.SpecialAttack);
               float attack = (float)pokemon.getStat(StatsType.Attack);
               float stat = 1.0F;
               if (pokemon.getMoveset().hasAttack("Gravity")) {
                  if (pokemon.getMoveset().hasAttack("Smack Down")) {
                     stat = attack > specialAttack ? attack : specialAttack;
                  } else {
                     stat = specialAttack;
                  }
               } else {
                  stat = attack;
               }

               return !(pixelmonx.func_70032_d(usingPixelmon) > LineCalc.lerp(stat, 1.0F, 300.0F, 20.0F, 100.0F));
            }
         });
         usingPixelmon.func_184185_a(SoundEvents.field_187524_aN, 1.0F, 2.0F);

         EntityPixelmon pixelmon;
         for(Iterator var3 = nearby.iterator(); var3.hasNext(); pixelmon.grounded = true) {
            pixelmon = (EntityPixelmon)var3.next();
            pixelmon.setSpawnLocation(SpawnLocationType.Land);
         }

         return moveSkill.cooldownTicks;
      });
      return moveSkill;
   }
}
