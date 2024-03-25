package com.pixelmonmod.pixelmon.client.particle;

import com.pixelmonmod.pixelmon.client.particle.systems.Bloom;
import com.pixelmonmod.pixelmon.client.particle.systems.Discharge;
import com.pixelmonmod.pixelmon.client.particle.systems.Drain;
import com.pixelmonmod.pixelmon.client.particle.systems.Heal;
import com.pixelmonmod.pixelmon.client.particle.systems.RadialThunder;
import com.pixelmonmod.pixelmon.client.particle.systems.RedChainPortal;
import com.pixelmonmod.pixelmon.client.particle.systems.Shiny;
import com.pixelmonmod.pixelmon.client.particle.systems.SlingRing;
import com.pixelmonmod.pixelmon.client.particle.systems.UltraWormhole;

public enum ParticleSystems {
   HEAL(new Heal()),
   REDCHAINPORTAL(new RedChainPortal()),
   DRAIN(new Drain()),
   BLOOM(new Bloom()),
   DISCHARGE(new Discharge()),
   RADIALTHUNDER(new RadialThunder()),
   SLINGRING(new SlingRing()),
   ULTRAWORMHOLE(new UltraWormhole()),
   SHINY(new Shiny());

   private ParticleSystem system;

   private ParticleSystems(ParticleSystem system) {
      this.system = system;
   }

   public ParticleSystem getSystem() {
      return this.system;
   }

   public static ParticleSystem get(int i) {
      return values()[i].system;
   }
}
