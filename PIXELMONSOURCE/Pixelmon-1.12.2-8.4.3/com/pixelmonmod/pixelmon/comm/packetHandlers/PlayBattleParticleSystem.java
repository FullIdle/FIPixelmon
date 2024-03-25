package com.pixelmonmod.pixelmon.comm.packetHandlers;

import com.pixelmonmod.pixelmon.api.attackAnimations.AttackAnimation;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.attacks.animations.AttackAnimationData;
import com.pixelmonmod.pixelmon.battles.attacks.animations.particles.EnumEffectType;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.battle.AttackCategory;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PlayBattleParticleSystem implements IMessage {
   private AttackAnimationData effect;
   private AttackBase attackBase;
   private int dimension;
   private int power;
   private byte effectiveType;
   private int accuracy;
   private float startX;
   private float startY;
   private float startZ;
   private float endX;
   private float endY;
   private float endZ;
   private int startID;
   private int endID;

   public PlayBattleParticleSystem() {
   }

   public PlayBattleParticleSystem(AttackAnimation animation, Attack attack, AttackAnimationData effect, boolean groundedStartPosition, boolean groundedEndPosition) {
      this.dimension = animation.dimension;
      this.startID = animation.startID;
      this.startX = animation.user.entity == null ? animation.userPos[0] : (float)animation.user.entity.field_70165_t;
      this.startY = animation.user.entity == null ? animation.userPos[1] : (groundedStartPosition ? (float)animation.user.entity.field_70163_u : (float)animation.user.entity.getYCentre());
      this.startZ = animation.user.entity == null ? animation.userPos[2] : (float)animation.user.entity.field_70161_v;
      this.endID = animation.endID;
      this.endX = animation.target.entity == null ? animation.targetPos[0] : (float)animation.target.entity.field_70165_t;
      this.endY = animation.target.entity == null ? animation.targetPos[1] : (groundedEndPosition ? (float)animation.target.entity.field_70163_u : (float)animation.target.entity.getYCentre());
      this.endZ = animation.target.entity == null ? animation.targetPos[2] : (float)animation.target.entity.field_70161_v;
      this.power = attack.getAttackCategory() == AttackCategory.STATUS ? 50 : attack.movePower;
      this.effectiveType = (byte)animation.effectiveType.ordinal();
      this.accuracy = attack.moveAccuracy;
      this.effect = effect;
      this.attackBase = attack.getMove();
   }

   public PlayBattleParticleSystem(int dimension, AttackAnimationData effect, AttackBase ab, int startID, Vec3d startPos, int endID, Vec3d endPos) {
      this.dimension = dimension;
      this.startID = startID;
      this.startX = (float)startPos.field_72450_a;
      this.startY = (float)startPos.field_72448_b;
      this.startZ = (float)startPos.field_72449_c;
      this.endID = endID;
      this.endX = (float)endPos.field_72450_a;
      this.endY = (float)endPos.field_72448_b;
      this.endZ = (float)endPos.field_72449_c;
      this.power = ab.getAttackCategory() == AttackCategory.STATUS ? 50 : ab.getBasePower();
      this.effectiveType = (byte)ab.getAttackType().ordinal();
      this.accuracy = ab.getAccuracy();
      this.effect = effect;
      this.attackBase = ab;
   }

   public void fromBytes(ByteBuf buf) {
      this.dimension = buf.readShort();
      this.power = buf.readShort();
      this.effectiveType = buf.readByte();
      this.accuracy = buf.readShort();
      this.startX = buf.readFloat();
      this.startY = buf.readFloat();
      this.startZ = buf.readFloat();
      this.endX = buf.readFloat();
      this.endY = buf.readFloat();
      this.endZ = buf.readFloat();
      this.startID = buf.readInt();
      this.endID = buf.readInt();
      this.effect = ((AttackAnimationData)EnumEffectType.values()[buf.readByte()].dataSupplier.get()).readFromByteBuffer(buf);
      this.attackBase = (AttackBase)AttackBase.getAttackBase(buf.readShort()).get();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeShort(this.dimension);
      buf.writeShort(this.power);
      buf.writeByte(this.effectiveType);
      buf.writeShort(this.accuracy);
      buf.writeFloat(this.startX);
      buf.writeFloat(this.startY);
      buf.writeFloat(this.startZ);
      buf.writeFloat(this.endX);
      buf.writeFloat(this.endY);
      buf.writeFloat(this.endZ);
      buf.writeInt(this.startID);
      buf.writeInt(this.endID);
      buf.writeByte(this.effect.getEffectEnum().ordinal());
      this.effect.writeToByteBuffer(buf);
      buf.writeShort(this.attackBase.getAttackId());
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(PlayBattleParticleSystem message, MessageContext ctx) {
         handlePacket(message);
         return null;
      }

      @SideOnly(Side.CLIENT)
      private static void handlePacket(PlayBattleParticleSystem message) {
         if (Minecraft.func_71410_x().field_71441_e.field_73011_w.getDimension() == message.dimension) {
            message.effect.initFromAttack(message.attackBase, message.power, EnumType.values()[message.effectiveType]);
            Minecraft mc = Minecraft.func_71410_x();
            if (mc.field_71441_e.field_73011_w.getDimension() == message.dimension) {
               message.effect.createSystem(mc, message.startID, new float[]{message.startX, message.startY, message.startZ}, message.endID, new float[]{message.endX, message.endY, message.endZ});
            }
         }

      }
   }
}
