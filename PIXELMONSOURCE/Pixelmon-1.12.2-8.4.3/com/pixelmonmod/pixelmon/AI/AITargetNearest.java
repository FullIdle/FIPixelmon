package com.pixelmonmod.pixelmon.AI;

import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class AITargetNearest extends AITarget {
   EntityLivingBase targetEntity;
   private TargetSorter targetSorter;

   public AITargetNearest(EntityCreature entity, float par3, boolean par5) {
      this(entity, par3, par5, false);
   }

   public AITargetNearest(EntityCreature entity, float par3, boolean par5, boolean par6) {
      super(entity, par3, par5, par6);
      this.targetDistance = par3;
      this.targetSorter = new TargetSorter(this, entity);
      this.func_75248_a(3);
   }

   public void setTargetDistance(float targetDistance) {
      this.targetDistance = targetDistance;
   }

   public boolean func_75250_a() {
      if (this.taskOwner instanceof EntityPixelmon) {
         if (((EntityPixelmon)this.taskOwner).battleController != null) {
            return false;
         }
      } else if (((NPCTrainer)this.taskOwner).battleController != null) {
         return false;
      }

      EntityPlayer player = this.taskOwner.field_70170_p.func_72890_a(this.taskOwner, (double)this.targetDistance);
      if (this.isSuitableTarget(player, true)) {
         this.targetEntity = player;
         return true;
      } else if (this.taskOwner instanceof NPCTrainer) {
         return false;
      } else {
         List nearEntities = this.taskOwner.field_70170_p.func_72872_a(EntityPixelmon.class, this.taskOwner.func_174813_aQ().func_72321_a((double)this.targetDistance, 4.0, (double)this.targetDistance));
         Collections.sort(nearEntities, this.targetSorter);
         Iterator var3 = nearEntities.iterator();

         Entity entity;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            entity = (Entity)var3.next();
         } while(!(entity instanceof EntityLiving) || !this.isSuitableTarget((EntityLiving)entity, true));

         this.targetEntity = (EntityLiving)entity;
         return true;
      }
   }

   public void func_75249_e() {
      this.taskOwner.func_70624_b(this.targetEntity);
      super.func_75249_e();
   }
}
