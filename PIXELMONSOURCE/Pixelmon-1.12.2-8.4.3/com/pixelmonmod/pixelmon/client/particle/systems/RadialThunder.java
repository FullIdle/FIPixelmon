package com.pixelmonmod.pixelmon.client.particle.systems;

import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.ParticleSystem;
import com.pixelmonmod.pixelmon.client.particle.particles.Electric;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RadialThunder extends ParticleSystem {
   @SideOnly(Side.CLIENT)
   public void execute(Minecraft mc, World w, double x, double y, double z, float scale, boolean shiny, double... args) {
      double radius = 4.5;

      for(int i = 0; i < 1000; ++i) {
         double posX = x + 0.5 + w.field_73012_v.nextDouble() * radius * 2.0 - radius;
         double posY = y + w.field_73012_v.nextDouble() - 0.5;
         double posZ = z + 0.5 + w.field_73012_v.nextDouble() * radius * 2.0 - radius;
         if (Math.sqrt((x - posX) * (x - posX) + (z - posZ) * (z - posZ)) <= radius) {
            ParticleArcanery parent = new ParticleArcanery(w, posX, posY, posZ, posX, posY + 8.0, posZ, new Electric(5 + w.field_73012_v.nextInt(10), false, -90.0F, 0.0F, 6.0F, 0.0F, (float)args[0], (float)args[1], (float)args[2]));
            Minecraft.func_71410_x().field_71452_i.func_78873_a(parent);
         }
      }

   }
}
