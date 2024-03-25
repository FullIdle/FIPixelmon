package com.pixelmonmod.pixelmon.client.particle.systems;

import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.ParticleSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SlingRing extends ParticleSystem {
   @SideOnly(Side.CLIENT)
   public void execute(Minecraft mc, World w, double x, double y, double z, float scale, boolean shiny, double... args) {
      int totalPoints = 16;
      int totalHeight = 5;
      double radius = 2.0;
      x -= 0.5;
      z -= 0.5;

      for(double height = 0.0; height < (double)totalHeight; height += 0.1) {
         for(int i = 1; i <= totalPoints; ++i) {
            double theta = 6.283185307179586 / (double)totalPoints;
            double angle = theta * (double)i;
            double dx = radius * Math.cos(angle) + mc.field_71441_e.field_73012_v.nextDouble() * 1.5;
            double dz = radius * Math.sin(angle) + mc.field_71441_e.field_73012_v.nextDouble() * 1.5;
            mc.field_71452_i.func_78873_a(new ParticleArcanery(w, x + dx, y + height + mc.field_71441_e.field_73012_v.nextDouble() * 1.5, z + dz, 0.0, 0.0, 0.0, new com.pixelmonmod.pixelmon.client.particle.particles.SlingRing(true, 30 - mc.field_71441_e.field_73012_v.nextInt(10))));
         }
      }

   }
}
