package com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills;

import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.blocks.spawning.BlockSpawningHandler;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleStartTypes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SweetScent {
   public static MoveSkill createMoveSkill() {
      MoveSkill moveSkill = (new MoveSkill("sweet_scent")).setName("pixelmon.moveskill.sweet_scent.name").describe("pixelmon.moveskill.sweet_scent.description").setAnyMoves("Sweet Scent").setDefaultCooldownTicks(500).setUsePP(true).setIcon(new ResourceLocation("pixelmon", "textures/gui/overlay/externalMoves/sweetscent.png"));
      moveSkill.setBehaviourNoTarget((pixelmon) -> {
         BlockPos pos = pixelmon.func_180425_c();
         World world = pixelmon.field_70170_p;
         BlockSpawningHandler.getInstance().performBattleStartCheck(world, pos, pixelmon.func_70902_q(), pixelmon, EnumBattleStartTypes.SWEETSCENT, (IBlockState)null);
         return moveSkill.cooldownTicks - pixelmon.getPokemonData().getStat(StatsType.Speed);
      });
      return moveSkill;
   }
}
