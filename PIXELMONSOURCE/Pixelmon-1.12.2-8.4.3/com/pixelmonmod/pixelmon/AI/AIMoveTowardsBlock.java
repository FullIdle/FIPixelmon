package com.pixelmonmod.pixelmon.AI;

import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;

public class AIMoveTowardsBlock extends EntityAIBase {
   private EntityPixelmon pixelmon;
   private double movePosX;
   private double movePosY;
   private double movePosZ;

   public AIMoveTowardsBlock(EntityPixelmon pixelmon, float targetDistance) {
      this.pixelmon = pixelmon;
      this.func_75248_a(3);
   }

   public boolean func_75250_a() {
      if (this.pixelmon.skillId == null) {
         return false;
      } else if (this.pixelmon.battleController != null) {
         return false;
      } else if (this.pixelmon.func_70902_q() != null && BattleRegistry.getBattle((EntityPlayerMP)this.pixelmon.func_70902_q()) != null) {
         this.pixelmon.func_70624_b((EntityLivingBase)null);
         return false;
      } else {
         this.movePosX = (double)this.pixelmon.targetX;
         this.movePosY = (double)this.pixelmon.targetY;
         this.movePosZ = (double)this.pixelmon.targetZ;
         MoveSkill moveSkill = MoveSkill.getMoveSkillByID(this.pixelmon.skillId);
         if (moveSkill != null && PixelmonConfig.allowExternalMoves && moveSkill.hasMoveSkill(this.pixelmon.getPokemonData()) && !this.pixelmon.getPokemonData().isMoveSkillCoolingDown(moveSkill)) {
            if (this.pixelmon.func_70011_f((double)this.pixelmon.targetX, (double)this.pixelmon.targetY, (double)this.pixelmon.targetZ) < (double)moveSkill.range) {
               BlockPos pos = new BlockPos(this.pixelmon.targetX, this.pixelmon.targetY, this.pixelmon.targetZ);
               EnumFacing facing = this.pixelmon.targetSide;
               Tuple tup = new Tuple(pos, facing);
               moveSkill.onUsed(this.pixelmon, tup, MoveSkill.EnumTargetType.BLOCK);
               this.pixelmon.func_70624_b((EntityLivingBase)null);
               this.movePosX = this.pixelmon.field_70165_t;
               this.movePosY = this.pixelmon.field_70163_u;
               this.movePosZ = this.pixelmon.field_70161_v;
               this.pixelmon.func_70661_as().func_75492_a(this.movePosX, this.movePosY, this.movePosZ, (double)((float)this.pixelmon.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111126_e()));
               return false;
            } else {
               return true;
            }
         } else {
            return false;
         }
      }
   }

   public boolean func_75253_b() {
      return false;
   }

   public void func_75251_c() {
   }

   public void func_75249_e() {
      this.pixelmon.func_70661_as().func_75492_a(this.movePosX, this.movePosY, this.movePosZ, (double)((float)this.pixelmon.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111126_e()));
   }
}
