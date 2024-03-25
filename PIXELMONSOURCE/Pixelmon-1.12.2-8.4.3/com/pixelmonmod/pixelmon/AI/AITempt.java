package com.pixelmonmod.pixelmon.AI;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;

public class AITempt extends EntityAIBase {
   private EntityCreature temptedEntity;
   private double targetX;
   private double targetY;
   private double targetZ;
   private double field_75278_f;
   private double field_75279_g;
   private EntityPlayer temptingPlayer;
   private int delayTemptCounter;
   private boolean isRunning;
   private Set temptingItems;
   private boolean scaredByPlayerMovement;
   private boolean canSwim;

   public AITempt(EntityCreature par1EntityCreature, boolean par5, Item... item) {
      this.temptedEntity = par1EntityCreature;
      this.temptingItems = Sets.newHashSet(item);
      this.scaredByPlayerMovement = par5;
      this.func_75248_a(3);
   }

   public boolean func_75250_a() {
      if (this.delayTemptCounter > 0) {
         --this.delayTemptCounter;
         return false;
      } else {
         this.temptingPlayer = this.temptedEntity.field_70170_p.func_72890_a(this.temptedEntity, 10.0);
         if (this.temptingPlayer == null) {
            return false;
         } else {
            ItemStack itemstack = this.temptingPlayer.func_184614_ca();
            return this.temptingItems.contains(itemstack.func_77973_b());
         }
      }
   }

   public boolean func_75253_b() {
      if (this.scaredByPlayerMovement) {
         if (this.temptedEntity.func_70068_e(this.temptingPlayer) < 36.0) {
            if (this.temptingPlayer.func_70092_e(this.targetX, this.targetY, this.targetZ) > 0.010000000000000002) {
               return false;
            }

            if (Math.abs((double)this.temptingPlayer.field_70125_A - this.field_75278_f) > 5.0 || Math.abs((double)this.temptingPlayer.field_70177_z - this.field_75279_g) > 5.0) {
               return false;
            }
         } else {
            this.targetX = this.temptingPlayer.field_70165_t;
            this.targetY = this.temptingPlayer.field_70163_u;
            this.targetZ = this.temptingPlayer.field_70161_v;
         }

         this.field_75278_f = (double)this.temptingPlayer.field_70125_A;
         this.field_75279_g = (double)this.temptingPlayer.field_70177_z;
      }

      return this.func_75250_a();
   }

   public void func_75249_e() {
      this.targetX = this.temptingPlayer.field_70165_t;
      this.targetY = this.temptingPlayer.field_70163_u;
      this.targetZ = this.temptingPlayer.field_70161_v;
      this.isRunning = true;
      PathNavigate navigate = this.temptedEntity.func_70661_as();
      if (navigate instanceof PathNavigateGround) {
         this.canSwim = ((PathNavigateGround)navigate).func_179684_h();
         ((PathNavigateGround)navigate).func_179693_d(true);
      } else if (navigate instanceof PathNavigateFlying) {
         this.canSwim = ((PathNavigateFlying)navigate).func_192880_g();
         ((PathNavigateFlying)navigate).func_192877_c(true);
      }

   }

   public void func_75251_c() {
      this.temptingPlayer = null;
      this.temptedEntity.func_70661_as().func_75499_g();
      this.delayTemptCounter = 100;
      this.isRunning = false;
      PathNavigate navigate = this.temptedEntity.func_70661_as();
      if (navigate instanceof PathNavigateGround) {
         ((PathNavigateGround)navigate).func_179693_d(this.canSwim);
      } else if (navigate instanceof PathNavigateFlying) {
         ((PathNavigateFlying)navigate).func_192877_c(this.canSwim);
      }

   }

   public void func_75246_d() {
      this.temptedEntity.func_70671_ap().func_75651_a(this.temptingPlayer, 30.0F, (float)this.temptedEntity.func_70646_bf());
      if (this.temptedEntity.func_70068_e(this.temptingPlayer) < 6.25) {
         this.temptedEntity.func_70661_as().func_75499_g();
      } else {
         this.temptedEntity.func_70661_as().func_75497_a(this.temptingPlayer, this.temptedEntity.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111126_e());
      }

   }

   public boolean isRunning() {
      return this.isRunning;
   }
}
