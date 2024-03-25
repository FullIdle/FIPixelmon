package com.pixelmonmod.pixelmon.tickHandlers;

import com.pixelmonmod.pixelmon.api.pokemon.IHatchEggs;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.TransientData;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayerMP;

public class WalkingEggHatcher implements IHatchEggs {
   public void tick(EntityPlayerMP player, PlayerPartyStorage party) {
      ++party.transientData.eggTick;
      if (party.transientData.eggTick >= 20) {
         party.transientData.eggTick = 0;
         this.doWalkHatching(player, party);
      }

   }

   public int doWalkHatching(EntityPlayerMP player, PlayerPartyStorage party) {
      if (party.countEggs() < 1) {
         return 0;
      } else {
         TransientData td = party.transientData;
         int posX = player.func_180425_c().func_177958_n();
         int posZ = player.func_180425_c().func_177952_p();
         int changeX = td.lastEggX - posX;
         int changeZ = td.lastEggZ - posZ;
         td.lastEggX = posX;
         td.lastEggZ = posZ;
         if (changeX == -posX && changeZ == -posZ) {
            return 0;
         } else {
            int steps = Math.abs(changeX) + Math.abs(changeZ);
            if (steps > 20) {
               return 0;
            } else if (steps == 0) {
               return 0;
            } else {
               int hatchBonus = this.getHatchBonus(party);
               Pokemon[] var10 = party.getAll();
               int var11 = var10.length;

               for(int var12 = 0; var12 < var11; ++var12) {
                  Pokemon pokemon = var10[var12];
                  if (pokemon != null && pokemon.isEgg()) {
                     pokemon.addEggSteps(steps, hatchBonus);
                  }
               }

               return steps;
            }
         }
      }
   }
}
