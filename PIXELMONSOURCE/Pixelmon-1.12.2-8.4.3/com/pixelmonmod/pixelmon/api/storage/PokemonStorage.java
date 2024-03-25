package com.pixelmonmod.pixelmon.api.storage;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.ClientSet;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumBerryFlavor;
import com.pixelmonmod.pixelmon.enums.EnumCurryKey;
import com.pixelmonmod.pixelmon.enums.EnumCurryRating;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public abstract class PokemonStorage {
   public UUID uuid;
   public transient boolean shouldSendUpdates = true;
   protected transient boolean hasChanged = false;

   public PokemonStorage(UUID uuid) {
      this.uuid = uuid;
   }

   public abstract Pokemon[] getAll();

   @Nullable
   public abstract StoragePosition getFirstEmptyPosition();

   public abstract void set(StoragePosition var1, Pokemon var2);

   @Nullable
   public abstract Pokemon get(StoragePosition var1);

   public abstract void swap(StoragePosition var1, StoragePosition var2);

   public abstract NBTTagCompound writeToNBT(NBTTagCompound var1);

   public abstract PokemonStorage readFromNBT(NBTTagCompound var1);

   public abstract StoragePosition getPosition(Pokemon var1);

   public abstract List getPlayersToUpdate();

   public abstract File getFile();

   public boolean add(Pokemon pokemon) {
      StoragePosition openPosition = this.getFirstEmptyPosition();
      if (openPosition != null) {
         this.set(openPosition, pokemon);
         return true;
      } else {
         return false;
      }
   }

   public boolean transfer(PokemonStorage from, StoragePosition fromPosition, StoragePosition toPosition) {
      Pokemon fromPokemon = from.get(fromPosition);
      Pokemon toPokemon = this.get(toPosition);
      if (this.canTransfer(toPosition, fromPosition) && from.canTransfer(fromPosition, toPosition)) {
         from.set(fromPosition, toPokemon);
         this.set(toPosition, fromPokemon);
         return true;
      } else {
         return false;
      }
   }

   public boolean hasSpace() {
      return this.getFirstEmptyPosition() != null;
   }

   public boolean canTransfer(StoragePosition from, StoragePosition to) {
      return true;
   }

   public int countPokemon() {
      int count = 0;
      Pokemon[] var2 = this.getAll();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Pokemon pokemon = var2[var4];
         if (pokemon != null && !pokemon.isEgg()) {
            ++count;
         }
      }

      return count;
   }

   public int countEggs() {
      int count = 0;
      Pokemon[] var2 = this.getAll();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Pokemon pokemon = var2[var4];
         if (pokemon != null && pokemon.isEgg()) {
            ++count;
         }
      }

      return count;
   }

   public int countAll() {
      int count = 0;
      Pokemon[] var2 = this.getAll();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Pokemon pokemon = var2[var4];
         if (pokemon != null) {
            ++count;
         }
      }

      return count;
   }

   public boolean getShouldSendUpdates() {
      return this.shouldSendUpdates;
   }

   public Pokemon findOne(Predicate condition) {
      Pokemon[] var2 = this.getAll();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Pokemon pokemon = var2[var4];
         if (pokemon != null && condition.test(pokemon)) {
            return pokemon;
         }
      }

      return null;
   }

   public Pokemon findOne(PokemonSpec spec) {
      spec.getClass();
      return this.findOne(spec::matches);
   }

   public List findAll(Predicate condition) {
      List matches = new ArrayList();
      Pokemon[] var3 = this.getAll();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Pokemon pokemon = var3[var5];
         if (pokemon != null && condition.test(pokemon)) {
            matches.add(pokemon);
         }
      }

      return matches;
   }

   public List findAll(PokemonSpec spec) {
      spec.getClass();
      return this.findAll(spec::matches);
   }

   public Pokemon find(UUID uuid) {
      return uuid == null ? null : this.findOne((pokemon) -> {
         return pokemon.getUUID().equals(uuid);
      });
   }

   public boolean validate(StoragePosition position, UUID pokemonUUID) {
      Pokemon pokemon = this.get(position);
      if (pokemon == null && pokemonUUID == null) {
         return true;
      } else {
         return pokemon != null && pokemonUUID != null && pokemon.getUUID().equals(pokemonUUID);
      }
   }

   public void notifyListeners(StoragePosition position, Pokemon pokemon, EnumUpdateType... dataTypes) {
      if (this.getShouldSendUpdates() && dataTypes != null) {
         this.getPlayersToUpdate().forEach((player) -> {
            this.notifyListener(player, position, pokemon, dataTypes);
         });
      }

   }

   public void notifyListener(EntityPlayerMP player, StoragePosition position, Pokemon pokemon, EnumUpdateType... dataTypes) {
      if (this.getShouldSendUpdates() && player != null) {
         Pixelmon.network.sendTo(new ClientSet(this, position, pokemon, dataTypes), player);
      }

   }

   public boolean getShouldSave() {
      return this.hasChanged;
   }

   public void setHasChanged(boolean hasChanged) {
      this.hasChanged = hasChanged;
   }

   public void setNeedsSaving() {
      this.setHasChanged(true);
   }

   public void doWithoutSendingUpdates(Runnable process) {
      boolean original = this.shouldSendUpdates;
      this.shouldSendUpdates = false;
      process.run();
      this.shouldSendUpdates = original;
   }

   public String toString() {
      return "PokemonStorage{uuid=" + this.uuid + ", hasChanged=" + this.getShouldSave() + ", shouldSendUpdates=" + this.shouldSendUpdates + '}';
   }

   public void retrieveAll() {
      Pokemon[] var1 = this.getAll();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Pokemon pokemon = var1[var3];
         if (pokemon != null) {
            pokemon.ifEntityExists(EntityPixelmon::retrieve);
         }
      }

   }

   public int[] getCurryData() {
      return new int[26];
   }

   public void updateSingleCurryData(EnumCurryKey curryKey, EnumBerryFlavor cookingFlavor, EnumCurryRating rating) {
   }
}
