package com.pixelmonmod.pixelmon.AI;

import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;

public class AIMoveTowardsTarget extends EntityAIBase {
   private EntityCreature theEntity;
   private EntityLivingBase targetEntity;
   private double movePosX;
   private double movePosY;
   private double movePosZ;
   private float field_75426_g;

   public AIMoveTowardsTarget(EntityCreature par1EntityCreature, float par3) {
      this.theEntity = par1EntityCreature;
      this.field_75426_g = par3;
      this.func_75248_a(3);
   }

   public void setTargetDistance(float targetDistance) {
      this.field_75426_g = targetDistance;
   }

   public boolean func_75250_a() {
      if (this.theEntity.func_70638_az() == null) {
         return false;
      } else {
         if (this.theEntity instanceof EntityPixelmon) {
            if (((EntityPixelmon)this.theEntity).aggressionTimer > 0) {
               return false;
            }

            if (((EntityPixelmon)this.theEntity).battleController != null) {
               return false;
            }

            if (((EntityPixelmon)this.theEntity).func_70902_q() != null && BattleRegistry.getBattle((EntityPlayerMP)((EntityPixelmon)this.theEntity).func_70902_q()) != null) {
               this.theEntity.func_70624_b((EntityLivingBase)null);
               return false;
            }
         } else if (((NPCTrainer)this.theEntity).battleController != null) {
            return false;
         }

         this.targetEntity = this.theEntity.func_70638_az();
         if (this.targetEntity.func_70068_e(this.theEntity) > (double)(this.field_75426_g * this.field_75426_g)) {
            return false;
         } else {
            Vec3d var1 = RandomPositionGenerator.func_75464_a(this.theEntity, 16, 7, new Vec3d(this.targetEntity.field_70165_t, this.targetEntity.field_70163_u, this.targetEntity.field_70161_v));
            if (var1 == null) {
               return false;
            } else {
               this.movePosX = var1.field_72450_a;
               this.movePosY = var1.field_72448_b;
               this.movePosZ = var1.field_72449_c;
               return true;
            }
         }
      }
   }

   public boolean func_75253_b() {
      return false;
   }

   public void func_75251_c() {
      this.targetEntity = null;
   }

   public void func_75249_e() {
      this.theEntity.func_70661_as().func_75492_a(this.movePosX, this.movePosY, this.movePosZ, (double)((float)this.theEntity.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111126_e()));
   }
}
