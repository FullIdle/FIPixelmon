package com.pixelmonmod.pixelmon.storage.adapters;

import com.google.common.io.Files;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.storage.IStorageSaveAdapter;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

public class ReforgedFileAdapter implements IStorageSaveAdapter {
   public void save(PokemonStorage storage) {
      NBTTagCompound nbt = storage.writeToNBT(new NBTTagCompound());
      File file = storage.getFile();
      File tempFile = new File(file.getPath() + ".temp");
      if (!file.getParentFile().exists()) {
         file.getParentFile().mkdirs();
      }

      try {
         tempFile.createNewFile();
         DataOutputStream dataStream = new DataOutputStream(new FileOutputStream(tempFile));
         Throwable var6 = null;

         try {
            CompressedStreamTools.func_74800_a(nbt, dataStream);
         } catch (Throwable var16) {
            var6 = var16;
            throw var16;
         } finally {
            if (dataStream != null) {
               if (var6 != null) {
                  try {
                     dataStream.close();
                  } catch (Throwable var15) {
                     var6.addSuppressed(var15);
                  }
               } else {
                  dataStream.close();
               }
            }

         }

         if (tempFile.exists()) {
            if (file.exists()) {
               file.delete();
            }

            tempFile.renameTo(file);
         }
      } catch (IOException var18) {
         Pixelmon.LOGGER.error("Couldn't write player data file for " + storage.uuid.toString(), var18);
      }

   }

   public PokemonStorage load(UUID uuid, Class clazz) {
      try {
         PokemonStorage storage = (PokemonStorage)clazz.getConstructor(UUID.class).newInstance(uuid);
         File file = storage.getFile();
         if (file.exists()) {
            try {
               DataInputStream dataStream = new DataInputStream(new FileInputStream(file));
               Throwable var23 = null;

               try {
                  NBTTagCompound nbt = CompressedStreamTools.func_74794_a(dataStream);
                  storage.readFromNBT(nbt);
               } catch (Throwable var19) {
                  var23 = var19;
                  throw var19;
               } finally {
                  if (dataStream != null) {
                     if (var23 != null) {
                        try {
                           dataStream.close();
                        } catch (Throwable var18) {
                           var23.addSuppressed(var18);
                        }
                     } else {
                        dataStream.close();
                     }
                  }

               }
            } catch (Exception var21) {
               Pixelmon.LOGGER.error("Couldn't load player data file for " + uuid.toString(), var21);

               try {
                  File backupFile = new File(file.getParentFile(), file.getName() + ".backup");
                  Files.copy(file, backupFile);
               } catch (Exception var17) {
                  Pixelmon.LOGGER.error("Unable to save a backup", var21);
               }

               storage = (PokemonStorage)clazz.getConstructor(UUID.class).newInstance(uuid);
            }

            return storage;
         } else {
            return storage;
         }
      } catch (Exception var22) {
         Pixelmon.LOGGER.error("Failed to load storage! " + clazz.getSimpleName() + ", UUID: " + uuid.toString());
         var22.printStackTrace();
         return null;
      }
   }
}
