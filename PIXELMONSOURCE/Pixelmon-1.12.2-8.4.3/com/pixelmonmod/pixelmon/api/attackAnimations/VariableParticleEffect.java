package com.pixelmonmod.pixelmon.api.attackAnimations;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.attacks.animations.AttackAnimationData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.PlayBattleParticleSystem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class VariableParticleEffect {
   public int dimension;
   public AttackAnimationData effectData;
   public int startEntityID = -1;
   public Vec3d startPos = new Vec3d(0.0, 0.0, 0.0);
   public int endEntityID = -1;
   public Vec3d endPos = new Vec3d(0.0, 0.0, 0.0);
   public AttackBase attackBase = (AttackBase)AttackBase.getAttackBaseFromEnglishName("Tackle").get();

   public VariableParticleEffect(int dimension, AttackAnimationData effectData) {
      this.dimension = dimension;
      this.effectData = effectData;
   }

   public VariableParticleEffect setStartPosition(EntityLivingBase entity) {
      this.startEntityID = entity.func_145782_y();
      this.startPos = entity.func_174791_d().func_178787_e(new Vec3d(0.0, (double)(entity.field_70131_O / 2.0F), 0.0));
      return this;
   }

   public VariableParticleEffect setStartPosition(Vec3d vec) {
      this.startPos = vec;
      return this;
   }

   public VariableParticleEffect setEndPosition(EntityLivingBase entity) {
      this.endEntityID = entity.func_145782_y();
      this.endPos = entity.func_174791_d();
      return this;
   }

   public VariableParticleEffect setEndPosition(Vec3d vec) {
      this.endPos = vec;
      return this;
   }

   public VariableParticleEffect setAttackBase(AttackBase attackBase) {
      this.attackBase = attackBase;
      return this;
   }

   private PlayBattleParticleSystem makePacket() {
      return new PlayBattleParticleSystem(this.dimension, this.effectData, this.attackBase, this.startEntityID, this.startPos, this.endEntityID, this.endPos);
   }

   public void showTo(EntityPlayerMP... players) {
      PlayBattleParticleSystem packet = this.makePacket();
      EntityPlayerMP[] var3 = players;
      int var4 = players.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EntityPlayerMP player = var3[var5];
         Pixelmon.network.sendTo(packet, player);
      }

   }

   public void showAllWithin(int range) {
      Vec3d use = this.startPos == Vec3d.field_186680_a ? this.endPos : this.startPos;
      PlayBattleParticleSystem packet = this.makePacket();
      Pixelmon.network.sendToAllAround(packet, new NetworkRegistry.TargetPoint(this.dimension, use.field_72450_a, use.field_72448_b, use.field_72449_c, (double)range));
   }
}
