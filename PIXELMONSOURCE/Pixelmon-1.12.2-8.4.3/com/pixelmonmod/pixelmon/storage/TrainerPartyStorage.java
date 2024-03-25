package com.pixelmonmod.pixelmon.storage;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ClearTrainerPokemon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.StoreTrainerPokemon;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;

public class TrainerPartyStorage extends PartyStorage {
   protected NPCTrainer trainer;

   public TrainerPartyStorage(UUID uuid) {
      super(uuid);
   }

   public TrainerPartyStorage(NPCTrainer trainer) {
      this(trainer.func_110124_au());
      this.trainer = trainer;
   }

   public NPCTrainer getTrainer() {
      return this.trainer;
   }

   public void sendCacheToPlayer(EntityPlayerMP player) {
      Pixelmon.network.sendTo(new ClearTrainerPokemon(), player);
      Pokemon[] var2 = this.party;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Pokemon pokemon = var2[var4];
         if (pokemon != null) {
            Pixelmon.network.sendTo(new StoreTrainerPokemon(pokemon), player);
         }
      }

   }
}
