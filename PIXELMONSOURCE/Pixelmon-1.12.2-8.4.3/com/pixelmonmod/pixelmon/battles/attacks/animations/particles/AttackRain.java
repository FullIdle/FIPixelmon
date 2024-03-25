package com.pixelmonmod.pixelmon.battles.attacks.animations.particles;

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

public class AttackRain extends AttackSystemBase {
   @SideOnly(Side.CLIENT)
   public void execute(Minecraft mc, World w, double x, double y, double z, float scale, boolean shiny, double... args) {
      float height = (float)((RainData)this.data).heightAboveTarget;
      EntityPixelmon pixelmon = (EntityPixelmon)w.func_73045_a(this.endID);
      height += pixelmon == null ? 2.0F : pixelmon.field_70131_O;
      int particlesPerWave = Math.round(1.0F * (float)((RainData)this.data).power / (float)((RainData)this.data).durationTicks);

      for(int tick = 0; tick < ((RainData)this.data).durationTicks; ++tick) {
         for(int i = 0; i < particlesPerWave; ++i) {
            AttackEffect effect = ((RainData)this.data).makeEffect(this).setHideTicks(tick + 1, true);
            double offsetX = 0.0;
            double offsetZ = 0.0;
            Vec3d startOffset = ParticleMathHelper.generatePointInSphere((double)(height / 1.5F), w.field_73012_v);
            double startOffsetX = startOffset.field_72450_a;
            double startOffsetZ = startOffset.field_72448_b;
            if (((RainData)this.data).maxHorizontalDisplacement > 0.0F) {
               Vec3d offset = ParticleMathHelper.generatePointInSphere((double)((RainData)this.data).maxHorizontalDisplacement, w.field_73012_v);
               offsetX = startOffsetX + offset.field_72450_a;
               offsetZ = startOffsetZ + offset.field_72448_b;
            } else {
               offsetX = startOffsetX;
               offsetZ = startOffsetZ;
            }

            effect.startX = effect.endX + startOffsetX;
            effect.startY = effect.endY + (double)height;
            effect.startZ = effect.endZ + startOffsetZ;
            effect.endX += offsetX;
            effect.endY = effect.startY - (double)height;
            effect.endZ += offsetZ;
            effect.setHeading();
            mc.field_71452_i.func_78873_a(new ParticleArcanery(w, effect.startX, effect.startY, effect.startZ, 0.0, 0.0, 0.0, effect));
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
   }

   public void onTarget(ParticleArcanery particle, AttackEffect effect) {
   }

   public void onUpdateEol(ParticleArcanery particle, AttackEffect effect) {
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

   public static class RainData extends StandardParticleAnimationData {
      public int durationTicks;
      public float maxHorizontalDisplacement = 0.0F;
      public int heightAboveTarget = 2;

      public void initFromAttack(AttackBase attackBase, int effectivePower, EnumType effectiveType) {
         if (this.power == -1) {
            this.power = effectivePower / 2;
         }

         super.initFromAttack(attackBase, effectivePower, effectiveType);
         if (this.durationTicks == 0) {
            this.durationTicks = (int)Math.ceil(Math.sqrt((double)effectivePower) * 3.0);
         }

      }

      public void writeToByteBuffer(ByteBuf buf) {
         super.writeToByteBuffer(buf);
         buf.writeByte(this.durationTicks);
         buf.writeBoolean(this.maxHorizontalDisplacement != 0.0F);
         if (this.maxHorizontalDisplacement != 0.0F) {
            buf.writeFloat(this.maxHorizontalDisplacement);
         }

         buf.writeByte(this.heightAboveTarget);
      }

      public RainData readFromByteBuffer(ByteBuf buf) {
         super.readFromByteBuffer(buf);
         this.durationTicks = buf.readByte();
         if (buf.readBoolean()) {
            this.maxHorizontalDisplacement = buf.readFloat();
         }

         this.heightAboveTarget = buf.readByte();
         return this;
      }

      public EnumEffectType getEffectEnum() {
         return EnumEffectType.RAIN;
      }

      public RainData setDurationTicks(int durationTicks) {
         this.durationTicks = durationTicks;
         return this;
      }

      public RainData setMaxHorizontalDisplacement(float maxHorizontalDisplacement) {
         this.maxHorizontalDisplacement = maxHorizontalDisplacement;
         return this;
      }

      public RainData setHeightAboveTarget(int heightAboveTarget) {
         this.heightAboveTarget = heightAboveTarget;
         return this;
      }
   }
}
