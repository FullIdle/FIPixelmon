package com.pixelmonmod.pixelmon.battles.attacks.animations.particles;

import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.ParticleSystem;
import com.pixelmonmod.pixelmon.client.particle.particles.AttackEffect;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AttackDebuff extends ParticleSystem implements IAttackEffect {
   @SideOnly(Side.CLIENT)
   public void execute(Minecraft mc, World w, double x, double y, double z, float scale, boolean shiny, double... args) {
      EnumType type = EnumType.values()[(int)args[2]];
      int category = (int)args[3];
      int startID = (int)args[7];
      int endID = (int)args[8];
      x = args[4];
      y = args[5];
      z = args[6];
      double width = 2.0;
      double height = 2.0;
      EntityPixelmon e = (EntityPixelmon)w.func_73045_a(endID);
      if (e != null) {
         width = (double)(e.field_70130_N * 2.0F);
         height = (double)(e.field_70131_O + 3.0F);
      }

      for(int i = 0; i < 80; ++i) {
         double sX = w.field_73012_v.nextDouble() * width * 2.0 - width;
         double sY = w.field_73012_v.nextDouble() - 1.0;
         double var26 = w.field_73012_v.nextDouble() * width * 2.0 - width;
      }

   }

   public void onConstruct(AttackEffect effect) {
   }

   public void onInit(ParticleArcanery particle, AttackEffect effect) {
      particle.setRGBA(particle.func_70534_d(), particle.func_70542_f(), particle.func_70535_g(), 0.0F);
   }

   public void onEnable(ParticleArcanery particle, AttackEffect effect) {
   }

   public void onUpdate(ParticleArcanery particle, AttackEffect effect) {
      if (!effect.eol) {
         particle.setRGBA(particle.func_70534_d(), particle.func_70542_f(), particle.func_70535_g(), Math.min(1.0F, particle.getAlphaF() + 0.15F));
         if (effect.ticker > 5) {
            effect.eol = true;
         }
      }

   }

   public void onTarget(ParticleArcanery particle, AttackEffect effect) {
   }

   public void onUpdateEol(ParticleArcanery particle, AttackEffect effect) {
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
}
