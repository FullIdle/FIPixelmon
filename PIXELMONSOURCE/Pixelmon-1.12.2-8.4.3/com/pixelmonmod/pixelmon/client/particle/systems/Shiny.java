package com.pixelmonmod.pixelmon.client.particle.systems;

import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.ParticleSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Shiny extends ParticleSystem {
   @SideOnly(Side.CLIENT)
   public void execute(Minecraft mc, World w, double x, double y, double z, float scale, boolean shiny, double... args) {
      mc.field_71452_i.func_78873_a(new ParticleArcanery(w, x, y, z, 0.0, 0.0, 0.0, new com.pixelmonmod.pixelmon.client.particle.particles.Shiny(args[0], args[1], args[2])));
   }
}
