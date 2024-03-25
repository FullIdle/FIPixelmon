package com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills;

import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.blocks.spawning.BlockSpawningHandler;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleStartTypes;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Headbutt {
   public static MoveSkill createMoveSkill() {
      MoveSkill moveSkill = (new MoveSkill("headbutt")).setName("pixelmon.moveskill.headbutt.name").describe("pixelmon.moveskill.headbutt.description").setUsePP(true).setIcon(new ResourceLocation("pixelmon", "textures/gui/overlay/externalMoves/headbutt.png")).setAnyMoves("Headbutt");
      moveSkill.setBehaviourBlockTarget((pixelmon, tup) -> {
         BlockPos pos = (BlockPos)tup.func_76341_a();
         World world = pixelmon.field_70170_p;
         IBlockState state = world.func_180495_p(pos);
         if (state.func_185904_a() == Material.field_151575_d) {
            BlockSpawningHandler.getInstance().performBattleStartCheck(world, pos, pixelmon.func_70902_q(), pixelmon, EnumBattleStartTypes.HEADBUTT, state);
            return 500 - pixelmon.getPokemonData().getStat(StatsType.Speed);
         } else {
            return -1;
         }
      });
      return moveSkill;
   }
}
