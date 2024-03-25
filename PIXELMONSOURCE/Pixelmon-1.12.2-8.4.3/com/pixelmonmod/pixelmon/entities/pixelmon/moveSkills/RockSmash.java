package com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills;

import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.blocks.BlockFossil;
import com.pixelmonmod.pixelmon.blocks.MultiBlock;
import com.pixelmonmod.pixelmon.blocks.spawning.BlockSpawningHandler;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleStartTypes;
import com.pixelmonmod.pixelmon.util.PixelBlockSnapshot;
import javax.swing.Timer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.World;

public class RockSmash {
   public static MoveSkill createMoveSkill() {
      MoveSkill moveSkill = (new MoveSkill("rock_smash")).setName("pixelmon.moveskill.rock_smash.name").describe("pixelmon.moveskill.rock_smash.description1", "pixelmon.moveskill.rock_smash.description2").setIcon(new ResourceLocation("pixelmon", "textures/gui/overlay/externalMoves/rocksmash.png")).setDefaultCooldownTicks(800).setUsePP(true).setAnyMoves("Rock Smash");
      moveSkill.setBehaviourBlockTarget((pixelmon, tup) -> {
         if (!PixelmonConfig.allowDestructiveExternalMoves) {
            return -1;
         } else {
            BlockPos pos = (BlockPos)tup.func_76341_a();
            World world = pixelmon.field_70170_p;
            IBlockState state = world.func_180495_p(pos);
            if (state.func_185904_a() == Material.field_151576_e && state.func_177230_c() != Blocks.field_150357_h) {
               if (!(state.func_177230_c() instanceof MultiBlock) && !(state.func_177230_c() instanceof BlockFossil)) {
                  EntityPlayerMP owner = (EntityPlayerMP)pixelmon.func_70902_q();
                  if (owner.field_71134_c.func_73081_b() == GameType.ADVENTURE) {
                     PixelBlockSnapshot snapshot = new PixelBlockSnapshot(world, pos, state);
                     world.func_175698_g(pos);
                     Timer timer = new Timer(7000, (e) -> {
                        owner.func_184102_h().func_152344_a(() -> {
                           snapshot.restoreToLocation(world, pos, true, true);
                        });
                     });
                     timer.setRepeats(false);
                     timer.start();
                  } else {
                     ItemStack hand = owner.func_184582_a(EntityEquipmentSlot.MAINHAND);

                     try {
                        owner.func_184201_a(EntityEquipmentSlot.MAINHAND, ItemStack.field_190927_a);
                        if (owner.field_71134_c.func_180237_b(pos) && !state.func_177230_c().canHarvestBlock(world, pos, owner)) {
                           state.func_177230_c().func_176226_b(world, pos, state, 3);
                        }
                     } finally {
                        owner.func_184201_a(EntityEquipmentSlot.MAINHAND, hand);
                     }
                  }

                  BlockSpawningHandler.getInstance().performBattleStartCheck(world, pos, pixelmon.func_70902_q(), pixelmon, EnumBattleStartTypes.ROCKSMASH, state);
                  return moveSkill.cooldownTicks - pixelmon.getPokemonData().getStat(StatsType.Speed);
               } else {
                  return -1;
               }
            } else {
               return -1;
            }
         }
      });
      return moveSkill;
   }
}
