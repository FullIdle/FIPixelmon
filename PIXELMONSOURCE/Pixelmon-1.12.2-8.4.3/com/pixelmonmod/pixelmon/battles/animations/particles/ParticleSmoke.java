package com.pixelmonmod.pixelmon.battles.animations.particles;

import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleSmoke extends ParticleSmokeNormal {
   public ParticleSmoke(World par1World, double par2, double par4, double par6, double par8, double par10, double par12) {
      super(par1World, par2, par4, par6, par8, par10, par12, 1.0F);
   }

   public void func_187114_a(int i) {
      this.field_70547_e = i;
   }

   public int maxAge() {
      return this.field_70547_e;
   }

   public int currentAge() {
      return this.field_70546_d;
   }
}
