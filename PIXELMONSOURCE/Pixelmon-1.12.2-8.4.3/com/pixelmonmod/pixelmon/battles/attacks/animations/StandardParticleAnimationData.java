package com.pixelmonmod.pixelmon.battles.attacks.animations;

import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.attacks.animations.particles.AttackSystemBase;
import com.pixelmonmod.pixelmon.client.particle.particles.AttackEffect;
import com.pixelmonmod.pixelmon.enums.EnumType;
import io.netty.buffer.ByteBuf;

public abstract class StandardParticleAnimationData extends AttackAnimationData {
   public int power = -1;
   public float speed = -1.0F;
   public int[] rgb = null;
   public AttackEffect.EnumParticleTexture texture = null;
   public int lifetimeTicks = -1;
   public float scale = -1.0F;

   public void initFromAttack(AttackBase attack, int effectivePower, EnumType effectiveType) {
      if (this.power == -1 || this.power == 0) {
         this.power = effectivePower == 0 ? 40 : effectivePower;
      }

      if (this.speed == -1.0F) {
         this.speed = 1.0F;
      }

      if (this.rgb == null) {
         this.rgb = new int[]{(effectiveType.getColor() & 16711680) >> 16, (effectiveType.getColor() & '\uff00') >> 8, effectiveType.getColor() & 255};
      }

      if (this.texture == null) {
         if (effectiveType == EnumType.Grass) {
            this.texture = AttackEffect.EnumParticleTexture.LEAF;
         } else if (effectiveType == EnumType.Fire) {
            this.texture = AttackEffect.EnumParticleTexture.FLAME;
         } else if (effectiveType == EnumType.Water) {
            this.texture = AttackEffect.EnumParticleTexture.WATER;
         } else {
            this.texture = AttackEffect.EnumParticleTexture.NORMAL;
         }
      }

      if (this.lifetimeTicks == -1) {
         this.lifetimeTicks = 0;
      }

      if (this.scale == -1.0F) {
         this.scale = 0.15F + 0.03F * (float)(this.power / 5);
      }

   }

   public void writeToByteBuffer(ByteBuf buf) {
      buf.writeBoolean(this.power != -1);
      if (this.power != -1) {
         buf.writeShort(this.power);
      }

      buf.writeBoolean(this.speed != -1.0F);
      if (this.speed != -1.0F) {
         buf.writeFloat(this.speed);
      }

      buf.writeBoolean(this.rgb != null);
      if (this.rgb != null) {
         buf.writeByte(this.rgb[0]);
         buf.writeByte(this.rgb[1]);
         buf.writeByte(this.rgb[2]);
      }

      buf.writeBoolean(this.texture != null);
      if (this.texture != null) {
         buf.writeByte(this.texture.ordinal());
      }

      buf.writeBoolean(this.lifetimeTicks != -1);
      if (this.lifetimeTicks != -1) {
         buf.writeByte(this.lifetimeTicks);
      }

      buf.writeBoolean(this.scale != -1.0F);
      if (this.scale != -1.0F) {
         buf.writeFloat(this.scale);
      }

   }

   public StandardParticleAnimationData readFromByteBuffer(ByteBuf buf) {
      if (buf.readBoolean()) {
         this.power = buf.readShort();
      }

      if (buf.readBoolean()) {
         this.speed = buf.readFloat();
      }

      if (buf.readBoolean()) {
         this.rgb = new int[]{buf.readUnsignedByte(), buf.readUnsignedByte(), buf.readUnsignedByte()};
      }

      if (buf.readBoolean()) {
         this.texture = AttackEffect.EnumParticleTexture.values()[buf.readByte()];
      }

      if (buf.readBoolean()) {
         this.lifetimeTicks = buf.readUnsignedByte();
      }

      if (buf.readBoolean()) {
         this.scale = buf.readFloat();
      }

      return this;
   }

   public AttackEffect makeEffect(AttackSystemBase system) {
      return new AttackEffect(system, system.startID, system.startPos, system.endID, system.endPos, (StandardParticleAnimationData)system.data);
   }

   public StandardParticleAnimationData setPower(int power) {
      this.power = power;
      return this;
   }

   public StandardParticleAnimationData setSpeed(float speed) {
      this.speed = speed;
      return this;
   }

   public StandardParticleAnimationData setRGB(int r, int g, int b) {
      this.rgb = new int[]{r, g, b};
      return this;
   }

   public StandardParticleAnimationData setTexture(AttackEffect.EnumParticleTexture texture) {
      this.texture = texture;
      return this;
   }

   public StandardParticleAnimationData setLifetimeTicks(int lifetimeTicks) {
      this.lifetimeTicks = lifetimeTicks;
      return this;
   }

   public StandardParticleAnimationData setScale(float scale) {
      this.scale = scale;
      return this;
   }
}
