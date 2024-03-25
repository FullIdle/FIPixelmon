package com.pixelmonmod.pixelmon.storage.deepstorage;

import com.google.common.collect.ImmutableList;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class DeepStorage {
   private ArrayList archivedPokemon = new ArrayList();

   public DeepStorage(NBTTagCompound nbt) {
      int i = 0;

      while(nbt.func_74764_b("pokemon" + i)) {
         this.archivedPokemon.add(nbt.func_74775_l("pokemon" + i++));
      }

      this.clearDuplicates();
   }

   public boolean isEmpty() {
      return this.archivedPokemon.isEmpty();
   }

   public void write(File file) {
      try {
         file.createNewFile();
         NBTTagCompound deepStorageNBT = new NBTTagCompound();
         this.clearDuplicates();

         for(int i = 0; i < this.archivedPokemon.size(); ++i) {
            deepStorageNBT.func_74782_a("pokemon" + i, (NBTBase)this.archivedPokemon.get(i));
         }

         DataOutputStream dataStream = new DataOutputStream(new FileOutputStream(file));
         Throwable var4 = null;

         try {
            CompressedStreamTools.func_74800_a(deepStorageNBT, dataStream);
         } catch (Throwable var14) {
            var4 = var14;
            throw var14;
         } finally {
            if (dataStream != null) {
               if (var4 != null) {
                  try {
                     dataStream.close();
                  } catch (Throwable var13) {
                     var4.addSuppressed(var13);
                  }
               } else {
                  dataStream.close();
               }
            }

         }
      } catch (IOException var16) {
         if (PixelmonConfig.printErrors) {
            Pixelmon.LOGGER.error("Couldn't write deep store data file for " + file.getName(), var16);
         }
      }

   }

   public void clearDuplicates() {
      ArrayList ids = new ArrayList();
      Iterator it = this.archivedPokemon.iterator();

      while(it.hasNext()) {
         NBTTagCompound nbt = (NBTTagCompound)it.next();
         UUID idraw = nbt.func_186857_a("UUID");
         if (ids.contains(idraw)) {
            it.remove();
         } else {
            ids.add(idraw);
         }
      }

   }

   public void put(NBTTagCompound nbt) {
      nbt.func_74757_a("isInRanch", false);
      this.archivedPokemon.add(nbt);
   }

   public ArrayList tryRetrieve() {
      ArrayList freedPokemon = new ArrayList();
      Iterator var2 = this.archivedPokemon.iterator();

      while(var2.hasNext()) {
         NBTTagCompound nbt = (NBTTagCompound)var2.next();
         if (EnumSpecies.getFromNameAnyCaseNoTranslate(nbt.func_74779_i("Name")) != null) {
            freedPokemon.add(nbt);
         }
      }

      this.archivedPokemon.removeAll(freedPokemon);
      return freedPokemon;
   }

   public ImmutableList getArchivedPokemon() {
      return ImmutableList.copyOf(this.archivedPokemon);
   }
}
