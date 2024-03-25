package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Terrain;
import net.minecraft.block.Block;
import net.minecraft.block.BlockMagma;
import net.minecraft.block.BlockNetherrack;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TerrainExamine {
   public static TerrainType getTerrain(PixelmonWrapper user) {
      if (user != null && user.bc != null) {
         Terrain terrain = user.bc.globalStatusController.getTerrain();
         switch (terrain.type) {
            case ElectricTerrain:
               return TerrainExamine.TerrainType.Electric;
            case GrassyTerrain:
               return TerrainExamine.TerrainType.Grass;
            case MistyTerrain:
               return TerrainExamine.TerrainType.Misty;
            case PsychicTerrain:
               return TerrainExamine.TerrainType.Psychic;
            default:
               BlockPos userPosition = user.getWorldPosition();
               World world = user.getWorld();
               world.func_180494_b(userPosition);
               int numBelow = 0;

               Material blockMaterial;
               Block block;
               do {
                  IBlockState state = world.func_180495_p(userPosition.func_177979_c(numBelow));
                  block = state.func_177230_c();
                  blockMaterial = state.func_185904_a();
                  ++numBelow;
               } while(blockMaterial == Material.field_151579_a && numBelow < 5);

               String blockName = block.func_149739_a();
               if (blockMaterial != Material.field_151577_b && blockMaterial != Material.field_151575_d && blockMaterial != Material.field_151584_j && blockMaterial != Material.field_151585_k && blockMaterial != Material.field_151582_l && blockMaterial != Material.field_151570_A && blockMaterial != Material.field_151572_C) {
                  if (blockMaterial != Material.field_151586_h && blockMaterial != Material.field_151583_m && blockMaterial != Material.field_151589_v) {
                     if (blockMaterial != Material.field_151588_w && blockMaterial != Material.field_151598_x && blockMaterial != Material.field_151597_y && blockMaterial != Material.field_151596_z) {
                        if (blockMaterial != Material.field_151595_p && blockMaterial != Material.field_151571_B && blockMaterial != Material.field_151578_c) {
                           if (blockMaterial != Material.field_151567_E && blockMaterial != Material.field_151566_D && blockMaterial != Material.field_189963_J && !blockName.equals("whiteStone") && !blockName.contains("end") && !blockName.contains("purpur") && !blockName.contains("deep_sea")) {
                              if (blockMaterial != Material.field_151587_i && blockMaterial != Material.field_151581_o && !blockName.contains("nether") && !(block instanceof BlockNetherrack) && !(block instanceof BlockMagma)) {
                                 return blockMaterial == Material.field_151576_e ? TerrainExamine.TerrainType.Cave : TerrainExamine.TerrainType.Default;
                              } else {
                                 return TerrainExamine.TerrainType.Volcano;
                              }
                           } else {
                              return TerrainExamine.TerrainType.UltraSpace;
                           }
                        } else {
                           return TerrainExamine.TerrainType.Sand;
                        }
                     } else {
                        return TerrainExamine.TerrainType.SnowIce;
                     }
                  } else {
                     return TerrainExamine.TerrainType.Water;
                  }
               } else {
                  return TerrainExamine.TerrainType.Grass;
               }
         }
      } else {
         return TerrainExamine.TerrainType.Default;
      }
   }

   public static enum TerrainType {
      Default,
      Cave,
      Sand,
      Water,
      SnowIce,
      Volcano,
      UltraSpace,
      Grass,
      Misty,
      Electric,
      Psychic;
   }
}
