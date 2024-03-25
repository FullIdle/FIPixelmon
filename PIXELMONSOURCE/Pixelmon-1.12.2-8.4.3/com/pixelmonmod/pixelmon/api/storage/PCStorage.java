package com.pixelmonmod.pixelmon.api.storage;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.PokedexEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.pokedex.EnumPokedexRegisterStatus;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class PCStorage extends PokemonStorage {
   protected PCBox[] boxes;
   protected List overflowBoxes;
   protected int lastBox;
   public UUID playerUUID;
   public String playerName;
   protected transient StoragePosition cachePosition;

   public PCStorage(UUID uuid, int boxes) {
      super(uuid);
      this.overflowBoxes = new ArrayList();
      this.lastBox = 0;
      this.playerUUID = null;
      this.playerName = null;
      this.cachePosition = new StoragePosition(0, 0);
      this.boxes = new PCBox[boxes];

      for(int i = 0; i < boxes; ++i) {
         this.boxes[i] = new PCBox(this, i);
      }

   }

   public PCStorage(UUID uuid) {
      this(uuid, PixelmonConfig.computerBoxes);
   }

   public void setPlayer(UUID playerUUID, String playerName) {
      this.playerUUID = playerUUID;
      this.playerName = playerName;
   }

   public int getLastBox() {
      return this.lastBox;
   }

   public void setLastBox(int lastBox) {
      this.lastBox = lastBox;
   }

   public PCBox[] getBoxes() {
      return (PCBox[])this.boxes.clone();
   }

   public PCBox getBox(int boxNumber) {
      return boxNumber >= 0 && boxNumber < this.boxes.length ? this.boxes[boxNumber] : this.boxes[0];
   }

   public int getBoxCount() {
      return this.boxes.length;
   }

   public Pokemon[] getAll() {
      List all = new ArrayList();
      PCBox[] var2 = this.boxes;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         PCBox box = var2[var4];
         Pokemon[] var6 = box.getAll();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            Pokemon pokemon = var6[var8];
            all.add(pokemon);
         }
      }

      return (Pokemon[])all.toArray(new Pokemon[all.size()]);
   }

   public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
      if (this.playerName != null) {
         nbt.func_74778_a("player", this.playerName);
      }

      int i;
      for(i = 0; i < this.boxes.length; ++i) {
         if (this.boxes[i] != null) {
            nbt.func_74782_a("BoxNumber" + i, this.boxes[i].writeToNBT(new NBTTagCompound()));
         }
      }

      if (!this.overflowBoxes.isEmpty()) {
         for(i = 0; i < this.overflowBoxes.size(); ++i) {
            nbt.func_74782_a("BoxNumber" + (i + this.boxes.length), ((PCBox)this.overflowBoxes.get(i)).writeToNBT(new NBTTagCompound()));
         }
      }

      nbt.func_74777_a("lastBoxOpen", (short)this.lastBox);
      return nbt;
   }

   public PCStorage readFromNBT(NBTTagCompound nbt) {
      this.playerName = nbt.func_74779_i("player");

      for(int i = 0; i < this.boxes.length; ++i) {
         if (nbt.func_74764_b("BoxNumber" + i)) {
            this.boxes[i] = (new PCBox(this, i)).readFromNBT(nbt.func_74775_l("BoxNumber" + i));
         } else if (nbt.func_74764_b(String.valueOf(i))) {
            this.boxes[i] = (new PCBox(this, i)).readFromNBT(nbt.func_74775_l(String.valueOf(i)));
         } else {
            this.boxes[i] = new PCBox(this, i);
         }
      }

      if (nbt.func_74764_b("BoxNumber" + this.boxes.length) || nbt.func_74764_b(String.valueOf(this.boxes.length))) {
         boolean originallySavedUpdates = this.shouldSendUpdates;
         this.shouldSendUpdates = false;
         boolean haveFilledMainPC = false;
         PCBox overflowHandleBox = new PCBox(this, this.boxes.length);
         overflowHandleBox.shouldSendUpdates = false;

         for(int overflowIndex = this.boxes.length; nbt.func_74764_b("BoxNumber" + overflowIndex) || nbt.func_74764_b(String.valueOf(overflowIndex)); ++overflowIndex) {
            String key = nbt.func_74764_b("BoxNumber" + overflowIndex) ? "BoxNumber" + overflowIndex : String.valueOf(overflowIndex);
            PCBox overflowingBox = (new PCBox(this, -1)).readFromNBT(nbt.func_74775_l(key));
            overflowingBox.shouldSendUpdates = false;
            int overflowingBoxPokemonIndex = 0;
            int numOverFlown = overflowingBox.countAll();

            while(overflowHandleBox.hasSpace() && numOverFlown > 0) {
               while(overflowingBoxPokemonIndex < 30) {
                  Pokemon overflowingPokemon = overflowingBox.get(overflowingBoxPokemonIndex);
                  if (overflowingPokemon != null) {
                     if (!haveFilledMainPC) {
                        haveFilledMainPC = !this.add(overflowingPokemon);
                     }

                     if (haveFilledMainPC) {
                        overflowHandleBox.add(overflowingPokemon);
                     }

                     overflowingBox.set(overflowingBoxPokemonIndex, (Pokemon)null);
                     ++overflowingBoxPokemonIndex;
                     --numOverFlown;
                     break;
                  }

                  ++overflowingBoxPokemonIndex;
               }

               if (!overflowHandleBox.hasSpace() && numOverFlown > 0) {
                  this.overflowBoxes.add(overflowHandleBox);
                  overflowHandleBox = new PCBox(this, this.boxes.length + this.overflowBoxes.size());
                  overflowHandleBox.shouldSendUpdates = false;
               }
            }
         }

         if (overflowHandleBox.countAll() > 0) {
            this.overflowBoxes.add(overflowHandleBox);
         }

         this.shouldSendUpdates = originallySavedUpdates;
      }

      this.lastBox = nbt.func_74765_d("lastBoxOpen");
      if (this.lastBox >= this.boxes.length || this.lastBox < 0) {
         this.lastBox = this.boxes.length - 1;
      }

      return this;
   }

   public StoragePosition getFirstEmptyPosition() {
      PCBox box = this.boxes[this.lastBox];
      if (box != null && box.hasSpace()) {
         return new StoragePosition(this.lastBox, box.getFirstEmptyPosition().order);
      } else {
         for(int i = 0; i < this.boxes.length; ++i) {
            if (i != this.lastBox && this.boxes[i].hasSpace()) {
               return new StoragePosition(i, this.boxes[i].getFirstEmptyPosition().order);
            }
         }

         return null;
      }
   }

   public void set(StoragePosition position, Pokemon pokemon) {
      this.boxes[position.box].set(position, pokemon);
      this.setNeedsSaving();
      if (this.getShouldSendUpdates() && this.playerUUID != null) {
         PlayerPartyStorage partyStorage = Pixelmon.storageManager.getParty(this.playerUUID);
         if (pokemon != null && !pokemon.isEgg() && partyStorage.pokedex.getSeenMap().get(pokemon.getSpecies().getNationalPokedexInteger()) != EnumPokedexRegisterStatus.caught && !Pixelmon.EVENT_BUS.post(new PokedexEvent(this.uuid, pokemon, EnumPokedexRegisterStatus.caught, "storageMovement"))) {
            partyStorage.pokedex.set(pokemon, EnumPokedexRegisterStatus.caught);
            partyStorage.pokedex.update();
         }
      }

   }

   public void set(int box, int slot, Pokemon pokemon) {
      this.set(new StoragePosition(box, slot), pokemon);
   }

   @Nullable
   public Pokemon get(StoragePosition position) {
      return this.boxes[position.box].pokemon[position.order];
   }

   @Nullable
   public Pokemon get(int box, int slot) {
      return this.get(this.cachePosition.set(box, slot));
   }

   public void swap(StoragePosition position1, StoragePosition position2) {
      if (position1.box == position2.box) {
         this.boxes[position1.box].swap(position1, position2);
      } else {
         Pokemon temp = this.boxes[position1.box].get(position1);
         this.boxes[position1.box].pokemon[position1.order] = this.boxes[position2.box].get(position2);
         this.boxes[position2.box].pokemon[position2.order] = temp;
         Pokemon pokemon1 = this.get(position1);
         Pokemon pokemon2 = this.get(position2);
         if (pokemon1 != null) {
            pokemon1.setStorage(this, position1);
         }

         if (pokemon2 != null) {
            pokemon2.setStorage(this, position2);
         }

         this.notifyListeners(position1, pokemon1, new EnumUpdateType[0]);
         this.notifyListeners(position2, pokemon2, new EnumUpdateType[0]);
      }

      this.setNeedsSaving();
   }

   public void swap(int box1, int slot1, int box2, int slot2) {
      this.swap(new StoragePosition(box1, slot1), new StoragePosition(box2, slot2));
   }

   public StoragePosition getPosition(Pokemon pokemon) {
      for(int i = 0; i < this.boxes.length; ++i) {
         StoragePosition position;
         if (this.boxes[i] != null && (position = this.boxes[i].getPosition(pokemon)) != null) {
            return position;
         }
      }

      return null;
   }

   public List getPlayersToUpdate() {
      EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_177451_a(this.uuid);
      return (List)(player == null ? new ArrayList() : Collections.singletonList(player));
   }

   public File getFile() {
      return new File(DimensionManager.getCurrentSaveRootDirectory(), "pokemon/" + this.uuid.toString() + ".comp");
   }

   public boolean getShouldSave() {
      if (this.hasChanged) {
         return true;
      } else {
         PCBox[] var1 = this.boxes;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            PCBox box = var1[var3];
            if (box.hasChanged) {
               return true;
            }
         }

         if (this.playerUUID != null && FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_177451_a(this.playerUUID) == null) {
            return true;
         } else {
            return false;
         }
      }
   }

   public void setHasChanged(boolean hasChanged) {
      super.setHasChanged(hasChanged);
      if (!hasChanged) {
         PCBox[] var2 = this.boxes;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            PCBox box = var2[var4];
            box.setHasChanged(false);
         }
      }

   }

   public void sendContents(EntityPlayerMP player) {
      PCBox[] var2 = this.boxes;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         PCBox box = var2[var4];
         if (box != null) {
            box.sendContents(player);
         }
      }

   }
}
