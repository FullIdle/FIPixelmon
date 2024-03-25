package com.pixelmonmod.pixelmon.listener;

import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(
   modid = "pixelmon"
)
public class CanUpdateListener {
   @SubscribeEvent
   public static void onCanUpdate(EntityEvent.CanUpdate event) {
      if (PixelmonConfig.useUpdateEntityWithOptionalForceFix && event.getEntity() instanceof EntityPixelmon) {
         EntityPixelmon pixelmon = (EntityPixelmon)event.getEntity();
         int x = MathHelper.func_76128_c(pixelmon.field_70165_t);
         int z = MathHelper.func_76128_c(pixelmon.field_70161_v);
         boolean forcedChunk = !pixelmon.field_70170_p.field_72995_K && pixelmon.field_70170_p.getPersistentChunks().containsKey(new ChunkPos(x >> 4, z >> 4));
         int range = forcedChunk ? 0 : 12;
         if (pixelmon.field_70170_p.func_175707_a(new BlockPos(x - range, 0, z - range), new BlockPos(x + range, 0, z + range))) {
            event.setCanUpdate(true);
         } else if (pixelmon.canDespawn && pixelmon.func_70692_ba()) {
            pixelmon.updateDespawn();
         }
      }

   }
}
