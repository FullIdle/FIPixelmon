package com.pixelmonmod.pixelmon.api.world;

import com.google.common.collect.Maps;
import com.pixelmonmod.pixelmon.util.ITranslatable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;
import net.minecraft.world.World;

public enum WorldTime implements ITranslatable {
   DAWN((tick) -> {
      return tick >= 22500 || tick <= 300;
   }),
   MORNING((tick) -> {
      return tick <= 6000 || tick >= 22550;
   }),
   DAY((tick) -> {
      return tick <= 12000;
   }),
   MIDDAY((tick) -> {
      return tick >= 5500 && tick <= 6500;
   }),
   AFTERNOON((tick) -> {
      return tick >= 6000 && tick <= 12000;
   }),
   DUSK((tick) -> {
      return tick >= 12000 && tick <= 13800;
   }),
   NIGHT((tick) -> {
      return tick >= 13450 && tick <= 22550;
   }),
   MIDNIGHT((tick) -> {
      return tick >= 17500 && tick <= 18500;
   });

   public Predicate tickCondition;
   public static ConcurrentMap worldTimes = Maps.newConcurrentMap();
   private static final WorldTime[] ALL_TIMES = values();

   private WorldTime(Predicate tickCondition) {
      this.tickCondition = tickCondition;
   }

   public String getUnlocalizedName() {
      return "pixelmon.time." + this.name().toLowerCase();
   }

   public static ArrayList getCurrent(World world) {
      return (ArrayList)worldTimes.get(world.field_73011_w.getDimension());
   }

   private static ArrayList getCurrent(int ticks) {
      ArrayList current = new ArrayList();
      WorldTime[] var2 = ALL_TIMES;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         WorldTime time = var2[var4];
         if (time.tickCondition.test(ticks)) {
            current.add(time);
         }
      }

      return current;
   }

   public static void updateWorldTime(World world) {
      Objects.requireNonNull(world, "null world supplied");
      worldTimes.put(world.field_73011_w.getDimension(), getCurrent((int)(world.func_72820_D() % 24000L)));
   }
}
