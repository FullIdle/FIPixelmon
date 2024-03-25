package com.pixelmonmod.pixelmon.api.storage;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.tools.Quadstate;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;

public class PartyStorage extends PokemonStorage {
   public static final int MAX_PARTY = 6;
   protected Pokemon[] party = new Pokemon[6];
   protected transient StoragePosition cachePosition = new StoragePosition(-1, 0);

   public PartyStorage(UUID uuid) {
      super(uuid);
   }

   public Pokemon[] getAll() {
      return (Pokemon[])Arrays.copyOf(this.party, 6);
   }

   public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
      for(int i = 0; i < 6; ++i) {
         if (this.party[i] != null) {
            nbt.func_74782_a("party" + i, this.party[i].writeToNBT(new NBTTagCompound()));
         }
      }

      return nbt;
   }

   public PartyStorage readFromNBT(NBTTagCompound nbt) {
      for(int i = 0; i < 6; ++i) {
         if (nbt.func_74764_b("party" + i)) {
            this.party[i] = Pixelmon.pokemonFactory.create(nbt.func_74775_l("party" + i));
            this.party[i].setStorage(this, new StoragePosition(-1, i));
         } else {
            this.party[i] = null;
         }
      }

      return this;
   }

   @Nullable
   public StoragePosition getFirstEmptyPosition() {
      for(int i = 0; i < 6; ++i) {
         if (this.party[i] == null) {
            return new StoragePosition(-1, i);
         }
      }

      return null;
   }

   public void set(StoragePosition position, Pokemon pokemon) {
      this.party[position.order] = pokemon;
      this.setNeedsSaving();
      if (pokemon != null) {
         pokemon.setStorage(this, position);
      }

      this.notifyListeners(position, pokemon, new EnumUpdateType[0]);
   }

   public void set(int slot, Pokemon pokemon) {
      this.set(new StoragePosition(-1, slot), pokemon);
   }

   @Nullable
   public Pokemon get(StoragePosition position) {
      return this.party[position.order];
   }

   @Nullable
   public Pokemon get(int slot) {
      return this.get(this.cachePosition.set(-1, slot));
   }

   public Pokemon get(UUID pokemonUUID) {
      return this.get(this.getSlot(pokemonUUID));
   }

   public int getSlot(Pokemon pokemon) {
      return this.getSlot(pokemon.getUUID());
   }

   public int getSlot(UUID pokemonUUID) {
      for(int i = 0; i < 6; ++i) {
         if (this.party[i] != null && Objects.equals(this.party[i].getUUID(), pokemonUUID)) {
            return i;
         }
      }

      return -1;
   }

   public List getTeam() {
      List team = new ArrayList();
      Pokemon[] var2 = this.party;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Pokemon pokemon = var2[var4];
         if (pokemon != null && !pokemon.isEgg()) {
            team.add(pokemon);
         }
      }

      return team;
   }

   public void heal() {
      Iterator var1 = this.getTeam().iterator();

      while(var1.hasNext()) {
         Pokemon pokemon = (Pokemon)var1.next();
         pokemon.heal();
      }

   }

   public void swap(StoragePosition position1, StoragePosition position2) {
      Pokemon temp = this.party[position1.order];
      this.party[position1.order] = this.party[position2.order];
      this.party[position2.order] = temp;
      this.setNeedsSaving();
      if (this.party[position1.order] != null) {
         this.party[position1.order].setStorage(this, position1);
      }

      if (this.party[position2.order] != null) {
         this.party[position2.order].setStorage(this, position2);
      }

      this.notifyListeners(position1, this.party[position1.order], new EnumUpdateType[0]);
      this.notifyListeners(position2, this.party[position2.order], new EnumUpdateType[0]);
   }

   public void swap(int slot1, int slot2) {
      this.swap(new StoragePosition(-1, slot1), new StoragePosition(-1, slot2));
   }

   public StoragePosition getPosition(Pokemon pokemon) {
      for(int i = 0; i < 6; ++i) {
         if (this.party[i] == pokemon || this.party[i] != null && this.party[i].getUUID().equals(pokemon.getUUID())) {
            return new StoragePosition(-1, i);
         }
      }

      return null;
   }

   public List getPlayersToUpdate() {
      return new ArrayList();
   }

   public File getFile() {
      return new File(DimensionManager.getCurrentSaveRootDirectory(), "pokemon/" + this.uuid.toString() + ".pk");
   }

   public int countAblePokemon() {
      int c = 0;
      Pokemon[] var2 = this.party;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Pokemon pokemon = var2[var4];
         if (pokemon != null && pokemon.getHealth() > 0 && !pokemon.isEgg()) {
            ++c;
         }
      }

      return c;
   }

   public int getHighestLevel() {
      int lvl = 1;
      Pokemon[] var2 = this.party;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Pokemon pokemon = var2[var4];
         if (pokemon != null) {
            lvl = Math.max(lvl, pokemon.getLevel());
         }
      }

      return lvl;
   }

   public int getAverageLevel() {
      float c = 0.0F;
      int levelSum = 0;
      Pokemon[] var3 = this.party;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Pokemon pokemon = var3[var5];
         if (pokemon != null && !pokemon.isEgg()) {
            ++c;
            levelSum += pokemon.getLevel();
         }
      }

      if (c == 0.0F) {
         return 1;
      } else {
         return (int)((float)levelSum / c);
      }
   }

   public int getLowestLevel() {
      int level = 100;
      Pokemon[] var2 = this.party;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Pokemon pokemon = var2[var4];
         if (pokemon != null && !pokemon.isEgg() && pokemon.getLevel() < level) {
            level = pokemon.getLevel();
         }
      }

      return level;
   }

   public Pokemon getFirstAblePokemon() {
      return this.findOne((pk) -> {
         EntityPixelmon pixelmon = pk.getPixelmonIfExists();
         if (pixelmon != null && pixelmon.battleController != null) {
            return false;
         } else {
            return pk.getHealth() > 0 && !pk.isEgg();
         }
      });
   }

   public EntityPixelmon getAndSendOutFirstAblePokemon(Entity parent) {
      Pokemon pokemon = this.getFirstAblePokemon();
      return pokemon == null ? null : pokemon.getOrSpawnPixelmon(parent);
   }

   public Quadstate isOldGen(int dimension) {
      boolean oldGenDimension = PixelmonConfig.oldGenDimensions.contains(dimension);
      boolean bothGenDimension = PixelmonConfig.bothGenDimensions.contains(dimension);
      boolean relaxedRules = PixelmonConfig.relaxedBattleGimmickRules;
      if (this instanceof PlayerPartyStorage) {
         PlayerPartyStorage pps = (PlayerPartyStorage)this;
         return pps.getMegaItem().canSpecialEvolve() ? Quadstate.valueOf(pps.getMegaItem().canMega()) : Quadstate.NONE;
      } else {
         return !bothGenDimension && !relaxedRules ? Quadstate.valueOf(oldGenDimension) : Quadstate.BOTH;
      }
   }
}
