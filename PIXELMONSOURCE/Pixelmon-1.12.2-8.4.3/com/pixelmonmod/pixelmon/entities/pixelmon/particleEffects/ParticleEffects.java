package com.pixelmonmod.pixelmon.entities.pixelmon.particleEffects;

import com.google.common.collect.Sets;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public abstract class ParticleEffects {
   static Map map = new EnumMap(EnumSpecies.class);
   static ShinyParticles shinyParticles;
   Random rand;

   public ParticleEffects() {
      this.rand = RandomHelper.rand;
   }

   static void init() {
      map.put(EnumSpecies.Weezing, new WeezingParticles());
      map.put(EnumSpecies.Koffing, new KoffingParticles());
      map.put(EnumSpecies.Diglett, new DiglettParticles());
      map.put(EnumSpecies.Dugtrio, new DiglettParticles());
      map.put(EnumSpecies.Gastly, new GastlyParticles());
      map.put(EnumSpecies.Charmander, new FlameParticles(0.7F, 0.45F, 3));
      map.put(EnumSpecies.Charmeleon, new FlameParticles(1.2F, 0.7F, 5));
      map.put(EnumSpecies.Chimchar, new FlameParticles(0.3F, 0.38F, 3));
      map.put(EnumSpecies.Monferno, new FlameParticles(0.84F, 0.81F, 5));
      shinyParticles = new ShinyParticles();
   }

   public abstract void onUpdate(Entity2Client var1);

   public static Set getParticleEffects(Entity2Client pixelmon) {
      if (map.isEmpty()) {
         init();
      }

      Set effects = Sets.newHashSet();
      if (map.containsKey(pixelmon.getSpecies())) {
         effects.add(map.get(pixelmon.getSpecies()));
      }

      String texture = pixelmon.getTextureNoCheck().toString();
      if (texture.contains("shiny")) {
         effects.add(shinyParticles);
      }

      return effects;
   }
}
