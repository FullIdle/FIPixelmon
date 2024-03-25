package com.pixelmonmod.pixelmon.api.storage;

import com.google.common.base.Preconditions;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityPC;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.pc.ClientInitializePC;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;

public interface IStorageManager {
   IStorageSaveScheduler getSaveScheduler();

   IStorageSaveAdapter getSaveAdapter();

   PCStorage getPCForPlayer(UUID var1);

   PlayerPartyStorage getParty(UUID var1);

   Iterable getAllCachedStorages();

   void onStorageSaved(PokemonStorage var1);

   void clearAll();

   default PCStorage getPC(EntityPlayerMP player, TileEntityPC pc) {
      return this.getPCForPlayer(player);
   }

   default PCStorage getPCForPlayer(EntityPlayerMP player) {
      Preconditions.checkNotNull(player);
      return this.getPCForPlayer(player.func_110124_au());
   }

   default PlayerPartyStorage getParty(EntityPlayerMP player) {
      Preconditions.checkNotNull(player);
      return this.getParty(player.func_110124_au());
   }

   default PokemonStorage getStorage(EntityPlayerMP player, StoragePosition position) {
      return (PokemonStorage)(position.box == -1 ? this.getParty(player) : this.getPCForPlayer(player));
   }

   default Pokemon getPokemon(EntityPlayerMP player, StoragePosition position) {
      return position.box == -1 ? this.getParty(player).get(position) : this.getPCForPlayer(player).get(position);
   }

   default void initializePCForPlayer(EntityPlayerMP player, PCStorage pc) {
      Pixelmon.network.sendTo(new ClientInitializePC(pc), player);
      pc.sendContents(player);
   }
}
