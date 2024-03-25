package com.pixelmonmod.pixelmon.client.particle.systems;

import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.ParticleSystem;
import com.pixelmonmod.pixelmon.client.particle.particles.RedOrbShrinking;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Drain extends ParticleSystem {
   @SideOnly(Side.CLIENT)
   public void execute(Minecraft mc, World w, double x, double y, double z, float scale, boolean shiny, double... args) {
      int totalPoints = 3;

      for(int i = 1; i <= totalPoints; ++i) {
         Random rand = new Random();
         mc.field_71452_i.func_78873_a(new ParticleArcanery(w, x + rand.nextDouble() - 0.5, y + rand.nextDouble() - 0.5, z + rand.nextDouble() - 0.5, 0.0, 0.0, 0.0, new RedOrbShrinking(args[0], args[1], args[2])));
      }

   }
}
