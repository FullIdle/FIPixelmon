package com.pixelmonmod.pixelmon.enums;

import com.pixelmonmod.pixelmon.battles.animations.particles.ParticleGastly;
import com.pixelmonmod.pixelmon.battles.animations.particles.ParticleKoffing;
import com.pixelmonmod.pixelmon.battles.animations.particles.ParticlePixelmonFlame;
import com.pixelmonmod.pixelmon.battles.animations.particles.ParticleShiny;
import com.pixelmonmod.pixelmon.battles.animations.particles.ParticleSmoke;

public enum EnumPixelmonParticles {
   gastly(ParticleGastly.class),
   koffing(ParticleKoffing.class),
   flame(ParticlePixelmonFlame.class),
   smoke(ParticleSmoke.class),
   shiny(ParticleShiny.class);

   public Class particleClass;

   private EnumPixelmonParticles(Class particleClass) {
      this.particleClass = particleClass;
   }

   public static boolean hasPixelmonParticle(String name) {
      try {
         return valueOf(name) != null;
      } catch (Exception var2) {
         return false;
      }
   }
}
