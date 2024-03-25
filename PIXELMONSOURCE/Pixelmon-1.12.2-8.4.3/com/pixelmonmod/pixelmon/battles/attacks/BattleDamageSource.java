package com.pixelmonmod.pixelmon.battles.attacks;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;

public class BattleDamageSource extends DamageSource {
   PixelmonWrapper source;

   public BattleDamageSource(String dmgIdentifier, PixelmonWrapper source) {
      super(dmgIdentifier);
      this.source = source;
   }

   public Entity func_76346_g() {
      return this.source.entity;
   }

   public boolean func_76350_n() {
      return false;
   }

   public boolean func_76357_e() {
      return true;
   }
}
