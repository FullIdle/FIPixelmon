package com.pixelmonmod.pixelmon.client.particle;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class ParticleEffect {
   public abstract void init(ParticleArcanery var1, World var2, double var3, double var5, double var7, double var9, double var11, double var13, float var15);

   public abstract void update(ParticleArcanery var1);

   public abstract ResourceLocation texture();

   public void render(ParticleArcanery particle, Tessellator tessellator, float partialTicks) {
   }

   public void preRender(ParticleArcanery particle, float partialTicks) {
   }

   public void postRender(ParticleArcanery particle, float partialTicks) {
   }

   public boolean customRenderer() {
      return false;
   }
}
