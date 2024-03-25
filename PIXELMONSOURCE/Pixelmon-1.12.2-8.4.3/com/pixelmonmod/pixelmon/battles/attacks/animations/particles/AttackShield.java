package com.pixelmonmod.pixelmon.battles.attacks.animations.particles;

import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.ParticleSystem;
import com.pixelmonmod.pixelmon.client.particle.particles.AttackEffect;
import com.pixelmonmod.pixelmon.enums.EnumType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AttackShield extends ParticleSystem implements IAttackEffect {
   @SideOnly(Side.CLIENT)
   public void execute(Minecraft mc, World w, double x, double y, double z, float scale, boolean shiny, double... args) {
      int power = (int)args[0];
      int accuracy = (int)args[1];
      EnumType type = EnumType.values()[(int)args[2]];
      int category = (int)args[3];
      double endX = args[4];
      double endY = args[5];
      double endZ = args[6];
      int startID = (int)args[7];
      int endID = (int)args[8];
      double dX = endX - x;
      double dY = endY - y;
      double dZ = endZ - z;
      double distanceFactor = 1.0 / Math.sqrt(dX * dX + dY * dY + dZ * dZ);
      dX *= distanceFactor;
      double var10000 = dY * distanceFactor;
      dZ *= distanceFactor;
      double rotateBy = Math.asin(dX / dZ);
      if (dZ == 0.0) {
         rotateBy = 0.0;
      }

      int totalPoints = 32;

      for(int i = 0; i < totalPoints; ++i) {
         double theta = 6.283185307179586 / (double)totalPoints;
         double angle = theta * (double)i;
         double dx = Math.cos(angle);
         double dy = Math.sin(angle);
         int delay = 0;

         for(double r = 0.75; r > 0.0; r -= 0.075) {
            var10000 = x + (x + dx * r + dX - x) * Math.cos(rotateBy) + (z + dZ - z) * Math.sin(rotateBy);
            var10000 = z - (x + dx * r + dX - x) * Math.sin(rotateBy) + (z + dZ - z) * Math.cos(rotateBy);
            ++delay;
         }
      }

   }

   public void onConstruct(AttackEffect effect) {
      effect.maxAge = 35;
   }

   public void onInit(ParticleArcanery particle, AttackEffect effect) {
      particle.setRGBA(effect.r, effect.g, effect.b, 0.0F);
      particle.setMotion(0.0, 0.0, 0.0);
      effect.mX = 0.0;
      effect.mY = 0.0;
      effect.mZ = 0.0;
   }

   public void onEnable(ParticleArcanery particle, AttackEffect effect) {
   }

   public void onUpdate(ParticleArcanery particle, AttackEffect effect) {
      ++effect.age;
      particle.setRGBA(effect.r, effect.g, effect.b, particle.getAlphaF() + 0.01F);
      if (effect.age >= effect.maxAge) {
         particle.func_187112_i();
      }

   }

   public void onTarget(ParticleArcanery particle, AttackEffect effect) {
   }

   public void onUpdateEol(ParticleArcanery particle, AttackEffect effect) {
      particle.setRGBA(particle.func_70534_d(), particle.func_70542_f(), particle.func_70535_g(), particle.getAlphaF() * 0.95F);
   }

   public void onUpdateLast(ParticleArcanery particle, AttackEffect effect) {
   }

   public void onPreRender(ParticleArcanery particle, float partialTicks, AttackEffect effect) {
   }

   public void onPostRender(ParticleArcanery particle, float partialTicks, AttackEffect effect) {
   }

   public boolean hasCustomRenderer(AttackEffect effect) {
      return false;
   }

   public void onRender(ParticleArcanery particle, Tessellator tessellator, float partialTicks, AttackEffect effect) {
   }
}
