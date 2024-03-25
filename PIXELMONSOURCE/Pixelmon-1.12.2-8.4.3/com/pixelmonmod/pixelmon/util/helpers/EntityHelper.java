package com.pixelmonmod.pixelmon.util.helpers;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.client.models.animations.EnumRotation;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class EntityHelper {
   public static void setVelocity(Entity entity, double x, double y, double z) {
      entity.field_70159_w = x;
      entity.field_70181_x = y;
      entity.field_70179_y = z;
      entity.field_70133_I = true;
   }

   public static double getMotion(Entity entity, EnumRotation axis) {
      switch (axis) {
         case x:
            return entity.field_70159_w;
         case y:
            return entity.field_70181_x;
         case z:
            return entity.field_70179_y;
         default:
            return Double.NaN;
      }
   }

   public static List getNearbyEntities(Class entityType, World world, double x, double y, double z, float radius) {
      List found = Lists.newArrayList();

      for(int cX = (int)x - (int)radius >> 4; cX < (int)x + (int)(radius + 1.0F) >> 4; ++cX) {
         for(int cY = (int)y - (int)radius >> 4; cY < (int)y + (int)(radius + 1.0F) >> 4; ++cY) {
            Chunk chunk = world.func_72964_e(cX, cY);
            chunk.func_177430_a(entityType, new AxisAlignedBB(x - (double)radius, 0.0, z - (double)radius, x + (double)radius, 500.0, z + (double)radius), found, (e) -> {
               return true;
            });
         }
      }

      return found;
   }

   public static NBTTagCompound getPersistentData(EntityPlayer player) {
      NBTTagCompound data = player.getEntityData();
      if (!data.func_74764_b("PlayerPersisted")) {
         data.func_74782_a("PlayerPersisted", new NBTTagCompound());
      }

      return data.func_74775_l("PlayerPersisted");
   }
}
