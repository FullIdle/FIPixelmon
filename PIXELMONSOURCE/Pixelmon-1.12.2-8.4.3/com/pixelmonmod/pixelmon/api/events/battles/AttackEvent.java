package com.pixelmonmod.pixelmon.api.events.battles;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.battles.attacks.Effectiveness;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class AttackEvent extends Event {
   public final PixelmonWrapper user;
   public final PixelmonWrapper target;

   protected AttackEvent(PixelmonWrapper user, PixelmonWrapper target) {
      this.user = user;
      this.target = target;
   }

   public Attack getAttack() {
      return this.user.attack;
   }

   public boolean isSelf() {
      return this.user == this.target;
   }

   public static class Damage extends AttackEvent {
      public double damage;

      public Damage(PixelmonWrapper user, PixelmonWrapper target, double damage) {
         super(user, target);
         this.damage = damage;
      }

      public boolean willBeFatal() {
         return this.target.getHealth() < (this.damage < 1.0 && this.damage > 0.0 ? 1 : (int)this.damage);
      }
   }

   public static class CriticalHit extends AttackEvent {
      public double critMultiplier;

      public CriticalHit(PixelmonWrapper user, PixelmonWrapper target, double critMultiplier) {
         super(user, target);
         this.critMultiplier = critMultiplier;
      }

      public boolean isCrit() {
         return this.critMultiplier != 1.0;
      }

      public void setCrit(boolean crit) {
         this.critMultiplier = crit ? 2.0 : 1.0;
      }
   }

   public static class TypeEffectiveness extends AttackEvent {
      private double effectiveness = -1.0;

      public TypeEffectiveness(PixelmonWrapper user, PixelmonWrapper target, double effectiveness) {
         super(user, target);
         this.effectiveness = effectiveness;
      }

      public void setEffectiveness(Effectiveness effectiveness) {
         this.effectiveness = (double)effectiveness.value;
      }

      public Effectiveness getEffectiveness() {
         return (Effectiveness)CollectionHelper.find(Lists.newArrayList(Effectiveness.values()), (e) -> {
            return (double)e.value == this.effectiveness;
         });
      }

      public double getMultiplier() {
         return this.effectiveness;
      }
   }

   public static class Stab extends AttackEvent {
      public double stabMultiplier;
      private final double originalMultiplier;

      public Stab(PixelmonWrapper user, PixelmonWrapper target, double stabMultiplier) {
         super(user, target);
         this.stabMultiplier = this.originalMultiplier = stabMultiplier;
      }

      public boolean isStabbing() {
         return this.stabMultiplier != 1.0;
      }

      public void setStabbing(boolean stabbing) {
         this.stabMultiplier = stabbing ? this.user.getAbility().modifyStab(this.originalMultiplier) : 1.0;
      }
   }

   public static class Use extends AttackEvent {
      public final AttackBase attack;
      public double accuracy;
      public boolean cantMiss;

      public Use(PixelmonWrapper user, PixelmonWrapper target, AttackBase attack, double accuracy, boolean cantMiss) {
         super(user, target);
         this.attack = attack;
         this.accuracy = accuracy;
         this.cantMiss = cantMiss;
      }
   }
}
