package com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Burn;
import com.pixelmonmod.pixelmon.battles.status.Flinch;
import com.pixelmonmod.pixelmon.battles.status.Freeze;
import com.pixelmonmod.pixelmon.battles.status.Paralysis;
import com.pixelmonmod.pixelmon.battles.status.Sleep;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.worldGeneration.dimension.ultraspace.UltraSpace;

public class SecretPower extends SpecialAttackBase {
   public void applyEffect(PixelmonWrapper user, PixelmonWrapper target) {
      if (!target.isFainted() && this.checkChance()) {
         TerrainExamine.TerrainType terrainType = TerrainExamine.getTerrain(user);
         if (user.getWorld().field_73011_w.field_76577_b == UltraSpace.WORLD_TYPE) {
            target.getBattleStats().modifyStat(-1, (StatsType)StatsType.Defence);
            return;
         }

         switch (terrainType) {
            case Sand:
               target.getBattleStats().modifyStat(-1, (StatsType)StatsType.Accuracy);
               break;
            case Cave:
               Flinch.flinch(user, target);
               break;
            case UltraSpace:
               target.getBattleStats().modifyStat(-1, (StatsType)StatsType.Defence);
               break;
            case Water:
               target.getBattleStats().modifyStat(-1, (StatsType)StatsType.Attack);
               break;
            case SnowIce:
               Freeze.freeze(user, target);
               break;
            case Volcano:
               Burn.burn(user, target, user.attack, false);
               break;
            case Grass:
               Sleep.sleep(user, target, user.attack, false);
               break;
            case Misty:
               target.getBattleStats().modifyStat(-1, (StatsType)StatsType.SpecialAttack);
               break;
            case Psychic:
               target.getBattleStats().modifyStat(-1, (StatsType)StatsType.Speed);
               break;
            default:
               Paralysis.paralyze(user, target, user.attack, false);
         }
      }

   }
}
