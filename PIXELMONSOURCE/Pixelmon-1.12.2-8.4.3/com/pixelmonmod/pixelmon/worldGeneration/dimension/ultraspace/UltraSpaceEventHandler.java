package com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace;

import com.pixelmonmod.pixelmon.entities.EntityDen;
import com.pixelmonmod.pixelmon.entities.EntityPokestop;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.items.heldItems.ItemSafetyGoggles;
import com.pixelmonmod.pixelmon.util.helpers.DimensionHelper;
import com.pixelmonmod.pixelmon.util.helpers.EntityHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.FogMode;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

public class UltraSpaceEventHandler {
   public static float fogWane = 0.0F;
   public static boolean fogWaneIncr = true;

   @SubscribeEvent
   public static void onLivingTick(LivingEvent.LivingUpdateEvent event) {
      EntityLivingBase entity = event.getEntityLiving();
      if (entity.field_70170_p.field_73011_w.field_76577_b == UltraSpace.WORLD_TYPE) {
         boolean modifyGravity = false;
         if (!entity.func_189652_ae() && !entity.func_184218_aH() && entity.field_184629_bo == 0) {
            modifyGravity = true;
         }

         if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entity;
            if (player.field_71075_bZ.field_75100_b || player.field_71075_bZ.field_75098_d) {
               modifyGravity = false;
            }
         }

         if (entity instanceof EntityPixelmon) {
            EntityPixelmon pixelmon = (EntityPixelmon)entity;
            if (pixelmon.isFlying || pixelmon.func_184207_aI()) {
               modifyGravity = false;
            }
         }

         if (entity instanceof EntityPokestop || entity instanceof EntityDen) {
            modifyGravity = false;
         }

         if (modifyGravity && entity.field_70170_p.field_72995_K) {
            EntityHelper.setVelocity(entity, entity.field_70159_w, entity.field_70181_x + 0.08, entity.field_70179_y);
            boolean superLowGrav = true;

            for(int dy = 0; dy < 20; ++dy) {
               if (!entity.field_70170_p.func_175623_d(new BlockPos(entity.field_70165_t, entity.field_70163_u - 1.0 - (double)dy, entity.field_70161_v))) {
                  superLowGrav = false;
                  break;
               }
            }

            if (!entity.func_70093_af() && superLowGrav) {
               EntityHelper.setVelocity(entity, entity.field_70159_w, entity.field_70181_x - 0.005, entity.field_70179_y);
            } else {
               EntityHelper.setVelocity(entity, entity.field_70159_w, entity.field_70181_x - 0.016, entity.field_70179_y);
            }
         }

         if (entity instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)entity;
            if (entity.field_70163_u < -3.0 && entity.field_71093_bK == UltraSpace.DIM_ID) {
               player.field_70170_p.func_184133_a((EntityPlayer)null, entity.func_180425_c(), SoundEvents.field_187812_eh, SoundCategory.MASTER, 0.5F, 1.0F);
               double y;
               double z;
               int d;
               double x;
               if (player.getEntityData().func_74764_b("PortalX") && player.getEntityData().func_74764_b("PortalY") && player.getEntityData().func_74764_b("PortalZ") && player.getEntityData().func_74764_b("PortalD")) {
                  x = player.getEntityData().func_74769_h("PortalX");
                  y = player.getEntityData().func_74769_h("PortalY");
                  z = player.getEntityData().func_74769_h("PortalZ");
                  d = player.getEntityData().func_74762_e("PortalD");
               } else {
                  d = player.getSpawnDimension();
                  WorldServer w = player.func_184102_h().func_71218_a(d);
                  x = (double)w.func_175694_M().func_177958_n();
                  y = (double)w.func_175694_M().func_177956_o();
                  z = (double)w.func_175694_M().func_177952_p();
               }

               DimensionHelper.teleport(player, d, x, y, z);
            } else if (entity.field_70163_u > 300.0) {
               if (player.field_70170_p.func_72820_D() % 20L == 0L) {
                  player.func_70097_a(DamageSource.field_76368_d, 2.0F);
               }
            } else if (entity.field_70163_u > 255.0 && player.field_70170_p.func_72820_D() % 60L == 0L) {
               player.func_70097_a(DamageSource.field_76368_d, 1.0F);
            }
         }
      }

   }

   @SubscribeEvent
   public static void onLivingFall(LivingFallEvent event) {
      EntityLivingBase entity = event.getEntityLiving();
      if (entity.field_70170_p.field_73011_w.field_76577_b == UltraSpace.WORLD_TYPE) {
         event.setDistance(event.getDistance() / 5.0F);
         event.setDamageMultiplier(0.5F);
      }

   }

   @SubscribeEvent
   public static void onClonePlayer(PlayerEvent.Clone event) {
      if (!event.isWasDeath()) {
         EntityPlayer old = event.getOriginal();
         EntityPlayer copy = event.getEntityPlayer();
         copy.getEntityData().func_74780_a("PortalX", old.getEntityData().func_74769_h("PortalX"));
         copy.getEntityData().func_74780_a("PortalY", old.getEntityData().func_74769_h("PortalY"));
         copy.getEntityData().func_74780_a("PortalZ", old.getEntityData().func_74769_h("PortalZ"));
         copy.getEntityData().func_74768_a("PortalD", old.getEntityData().func_74762_e("PortalD"));
      }

   }

   @SideOnly(Side.CLIENT)
   @SubscribeEvent
   public static void fogDensityEvent(EntityViewRenderEvent.FogDensity event) {
      if (Minecraft.func_71410_x().field_71441_e.field_73011_w.getDimension() == UltraSpace.DIM_ID) {
         GlStateManager.func_187430_a(FogMode.EXP2);
         if (fogWaneIncr) {
            if (fogWane > 0.06F) {
               fogWaneIncr = false;
            }

            fogWane += 2.0E-6F;
         } else {
            if (fogWane < 0.0F) {
               fogWaneIncr = true;
            }

            fogWane -= 2.0E-6F;
         }

         float modifier = 1.0F;
         if (Minecraft.func_71410_x().field_71439_g.field_71071_by.func_70440_f(3).func_77973_b() instanceof ItemSafetyGoggles) {
            modifier -= 0.5F;
         }

         event.setDensity((0.045F + fogWane) * modifier);
         if (Minecraft.func_71410_x().field_71439_g.field_71075_bZ.field_75098_d) {
            event.setDensity(0.0F);
         }

         GL11.glFogi(2917, 2049);
         event.setCanceled(true);
      }

   }
}
