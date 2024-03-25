package com.pixelmonmod.pixelmon.api.attackAnimations;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.animations.AttackAnimationData;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.comm.packetHandlers.PlayBattleParticleSystem;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public abstract class AttackAnimation {
   public transient BattleControllerBase bc;
   public transient int ticks = -1;
   public transient int dimension = 0;
   public transient int startID;
   public transient PixelmonWrapper user;
   public transient float[] userPos;
   public transient int endID;
   public transient PixelmonWrapper target;
   public transient float[] targetPos;
   public transient Attack attack;
   public transient EnumType effectiveType;

   public AttackAnimation instantiate(PixelmonWrapper user, PixelmonWrapper target, Attack attack) {
      try {
         AttackAnimation animation = (AttackAnimation)this.getClass().newInstance();
         Field[] var5 = animation.getClass().getFields();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Field f = var5[var7];
            if (!Modifier.isTransient(f.getModifiers())) {
               f.set(animation, f.get(this));
            }
         }

         animation.initialize(user, target, attack);
         animation.tickAnimation(++animation.ticks);
         return animation;
      } catch (Exception var9) {
         var9.printStackTrace();
         return null;
      }
   }

   public void initialize(PixelmonWrapper user, PixelmonWrapper target, Attack attack) {
      this.bc = user.bc;
      this.user = user;
      this.target = target;
      this.attack = attack;
      this.effectiveType = attack.getMove().getAttackType();
      if (user.entity != null && target.entity != null) {
         this.dimension = user.entity.field_71093_bK;
         this.startID = user.entity.func_145782_y();
         this.userPos = new float[]{(float)user.entity.field_70165_t, (float)user.entity.field_70163_u, (float)user.entity.field_70161_v};
         this.endID = target.entity.func_145782_y();
         this.targetPos = new float[]{(float)target.entity.field_70165_t, (float)target.entity.field_70163_u, (float)target.entity.field_70161_v};
      }
   }

   public abstract boolean tickAnimation(int var1);

   public abstract boolean usedOncePerTurn();

   public void sendBattleEffect(AttackAnimationData data, boolean groundedStartPosition, boolean groundedEndPosition) {
      try {
         PlayBattleParticleSystem packet = new PlayBattleParticleSystem(this, this.attack, data, groundedStartPosition, groundedEndPosition);
         if (PixelmonConfig.onlyShowAttackEffectsToBattlers) {
            this.user.bc.spectators.forEach((spectator) -> {
               Pixelmon.network.sendTo(packet, spectator.getEntity());
            });
            this.user.bc.getPlayers().forEach((pp) -> {
               if (pp.player != null) {
                  Pixelmon.network.sendTo(packet, pp.player);
               }

            });
         } else {
            int dim = this.user.getParticipant().getEntity().field_71093_bK;
            double x = this.user.getParticipant().getEntity().field_70165_t;
            double y = this.user.getParticipant().getEntity().field_70163_u;
            double z = this.user.getParticipant().getEntity().field_70161_v;
            Pixelmon.network.sendToAllAround(packet, new NetworkRegistry.TargetPoint(dim, x, y, z, (double)PixelmonConfig.rangeToDisplayAttackAnimations));
         }
      } catch (Exception var12) {
         var12.printStackTrace();
      }

   }
}
