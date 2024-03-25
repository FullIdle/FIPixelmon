package com.pixelmonmod.pixelmon.AI;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import java.util.Random;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class AITeleportAway extends EntityAIBase {
   EntityPixelmon pixelmon;
   Random rand;

   public AITeleportAway(EntityPixelmon entity) {
      this.pixelmon = entity;
      this.rand = entity.func_70681_au();
   }

   public boolean func_75250_a() {
      return !this.pixelmon.hasOwner() && this.pixelmon.field_70170_p.func_72890_a(this.pixelmon, 7.0) != null;
   }

   public boolean func_75253_b() {
      return false;
   }

   public void func_75249_e() {
      teleportRandomly(this.pixelmon, this.rand);
   }

   public static boolean teleportRandomly(EntityPixelmon pixelmon, Random rand) {
      return teleportTo(pixelmon, rand, pixelmon.field_70165_t + (rand.nextDouble() - 0.5) * 64.0, 256.0, pixelmon.field_70161_v + (rand.nextDouble() - 0.5) * 64.0);
   }

   protected static boolean teleportTo(EntityPixelmon pixelmon, Random rand, double suggestedXPos, double suggestedYPos, double suggestedZPos) {
      double currentXPos = pixelmon.field_70165_t;
      double currentYPos = pixelmon.field_70163_u;
      double currentZPos = pixelmon.field_70161_v;
      int intPosX = MathHelper.func_76128_c(suggestedXPos);
      int intPosY = MathHelper.func_76128_c(suggestedYPos);
      int intPosZ = MathHelper.func_76128_c(suggestedZPos);
      BlockPos newLocation = pixelmon.field_70170_p.func_175672_r(new BlockPos(intPosX, intPosY, intPosZ));
      intPosY = newLocation.func_177956_o();
      if (pixelmon.field_70170_p.func_175667_e(newLocation)) {
         pixelmon.field_70165_t = (double)intPosX;
         pixelmon.field_70163_u = (double)intPosY;
         pixelmon.field_70161_v = (double)intPosZ;
         pixelmon.func_70107_b(pixelmon.field_70165_t, pixelmon.field_70163_u, pixelmon.field_70161_v);
         createEndermanEffect(pixelmon, rand, currentXPos, currentYPos, currentZPos);
         if (pixelmon.field_70170_p.func_184144_a(pixelmon, pixelmon.func_174813_aQ()).size() != 0 || pixelmon.field_70170_p.func_72953_d(pixelmon.func_174813_aQ())) {
            return false;
         }
      }

      return true;
   }

   private static void createEndermanEffect(EntityPixelmon pixelmon, Random rand, double currentXPos, double currentYPos, double currentZPos) {
      int baseArea = 128;

      for(int variance = 0; variance < baseArea; ++variance) {
         double bounding = (double)variance / ((double)baseArea - 1.0);
         float floatRandX = (rand.nextFloat() - 0.5F) * 0.2F;
         float floatRandY = (rand.nextFloat() - 0.5F) * 0.2F;
         float floatRandZ = (rand.nextFloat() - 0.5F) * 0.2F;
         double doubleRandX = currentXPos + (pixelmon.field_70165_t - currentXPos) * bounding + (rand.nextDouble() - 0.5) * (double)pixelmon.field_70130_N * 2.0;
         double doubleRandY = currentYPos + (pixelmon.field_70163_u - currentYPos) * bounding + rand.nextDouble() * (double)pixelmon.field_70131_O;
         double doubleRandZ = currentZPos + (pixelmon.field_70161_v - currentZPos) * bounding + (rand.nextDouble() - 0.5) * (double)pixelmon.length * 2.0;
         pixelmon.field_70170_p.func_175688_a(EnumParticleTypes.PORTAL, doubleRandX, doubleRandY, doubleRandZ, (double)floatRandX, (double)floatRandY, (double)floatRandZ, new int[0]);
      }

      pixelmon.field_70170_p.func_184148_a((EntityPlayer)null, currentXPos, currentYPos, currentZPos, SoundEvents.field_187534_aX, SoundCategory.NEUTRAL, 1.0F, 1.0F);
   }
}
