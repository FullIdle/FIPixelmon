package com.pixelmonmod.pixelmon.entities.pixelmon.helpers;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.evolution.EvolutionStage;
import com.pixelmonmod.pixelmon.comm.packetHandlers.evolution.EvolvePokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class EvolutionQueryList {
   public static final List queryList = Collections.synchronizedList(new ArrayList());

   public static void declineQuery(EntityPlayerMP player, UUID pokemonUUID) {
      synchronized(queryList) {
         for(int i = 0; i < queryList.size(); ++i) {
            EvolutionQuery query = (EvolutionQuery)queryList.get(i);
            if (query.pokemonUUID.equals(pokemonUUID)) {
               if (query.player == player) {
                  query.decline();
                  queryList.remove(i);
               }

               return;
            }
         }

      }
   }

   public static void acceptQuery(EntityPlayerMP player, UUID pokemonUUID) {
      synchronized(queryList) {
         for(int i = 0; i < queryList.size(); ++i) {
            EvolutionQuery query = (EvolutionQuery)queryList.get(i);
            if (query.pokemonUUID.equals(pokemonUUID)) {
               if (query.player == player) {
                  query.accept();
               }

               return;
            }
         }

      }
   }

   public static EvolutionQuery get(EntityPlayer player) {
      synchronized(queryList) {
         for(int i = 0; i < queryList.size(); ++i) {
            EvolutionQuery q = (EvolutionQuery)queryList.get(i);
            if (q.player == player) {
               return q;
            }
         }

         return null;
      }
   }

   public static void tick(World world) {
      synchronized(queryList) {
         for(int i = 0; i < queryList.size(); ++i) {
            EvolutionQuery q = (EvolutionQuery)queryList.get(i);
            q.tick(world);
         }

      }
   }

   public static void spawnPokemon(EntityPlayerMP player, UUID pokemonUUID) {
      synchronized(queryList) {
         for(int i = 0; i < queryList.size(); ++i) {
            EvolutionQuery query = (EvolutionQuery)queryList.get(i);
            if (query.pokemonUUID.equals(pokemonUUID)) {
               PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
               EntityPixelmon pixelmon = party.find(pokemonUUID).getOrSpawnPixelmon(player);
               if (pixelmon != null && party.find(pokemonUUID).getPixelmonIfExists() != null) {
                  Pixelmon.network.sendToAllAround(new EvolvePokemon(pokemonUUID, EvolutionStage.Choice), new NetworkRegistry.TargetPoint(pixelmon.field_71093_bK, pixelmon.field_70165_t, pixelmon.field_70163_u, pixelmon.field_70161_v, 60.0));
                  query.pixelmon = pixelmon;
                  query.pixelmon.field_70714_bg.field_75782_a.clear();
                  return;
               }

               return;
            }
         }

      }
   }

   public static boolean isCurrentlyEvolving(Pokemon pokemon) {
      synchronized(queryList) {
         for(int i = 0; i < queryList.size(); ++i) {
            if (((EvolutionQuery)queryList.get(i)).pokemonUUID.equals(pokemon.getUUID())) {
               return true;
            }
         }

         return false;
      }
   }
}
