package com.pixelmonmod.pixelmon.AI;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.AggressionEvent;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.EvolutionQueryList;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.pixelmon.listener.RepelHandler;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public abstract class AITarget extends EntityAIBase {
   protected EntityCreature taskOwner;
   protected float targetDistance;
   protected boolean shouldCheckSight;
   private boolean field_75303_a;
   private int field_75301_b;
   private int field_75302_c;
   private int field_75298_g;

   public AITarget(EntityPixelmon par1EntityLiving, float par2, boolean par3) {
      this(par1EntityLiving, par2, par3, false);
      this.func_75248_a(3);
   }

   public AITarget(EntityCreature entity, float par2, boolean par3, boolean par4) {
      this.field_75301_b = 0;
      this.field_75302_c = 0;
      this.field_75298_g = 0;
      this.taskOwner = entity;
      this.targetDistance = par2;
      this.shouldCheckSight = par3;
      this.field_75303_a = par4;
   }

   public boolean func_75253_b() {
      if (this.taskOwner instanceof EntityPixelmon) {
         EntityPixelmon pokemon = (EntityPixelmon)this.taskOwner;
         if (pokemon.battleController != null || pokemon.getBossMode() != EnumBossMode.NotBoss) {
            return false;
         }
      } else if (((NPCTrainer)this.taskOwner).battleController != null) {
         return false;
      }

      EntityLivingBase var1 = this.taskOwner.func_70638_az();
      if (var1 != null && var1.func_70089_S() && !(this.taskOwner.func_70068_e(var1) > (double)(this.targetDistance * this.targetDistance))) {
         if (this.shouldCheckSight) {
            if (this.taskOwner instanceof NPCTrainer) {
               if (this.taskOwner.func_70635_at().func_75522_a(var1) && this.checkAngle(var1)) {
                  this.field_75298_g = 0;
               } else if (++this.field_75298_g > 60) {
                  return false;
               }
            } else if (this.taskOwner.func_70635_at().func_75522_a(var1)) {
               this.field_75298_g = 0;
            } else if (++this.field_75298_g > 60) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean checkAngle(EntityLivingBase var1) {
      Vec3d look = this.taskOwner.func_70676_i(1.0F);
      Vec3d toTarget = new Vec3d(this.taskOwner.field_70165_t - var1.field_70165_t, this.taskOwner.field_70163_u - var1.field_70163_u, this.taskOwner.field_70161_v - var1.field_70161_v);
      double top = look.field_72450_a * toTarget.field_72450_a + look.field_72448_b * toTarget.field_72448_b + look.field_72449_c * toTarget.field_72449_c;
      double A = Math.sqrt(Math.pow(look.field_72450_a, 2.0) + Math.pow(look.field_72448_b, 2.0) + Math.pow(look.field_72449_c, 2.0));
      double B = Math.sqrt(Math.pow(toTarget.field_72450_a, 2.0) + Math.pow(toTarget.field_72448_b, 2.0) + Math.pow(toTarget.field_72449_c, 2.0));
      double angle = Math.acos(top / (A * B));
      return Math.abs(angle - Math.PI) <= 0.2;
   }

   public void func_75249_e() {
      this.field_75301_b = 0;
      this.field_75302_c = 0;
      this.field_75298_g = 0;
   }

   public void func_75251_c() {
      this.taskOwner.func_70624_b((EntityLivingBase)null);
   }

   protected boolean isSuitableTarget(EntityLivingBase entity, boolean par2) {
      if (entity != null && (entity instanceof EntityPixelmon || entity instanceof EntityPlayer) && entity != this.taskOwner && entity.func_70089_S()) {
         if (entity instanceof EntityPlayer && this.taskOwner instanceof NPCTrainer) {
            NPCTrainer trainer = (NPCTrainer)this.taskOwner;
            EntityPlayer player = (EntityPlayer)entity;
            ItemStack heldItem = player.func_184614_ca();
            if (!heldItem.func_190926_b() && heldItem.func_77973_b() == PixelmonItems.trainerEditor && trainer.getAIMode().doesEngage() || !trainer.canStartBattle(player, false)) {
               return false;
            }
         }

         AxisAlignedBB entityBounds = entity.func_174813_aQ();
         AxisAlignedBB thisBounds = this.taskOwner.func_174813_aQ();
         if (entityBounds.field_72337_e > thisBounds.field_72338_b && entityBounds.field_72338_b < thisBounds.field_72337_e) {
            Entity thisOwner = null;
            if (this.taskOwner instanceof EntityPixelmon) {
               thisOwner = ((EntityPixelmon)this.taskOwner).func_70902_q();
            }

            if (thisOwner == null) {
               if (entity instanceof EntityPlayer && !par2 && ((EntityPlayer)entity).field_71075_bZ.field_75102_a) {
                  return false;
               }

               if (entity instanceof EntityPlayerMP) {
                  PlayerPartyStorage storage = Pixelmon.storageManager.getParty((EntityPlayerMP)entity);
                  if (storage.countAblePokemon() < 1) {
                     return false;
                  }

                  if (RepelHandler.hasRepel((EntityPlayerMP)entity) && this.taskOwner instanceof EntityPixelmon) {
                     int highestLevel = storage.getHighestLevel();
                     if (highestLevel > ((EntityPixelmon)this.taskOwner).getLvl().getLevel()) {
                        return false;
                     }
                  }

                  if (Pixelmon.EVENT_BUS.post(new AggressionEvent(this.taskOwner, (EntityPlayerMP)entity))) {
                     return false;
                  }
               }
            } else {
               if (entity instanceof EntityPixelmon) {
                  EntityPixelmon targetPokemon = (EntityPixelmon)entity;
                  if (targetPokemon.func_70902_q() == thisOwner || targetPokemon.spawner != null) {
                     return false;
                  }
               }

               if (entity == ((EntityTameable)this.taskOwner).func_70902_q()) {
                  return false;
               }
            }

            if (this.taskOwner.func_110173_bK() && (!this.shouldCheckSight || this.taskOwner.func_70635_at().func_75522_a(entity))) {
               if (this.field_75303_a) {
                  if (--this.field_75302_c <= 0) {
                     this.field_75301_b = 0;
                  }

                  if (this.field_75301_b == 0) {
                     this.field_75301_b = this.func_75295_a(entity) ? 1 : 2;
                  }

                  if (this.field_75301_b == 2) {
                     return false;
                  }
               }

               if (entity instanceof EntityPlayerMP) {
                  if (((EntityPlayer)entity).func_184812_l_()) {
                     return false;
                  } else {
                     return EvolutionQueryList.get((EntityPlayerMP)entity) == null;
                  }
               } else {
                  return true;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean func_75295_a(EntityLivingBase par1EntityLiving) {
      this.field_75302_c = 10 + this.taskOwner.func_70681_au().nextInt(5);
      Path var2 = this.taskOwner.func_70661_as().func_75494_a(par1EntityLiving);
      if (var2 == null) {
         return false;
      } else {
         PathPoint var3 = var2.func_75870_c();
         if (var3 == null) {
            return false;
         } else {
            int var4 = var3.field_75839_a - MathHelper.func_76128_c(par1EntityLiving.field_70165_t);
            int var5 = var3.field_75839_a - MathHelper.func_76128_c(par1EntityLiving.field_70161_v);
            return (double)(var4 * var4 + var5 * var5) <= 2.25;
         }
      }
   }
}
