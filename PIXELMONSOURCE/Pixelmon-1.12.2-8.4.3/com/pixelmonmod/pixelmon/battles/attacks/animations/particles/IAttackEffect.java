package com.pixelmonmod.pixelmon.battles.attacks.animations.particles;

import com.pixelmonmod.pixelmon.client.particle.ParticleArcanery;
import com.pixelmonmod.pixelmon.client.particle.particles.AttackEffect;
import net.minecraft.client.renderer.Tessellator;

public interface IAttackEffect {
   void onConstruct(AttackEffect var1);

   void onInit(ParticleArcanery var1, AttackEffect var2);

   void onEnable(ParticleArcanery var1, AttackEffect var2);

   void onUpdate(ParticleArcanery var1, AttackEffect var2);

   void onTarget(ParticleArcanery var1, AttackEffect var2);

   void onUpdateEol(ParticleArcanery var1, AttackEffect var2);

   void onUpdateLast(ParticleArcanery var1, AttackEffect var2);

   void onPreRender(ParticleArcanery var1, float var2, AttackEffect var3);

   void onPostRender(ParticleArcanery var1, float var2, AttackEffect var3);

   boolean hasCustomRenderer(AttackEffect var1);

   void onRender(ParticleArcanery var1, Tessellator var2, float var3, AttackEffect var4);
}
