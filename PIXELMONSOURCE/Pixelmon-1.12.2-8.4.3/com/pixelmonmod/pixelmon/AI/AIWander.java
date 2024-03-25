package com.pixelmonmod.pixelmon.AI;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class AIWander extends EntityAIBase {
   private EntityCreature entity;
   private double xPosition;
   private double yPosition;
   private double zPosition;
   private boolean mustUpdate;
   private int executionChance = 120;

   public AIWander(EntityCreature par1EntityCreature) {
      this.entity = par1EntityCreature;
      this.func_75248_a(1);
   }

   public boolean func_75250_a() {
      if (!this.mustUpdate) {
         if (this.entity.func_70681_au().nextInt(this.executionChance) != 0) {
            return false;
         }

         if (this.entity instanceof EntityPixelmon && !((EntityPixelmon)this.entity).canMove) {
            return false;
         }
      }

      Vec3d vec3D = RandomPositionGenerator.func_75463_a(this.entity, 10, 7);
      if (vec3D == null) {
         return false;
      } else {
         if (this.entity instanceof EntityPixelmon) {
            EntityPixelmon p = (EntityPixelmon)this.entity;
            if (p == null || p.field_70170_p == null || !p.field_70170_p.func_175697_a(new BlockPos(p.field_70165_t, p.field_70163_u, p.field_70161_v), 2)) {
               return false;
            }

            vec3D = this.checkBlockOwner(p, vec3D);
            if (vec3D == null) {
               return false;
            }

            vec3D = this.checkSpawner(p, vec3D);
            if (vec3D == null) {
               return false;
            }
         }

         this.xPosition = vec3D.field_72450_a;
         this.yPosition = vec3D.field_72448_b;
         this.zPosition = vec3D.field_72449_c;
         this.mustUpdate = false;
         return true;
      }
   }

   private Vec3d checkSpawner(EntityPixelmon p, Vec3d vec3) {
      if (p.spawner != null) {
         BlockPos spawnerPos = p.spawner.func_174877_v();
         int spawnerX = spawnerPos.func_177958_n();
         int spawnerZ = spawnerPos.func_177952_p();

         int count;
         for(count = 2; (vec3.field_72450_a - (double)spawnerX) * (vec3.field_72450_a - (double)spawnerX) + (vec3.field_72449_c - (double)spawnerZ) * (vec3.field_72449_c - (double)spawnerZ) > (double)(p.spawner.spawnRadius * p.spawner.spawnRadius) && count >= 0; --count) {
            Vec3d newVec3 = RandomPositionGenerator.func_75463_a(this.entity, 10, 7);
            if (newVec3 != null) {
               vec3 = newVec3;
            }
         }

         if (count < 0) {
            return null;
         }
      }

      return vec3;
   }

   private Vec3d checkBlockOwner(EntityPixelmon p, Vec3d vec3) {
      if (p.blockOwner != null) {
         int count;
         for(count = 2; !p.blockOwner.getBounds().isIn(vec3) && count >= 0; --count) {
            Vec3d newVec3 = RandomPositionGenerator.func_75463_a(this.entity, 5, 7);
            if (newVec3 != null) {
               vec3 = newVec3;
            }
         }

         if (count < 0) {
            return null;
         }
      }

      return vec3;
   }

   public boolean func_75253_b() {
      return !this.entity.func_70661_as().func_75500_f();
   }

   public void func_75249_e() {
      this.entity.func_70661_as().func_75492_a(this.xPosition, this.yPosition, this.zPosition, this.entity.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111126_e());
      if (this.entity instanceof EntityPixelmon) {
         EntityPixelmon p = (EntityPixelmon)this.entity;
         if (p.blockOwner != null && this.func_75253_b()) {
            Path path = p.func_70661_as().func_75505_d();
            if (path != null) {
               int length = path.func_75874_d();

               for(int i = 0; i < length; ++i) {
                  if (!p.blockOwner.getBounds().isIn(path.func_75881_a(p, i))) {
                     p.func_70661_as().func_75499_g();
                     return;
                  }
               }
            }
         }
      }

   }

   public void makeUpdate() {
      this.mustUpdate = true;
   }

   public void setExecutionChance(int newchance) {
      this.executionChance = newchance;
   }
}
