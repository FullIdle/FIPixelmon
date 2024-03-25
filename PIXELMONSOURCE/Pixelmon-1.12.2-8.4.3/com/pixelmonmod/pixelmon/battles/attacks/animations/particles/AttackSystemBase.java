package com.pixelmonmod.pixelmon.battles.attacks.animations.particles;

import com.pixelmonmod.pixelmon.battles.attacks.animations.AttackAnimationData;
import com.pixelmonmod.pixelmon.client.particle.ParticleSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

public abstract class AttackSystemBase extends ParticleSystem implements IAttackEffect {
   public AttackAnimationData data;
   public int startID;
   public int endID;
   public float[] startPos;
   public float[] endPos;

   public AttackSystemBase setData(AttackAnimationData data, int startID, float[] startPos, int endID, float[] endPos) {
      this.data = data;
      this.startID = startID;
      this.endID = endID;
      this.startPos = startPos;
      this.endPos = endPos;
      return this;
   }

   public void execute(Minecraft mc, World w) {
      this.execute(mc, w, (double)this.startPos[0], (double)this.startPos[1], (double)this.startPos[2], 0.0F, false, new double[0]);
   }
}
