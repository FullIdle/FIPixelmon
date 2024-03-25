package com.pixelmonmod.pixelmon.listener;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.PokerusEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackAction;
import com.pixelmonmod.pixelmon.battles.controller.log.BattleActionBase;
import com.pixelmonmod.pixelmon.battles.controller.log.MoveResults;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Pokerus;
import com.pixelmonmod.pixelmon.enums.EnumPokerusType;
import com.pixelmonmod.pixelmon.enums.battle.BattleResults;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PokerusSpreader {
   private static Random rnd = new Random();

   @SubscribeEvent
   public static void onBattleEnd(BattleEndEvent event) {
      if (PixelmonConfig.pokerusEnabled && PixelmonConfig.pokerusSpreadRate >= 0) {
         if (!event.abnormal) {
            List winners = new ArrayList();
            event.results.forEach((participant, result) -> {
               if (result == BattleResults.VICTORY && participant instanceof PlayerParticipant) {
                  spreadToParty((PlayerParticipant)participant);
                  winners.add(((PlayerParticipant)participant).player.func_110124_au());
               }

            });
            if (!winners.isEmpty()) {
               Map analyzedMap = new HashMap();
               Iterator var3 = event.bc.battleLog.getAllActions().iterator();

               while(true) {
                  AttackAction attack;
                  do {
                     do {
                        do {
                           BattleActionBase un1;
                           do {
                              if (!var3.hasNext()) {
                                 return;
                              }

                              un1 = (BattleActionBase)var3.next();
                           } while(!(un1 instanceof AttackAction));

                           attack = (AttackAction)un1;
                        } while(attack.pokemon.getPokerus().isPresent());
                     } while(!(attack.pokemon.getParticipant() instanceof PlayerParticipant));
                  } while(!winners.contains(((PlayerParticipant)attack.pokemon.getParticipant()).player.func_110124_au()));

                  UUID receiver = attack.pokemon.getPokemonUUID();
                  if (!analyzedMap.containsKey(receiver)) {
                     analyzedMap.put(receiver, new ArrayList());
                  }

                  MoveResults[] var7 = attack.moveResults;
                  int var8 = var7.length;

                  for(int var9 = 0; var9 < var8; ++var9) {
                     MoveResults res = var7[var9];
                     if (res != null && res.target != null && res.target.getPokerus().isPresent() && res.result.isSuccess()) {
                        UUID donor = res.target.getPokemonUUID();
                        List donors = (List)analyzedMap.get(receiver);
                        if (!donors.contains(donor)) {
                           donors.add(donor);
                           analyzedMap.put(receiver, donors);
                           res.target.getPokerus().ifPresent((pkrs) -> {
                              spread(pkrs.type, attack.pokemon, (PlayerPartyStorage)attack.pokemon.getParticipant().getStorage());
                           });
                        }
                     }
                  }
               }
            }
         }

      }
   }

   private static void spreadToParty(PlayerParticipant participant) {
      PixelmonWrapper[] party = participant.allPokemon;

      for(int i = 0; i < party.length; ++i) {
         if (party[i].isAlive() || party[i].pokemon.isEgg()) {
            Pokerus pkrs = (Pokerus)party[i].getPokerus().orElse((Object)null);
            if (pkrs != null) {
               if (i > 0) {
                  spread(pkrs.type, party[i - 1], participant.getStorage());
               }

               if (i + 1 < party.length) {
                  spread(pkrs.type, party[i + 1], participant.getStorage());
               }
            }
         }
      }

   }

   private static void spread(EnumPokerusType donorType, PixelmonWrapper wrapper, PlayerPartyStorage storage) {
      if (!wrapper.isFainted()) {
         if ((PixelmonConfig.pokerusSpreadRate == 0 || rnd.nextInt(PixelmonConfig.pokerusSpreadRate) == 0) && !Pixelmon.EVENT_BUS.post(new PokerusEvent.Spread.Pre(storage, wrapper, donorType))) {
            if (wrapper.entity == null) {
               Pokemon pokemon = wrapper.pokemon;
               Pokerus pokerus = new Pokerus(donorType);
               pokemon.setPokerus(pokerus);
            } else {
               EntityPixelmon receiver = wrapper.entity;
               receiver.getPokemonData().setPokerus(new Pokerus(donorType));
               receiver.update(new EnumUpdateType[]{EnumUpdateType.Pokerus});
            }

            Pixelmon.EVENT_BUS.post(new PokerusEvent.Spread.Post(storage, wrapper, donorType));
         }

      }
   }
}
