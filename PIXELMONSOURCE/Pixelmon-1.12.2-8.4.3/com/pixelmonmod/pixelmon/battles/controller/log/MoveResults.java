package com.pixelmonmod.pixelmon.battles.controller.log;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class MoveResults {
   public PixelmonWrapper target;
   public int damage;
   public int fullDamage;
   public int accuracy;
   public float priority;
   public AttackResult result;
   public float weightMod;

   public MoveResults(PixelmonWrapper target) {
      this.result = AttackResult.proceed;
      this.target = target;
   }

   public MoveResults(PixelmonWrapper target, int damage, AttackResult result) {
      this.result = AttackResult.proceed;
      this.target = target;
      this.damage = damage;
      this.fullDamage = damage;
      this.result = result;
   }

   public MoveResults(PixelmonWrapper target, int damage, float priority, AttackResult result) {
      this(target, damage, result);
      this.priority = priority;
   }

   public MoveResults(MoveResults moveResult) {
      this.result = AttackResult.proceed;
      this.accuracy = moveResult.accuracy;
      this.damage = moveResult.damage;
      this.fullDamage = moveResult.damage;
      this.priority = moveResult.priority;
      this.result = moveResult.result;
      this.target = moveResult.target;
      this.weightMod = moveResult.weightMod;
   }

   public String toString() {
      return "MoveResults{damage=" + this.damage + ", fullDamage=" + this.fullDamage + ", accuracy=" + this.accuracy + ", priority=" + this.priority + ", result=" + this.result + ", weightMod=" + this.weightMod + '}';
   }
}
