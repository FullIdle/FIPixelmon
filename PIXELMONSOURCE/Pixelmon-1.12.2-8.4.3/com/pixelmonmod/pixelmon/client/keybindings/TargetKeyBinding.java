package com.pixelmonmod.pixelmon.client.keybindings;

import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class TargetKeyBinding extends KeyBinding {
   protected boolean targetLiquids = false;

   public TargetKeyBinding(String p_i45001_1_, int p_i45001_2_, String p_i45001_3_) {
      super(p_i45001_1_, p_i45001_2_, p_i45001_3_);
   }

   public static RayTraceResult getTarget(boolean targetLiquids) {
      return getTarget(targetLiquids, 10.0);
   }

   public static RayTraceResult getTarget(boolean targetLiquids, double range) {
      Minecraft mc = Minecraft.func_71410_x();
      Entity pointedEntity = null;
      float partialTick = 1.0F;
      Vec3d Vec3d = mc.func_175606_aa().func_174824_e(partialTick);
      Vec3d Vec3d1 = mc.func_175606_aa().func_70676_i(partialTick);
      Vec3d Vec3d2 = Vec3d.func_72441_c(Vec3d1.field_72450_a * range, Vec3d1.field_72448_b * range, Vec3d1.field_72449_c * range);
      RayTraceResult objectMouseOver = mc.field_71441_e.func_147447_a(Vec3d, Vec3d2, targetLiquids, false, true);
      double d1 = range;
      Vec3d = mc.func_175606_aa().func_174824_e(partialTick);
      if (objectMouseOver != null) {
         d1 = objectMouseOver.field_72307_f.func_72438_d(Vec3d);
      }

      Vec3d Vec3d3 = null;
      float f1 = 1.0F;
      List list = mc.field_71441_e.func_72839_b(mc.func_175606_aa(), mc.func_175606_aa().func_174813_aQ().func_72321_a(Vec3d1.field_72450_a * range, Vec3d1.field_72448_b * range, Vec3d1.field_72449_c * range).func_72321_a((double)f1, (double)f1, (double)f1));
      double d2 = d1;
      Iterator var17 = list.iterator();

      while(true) {
         Entity aList;
         RayTraceResult movingobjectposition;
         do {
            while(true) {
               do {
                  if (!var17.hasNext()) {
                     if (pointedEntity != null && (d2 < d1 || objectMouseOver == null)) {
                        objectMouseOver = new RayTraceResult(pointedEntity, Vec3d3);
                     }

                     return objectMouseOver;
                  }

                  aList = (Entity)var17.next();
               } while(!aList.func_70067_L());

               float f2 = aList.func_70111_Y();
               AxisAlignedBB axisalignedbb = aList.func_174813_aQ().func_72321_a((double)f2, (double)f2, (double)f2);
               movingobjectposition = axisalignedbb.func_72327_a(Vec3d, Vec3d2);
               if (axisalignedbb.func_72318_a(Vec3d)) {
                  break;
               }

               if (movingobjectposition != null) {
                  double d3 = Vec3d.func_72438_d(movingobjectposition.field_72307_f);
                  if (d3 < d2 || d2 == 0.0) {
                     if (aList == mc.func_175606_aa().func_184179_bs() && !aList.canRiderInteract()) {
                        if (d2 == 0.0) {
                           pointedEntity = aList;
                           Vec3d3 = movingobjectposition.field_72307_f;
                        }
                     } else {
                        pointedEntity = aList;
                        Vec3d3 = movingobjectposition.field_72307_f;
                        d2 = d3;
                     }
                  }
               }
            }
         } while(!(0.0 < d2) && d2 != 0.0);

         pointedEntity = aList;
         Vec3d3 = movingobjectposition == null ? Vec3d : movingobjectposition.field_72307_f;
         d2 = 0.0;
      }
   }
}
