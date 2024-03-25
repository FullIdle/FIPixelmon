package com.pixelmonmod.pixelmon.api.storage;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import javax.annotation.Nullable;

public class PCBoxSearch extends PCBox {
   public PCBoxSearch(PCStorageSearch pc, int boxNumber) {
      super(pc, boxNumber);
   }

   public String getWallpaper() {
      return "search";
   }

   public void set(StoragePosition position, Pokemon pokemon) {
      this.pokemon[position.order] = pokemon;
   }

   @Nullable
   public StoragePosition getFirstEmptyPosition() {
      int lastBox = ClientStorageManager.openPC.lastBox;
      ClientStorageManager.openPC.lastBox = 0;
      StoragePosition result = ClientStorageManager.openPC.getFirstEmptyPosition();
      ClientStorageManager.openPC.lastBox = lastBox;
      return result;
   }
}
