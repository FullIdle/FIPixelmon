package com.pixelmonmod.pixelmon.battles.attacks.animations.particles;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.attacks.animations.StandardParticleAnimationData;
import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.ParticleMathHelper;
import com.pixelmonmod.pixelmon.client.particle.particles.AttackEffect;
import com.pixelmonmod.pixelmon.enums.EnumType;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AttackExplosion extends AttackSystemBase {
   @SideOnly(Side.CLIENT)
   public void execute(Minecraft mc, World w, double x, double y, double z, float scale, boolean shiny, double... args) {
      for(int i = 0; i < ((ExplosionData)this.data).power; ++i) {
         mc.field_71452_i.func_78873_a(new ParticleArcanery(w, ((ExplosionData)this.data).atTarget ? (double)this.endPos[0] : x, ((ExplosionData)this.data).atTarget ? (double)this.endPos[1] : y, ((ExplosionData)this.data).atTarget ? (double)this.endPos[2] : z, 0.0, 0.0, 0.0, ((ExplosionData)this.data).makeEffect(this).setNoise(30.0)));
      }

   }

   public void onConstruct(AttackEffect effect) {
      Vec3d vec = ParticleMathHelper.generatePointInSphere(effect.speed, effect.rand);
      effect.mX = vec.field_72450_a;
      effect.mY = vec.field_72448_b;
      effect.mZ = vec.field_72449_c;
   }

   public void onInit(ParticleArcanery particle, AttackEffect effect) {
      particle.setScale(2.0F);
   }

   public void onEnable(ParticleArcanery particle, AttackEffect effect) {
   }

   public void onUpdate(ParticleArcanery particle, AttackEffect effect) {
      if (!effect.eol) {
         if (effect.ticker > 5) {
            effect.eol = true;
         }

         particle.setRGBA(particle.func_70534_d(), particle.func_70542_f(), particle.func_70535_g(), particle.getAlphaF() * 0.99F);
      }

   }

   public void onTarget(ParticleArcanery particle, AttackEffect effect) {
   }

   public void onUpdateEol(ParticleArcanery particle, AttackEffect effect) {
      particle.setMotion(particle.getMotionX() * 0.95, particle.getMotionY() * 0.95, particle.getMotionZ() * 0.95);
      particle.setRGBA(particle.func_70534_d(), particle.func_70542_f(), particle.func_70535_g(), particle.getAlphaF() * 0.8F);
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

   public static class ExplosionData extends StandardParticleAnimationData {
      public boolean atTarget = false;

      public void initFromAttack(AttackBase attack, int effectivePower, EnumType effectiveType) {
         if (this.speed == -1.0F) {
            this.speed = 1.2F + RandomHelper.rand.nextFloat();
         }

         super.initFromAttack(attack, effectivePower, effectiveType);
      }

      public void writeToByteBuffer(ByteBuf buf) {
         super.writeToByteBuffer(buf);
         buf.writeBoolean(this.atTarget);
      }

      public ExplosionData readFromByteBuffer(ByteBuf buf) {
         super.readFromByteBuffer(buf);
         this.atTarget = buf.readBoolean();
         return this;
      }

      public EnumEffectType getEffectEnum() {
         return EnumEffectType.EXPLOSION;
      }
   }
}
