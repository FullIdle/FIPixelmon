package com.pixelmonmod.pixelmon.util;

import com.mojang.authlib.GameProfile;
import com.pixelmonmod.pixelmon.Pixelmon;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class PixelmonPlayerUtils {
   private static Field usernameToProfileEntryMap;

   @Nullable
   public static EntityPlayerMP getUniquePlayerStartingWith(String username, ICommandSender sender) {
      if (username.startsWith("@")) {
         try {
            List playerMatches = EntitySelector.func_179656_b(sender, username, EntityPlayerMP.class);
            if (playerMatches.size() != 1) {
               return null;
            }

            return (EntityPlayerMP)playerMatches.get(0);
         } catch (CommandException var3) {
            var3.printStackTrace();
         }
      }

      return getUniquePlayerStartingWith(username);
   }

   @Nullable
   public static EntityPlayerMP getUniquePlayerStartingWith(String username) {
      List playerMatches = new ArrayList();
      Iterator var2 = FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_181057_v().iterator();

      while(var2.hasNext()) {
         EntityPlayerMP player = (EntityPlayerMP)var2.next();
         if (player.func_70005_c_().equalsIgnoreCase(username)) {
            return player;
         }

         if (player.func_70005_c_().toLowerCase().startsWith(username.toLowerCase())) {
            playerMatches.add(player);
         }
      }

      if (playerMatches.size() != 1) {
         return null;
      } else {
         return (EntityPlayerMP)playerMatches.get(0);
      }
   }

   @Nullable
   public static GameProfile getGameProfileNoLookup(PlayerProfileCache cache, String username) {
      try {
         Map map = (Map)usernameToProfileEntryMap.get(cache);
         return map.containsKey(username) ? ((PlayerProfileCache.ProfileEntry)map.get(username)).func_152668_a() : null;
      } catch (IllegalAccessException var3) {
         var3.printStackTrace();
         return null;
      }
   }

   static {
      try {
         usernameToProfileEntryMap = PlayerProfileCache.class.getDeclaredField(Pixelmon.devEnvironment ? "usernameToProfileEntryMap" : "field_152661_c");
         usernameToProfileEntryMap.setAccessible(true);
      } catch (NoSuchFieldException var1) {
         var1.printStackTrace();
      }

   }
}
