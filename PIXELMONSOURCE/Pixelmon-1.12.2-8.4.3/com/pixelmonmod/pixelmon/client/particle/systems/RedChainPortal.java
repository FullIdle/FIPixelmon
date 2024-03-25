package com.pixelmonmod.pixelmon.client.particle.systems;

import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.ParticleSystem;
import com.pixelmonmod.pixelmon.client.particle.particles.CycloneBlob;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RedChainPortal extends ParticleSystem {
   @SideOnly(Side.CLIENT)
   public void execute(Minecraft mc, World w, double x, double y, double z, float scale, boolean shiny, double... args) {
      int totalPoints = 48;

      for(int i = 1; i <= totalPoints; ++i) {
         double theta = 6.283185307179586 / (double)totalPoints;
         double angle = theta * (double)i;
         double radius = 4.0;
         double dx = -(radius * Math.cos(angle));
         double dz = -(radius * Math.sin(angle));
         mc.field_71452_i.func_78873_a(new ParticleArcanery(w, x + dx, y + 0.025, z + dz, dz / 6.0, 0.0, -dx / 6.0, new CycloneBlob(0.35, angle, 0.01, radius, args[0])));
      }

   }
}
