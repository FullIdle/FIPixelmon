package com.pixelmonmod.pixelmon.client.particle.systems;

import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.ParticleSystem;
import com.pixelmonmod.pixelmon.client.particle.particles.Electric;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Discharge extends ParticleSystem {
   @SideOnly(Side.CLIENT)
   public void execute(Minecraft mc, World w, double x, double y, double z, float scale, boolean shiny, double... args) {
      for(int i = 0; i < 200; ++i) {
         double posX = x - 0.5 + w.field_73012_v.nextDouble();
         double posY = y + 1.0 + w.field_73012_v.nextDouble();
         double posZ = z - 0.5 + w.field_73012_v.nextDouble();
         ParticleArcanery parent = new ParticleArcanery(w, posX, posY, posZ, posX, posY, posZ, new Electric(13 + w.field_73012_v.nextInt(4), true, w.field_73012_v.nextFloat() * 360.0F, w.field_73012_v.nextFloat() * 360.0F, 1.0F, 0.5F, args[3] == 1.0 ? w.field_73012_v.nextFloat() : (float)args[0], args[3] == 1.0 ? w.field_73012_v.nextFloat() : (float)args[1], args[3] == 1.0 ? w.field_73012_v.nextFloat() : (float)args[2]));
         Minecraft.func_71410_x().field_71452_i.func_78873_a(parent);
      }

   }
}
