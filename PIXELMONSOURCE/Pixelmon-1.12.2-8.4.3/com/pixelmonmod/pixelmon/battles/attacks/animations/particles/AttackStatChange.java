package com.pixelmonmod.pixelmon.battles.attacks.animations.particles;

import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.attacks.animations.StandardParticleAnimationData;
import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.particles.AttackEffect;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumType;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AttackStatChange extends AttackSystemBase {
   static final int PARTICLES_PER_STAGE = 8;

   @SideOnly(Side.CLIENT)
   public void execute(Minecraft mc, World w, double x, double y, double z, float scale, boolean shiny, double... args) {
      EntityPixelmon p = (EntityPixelmon)w.func_73045_a(this.endID);
      float radius = p == null ? 2.0F : p.field_70130_N;
      double randomisation = (double)w.field_73012_v.nextFloat() * Math.PI * 2.0;
      double dTheta = 0.7853981633974483;
      float top = this.endPos[1] + 4.0F;
      float bot = this.endPos[1] + 0.25F;
      if (((StatChangeData)this.data).stages < 0) {
         float temp = top;
         top = bot;
         bot = temp;
      }

      for(int i = 0; i < Math.abs(((StatChangeData)this.data).stages); ++i) {
         for(int j = 0; j < 8; ++j) {
            float startX = (float)((double)this.endPos[0] + (double)radius * Math.cos(randomisation + dTheta * (double)j));
            float startZ = (float)((double)this.endPos[2] + (double)radius * Math.sin(randomisation + dTheta * (double)j));
            mc.field_71452_i.func_78873_a(new ParticleArcanery(w, (double)startX, (double)bot, (double)startZ, 0.0, 0.0, 0.0, ((StatChangeData)this.data).makeEffect(this).setNoise(0.0).setStartPos(startX, bot, startZ).setEndPos(startX, top, startZ).setHideTicks(i * 8, true)));
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

   public static class StatChangeData extends StandardParticleAnimationData {
      public StatsType stat;
      public int stages;

      public StatChangeData() {
      }

      public StatChangeData(StatsType stat, int stages) {
         this.stat = stat;
         this.stages = stages;
      }

      public EnumEffectType getEffectEnum() {
         return EnumEffectType.STAT_CHANGE;
      }

      public StatChangeData setStat(StatsType stat) {
         this.stat = stat;
         return this;
      }

      public StatChangeData setStages(int stages) {
         this.stages = stages;
         return this;
      }

      public void initFromAttack(AttackBase attack, int effectivePower, EnumType effectiveType) {
         if (this.speed == -1.0F) {
            this.speed = 0.15F;
         }

         if (this.stat == null) {
            this.stat = StatsType.Attack;
         }

         if (this.stages == 0) {
            this.stages = 1;
         }

         if (this.scale <= 0.0F) {
            this.scale = 0.16F;
         }

         if (this.rgb == null) {
            switch (this.stat) {
               case Attack:
                  this.rgb = new int[]{237, 165, 64};
                  break;
               case Defence:
                  this.rgb = new int[]{99, 171, 94};
                  break;
               case SpecialAttack:
                  this.rgb = new int[]{228, 32, 48};
                  break;
               case SpecialDefence:
                  this.rgb = new int[]{147, 194, 26};
                  break;
               case Speed:
                  this.rgb = new int[]{16, 168, 246};
                  break;
               case Evasion:
                  this.rgb = new int[]{206, 84, 236};
                  break;
               case Accuracy:
                  this.rgb = new int[]{255, 204, 255};
            }
         }

         if (this.texture == null) {
            this.texture = this.stages < 0 ? AttackEffect.EnumParticleTexture.DOWN_ARROW : AttackEffect.EnumParticleTexture.UP_ARROW;
         }

         super.initFromAttack(attack, effectivePower, effectiveType);
      }

      public void writeToByteBuffer(ByteBuf buf) {
         super.writeToByteBuffer(buf);
         buf.writeBoolean(this.stat != null);
         if (this.stat != null) {
            buf.writeByte(this.stat.ordinal());
         }

         buf.writeByte(this.stages);
      }

      public StatChangeData readFromByteBuffer(ByteBuf buf) {
         super.readFromByteBuffer(buf);
         if (buf.readBoolean()) {
            this.stat = StatsType.values()[buf.readByte()];
         }

         this.stages = buf.readByte();
         return this;
      }
   }
}
