package com.pixelmonmod.pixelmon.items.heldItems;

import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.Terrain;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.battle.EnumTerrain;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;

public class ItemTerrainSeed extends HeldItem {
   public final EnumTerrain terrain;
   public final StatsType type;

   public ItemTerrainSeed(String itemName, EnumTerrain terrain, StatsType type) {
      super(EnumHeldItems.terrainSeed, itemName);
      this.terrain = terrain;
      this.type = type;
   }

   public void onTerrainSwitch(BattleControllerBase bc, PixelmonWrapper user, Terrain terrain) {
      if (terrain != null && terrain.getTerrainType() == this.terrain && user.getBattleStats().modifyStat(1, (StatsType)this.type)) {
         user.consumeItem();
      }

   }

   public void applySwitchInEffect(PixelmonWrapper newPokemon) {
      Terrain terrain = newPokemon.bc.globalStatusController.getTerrain();
      if (canUseItem(newPokemon) && terrain.getTerrainType() == this.terrain && newPokemon.getBattleStats().modifyStat(1, (StatsType)this.type)) {
         newPokemon.consumeItem();
      }

   }
}
