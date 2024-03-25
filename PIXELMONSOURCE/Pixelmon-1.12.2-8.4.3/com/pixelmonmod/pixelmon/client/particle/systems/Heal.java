package com.pixelmonmod.pixelmon.client.particle.systems;

import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.ParticleSystem;
import com.pixelmonmod.pixelmon.client.particle.particles.SmallRising;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Heal extends ParticleSystem {
   @SideOnly(Side.CLIENT)
   public void execute(Minecraft mc, World w, double x, double y, double z, float scale, boolean shiny, double... args) {
      int totalPoints = 72;

      for(int i = 1; i <= totalPoints; ++i) {
         double theta = 6.283185307179586 / (double)totalPoints;
         double angle = theta * (double)i;
         double dx = 0.75 * Math.cos(angle);
         double dz = 0.75 * Math.sin(angle);

         for(int j = 1; j <= 6; ++j) {
            mc.field_71452_i.func_78873_a(new ParticleArcanery(w, x + dx, y, z + dz, 0.0, 0.0, 0.0, new SmallRising(1.0F, 1.0F, 0.0F, 0.5F)));
         }
      }

   }
}
