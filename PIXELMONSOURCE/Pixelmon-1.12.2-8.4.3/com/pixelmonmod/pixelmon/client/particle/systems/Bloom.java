package com.pixelmonmod.pixelmon.client.particle.systems;

import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.ParticleSystem;
import com.pixelmonmod.pixelmon.client.particle.particles.Leaf;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Bloom extends ParticleSystem {
   @SideOnly(Side.CLIENT)
   public void execute(Minecraft mc, World w, double x, double y, double z, float scale, boolean shiny, double... args) {
      int totalPoints = 50;

      for(int i = 1; i <= totalPoints; ++i) {
         Random rand = new Random();
         mc.field_71452_i.func_78873_a(new ParticleArcanery(w, x + 0.2, y + 0.2, z + 0.2, rand.nextDouble() * 2.0 - 1.0, rand.nextDouble(), rand.nextDouble() * 2.0 - 1.0, new Leaf(0.85, scale, shiny)));
      }

   }
}
