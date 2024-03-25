package com.pixelmonmod.pixelmon.battles.attacks.animations.particles;

import com.pixelmonmod.pixelmon.battles.attacks.animations.StandardParticleAnimationData;
import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.particles.AttackEffect;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AttackRadial extends AttackSystemBase {
   @SideOnly(Side.CLIENT)
   public void execute(Minecraft mc, World w, double x, double y, double z, float scale, boolean shiny, double... args) {
      for(int i = 0; i < ((RadialData)this.data).totalPoints; ++i) {
         double theta = Math.PI * (double)(i * 360 / ((RadialData)this.data).totalPoints) / 180.0;
         double dx = Math.cos(theta) * (((RadialData)this.data).radius == -1 ? Math.sqrt((double)((RadialData)this.data).power) : (double)((RadialData)this.data).radius);
         double dz = Math.sin(theta) * (((RadialData)this.data).radius == -1 ? Math.sqrt((double)((RadialData)this.data).power) : (double)((RadialData)this.data).radius);
         mc.field_71452_i.func_78873_a(new ParticleArcanery(w, x, y, z, 0.0, 0.0, 0.0, ((RadialData)this.data).makeEffect(this).setEndPos(x + dx, y, z + dz)));
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

   public static class RadialData extends StandardParticleAnimationData {
      public int totalPoints = 36;
      public int radius = -1;

      public EnumEffectType getEffectEnum() {
         return EnumEffectType.RADIAL;
      }

      public void writeToByteBuffer(ByteBuf buf) {
         buf.writeBoolean(this.totalPoints != 36);
         if (this.totalPoints != 36) {
            buf.writeByte(this.totalPoints);
         }

         buf.writeBoolean(this.radius != -1);
         if (this.radius != -1) {
            buf.writeByte(this.radius);
         }

         super.writeToByteBuffer(buf);
      }

      public RadialData readFromByteBuffer(ByteBuf buf) {
         if (buf.readBoolean()) {
            this.totalPoints = buf.readUnsignedByte();
         }

         if (buf.readBoolean()) {
            this.radius = buf.readUnsignedByte();
         }

         return (RadialData)super.readFromByteBuffer(buf);
      }

      public RadialData setTotalPoints(int totalPoints) {
         this.totalPoints = totalPoints;
         return this;
      }

      public RadialData setRadius(int radius) {
         this.radius = radius;
         return this;
      }
   }
}
