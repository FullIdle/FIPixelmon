package com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills;

import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameType;

public class Explode {
   public static MoveSkill createMoveSkill() {
      MoveSkill moveSkill = (new MoveSkill("explode")).setName("pixelmon.moveskill.explode.name").describe("pixelmon.moveskill.explode.description").setIcon(new ResourceLocation("pixelmon", "textures/gui/overlay/externalMoves/explosion.png")).setDefaultCooldownTicks(1600).setUsePP(true).setAnyMoves("Self-Destruct", "Explosion");
      return moveSkill.setBehaviourNoTarget((pixelmon) -> {
         if (!PixelmonConfig.allowDestructiveExternalMoves) {
            return -1;
         } else {
            float explosionPower = 2.0F + pixelmon.func_110143_aJ() / 100.0F;
            if (((EntityPlayerMP)pixelmon.func_70902_q()).field_71134_c.func_73081_b() != GameType.ADVENTURE) {
               pixelmon.field_70170_p.func_72876_a(pixelmon.func_70902_q(), pixelmon.field_70165_t, pixelmon.field_70163_u, pixelmon.field_70161_v, explosionPower, true);
            }

            pixelmon.func_70606_j(0.0F);
            pixelmon.func_70106_y();
            return moveSkill.cooldownTicks;
         }
      });
   }
}
