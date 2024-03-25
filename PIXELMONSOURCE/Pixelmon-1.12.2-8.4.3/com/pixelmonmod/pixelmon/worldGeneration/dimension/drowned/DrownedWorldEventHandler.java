package com.pixelmonmod.pixelmon.worldGeneration.dimension.drowned;

import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.EntityWormhole;
import com.pixelmonmod.pixelmon.enums.forms.EnumSpecial;
import com.pixelmonmod.pixelmon.util.helpers.DimensionHelper;
import com.pixelmonmod.pixelmon.util.helpers.EntityHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DrownedWorldEventHandler {
   @SubscribeEvent
   public static void onCapture(CaptureEvent.StartCapture event) {
      if (PixelmonConfig.drownedWorld && event.getPokemon().getFormEnum() == EnumSpecial.Drowned && !EntityHelper.getPersistentData(event.player).func_74767_n("HalloweenLugia")) {
         event.setCanceled(true);
      }

   }

   @SubscribeEvent
   public static void onLivingTick(LivingEvent.LivingUpdateEvent event) {
      EntityLivingBase entity = event.getEntityLiving();
      if (entity instanceof EntityPlayerMP && entity.field_71093_bK == DrownedWorld.DIM_ID) {
         EntityPlayerMP player = (EntityPlayerMP)entity;
         int chunkRadius = PixelmonConfig.drownedWorldRadius;
         int bounds = chunkRadius * 16;
         int xzTest = bounds - 16;
         int xzTeleport = bounds - 32;
         if (entity.field_70163_u <= 10.0) {
            teleportOut(entity);
         } else if (entity.field_70163_u >= 246.0) {
            teleportOut(entity);
         } else if (entity.field_70165_t <= (double)(-xzTest)) {
            player.field_71135_a.func_147364_a((double)xzTeleport, player.field_70163_u, player.field_70161_v, player.field_70177_z, player.field_70125_A);
         } else if (entity.field_70165_t >= (double)xzTest) {
            player.field_71135_a.func_147364_a((double)(-xzTeleport), player.field_70163_u, player.field_70161_v, player.field_70177_z, player.field_70125_A);
         } else if (entity.field_70161_v <= (double)(-xzTest)) {
            player.field_71135_a.func_147364_a(player.field_70165_t, player.field_70163_u, (double)xzTeleport, player.field_70177_z, player.field_70125_A);
         } else if (entity.field_70161_v >= (double)xzTest) {
            player.field_71135_a.func_147364_a(player.field_70165_t, player.field_70163_u, (double)(-xzTeleport), player.field_70177_z, player.field_70125_A);
         }
      }

   }

   @SubscribeEvent
   public static void onLivingHurt(LivingHurtEvent event) {
      if (PixelmonConfig.drownedWorld) {
         EntityLivingBase entity = event.getEntityLiving();
         if (entity instanceof EntityPlayerMP && entity.field_71093_bK == DrownedWorld.DIM_ID && event.getSource() == DamageSource.field_76369_e && entity.func_110143_aJ() <= 6.0F) {
            event.setCanceled(true);
            teleportOut(entity);
         }
      }

   }

   private static void teleportOut(EntityLivingBase entity) {
      entity.func_70691_i(20.0F);
      EntityPlayerMP player = (EntityPlayerMP)entity;
      double x;
      double y;
      double z;
      int d;
      if (player.getEntityData().func_74764_b("PortalX") && player.getEntityData().func_74764_b("PortalY") && player.getEntityData().func_74764_b("PortalZ") && player.getEntityData().func_74764_b("PortalD")) {
         x = player.getEntityData().func_74769_h("PortalX");
         y = player.getEntityData().func_74769_h("PortalY");
         z = player.getEntityData().func_74769_h("PortalZ");
         d = player.getEntityData().func_74762_e("PortalD");
      } else {
         d = player.getSpawnDimension();
         WorldServer world = FMLCommonHandler.instance().getMinecraftServerInstance().func_71218_a(d);
         x = (double)world.func_175694_M().func_177958_n();
         y = (double)world.func_175694_M().func_177956_o();
         z = (double)world.func_175694_M().func_177952_p();
      }

      double[] destination = new double[]{x, y, z};
      EntityWormhole.wrapIntoWorldBorder(destination, d);
      DimensionHelper.teleport(player, d, destination[0], y, destination[1]);
   }
}
