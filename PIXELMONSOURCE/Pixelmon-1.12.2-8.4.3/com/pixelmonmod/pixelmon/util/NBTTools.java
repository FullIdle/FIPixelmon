package com.pixelmonmod.pixelmon.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.BlockSnapshot;

public class NBTTools {
   public static final Gson gson = (new GsonBuilder()).create();

   public static String serializeBlockSnapshot(BlockSnapshot snapshot) {
      NBTTagCompound nbt = new NBTTagCompound();
      snapshot.writeToNBT(nbt);
      return gson.toJson(nbtToMap(nbt));
   }

   public static BlockSnapshot deserializeBlockSnapshot(String json) {
      Map map = (Map)gson.fromJson(json, Map.class);
      return BlockSnapshot.readFromNBT(nbtFromMap(map));
   }

   public static Map nbtToMap(NBTTagCompound nbt) {
      Map map = new HashMap();
      Iterator var2 = nbt.func_150296_c().iterator();

      while(var2.hasNext()) {
         String key = (String)var2.next();

         try {
            NBTBase base = nbt.func_74781_a(key);
            if (base instanceof NBTTagString) {
               map.put(key, ((NBTTagString)base).func_150285_a_());
            } else if (base instanceof NBTPrimitive) {
               map.put(key, ((NBTPrimitive)base).func_150286_g());
            } else if (base instanceof NBTTagCompound) {
               map.put(key, nbtToMap((NBTTagCompound)base));
            }
         } catch (Exception var5) {
         }
      }

      return map;
   }

   public static NBTTagCompound nbtFromMap(Map map) {
      NBTTagCompound nbt = new NBTTagCompound();
      Iterator var2 = map.keySet().iterator();

      while(var2.hasNext()) {
         String key = (String)var2.next();

         try {
            if (map.get(key) instanceof List) {
               List list = (List)map.get(key);
               NBTTagList tagList = new NBTTagList();

               for(int i = 0; i < list.size(); ++i) {
                  tagList.func_74742_a(new NBTTagString((String)list.get(i)));
               }

               nbt.func_74782_a(key, tagList);
            }

            if (map.get(key) instanceof String) {
               nbt.func_74778_a(key, (String)map.get(key));
            } else if (map.get(key) instanceof Map) {
               nbt.func_74782_a(key, nbtFromMap((Map)map.get(key)));
            } else {
               Double d = (Double)map.get(key);
               if ((double)Math.round(d) == d) {
                  nbt.func_74768_a(key, (int)Math.round(d));
               } else {
                  nbt.func_74780_a(key, d);
               }
            }
         } catch (Exception var7) {
         }
      }

      return nbt;
   }

   public static NBTTagCompound loadNBT(File file) throws IOException {
      FileInputStream in = null;
      NBTTagCompound result = null;

      try {
         in = new FileInputStream(file);
         result = CompressedStreamTools.func_74796_a(in);
      } catch (IOException var8) {
         try {
            result = CompressedStreamTools.func_74797_a(file);
         } finally {
            in.close();
         }
      }

      return result;
   }

   public static void saveNBT(NBTTagCompound nbt, File file, boolean compressed) throws IOException {
      file.getParentFile().mkdirs();
      file.createNewFile();
      saveNBT(nbt, (OutputStream)(new FileOutputStream(file)), compressed);
   }

   public static void saveNBT(NBTTagCompound nbt, OutputStream out, boolean compressed) throws IOException {
      if (compressed) {
         CompressedStreamTools.func_74799_a(nbt, out);
      } else {
         CompressedStreamTools.func_74800_a(nbt, new DataOutputStream(out));
      }

   }
}
