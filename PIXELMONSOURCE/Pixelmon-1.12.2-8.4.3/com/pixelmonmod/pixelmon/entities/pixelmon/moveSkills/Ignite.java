package com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills;

import com.pixelmonmod.pixelmon.api.attackAnimations.VariableParticleEffect;
import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.attacks.animations.AttackAnimationData;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.util.Scheduling;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;

public class Ignite {
   public static MoveSkill createMoveSkill() {
      MoveSkill moveSkill = (new MoveSkill("ignite")).setName("pixelmon.moveskill.ignite.name").describe("pixelmon.moveskill.ignite.description1", "pixelmon.moveskill.ignite.description2").setIcon(new ResourceLocation("pixelmon", "textures/gui/overlay/externalMoves/fire.png")).setUsePP(true).setAnyMoves("Ember", "Fire Blast", "Flamethrower", "Incinerate", "Blast Burn", "Fire Spin", "Flame Burst", "Flame Wheel", "Heat Wave", "Inferno", "Sacred Fire", "Will-O-Wisp").setDefaultCooldownTicks(800).setRange(10);
      return moveSkill.setBehaviourBlockTarget((pixelmon, tup) -> {
         if (!PixelmonConfig.allowDestructiveExternalMoves) {
            return -1;
         } else if (((EntityPlayerMP)pixelmon.func_70902_q()).field_71134_c.func_73081_b() == GameType.ADVENTURE) {
            return moveSkill.cooldownTicks;
         } else {
            EntityPlayerMP player = (EntityPlayerMP)pixelmon.func_70902_q();
            Pokemon pokemon = pixelmon.getPokemonData();
            int width = 1;
            int length = true;
            boolean cross = false;
            byte lengthx;
            if (pokemon.getMoveset().hasAttack("Fire Blast")) {
               cross = true;
               width = 3;
               lengthx = 3;
            } else if (pokemon.getMoveset().hasAttack("Inferno", "Sacred Fire", "Incinerate")) {
               width = 3;
               lengthx = 3;
            } else {
               lengthx = 3;
            }

            BlockPos pos = (BlockPos)tup.func_76341_a();
            int initPosX = pos.func_177958_n();
            int initPosY = pos.func_177956_o();
            int initPosZ = pos.func_177952_p();
            ArrayList targets = new ArrayList();
            int x;
            int posX;
            int z;
            int posZ;
            BlockPos lit;
            if (!cross) {
               for(x = 0; x < width; ++x) {
                  posX = x + initPosX;

                  for(z = 0; z < lengthx; ++z) {
                     posZ = z + initPosZ;
                     lit = placeFire(pixelmon.field_70170_p, posX, initPosY, posZ, player);
                     if (lit != null) {
                        targets.add(new Vec3d((double)((float)lit.func_177958_n() + 0.5F), (double)lit.func_177956_o(), (double)((float)lit.func_177952_p() + 0.5F)));
                     }
                  }
               }
            } else {
               for(x = -1; x < 2; ++x) {
                  posX = x + initPosX;

                  for(z = -1; z < 2; ++z) {
                     posZ = z + initPosZ;
                     if (x == z || x == -z) {
                        lit = placeFire(pixelmon.field_70170_p, posX, initPosY, posZ, player);
                        if (lit != null) {
                           targets.add(new Vec3d((double)((float)lit.func_177958_n() + 0.5F), (double)lit.func_177956_o(), (double)((float)lit.func_177952_p() + 0.5F)));
                        }
                     }
                  }
               }
            }

            pixelmon.func_70671_ap().func_75650_a((double)pos.func_177958_n() + 0.5, (double)(pos.func_177956_o() + 1), (double)pos.func_177952_p() + 0.5, 360.0F, 360.0F);
            VariableParticleEffect effect = (new VariableParticleEffect(pixelmon.field_71093_bK, AttackAnimationData.beam().setScale(0.25F).setPower(30))).setAttackBase((AttackBase)AttackBase.getAttackBaseFromEnglishName("Flamethrower").get()).setStartPosition((EntityLivingBase)pixelmon);
            Iterator var20 = targets.iterator();

            while(var20.hasNext()) {
               Vec3d target = (Vec3d)var20.next();
               effect.setEndPosition(target).showAllWithin(20);
            }

            return moveSkill.cooldownTicks;
         }
      });
   }

   private static BlockPos placeFire(World world, int x, int y, int z, EntityPlayerMP player) {
      for(int origy = y; world.func_180495_p(new BlockPos(x, y, z)).func_177230_c() != Blocks.field_150350_a && y < origy + 3; ++y) {
      }

      BlockPos pos = new BlockPos(x, y, z);
      Block block = world.func_180495_p(pos).func_177230_c();
      if (block == Blocks.field_150350_a && Blocks.field_150480_ab.func_176196_c(world, pos) && block.canHarvestBlock(world, pos, player)) {
         Scheduling.schedule(10, (Runnable)(() -> {
            if (!world.func_73046_m().func_175579_a(world, pos, player)) {
               world.func_175656_a(pos, Blocks.field_150480_ab.func_176223_P());
            }

         }), false);
         return pos;
      } else {
         return null;
      }
   }
}
