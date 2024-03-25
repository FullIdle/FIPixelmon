package com.pixelmonmod.pixelmon.entities.pixelmon.abilities;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class Bulletproof extends AbilityBase {
   public boolean allowsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
      return !a.isAttack("Acid Spray", "Aura Sphere", "Barrage", "Beak Blast", "Bullet Seed", "Egg Bomb", "Electro Ball", "Energy Ball", "Focus Blast", "Gyro Ball", "Ice Ball", "Magnet Bomb", "Mist Ball", "Mud Bomb", "Octazooka", "Pollen Puff", "Pyro Ball", "Rock Blast", "Rock Wrecker", "Searing Shot", "Seed Bomb", "Shadow Ball", "Sludge Bomb", "Weather Ball", "Zap Cannon");
   }

   public void allowsIncomingAttackMessage(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a) {
      pokemon.bc.sendToAll("pixelmon.battletext.noeffect", pokemon.getNickname());
   }
}
