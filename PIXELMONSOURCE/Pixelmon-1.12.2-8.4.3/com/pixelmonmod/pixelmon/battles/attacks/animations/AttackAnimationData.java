package com.pixelmonmod.pixelmon.battles.attacks.animations;

import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.attacks.animations.particles.AttackBall;
import com.pixelmonmod.pixelmon.battles.attacks.animations.particles.AttackBeam;
import com.pixelmonmod.pixelmon.battles.attacks.animations.particles.AttackBuff;
import com.pixelmonmod.pixelmon.battles.attacks.animations.particles.AttackExplosion;
import com.pixelmonmod.pixelmon.battles.attacks.animations.particles.AttackRadial;
import com.pixelmonmod.pixelmon.battles.attacks.animations.particles.AttackRain;
import com.pixelmonmod.pixelmon.battles.attacks.animations.particles.AttackSystemBase;
import com.pixelmonmod.pixelmon.battles.attacks.animations.particles.AttackTube;
import com.pixelmonmod.pixelmon.battles.attacks.animations.particles.EnumEffectType;
import com.pixelmonmod.pixelmon.enums.EnumType;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class AttackAnimationData {
   public abstract EnumEffectType getEffectEnum();

   public abstract void initFromAttack(AttackBase var1, int var2, EnumType var3);

   public abstract void writeToByteBuffer(ByteBuf var1);

   public abstract AttackAnimationData readFromByteBuffer(ByteBuf var1);

   @SideOnly(Side.CLIENT)
   public void createSystem(Minecraft mc, int startID, float[] startPos, int endID, float[] endPos) {
      ((AttackSystemBase)this.getEffectEnum().systemSupplier.get()).setData(this, startID, startPos, endID, endPos).execute(mc, mc.field_71441_e);
   }

   public static AttackBall.BallData ball() {
      return new AttackBall.BallData();
   }

   public static AttackBeam.BeamData beam() {
      return new AttackBeam.BeamData();
   }

   public static AttackBuff.BuffData buff() {
      return new AttackBuff.BuffData();
   }

   public static AttackExplosion.ExplosionData explosion() {
      return new AttackExplosion.ExplosionData();
   }

   public static AttackRadial.RadialData radial() {
      return new AttackRadial.RadialData();
   }

   public static AttackRain.RainData rain() {
      return new AttackRain.RainData();
   }

   public static AttackTube.TubeData tube() {
      return new AttackTube.TubeData();
   }
}
