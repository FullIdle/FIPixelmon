package com.pixelmonmod.pixelmon.tickHandlers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.PassivePoisonEvent;
import com.pixelmonmod.pixelmon.api.pokemon.IPassiveEffects;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.TransientData;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.DelegateLink;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.PokemonLink;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayerMP;

public class WalkingPassiveEffects implements IPassiveEffects {
   public void tick(EntityPlayerMP player, PlayerPartyStorage party) {
      ++party.transientData.passivePoisonTick;
      if (party.transientData.passivePoisonTick >= 20 && PixelmonConfig.useOutsideEffects && BattleRegistry.getBattle(player) == null) {
         this.doWalkPoison(player, party);
         party.transientData.passivePoisonTick = 0;
      }

   }

   public int doWalkPoison(EntityPlayerMP player, PlayerPartyStorage party) {
      int posX = player.func_180425_c().func_177958_n();
      int posZ = player.func_180425_c().func_177952_p();
      TransientData td = party.transientData;
      int changeX = td.lastPassivePoisonHealX - posX;
      int changeZ = td.lastPassivePoisonHealZ - posZ;
      td.lastPassivePoisonHealX = posX;
      td.lastPassivePoisonHealZ = posZ;
      if (changeX == -posX && changeZ == -posZ) {
         return 0;
      } else {
         int steps = Math.abs(changeX) + Math.abs(changeZ);
         if (steps > 20) {
            return 0;
         } else if (steps == 0) {
            return 0;
         } else {
            Pokemon pokemon = party.getAll()[RandomHelper.rand.nextInt(6)];
            PassivePoisonEvent.Pre pre = new PassivePoisonEvent.Pre(player, pokemon);
            Pixelmon.EVENT_BUS.post(pre);
            if (pre.isCanceled()) {
               return 0;
            } else {
               if (pokemon != null) {
                  PokemonLink link = new DelegateLink(pokemon);
                  if (link.getPrimaryStatus().type != StatusType.Poison && link.getPrimaryStatus().type != StatusType.PoisonBadly) {
                     return 0;
                  }

                  if (pokemon.getAbility().getName().equalsIgnoreCase("Immunity")) {
                     return 0;
                  }

                  if (pokemon.getHealth() <= 1) {
                     link.removeStatuses(StatusType.Poison);
                     link.removeStatuses(StatusType.PoisonBadly);
                     link.sendMessage("pixelmon.status.poisoncure", link.getNickname());
                  }

                  int dmg = RandomHelper.getRandomNumberBetween(0, PixelmonConfig.poisonMaxDamage);
                  if (link.getPrimaryStatus().type == StatusType.PoisonBadly) {
                     dmg = (int)((double)dmg + (double)dmg * 0.5);
                  }

                  if (dmg >= pokemon.getHealth()) {
                     link.removeStatuses(StatusType.Poison);
                     link.removeStatuses(StatusType.PoisonBadly);
                     link.sendMessage("pixelmon.status.poisoncure", link.getNickname());
                     pokemon.setHealth(1);
                  }

                  pokemon.setHealth(pokemon.getHealth() - dmg);
                  Pixelmon.EVENT_BUS.post(new PassivePoisonEvent.Post(player, pokemon));
               }

               return steps;
            }
         }
      }
   }
}
