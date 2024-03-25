package com.pixelmonmod.pixelmon.entities.pixelmon.tickHandlers;

import com.google.common.collect.Maps;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity1Base;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.util.HashMap;
import net.minecraft.world.World;

public abstract class TickHandlerBase {
   protected Entity1Base pixelmon;
   private int refreshRate;
   private int ticks;
   private static HashMap tickHandlers = Maps.newHashMap();

   public TickHandlerBase(Entity1Base pixelmon, int refreshRate) {
      this.ticks = 0;
      this.pixelmon = pixelmon;
      this.refreshRate = refreshRate;
   }

   public TickHandlerBase(Entity1Base pixelmon) {
      this(pixelmon, 0);
   }

   protected abstract void onTick(World var1);

   public void tick(World world) {
      if (++this.ticks > this.refreshRate) {
         this.ticks = 0;
         this.onTick(world);
      }

   }

   public static TickHandlerBase getTickHandler(EntityPixelmon pixelmon) {
      if (!tickHandlers.containsKey(pixelmon.getSpecies())) {
         return null;
      } else {
         Class c = (Class)tickHandlers.get(pixelmon.getSpecies());

         try {
            return (TickHandlerBase)c.getConstructor(Entity1Base.class).newInstance(pixelmon);
         } catch (Exception var3) {
            var3.printStackTrace();
            return null;
         }
      }
   }

   static {
      tickHandlers.put(EnumSpecies.Castform, CastformTickHandler.class);
      tickHandlers.put(EnumSpecies.Mareep, ShearableTickHandler.class);
      tickHandlers.put(EnumSpecies.Wooloo, ShearableTickHandler.class);
      tickHandlers.put(EnumSpecies.Dubwool, ShearableTickHandler.class);
   }
}
