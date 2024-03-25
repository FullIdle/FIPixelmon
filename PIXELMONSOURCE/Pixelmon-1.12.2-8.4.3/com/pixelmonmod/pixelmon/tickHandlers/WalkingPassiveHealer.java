package com.pixelmonmod.pixelmon.tickHandlers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.events.PassiveHealEvent;
import com.pixelmonmod.pixelmon.api.pokemon.IPassiveHealer;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.TransientData;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.DelegateLink;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.links.PokemonLink;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayerMP;

public class WalkingPassiveHealer implements IPassiveHealer {
   public void tick(EntityPlayerMP player, PlayerPartyStorage party) {
      ++party.transientData.passiveHealTick;
      if (party.transientData.passiveHealTick >= 20 && PixelmonConfig.usePassiveHealer && BattleRegistry.getBattle(player) == null) {
         this.doWalkHealing(player, party);
         party.transientData.passiveHealTick = 0;
      }

   }

   public int doWalkHealing(EntityPlayerMP player, PlayerPartyStorage party) {
      int posX = player.func_180425_c().func_177958_n();
      int posZ = player.func_180425_c().func_177952_p();
      TransientData td = party.transientData;
      int changeX = td.lastPassiveHealX - posX;
      int changeZ = td.lastPassiveHealZ - posZ;
      td.lastPassiveHealX = posX;
      td.lastPassiveHealZ = posZ;
      if (changeX == -posX && changeZ == -posZ) {
         return 0;
      } else {
         int steps = Math.abs(changeX) + Math.abs(changeZ);
         if (steps > 20) {
            return 0;
         } else if (steps == 0) {
            return 0;
         } else {
            boolean revive = false;
            boolean status = false;
            boolean heal = RandomHelper.getRandomChance((float)steps / (float)PixelmonConfig.stepsToHealHealthPassively);

            for(int i = 0; i < steps; ++i) {
               if (!revive && RandomHelper.getRandomChance(PixelmonConfig.chanceToRevivePassively)) {
                  revive = true;
               }

               if (!status && RandomHelper.getRandomChance(PixelmonConfig.chanceToHealStatusPassively)) {
                  status = true;
               }
            }

            Pokemon pokemon = party.getAll()[RandomHelper.rand.nextInt(6)];
            PassiveHealEvent.Pre pre = new PassiveHealEvent.Pre(player, pokemon, revive, status, heal);
            Pixelmon.EVENT_BUS.post(pre);
            if (pre.isCanceled()) {
               return 0;
            } else {
               if (pokemon != null) {
                  PokemonLink link = new DelegateLink(pokemon);
                  if (revive && pokemon.getHealth() <= 0) {
                     link.sendMessage("pixelmon.effect.revived", link.getNickname());
                     link.setHealth(1);
                     link.update(EnumUpdateType.HP);
                     status = false;
                     heal = false;
                  }

                  if (status) {
                     if (link.getPrimaryStatus().type == StatusType.Burn && link.removeStatuses(StatusType.Burn)) {
                        link.sendMessage("pixelmon.status.burncure", link.getNickname());
                        heal = false;
                        link.update(EnumUpdateType.Status);
                     } else if (link.getPrimaryStatus().type == StatusType.Paralysis && link.removeStatuses(StatusType.Paralysis)) {
                        link.sendMessage("pixelmon.status.paralysiscure", link.getNickname());
                        heal = false;
                        link.update(EnumUpdateType.Status);
                     } else if (link.getPrimaryStatus().type == StatusType.Poison && link.removeStatuses(StatusType.Poison)) {
                        link.sendMessage("pixelmon.status.poisoncure", link.getNickname());
                        heal = false;
                        link.update(EnumUpdateType.Status);
                     } else if (link.getPrimaryStatus().type == StatusType.PoisonBadly && link.removeStatuses(StatusType.PoisonBadly)) {
                        link.sendMessage("pixelmon.status.poisoncure", link.getNickname());
                        heal = false;
                        link.update(EnumUpdateType.Status);
                     } else if (link.getPrimaryStatus().type == StatusType.Sleep && link.removeStatuses(StatusType.Sleep)) {
                        link.sendMessage("pixelmon.status.wokeup", link.getNickname());
                        heal = false;
                        link.update(EnumUpdateType.Status);
                     } else if (link.getPrimaryStatus().type == StatusType.Freeze && link.removeStatuses(StatusType.Freeze)) {
                        link.sendMessage("pixelmon.status.breakice", link.getNickname());
                        heal = false;
                        link.update(EnumUpdateType.Status);
                     }
                  }

                  if (heal) {
                     double percentageHP = (double)link.getHealth() / (double)link.getMaxHealth();
                     if (percentageHP < (double)PixelmonConfig.passiveHealingMaxHealthPercentage && link.getHealth() + 1 <= link.getMaxHealth() && link.getHealth() != 0) {
                        link.setHealth(link.getHealth() + 1);
                        link.update(EnumUpdateType.HP);
                     }
                  }

                  Pixelmon.EVENT_BUS.post(new PassiveHealEvent.Post(player, pokemon, revive, status, heal));
               }

               return steps;
            }
         }
      }
   }
}
