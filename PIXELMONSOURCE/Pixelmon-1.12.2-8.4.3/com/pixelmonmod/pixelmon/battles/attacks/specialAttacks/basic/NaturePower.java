package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class NaturePower extends SpecialAttackBase {
   public AttackResult applyEffectStart(PixelmonWrapper user, PixelmonWrapper target) {
      TerrainExamine.TerrainType terrainType = TerrainExamine.getTerrain(user);
      String attackName;
      switch (terrainType) {
         case Cave:
            attackName = "Power Gem";
            break;
         case Sand:
            attackName = "Earth Power";
            break;
         case Water:
            attackName = "Hydro Pump";
            break;
         case SnowIce:
            attackName = "Ice Beam";
            break;
         case Volcano:
            attackName = "Lava Plume";
            break;
         case UltraSpace:
            attackName = "Psyshock";
            break;
         case Grass:
            attackName = "Energy Ball";
            break;
         case Misty:
            attackName = "Moonblast";
            break;
         case Electric:
            attackName = "Thunderbolt";
            break;
         case Psychic:
            attackName = "Psychic";
            break;
         default:
            attackName = "Tri attack";
      }

      Attack calledAttack = new Attack(attackName);
      user.bc.sendToAll("pixelmon.effect.naturepower", calledAttack.getMove().getTranslatedName());
      user.useTempAttack(calledAttack, target);
      user.attack.moveResult.damage = calledAttack.moveResult.damage;
      user.attack.moveResult.fullDamage = calledAttack.moveResult.fullDamage;
      user.attack.moveResult.accuracy = calledAttack.moveResult.accuracy;
      return AttackResult.ignore;
   }
}
