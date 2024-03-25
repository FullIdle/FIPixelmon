package com.pixelmonmod.pixelmon.battles.attacks.animations.particles;

import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.attacks.animations.StandardParticleAnimationData;
import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.particles.AttackEffect;
import com.pixelmonmod.pixelmon.enums.EnumType;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AttackRise extends AttackSystemBase {
   @SideOnly(Side.CLIENT)
   public void execute(Minecraft mc, World w, double x, double y, double z, float scale, boolean shiny, double... args) {
      this.endPos[1] = (float)(w.func_73045_a(this.endID) == null ? (double)this.endPos[1] : w.func_73045_a(this.endID).field_70163_u);

      for(int i = 0; i < ((RiseData)this.data).particleAmount; ++i) {
         float radius = ((RiseData)this.data).radius * (float)Math.sqrt(Math.random());
         float theta = (float)(Math.random() * 2.0 * Math.PI);
         this.endPos[0] = (float)(x + (double)radius * Math.cos((double)theta));
         this.endPos[2] = (float)(z + (double)radius * Math.sin((double)theta));
         mc.field_71452_i.func_78873_a(new ParticleArcanery(w, (double)this.endPos[0], (double)this.endPos[1], (double)this.endPos[2], 0.0, 0.0, 0.0, ((RiseData)this.data).makeEffect(this).setHideTicks(((RiseData)this.data).particleAmount, true).setStartPos(this.endPos[0], this.endPos[1], this.endPos[2]).setEndPos(0.0F, 0.0F, 0.0F).setLifetimeTicks(((RiseData)this.data).durationTicks)));
      }

   }

   public void onConstruct(AttackEffect effect) {
      effect.mX = 0.0;
      effect.mY = 0.0;
      effect.mZ = 0.0;
   }

   public void onInit(ParticleArcanery particle, AttackEffect effect) {
      particle.setMotion(0.0, (double)((RiseData)this.data).speed, 0.0);
      effect.mX = 0.0;
      effect.mY = 0.0;
      effect.mZ = 0.0;
   }

   public void onEnable(ParticleArcanery particle, AttackEffect effect) {
      effect.mX = 0.0;
      effect.mY = 0.0;
      effect.mZ = 0.0;
   }

   public void onUpdate(ParticleArcanery particle, AttackEffect effect) {
      float height = (float)particle.getY();
      float maxHeight = this.startPos[1] + ((RiseData)this.data).endHeight;
      float yVelocity = (float)particle.getMotionY();
      if (yVelocity > 0.02F) {
         yVelocity += ((RiseData)this.data).acceleration / 100.0F;
      } else {
         yVelocity = 0.02F;
      }

      float alphaSubtraction = 0.002F / yVelocity;
      particle.func_82338_g(particle.getAlphaF() - alphaSubtraction);
      if (!(height > maxHeight) && (!(particle.getAlphaF() <= 0.0F) || ((RiseData)this.data).sparkle)) {
         particle.setMotion(0.0, (double)yVelocity, 0.0);
      } else {
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

   public static class RiseData extends StandardParticleAnimationData {
      public int durationTicks = 50;
      public int particleAmount = 15;
      public int startHeight = 0;
      public float acceleration = 0.1F;
      public float endHeight = 3.0F;
      public float radius = 1.0F;
      public boolean sparkle = false;

      public void initFromAttack(AttackBase attackBase, int effectivePower, EnumType effectiveType) {
         if (this.power == -1) {
            this.particleAmount = effectivePower / 2;
         }

         super.initFromAttack(attackBase, effectivePower, effectiveType);
         if (this.durationTicks == 0) {
            this.durationTicks = (int)Math.ceil(Math.sqrt((double)effectivePower) * 3.0);
         }

      }

      public void writeToByteBuffer(ByteBuf buf) {
         super.writeToByteBuffer(buf);
         buf.writeByte(this.durationTicks);
         buf.writeByte(this.startHeight);
         buf.writeFloat(this.endHeight);
         buf.writeBoolean(this.particleAmount != 13);
         if (this.particleAmount != 13) {
            buf.writeByte(this.particleAmount);
         }

         buf.writeBoolean(this.acceleration != 0.1F);
         if (this.acceleration != 0.1F) {
            buf.writeFloat(this.acceleration);
         }

         buf.writeBoolean(this.radius != 1.0F);
         if (this.radius != 1.0F) {
            buf.writeFloat(this.radius);
         }

         buf.writeBoolean(this.sparkle);
      }

      public RiseData readFromByteBuffer(ByteBuf buf) {
         super.readFromByteBuffer(buf);
         this.durationTicks = buf.readByte();
         this.startHeight = buf.readByte();
         this.endHeight = buf.readFloat();
         if (buf.readBoolean()) {
            this.particleAmount = buf.readByte();
         }

         if (buf.readBoolean()) {
            this.acceleration = buf.readFloat();
         }

         if (buf.readBoolean()) {
            this.radius = buf.readFloat();
         }

         this.sparkle = buf.readBoolean();
         return this;
      }

      public EnumEffectType getEffectEnum() {
         return EnumEffectType.RISE;
      }

      public RiseData setAcceleration(int acceleration) {
         this.acceleration = (float)acceleration;
         return this;
      }

      public RiseData setDurationTicks(int durationTicks) {
         this.durationTicks = durationTicks;
         return this;
      }

      public RiseData setEndHeight(float endHeight) {
         this.endHeight = endHeight;
         return this;
      }

      public RiseData setRadius(float radius) {
         this.radius = radius;
         return this;
      }

      public RiseData setParticleAmount(int particleAmount) {
         this.particleAmount = particleAmount;
         return this;
      }

      public RiseData setSparkle(boolean sparkle) {
         this.sparkle = sparkle;
         return this;
      }

      public RiseData setStartHeight(int startHeight) {
         this.startHeight = startHeight;
         return this;
      }
   }
}
