package com.pixelmonmod.pixelmon.battles.controller;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.enums.ExperienceGainType;
import com.pixelmonmod.pixelmon.api.events.battles.CatchComboEvent;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.ParticipantType;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.client.gui.battles.PixelmonInGui;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.SetPokemonBattleData;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.EVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Level;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Stats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.Evolution;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.types.LevelingEvolution;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemExpAll;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.items.heldItems.EVAdjusting;
import com.pixelmonmod.pixelmon.storage.playerData.CaptureCombo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.item.ItemStack;

public final class Experience {
   public static void awardExp(List participants, BattleParticipant losingTeamOwner, PixelmonWrapper faintedPokemon) {
      if (faintedPokemon != null && !faintedPokemon.bc.isLevelingDisabled()) {
         Iterator var3 = participants.iterator();

         while(true) {
            BattleParticipant teamOwner;
            do {
               do {
                  if (!var3.hasNext()) {
                     return;
                  }

                  teamOwner = (BattleParticipant)var3.next();
               } while(teamOwner.team == losingTeamOwner.team);
            } while(!(teamOwner instanceof PlayerParticipant));

            PlayerParticipant player = (PlayerParticipant)teamOwner;
            Set attackers = faintedPokemon.getAttackers();
            Iterator var7 = attackers.iterator();

            while(var7.hasNext()) {
               PixelmonWrapper pw = (PixelmonWrapper)var7.next();
               if (pw.getParticipant() == teamOwner) {
                  calcExp(faintedPokemon, pw, 1.0);
               }
            }

            boolean hasExpAll = false;
            ArrayList items = new ArrayList();
            items.addAll(player.player.field_71071_by.field_70462_a);
            items.addAll(player.player.field_71071_by.field_184439_c);
            Iterator var9 = items.iterator();

            while(var9.hasNext()) {
               ItemStack item = (ItemStack)var9.next();
               if (item != null && item.func_77973_b() instanceof ItemExpAll && ItemExpAll.isActivated(item)) {
                  hasExpAll = true;
                  break;
               }
            }

            PixelmonWrapper[] var16 = teamOwner.allPokemon;
            int var17 = var16.length;

            for(int var11 = 0; var11 < var17; ++var11) {
               PixelmonWrapper pw = var16[var11];
               if (!attackers.contains(pw)) {
                  boolean getsExp = false;
                  if (hasExpAll || pw.getHeldItem().getHeldItemType() == EnumHeldItems.expShare) {
                     getsExp = true;
                  }

                  if (getsExp) {
                     calcExp(faintedPokemon, pw, 0.5);
                  }
               }
            }

            player.givePlayerExp(faintedPokemon);
         }
      }
   }

   private static void calcExp(PixelmonWrapper faintedPokemon, PixelmonWrapper expReceiver, double scaleFactor) {
      if (!expReceiver.isFainted()) {
         ItemHeld heldItem = expReceiver.initialCopyOfPokemon.getHeldItemAsItemHeld();
         EnumHeldItems heldItemType = heldItem.getHeldItemType();
         Level level = expReceiver.getLevel();
         EVStore evStore = new EVStore(faintedPokemon.getBaseStats().evYields);
         if (heldItemType == EnumHeldItems.evAdjusting) {
            EVAdjusting item = (EVAdjusting)heldItem;
            if (item.type.statAffected == StatsType.None) {
               evStore.doubleValues();
            } else {
               evStore.addStat(item.type.statAffected, 8);
            }
         }

         if (PixelmonConfig.pokerusEnabled && expReceiver.getPokerus().isPresent()) {
            evStore.doubleValues();
         }

         Stats stats = expReceiver.getStats();
         stats.evs.gainEV(evStore);
         int beforeHP = stats.hp;
         stats.setLevelStats(expReceiver.getNature(), expReceiver.getBaseStats(), level.getLevel());
         if (stats.hp > beforeHP) {
            expReceiver.setHealth((int)Math.ceil((double)expReceiver.getHealth() / (double)beforeHP * (double)stats.hp));
            expReceiver.updateHPIncrease();
         }

         expReceiver.update(EnumUpdateType.HP, EnumUpdateType.Stats);
         if (expReceiver.getLevelNum() < expReceiver.bc.rules.levelCap) {
            double a = 1.0;
            if (faintedPokemon.getParticipant().getType() != ParticipantType.WildPokemon) {
               a = 1.5;
            }

            double t = expReceiver.getOriginalTrainer().equals(expReceiver.getParticipant().getDisplayName()) ? 1.0 : 1.5;
            double baseExp = (double)faintedPokemon.getBaseStats().getBaseExp();
            double eggMultiplier = heldItemType == EnumHeldItems.luckyEgg ? 1.5 : 1.0;
            double faintedLevel = (double)faintedPokemon.getLevelNum();
            PlayerParticipant participant = (PlayerParticipant)expReceiver.getParticipant();
            CaptureCombo combo = participant.getStorage().transientData.captureCombo;
            double captureCombo = 1.0;
            if (combo.getCurrentSpecies() == faintedPokemon.getSpecies()) {
               CatchComboEvent.ComboExperienceBonus event = new CatchComboEvent.ComboExperienceBonus(participant.player, combo.getExpBouns());
               Pixelmon.EVENT_BUS.post(event);
               captureCombo = (double)event.getExperienceModifier();
            }

            scaleFactor *= (double)PixelmonConfig.expModifier;
            double exp = a * t * baseExp * eggMultiplier * captureCombo * faintedLevel * scaleFactor / 7.0;
            Iterator var26 = expReceiver.getBaseStats().getEvolutions().iterator();

            while(var26.hasNext()) {
               Evolution e = (Evolution)var26.next();
               if (e instanceof LevelingEvolution && level.getLevel() >= ((LevelingEvolution)e).getLevel()) {
                  exp *= 1.2;
                  break;
               }
            }

            level.awardEXP((int)exp, ExperienceGainType.BATTLE);
            if (!expReceiver.bc.battleEnded) {
               expReceiver.updateHPIncrease();
            }

            if (!expReceiver.bc.battleEnded && expReceiver.getParticipant() instanceof PlayerParticipant) {
               ArrayList teamPokemon = participant.getTeamPokemonList();
               Pixelmon.network.sendTo(new SetPokemonBattleData(PixelmonInGui.convertToGUI(teamPokemon), false), participant.player);
               ArrayList playerSpectators = expReceiver.bc.getPlayerSpectators(participant);
               playerSpectators.forEach((spectator) -> {
                  spectator.sendMessage(new SetPokemonBattleData(PixelmonInGui.convertToGUI(teamPokemon), false));
               });
            }

         }
      }
   }
}
