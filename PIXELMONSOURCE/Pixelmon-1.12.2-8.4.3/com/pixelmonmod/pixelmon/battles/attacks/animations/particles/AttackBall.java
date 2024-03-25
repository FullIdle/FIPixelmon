package com.pixelmonmod.pixelmon.battles.attacks.animations.particles;

import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.attacks.animations.StandardParticleAnimationData;
import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.particles.AttackEffect;
import com.pixelmonmod.pixelmon.enums.EnumType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AttackBall extends AttackSystemBase {
   @SideOnly(Side.CLIENT)
   public void execute(Minecraft mc, World w, double x, double y, double z, float scale, boolean shiny, double... args) {
      for(int i = 0; i < ((BallData)this.data).power; ++i) {
         mc.field_71452_i.func_78873_a(new ParticleArcanery(w, x, y, z, 0.0, 0.0, 0.0, ((BallData)this.data).makeEffect(this)));
      }

   }

   public void onConstruct(AttackEffect effect) {
   }

   public void onInit(ParticleArcanery particle, AttackEffect effect) {
   }

   public void onEnable(ParticleArcanery particle, AttackEffect effect) {
   }

   public void onUpdate(ParticleArcanery particle, AttackEffect effect) {
   }

   public void onTarget(ParticleArcanery particle, AttackEffect effect) {
      particle.setMotion(effect.rand.nextDouble() - 0.5, effect.rand.nextDouble() - 0.5, effect.rand.nextDouble() - 0.5);
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

   public static class BallData extends StandardParticleAnimationData {
      public EnumEffectType getEffectEnum() {
         return EnumEffectType.BALL;
      }

      public void initFromAttack(AttackBase attackBase, int effectivePower, EnumType effectiveType) {
         if (this.speed == -1.0F) {
            this.speed = 0.8F;
         }

         if (this.power <= 0) {
            this.power = 10;
         }

         super.initFromAttack(attackBase, effectivePower, effectiveType);
      }
   }
}
