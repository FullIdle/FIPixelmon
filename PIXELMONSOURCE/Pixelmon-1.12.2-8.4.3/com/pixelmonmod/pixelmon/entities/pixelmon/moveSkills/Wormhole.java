package com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.entities.EntityWormhole;
import net.minecraft.util.ResourceLocation;

public class Wormhole {
   public static MoveSkill createMoveSkill() {
      MoveSkill moveSkill = (new MoveSkill("open_wormhole")).setName("pixelmon.moveskill.open_wormhole.name").describe("pixelmon.moveskill.open_wormhole.description").setIcon(new ResourceLocation("pixelmon", "textures/gui/overlay/externalMoves/wormhole.png")).setAbleSpecs(Lists.newArrayList(new PokemonSpec[]{new PokemonSpec("Palkia"), new PokemonSpec("Lunala"), new PokemonSpec("Solgaleo"), new PokemonSpec("Arceus"), new PokemonSpec("Necrozma")})).setDefaultCooldownTicks(18000);
      return moveSkill.setBehaviourNoTarget((pixelmon) -> {
         if (pixelmon.func_70902_q() != null) {
            int dx = pixelmon.field_70170_p.field_73012_v.nextInt(20) - 10;
            int dy = pixelmon.field_70170_p.field_73012_v.nextInt(10) + 7;
            int dz = pixelmon.field_70170_p.field_73012_v.nextInt(20) - 10;
            int da = pixelmon.field_70170_p.field_73012_v.nextInt(1800) + 1800;
            EntityWormhole wormhole = new EntityWormhole(pixelmon.field_70170_p, pixelmon.field_70165_t + (double)dx, pixelmon.field_70163_u + (double)dy, pixelmon.field_70161_v + (double)dz, da);
            pixelmon.field_70170_p.func_72838_d(wormhole);
         }

         return moveSkill.cooldownTicks;
      });
   }
}
