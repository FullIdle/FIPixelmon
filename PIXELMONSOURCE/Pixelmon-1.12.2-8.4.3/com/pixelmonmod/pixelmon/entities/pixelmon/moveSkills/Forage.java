package com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills;

import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.blocks.spawning.BlockSpawningHandler;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleStartTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class Forage {
   public static MoveSkill createMoveSkill() {
      MoveSkill moveSkill = (new MoveSkill("forage")).setName("pixelmon.moveskill.forage.name").describe("pixelmon.moveskill.forage.description1", "pixelmon.moveskill.forage.description2").setIcon(new ResourceLocation("pixelmon", "textures/gui/overlay/externalMoves/forage.png")).setUsePP(false).setDefaultCooldownTicks(500);
      return moveSkill.setBehaviourBlockTarget((pixelmon, tup) -> {
         BlockSpawningHandler.getInstance().performBattleStartCheck(pixelmon.field_70170_p, (BlockPos)tup.func_76341_a(), pixelmon.func_70902_q(), pixelmon, EnumBattleStartTypes.FORAGE, pixelmon.field_70170_p.func_180495_p((BlockPos)tup.func_76341_a()));
         return moveSkill.cooldownTicks;
      });
   }
}
