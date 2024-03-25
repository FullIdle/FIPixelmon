package com.pixelmonmod.pixelmon.storage;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.TickHandler;
import com.pixelmonmod.pixelmon.api.economy.IPixelmonBankAccountManager;
import com.pixelmonmod.pixelmon.api.storage.IStorageManager;
import com.pixelmonmod.pixelmon.api.storage.IStorageSaveAdapter;
import com.pixelmonmod.pixelmon.api.storage.IStorageSaveScheduler;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.pc.ClientInitializePC;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class ReforgedStorageManager implements IStorageManager, IPixelmonBankAccountManager {
   protected Map parties = new ConcurrentHashMap();
   protected Map pcs = new ConcurrentHashMap();
   public Queue extraStorages = new ConcurrentLinkedQueue();
   protected IStorageSaveScheduler scheduler;
   protected IStorageSaveAdapter adapter;
   public final List playersWithSyncedPCs = new ArrayList();

   public ReforgedStorageManager(IStorageSaveScheduler scheduler, IStorageSaveAdapter adapter) {
      this.scheduler = scheduler;
      this.adapter = adapter;
   }

   public IStorageSaveScheduler getSaveScheduler() {
      return this.scheduler;
   }

   public IStorageSaveAdapter getSaveAdapter() {
      return this.adapter;
   }

   public void setSaveScheduler(IStorageSaveScheduler scheduler) {
      this.scheduler = scheduler;
   }

   public void setSaveAdapter(IStorageSaveAdapter adapter) {
      this.adapter = adapter;
   }

   public PlayerPartyStorage getParty(UUID uuid) {
      PlayerPartyStorage partyStorage = (PlayerPartyStorage)this.parties.get(uuid);
      if (partyStorage == null) {
         partyStorage = (PlayerPartyStorage)this.getSaveAdapter().load(uuid, PlayerPartyStorage.class);
         partyStorage.tryUpdatePlayerName();
         this.parties.put(uuid, partyStorage);
      }

      if (partyStorage.countAll() == 0 && !partyStorage.starterPicked && PixelmonConfig.giveStarter && partyStorage.getPlayer() != null) {
         TickHandler.registerStarterList(partyStorage.getPlayer());
      }

      return partyStorage;
   }

   public PCStorage getPCForPlayer(UUID playerUUID) {
      PCStorage pc = (PCStorage)this.pcs.get(playerUUID);
      EntityPlayerMP player;
      if (pc == null) {
         pc = (PCStorage)this.getSaveAdapter().load(playerUUID, PCStorage.class);
         if (pc == null) {
            pc = new PCStorage(playerUUID);
         }

         this.pcs.put(playerUUID, pc);
         player = FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_177451_a(pc.uuid);
         if (player != null) {
            pc.setPlayer(player.func_110124_au(), player.func_70005_c_());
         }

         Iterator var4 = pc.getPlayersToUpdate().iterator();

         while(var4.hasNext()) {
            EntityPlayerMP player = (EntityPlayerMP)var4.next();
            Pixelmon.network.sendTo(new ClientInitializePC(pc), player);
         }
      }

      if (!this.playersWithSyncedPCs.contains(playerUUID)) {
         player = FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_177451_a(playerUUID);
         if (player != null) {
            this.initializePCForPlayer(player, pc);
            this.playersWithSyncedPCs.add(playerUUID);
         }
      }

      return pc;
   }

   @Nullable
   public PokemonStorage getExtraStorage(UUID uuid) {
      Iterator var2 = this.extraStorages.iterator();

      PokemonStorage storage;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         storage = (PokemonStorage)var2.next();
      } while(!storage.uuid.equals(uuid));

      return storage;
   }

   public Iterable getAllCachedStorages() {
      List allStorages = new ArrayList();
      allStorages.addAll(this.parties.values());
      allStorages.addAll(this.pcs.values());
      allStorages.addAll(this.extraStorages);
      return allStorages;
   }

   public void onStorageSaved(PokemonStorage storage) {
      if ((storage instanceof PartyStorage || storage instanceof PCStorage) && FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_177451_a(storage.uuid) == null) {
         if (storage instanceof PartyStorage) {
            this.parties.remove(storage.uuid);
         } else {
            this.pcs.remove(storage.uuid);
         }
      }

   }

   public void clearAll() {
      this.pcs.clear();
      this.parties.clear();
      this.extraStorages.clear();
   }

   public Optional getBankAccount(UUID uuid) {
      return Optional.of(this.getParty(uuid));
   }

   public Optional getBankAccount(EntityPlayerMP player) {
      return this.getBankAccount(player.func_110124_au());
   }

   @SubscribeEvent
   public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
      if (event.player instanceof EntityPlayerMP) {
         if (this.parties.containsKey(event.player.func_110124_au())) {
            ((PlayerPartyStorage)this.parties.get(event.player.func_110124_au())).tryUpdatePlayerName();
         }

         if (this.pcs.containsKey(event.player.func_110124_au())) {
            PCStorage pc = (PCStorage)this.pcs.get(event.player.func_110124_au());
            pc.setPlayer(event.player.func_110124_au(), event.player.func_70005_c_());
            Pixelmon.network.sendTo(new ClientInitializePC(pc), (EntityPlayerMP)event.player);
         }

      }
   }

   @SubscribeEvent
   public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
      this.playersWithSyncedPCs.remove(event.player.func_110124_au());
   }
}
