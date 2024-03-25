package com.pixelmonmod.pixelmon.storage.deepstorage;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class DeepStorageManager {
   private static final HashMap deepStorages = new HashMap();

   public static File getFile(UUID uuid) {
      return new File(DimensionManager.getCurrentSaveRootDirectory(), "pokemon/deepStorage/" + uuid.toString() + ".deep");
   }

   public static void bury(UUID uuid, ArrayList unaddedPokemon, boolean party) {
      DeepStorage deepStorage = getOrCreateDeepStorage(uuid);
      if (deepStorage != null) {
         Iterator var4 = unaddedPokemon.iterator();

         while(var4.hasNext()) {
            NBTTagCompound nbt = (NBTTagCompound)var4.next();
            deepStorage.put(nbt);
         }

         save(uuid);
         EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_177451_a(uuid);
         if (player != null) {
            TextComponentString msg = new TextComponentString(TextFormatting.GRAY + "" + unaddedPokemon.size() + (party ? " party" : " PC") + " un-added Pokémon have been safely stowed in deep storage");
            player.func_145747_a(msg);
         }
      }

   }

   public static boolean hasPokemonInDeepStorage(UUID uuid) {
      File file = getFile(uuid);
      return file.exists();
   }

   public static DeepStorage getOrCreateDeepStorage(UUID uuid) {
      if (deepStorages.containsKey(uuid)) {
         return (DeepStorage)deepStorages.get(uuid);
      } else if (hasPokemonInDeepStorage(uuid)) {
         File file = getFile(uuid);
         file.getParentFile().mkdirs();

         try {
            DataInputStream dataStream = new DataInputStream(new FileInputStream(file));
            Throwable var3 = null;

            DeepStorage var6;
            try {
               NBTTagCompound nbt = CompressedStreamTools.func_74794_a(dataStream);
               DeepStorage deepStorage = new DeepStorage(nbt);
               deepStorages.put(uuid, deepStorage);
               var6 = deepStorage;
            } catch (Throwable var16) {
               var3 = var16;
               throw var16;
            } finally {
               if (dataStream != null) {
                  if (var3 != null) {
                     try {
                        dataStream.close();
                     } catch (Throwable var15) {
                        var3.addSuppressed(var15);
                     }
                  } else {
                     dataStream.close();
                  }
               }

            }

            return var6;
         } catch (IOException var18) {
            if (PixelmonConfig.printErrors) {
               Pixelmon.LOGGER.error("Couldn't read deep store data file for " + uuid.toString(), var18);
            }

            return null;
         }
      } else {
         NBTTagCompound nbt = new NBTTagCompound();
         DeepStorage deepStorage = new DeepStorage(nbt);
         deepStorages.put(uuid, deepStorage);
         return deepStorage;
      }
   }

   public static void save(UUID uuid) {
      DeepStorage deepStorage = (DeepStorage)deepStorages.get(uuid);
      if (deepStorage != null) {
         File file = getFile(uuid);
         file.getParentFile().mkdirs();
         file.delete();
         if (!deepStorage.isEmpty()) {
            deepStorage.write(file);
         }
      }

   }
}
