package com.pixelmonmod.pixelmon.battles.attacks.animations.particles;

import com.pixelmonmod.pixelmon.RandomHelper;
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

public class AttackTube extends AttackSystemBase {
   @SideOnly(Side.CLIENT)
   public void execute(Minecraft mc, World w, double x, double y, double z, float scale, boolean shiny, double... args) {
      this.endPos[1] = (float)(w.func_73045_a(this.endID) == null ? (double)this.endPos[1] : w.func_73045_a(this.endID).field_70163_u);

      for(int segment = 1; segment < ((TubeData)this.data).segments; ++segment) {
         for(int i = 0; i < ((TubeData)this.data).power; ++i) {
            mc.field_71452_i.func_78873_a(new ParticleArcanery(w, (double)this.endPos[0], (double)this.endPos[1], (double)this.endPos[2], 0.0, 0.0, 0.0, ((TubeData)this.data).makeEffect(this).setHideTicks(segment, true).setStartPos(this.endPos[0], this.endPos[1], this.endPos[2]).setEndPos(0.0F, 0.0F, 0.0F).setLifetimeTicks(RandomHelper.getRandomNumberBetween(0, 360))));
         }
      }

   }

   public void onConstruct(AttackEffect effect) {
      effect.mX = 0.0;
      effect.mY = 0.0;
      effect.mZ = 0.0;
   }

   public void onInit(ParticleArcanery particle, AttackEffect effect) {
      particle.setMotion(0.0, 0.0, 0.0);
      effect.mX = 0.0;
      effect.mY = 0.0;
      effect.mZ = 0.0;
   }

   public float ticksVisible(AttackEffect effect) {
      return (float)(effect.ticker - effect.hiddenUntil);
   }

   public void onEnable(ParticleArcanery particle, AttackEffect effect) {
      effect.mX = 0.0;
      effect.mY = 0.0;
      effect.mZ = 0.0;
   }

   public void onUpdate(ParticleArcanery particle, AttackEffect effect) {
      float ticksVisible = this.ticksVisible(effect);
      if (!(ticksVisible <= 0.0F)) {
         float height = this.startPos[1] + ticksVisible * ((TubeData)this.data).speed / 20.0F * 2.0F;
         if (height + 1.0F > this.startPos[1] + ((TubeData)this.data).height) {
            particle.setRGBA(particle.func_70534_d(), particle.func_70542_f(), particle.func_70535_g(), particle.getAlphaF() * 0.9F);
            if (height > this.startPos[1] + ((TubeData)this.data).height) {
               particle.func_187112_i();
               return;
            }
         }

         float radius = ((TubeData)this.data).radius;
         if (((TubeData)this.data).conical) {
            radius = Math.min(((TubeData)this.data).radius, ((TubeData)this.data).apexRadius + ticksVisible * ((TubeData)this.data).speed / 20.0F / ((TubeData)this.data).narrowness);
         }

         float theta = (ticksVisible * ((TubeData)this.data).speed * 18.0F + 360.0F * (1.0F * (float)effect.maxAge / (float)((TubeData)this.data).power + 1.0F * (float)effect.hiddenUntil / (float)((TubeData)this.data).segments)) % 360.0F;
         float x = this.endPos[0] + radius * (float)Math.cos(0.017453292519943295 * (double)theta);
         float z = this.endPos[2] + radius * (float)Math.sin(0.017453292519943295 * (double)theta);
         particle.setPos((double)x, (double)height, (double)z);
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

   public static class TubeData extends StandardParticleAnimationData {
      public int segments = 13;
      public float height = 2.0F;
      public float radius = 2.0F;
      public boolean conical = true;
      public float narrowness = 1.0F;
      public float apexRadius = 0.75F;

      public void writeToByteBuffer(ByteBuf buf) {
         super.writeToByteBuffer(buf);
         buf.writeBoolean(this.segments != 13);
         if (this.segments != 13) {
            buf.writeByte(this.segments);
         }

         buf.writeBoolean(this.height != 2.0F);
         if (this.height != 2.0F) {
            buf.writeFloat(this.height);
         }

         buf.writeBoolean(this.radius != 1.0F);
         if (this.radius != 1.0F) {
            buf.writeFloat(this.radius);
         }

         buf.writeBoolean(this.conical);
         if (this.conical) {
            buf.writeBoolean(this.narrowness != 1.0F);
            if (this.narrowness != 1.0F) {
               buf.writeFloat(this.narrowness);
            }

            buf.writeBoolean(this.apexRadius != 0.0F);
            if (this.apexRadius != 0.0F) {
               buf.writeFloat(this.apexRadius);
            }
         }

      }

      public StandardParticleAnimationData readFromByteBuffer(ByteBuf buf) {
         super.readFromByteBuffer(buf);
         if (buf.readBoolean()) {
            this.segments = buf.readByte();
         }

         if (buf.readBoolean()) {
            this.height = buf.readFloat();
         }

         if (buf.readBoolean()) {
            this.radius = buf.readFloat();
         }

         this.conical = buf.readBoolean();
         if (this.conical) {
            if (buf.readBoolean()) {
               this.narrowness = buf.readFloat();
            }

            if (buf.readBoolean()) {
               this.apexRadius = buf.readFloat();
            }
         }

         return this;
      }

      public void initFromAttack(AttackBase attack, int effectivePower, EnumType effectiveType) {
         if (this.speed == -1.0F) {
            this.speed = (float)(1.0 + (RandomHelper.rand.nextDouble() - 0.5) / 5.0);
         }

         if (this.power == -1) {
            this.power = 6;
         }

         super.initFromAttack(attack, effectivePower, effectiveType);
      }

      public EnumEffectType getEffectEnum() {
         return EnumEffectType.TUBE;
      }

      public TubeData setSegments(int segments) {
         this.segments = segments;
         return this;
      }

      public TubeData setHeight(float height) {
         this.height = height;
         return this;
      }

      public TubeData setRadius(float radius) {
         this.radius = radius;
         return this;
      }

      public TubeData setConical(boolean conical) {
         this.conical = conical;
         return this;
      }

      public TubeData setNarrowness(float narrowness) {
         this.narrowness = narrowness;
         return this;
      }

      public TubeData setApexRadius(float apexRadius) {
         this.apexRadius = apexRadius;
         return this;
      }
   }
}
