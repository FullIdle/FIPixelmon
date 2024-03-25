package com.pixelmonmod.pixelmon.battles.attacks.animations.particles;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.attacks.animations.StandardParticleAnimationData;
import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.ParticleMathHelper;
import com.pixelmonmod.pixelmon.client.particle.particles.AttackEffect;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumType;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AttackBeam extends AttackSystemBase {
   @SideOnly(Side.CLIENT)
   public void execute(Minecraft mc, World w, double x, double y, double z, float scale, boolean shiny, double... args) {
      float[] trueStart = this.startPos;
      float trueVariation = ((BeamData)this.data).variation;
      if (((BeamData)this.data).inverted) {
         trueStart = this.endPos;
         this.endPos = this.startPos;
         this.startPos = trueStart;
         EntityPixelmon target = (EntityPixelmon)w.func_73045_a(this.endID);
         if (target == null) {
            trueVariation = 1.0F;
         } else {
            trueVariation = target.field_70131_O;
         }

         if (((BeamData)this.data).variation == -1.0F) {
            ((BeamData)this.data).variation = 0.7F;
         }
      }

      for(int segment = 1; segment <= ((BeamData)this.data).segments; ++segment) {
         for(int i = 0; i < ((BeamData)this.data).power / 10 + 5; ++i) {
            Vec3d vec = ParticleMathHelper.generatePointInSphere((double)trueVariation, w.field_73012_v);
            mc.field_71452_i.func_78873_a(new ParticleArcanery(w, (double)trueStart[0] + vec.field_72450_a, (double)trueStart[1] + vec.field_72448_b, (double)trueStart[2] + vec.field_72449_c, 0.0, 0.0, 0.0, ((BeamData)this.data).makeEffect(this).setStartPos((double)trueStart[0] + vec.field_72450_a, (double)trueStart[1] + vec.field_72448_b, (double)trueStart[2] + vec.field_72449_c).setHideTicks(segment, true).setNoise(0.0)));
         }
      }

   }

   public void onConstruct(AttackEffect effect) {
   }

   public void onInit(ParticleArcanery particle, AttackEffect effect) {
   }

   public void onEnable(ParticleArcanery particle, AttackEffect effect) {
   }

   public void onUpdate(ParticleArcanery particle, AttackEffect effect) {
      double completeFactor = effect.travelledDistance / effect.totalDistance;
      if (!(completeFactor > 1.0)) {
         particle.setPrevPos(particle.getX(), particle.getY(), particle.getZ());
         float newX = (float)(particle.getX() + ((double)this.startPos[0] + (double)(this.endPos[0] - this.startPos[0]) * completeFactor - particle.getX()) * (double)(1.0F - ((BeamData)this.data).variation));
         float newY = (float)(particle.getY() + ((double)this.startPos[1] + (double)(this.endPos[1] - this.startPos[1]) * completeFactor - particle.getY()) * (double)(1.0F - ((BeamData)this.data).variation));
         float newZ = (float)(particle.getZ() + ((double)this.startPos[2] + (double)(this.endPos[2] - this.startPos[2]) * completeFactor - particle.getZ()) * (double)(1.0F - ((BeamData)this.data).variation));
         particle.setPos((double)newX, (double)newY, (double)newZ);
         effect.travelledDistance += Math.abs(Math.sqrt(Math.pow((double)(newX - this.endPos[0]), 2.0) + Math.pow((double)(newY - this.endPos[1]), 2.0) + Math.pow((double)(newZ - this.endPos[2]), 2.0)) - Math.sqrt(Math.pow(particle.getPrevX() - (double)this.endPos[0], 2.0) + Math.pow(particle.getPrevY() - (double)this.endPos[1], 2.0) + Math.pow(particle.getPrevZ() - (double)this.endPos[2], 2.0)));
      }
   }

   public void onTarget(ParticleArcanery particle, AttackEffect effect) {
      if (!((BeamData)this.data).inverted) {
         particle.setMotion(effect.rand.nextDouble() - 0.5, effect.rand.nextDouble() - 0.5, effect.rand.nextDouble() - 0.5);
         particle.setMotion(particle.getMotionX() * 0.05, particle.getMotionY() * 0.05, particle.getMotionZ() * 0.05);
      } else {
         particle.func_187112_i();
      }

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

   public static class BeamData extends StandardParticleAnimationData {
      public int segments = 15;
      public float variation = -1.0F;
      public boolean inverted = false;

      public void initFromAttack(AttackBase attack, int effectivePower, EnumType effectiveType) {
         if (this.speed == -1.0F) {
            this.speed = 0.75F + (RandomHelper.rand.nextFloat() - 0.5F) / 5.0F;
         }

         if (this.variation == -1.0F && !this.inverted) {
            this.variation = 0.25F;
         }

         super.initFromAttack(attack, effectivePower, effectiveType);
      }

      public void writeToByteBuffer(ByteBuf buf) {
         buf.writeByte(this.segments);
         buf.writeBoolean(this.inverted);
         buf.writeBoolean(this.variation != -1.0F);
         if (this.variation != -1.0F) {
            buf.writeFloat(this.variation);
         }

         super.writeToByteBuffer(buf);
      }

      public BeamData readFromByteBuffer(ByteBuf buf) {
         this.segments = buf.readUnsignedByte();
         this.inverted = buf.readBoolean();
         if (buf.readBoolean()) {
            this.variation = buf.readFloat();
         }

         return (BeamData)super.readFromByteBuffer(buf);
      }

      public EnumEffectType getEffectEnum() {
         return EnumEffectType.BEAM;
      }

      public BeamData setSegments(int segments) {
         this.segments = segments;
         return this;
      }

      public BeamData setVariation(float variation) {
         this.variation = variation;
         return this;
      }

      public BeamData setInverted(boolean inverted) {
         this.inverted = inverted;
         return this;
      }
   }
}
