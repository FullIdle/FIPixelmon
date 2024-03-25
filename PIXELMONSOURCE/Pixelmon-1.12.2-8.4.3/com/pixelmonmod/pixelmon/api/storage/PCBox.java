package com.pixelmonmod.pixelmon.api.storage;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.ClientSet;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.pc.ServerUpdateBox;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PCBox extends PokemonStorage {
   public static final int POKEMON_PER_BOX = 30;
   public transient PCStorage pc;
   public transient int boxNumber;
   protected Pokemon[] pokemon = new Pokemon[30];
   protected String name;
   protected String wallpaper;
   protected transient StoragePosition cachePosition = new StoragePosition(0, 0);
   public transient boolean hasChangedClientSide = false;

   public PCBox() {
      super(UUID.randomUUID());
   }

   public PCBox(PCStorage pc, int boxNumber) {
      super(pc.uuid);
      this.pc = pc;
      this.boxNumber = boxNumber;
   }

   public Pokemon[] getAll() {
      return (Pokemon[])Arrays.copyOf(this.pokemon, 30);
   }

   public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
      for(int i = 0; i < this.pokemon.length; ++i) {
         if (this.pokemon[i] != null) {
            nbt.func_74782_a("pc" + i, this.pokemon[i].writeToNBT(new NBTTagCompound()));
         }
      }

      if (this.name != null && !this.name.isEmpty()) {
         nbt.func_74778_a("name", this.name);
      }

      if (this.wallpaper != null && !this.wallpaper.isEmpty() && !this.wallpaper.equals("default")) {
         nbt.func_74778_a("wallpaper", this.wallpaper);
      }

      return nbt;
   }

   public PCBox readFromNBT(NBTTagCompound nbt) {
      for(int i = 0; i < 30; ++i) {
         if (nbt.func_74764_b("pc" + i)) {
            this.pokemon[i] = Pixelmon.pokemonFactory.create(nbt.func_74775_l("pc" + i));
            this.pokemon[i].setStorage(this, new StoragePosition(this.boxNumber, i));
         } else {
            this.pokemon[i] = null;
         }
      }

      if (nbt.func_74764_b("name")) {
         this.name = nbt.func_74779_i("name");
         if (this.name.isEmpty()) {
            this.name = null;
         }
      }

      if (nbt.func_74764_b("wallpaper")) {
         this.wallpaper = nbt.func_74779_i("wallpaper");
         if (this.wallpaper.isEmpty() || this.wallpaper.equals("default")) {
            this.wallpaper = null;
         }
      }

      return this;
   }

   public StoragePosition getPosition(Pokemon pokemon) {
      for(int i = 0; i < 30; ++i) {
         if (this.pokemon[i] == pokemon || this.pokemon[i] != null && this.pokemon[i].getUUID().equals(pokemon.getUUID())) {
            return new StoragePosition(this.boxNumber, i);
         }
      }

      return null;
   }

   public List getPlayersToUpdate() {
      return this.pc.getPlayersToUpdate();
   }

   public int getSlot(Pokemon pokemon) {
      for(int i = 0; i < 30; ++i) {
         if (this.pokemon[i] == pokemon) {
            return i;
         }
      }

      return -1;
   }

   public File getFile() {
      return this.pc.getFile();
   }

   @Nullable
   public StoragePosition getFirstEmptyPosition() {
      for(int i = 0; i < 30; ++i) {
         if (this.pokemon[i] == null) {
            return new StoragePosition(this.boxNumber, i);
         }
      }

      return null;
   }

   public void set(StoragePosition position, Pokemon pokemon) {
      this.pokemon[position.order] = pokemon;
      this.setNeedsSaving();
      this.notifyListeners(position, pokemon, new EnumUpdateType[0]);
      if (pokemon != null) {
         pokemon.setStorage(this, position);
         if (this.pc.playerName != null && (pokemon.getOriginalTrainer() == null || pokemon.getOriginalTrainer().equals(this.pc.playerName) && pokemon.getOriginalTrainerUUID() == null)) {
            pokemon.setOriginalTrainer(this.uuid, this.pc.playerName);
         }
      }

   }

   public void set(int slot, Pokemon pokemon) {
      this.set(new StoragePosition(this.boxNumber, slot), pokemon);
   }

   @Nullable
   public Pokemon get(StoragePosition position) {
      return this.pokemon[position.order];
   }

   @Nullable
   public Pokemon get(int slot) {
      return this.get(this.cachePosition.set(this.boxNumber, slot));
   }

   public void swap(StoragePosition position1, StoragePosition position2) {
      Pokemon temp = this.pokemon[position1.order];
      this.pokemon[position1.order] = this.pokemon[position2.order];
      this.pokemon[position2.order] = temp;
      this.setNeedsSaving();
      if (this.pokemon[position1.order] != null) {
         this.pokemon[position1.order].setStorage(this, position1);
      }

      if (this.pokemon[position2.order] != null) {
         this.pokemon[position2.order].setStorage(this, position2);
      }

      this.notifyListeners(position1, this.pokemon[position1.order], new EnumUpdateType[0]);
      this.notifyListeners(position2, this.pokemon[position2.order], new EnumUpdateType[0]);
   }

   public void swap(int slot1, int slot2) {
      this.swap(new StoragePosition(this.boxNumber, slot1), new StoragePosition(this.boxNumber, slot2));
   }

   public void sendContents(EntityPlayerMP player) {
      for(int slot = 0; slot < 30; ++slot) {
         Pokemon pokemon = this.get(slot);
         if (pokemon != null) {
            Pixelmon.network.sendTo(new ClientSet(this, new StoragePosition(this.boxNumber, slot), pokemon, EnumUpdateType.CLIENT), player);
         }
      }

   }

   public boolean getShouldSendUpdates() {
      return this.shouldSendUpdates ? this.pc.getShouldSendUpdates() : false;
   }

   public String getName() {
      return this.name;
   }

   public String getWallpaper() {
      return this.wallpaper;
   }

   @SideOnly(Side.CLIENT)
   public void sendChangesToServer() {
      if (this.hasChangedClientSide) {
         Pixelmon.network.sendToServer(new ServerUpdateBox(this.boxNumber, this.name, this.wallpaper));
      }

      this.hasChangedClientSide = false;
   }

   private void updateValue(Object oldValue, Object newValue) {
      if (oldValue == null && newValue != null || oldValue != null && !oldValue.equals(newValue) || newValue != null && !newValue.equals(oldValue)) {
         this.setNeedsSaving();
         this.hasChangedClientSide = true;
      }

   }

   public void setName(String name) {
      if (name == null || name.isEmpty()) {
         name = null;
      }

      this.updateValue(this.name, name);
      this.name = name;
   }

   public void setWallpaper(String wallpaper) {
      if (wallpaper == null || wallpaper.isEmpty() || wallpaper.equals("default")) {
         wallpaper = null;
      }

      this.updateValue(this.wallpaper, wallpaper);
      this.wallpaper = wallpaper;
   }
}
