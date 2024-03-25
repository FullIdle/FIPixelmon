package com.pixelmonmod.pixelmon.battles.attacks.animations.particles;

import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.ParticleMathHelper;
import com.pixelmonmod.pixelmon.client.particle.ParticleSystem;
import com.pixelmonmod.pixelmon.client.particle.particles.AttackEffect;
import com.pixelmonmod.pixelmon.enums.EnumType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AttackSwarm extends ParticleSystem implements IAttackEffect {
   @SideOnly(Side.CLIENT)
   public void execute(Minecraft mc, World w, double x, double y, double z, float scale, boolean shiny, double... args) {
      int power = (int)args[0];
      int accuracy = (int)args[1];
      EnumType type = EnumType.values()[(int)args[2]];
      int category = (int)args[3];
      double endX = args[4];
      double endY = args[5];
      double endZ = args[6];
      int startID = (int)args[7];
      int endID = (int)args[8];

      for(int i = 0; i < power + 5; ++i) {
      }

   }

   public void onConstruct(AttackEffect effect) {
      effect.maxAge = 30 - effect.hiddenUntil;
   }

   public void onInit(ParticleArcanery particle, AttackEffect effect) {
      Vec3d vec = ParticleMathHelper.generatePointInSphere(0.2, effect.rand);
      particle.setMotion(effect.mX + vec.field_72450_a, effect.mY + vec.field_72448_b, effect.mZ + vec.field_72449_c);
   }

   public void onEnable(ParticleArcanery particle, AttackEffect effect) {
      Entity start = effect.mc.field_71441_e.func_73045_a(effect.startID);
      Entity end = effect.mc.field_71441_e.func_73045_a(effect.endID);
      if (start != null) {
         effect.startX = start.field_70165_t + (start.func_174813_aQ().field_72336_d - start.func_174813_aQ().field_72340_a) / 2.0;
         effect.startY = start.field_70163_u + (start.func_174813_aQ().field_72337_e - start.func_174813_aQ().field_72338_b) / 2.0;
         effect.startZ = start.field_70161_v + (start.func_174813_aQ().field_72334_f - start.func_174813_aQ().field_72339_c) / 2.0;
      }

      if (end != null) {
         effect.endX = end.field_70165_t + (end.func_174813_aQ().field_72336_d - end.func_174813_aQ().field_72340_a) / 2.0;
         effect.endY = end.field_70163_u + (end.func_174813_aQ().field_72337_e - end.func_174813_aQ().field_72338_b) / 2.0;
         effect.endZ = end.field_70161_v + (end.func_174813_aQ().field_72334_f - end.func_174813_aQ().field_72339_c) / 2.0;
      }

      effect.setHeading();
      particle.setPos(effect.startX, effect.startY, effect.startZ);
      particle.setPrevPos(effect.startX, effect.startY, effect.startZ);
      particle.setMotion(effect.mX, effect.mY, effect.mZ);
   }

   public void onUpdate(ParticleArcanery particle, AttackEffect effect) {
   }

   public void onTarget(ParticleArcanery particle, AttackEffect effect) {
   }

   public void onUpdateEol(ParticleArcanery particle, AttackEffect effect) {
      double ageLeft = 1.0 - (double)(particle.getAge() / Math.max(particle.getMaxAge(), 1));
      double eolRadius = 0.7 * ageLeft;
      Vec3d vec = ParticleMathHelper.generatePointInSphere(eolRadius, effect.rand);
      particle.setMotion(effect.mX + vec.field_72450_a, effect.mY + vec.field_72448_b, effect.mZ + vec.field_72449_c);
      particle.setRGBA(particle.func_70534_d(), particle.func_70542_f(), particle.func_70535_g(), particle.getAlphaF() * 0.95F);
   }

   public void onUpdateLast(ParticleArcanery particle, AttackEffect effect) {
      if (!effect.eol) {
         Vec3d vec = ParticleMathHelper.generatePointInSphere(0.7, effect.rand);
         particle.setMotion(effect.mX + vec.field_72450_a, effect.mY + vec.field_72448_b, effect.mZ + vec.field_72449_c);
      }

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
