package com.pixelmonmod.pixelmon.battles.attacks.animations.particles;

import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.attacks.animations.StandardParticleAnimationData;
import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.particles.AttackEffect;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumType;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AttackBuff extends AttackSystemBase {
   @SideOnly(Side.CLIENT)
   public void execute(Minecraft mc, World w, double x, double y, double z, float scale, boolean shiny, double... args) {
      double width = 2.0;
      EntityPixelmon e = (EntityPixelmon)w.func_73045_a(this.endID);
      if (e != null) {
         width = (double)(e.field_70130_N * 2.0F);
      }

      if (!((BuffData)this.data).atTarget && this.startID != this.endID) {
         this.endPos = this.startPos;
      }

      for(int i = 0; i < 80; ++i) {
         double sX = w.field_73012_v.nextDouble() * width * 2.0 - width;
         double sY = w.field_73012_v.nextDouble() * 0.4 - 1.0;
         double sZ = w.field_73012_v.nextDouble() * width * 2.0 - width;
         if (sX * sX + sY * sY + sZ * sZ <= width * width) {
            mc.field_71452_i.func_78873_a(new ParticleArcanery(w, (double)this.endPos[0] + sX, (double)this.endPos[1] + sY, (double)this.endPos[2] + sZ, 0.0, 0.0, 0.0, ((BuffData)this.data).makeEffect(this).setStartPos((double)this.endPos[0] + sX, (double)this.endPos[1] + sY, (double)this.endPos[2] + sZ).setHideTicks(w.field_73012_v.nextInt(20) + 1, true).setNoise(0.0)));
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
      particle.setRGBA(particle.func_70534_d(), particle.func_70542_f(), particle.func_70535_g(), particle.getAlphaF() * 0.92F);
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

   public static class BuffData extends StandardParticleAnimationData {
      boolean atTarget = true;

      public void initFromAttack(AttackBase attack, int effectivePower, EnumType effectiveType) {
         if (this.speed == -1.0F) {
            this.speed = 0.25F;
         }

         super.initFromAttack(attack, effectivePower, effectiveType);
      }

      public void writeToByteBuffer(ByteBuf buf) {
         super.writeToByteBuffer(buf);
         buf.writeBoolean(this.atTarget);
      }

      public BuffData readFromByteBuffer(ByteBuf buf) {
         super.readFromByteBuffer(buf);
         this.atTarget = buf.readBoolean();
         return this;
      }

      public EnumEffectType getEffectEnum() {
         return EnumEffectType.BUFF;
      }
   }
}
