package com.pixelmonmod.pixelmon.client;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ListenableFuture;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.util.RegexPatterns;
import java.io.File;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.IProgressUpdate;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public class DefaultServerList {
   private static final Executor DOWNLOADER = Executors.newSingleThreadExecutor();
   private static final String URL = "https://ogn.pixelmonmod.com/servers/112/servers.dat";
   private static final ReentrantLock LOCK = new ReentrantLock();
   private static boolean searching = false;

   public static void tryFetchDefaultServers() {
      tryFetchDefaultServers((Runnable)null);
   }

   public static void tryFetchDefaultServers(@Nullable Runnable onComplete) {
      if (!searching) {
         ServerList serverListPre = new ServerList(Minecraft.func_71410_x());
         if (serverListPre.func_78856_c() == 0) {
            searching = true;
            fetchDefaultServers().addListener(() -> {
               if (onComplete != null) {
                  onComplete.run();
               }

            }, DOWNLOADER);
         }

      }
   }

   protected static ListenableFuture fetchDefaultServers() {
      LOCK.lock();

      ListenableFuture future;
      try {
         File file = new File(".", "servers.dat");
         String url = "https://ogn.pixelmonmod.com/servers/112/servers.dat".replace("%s", getVersion());
         Map map = getDownloadRequestProperties();
         future = HttpUtil.func_180192_a(file, url, map, 1258291200, (IProgressUpdate)null, Minecraft.func_71410_x().func_110437_J());
      } finally {
         LOCK.unlock();
      }

      return future;
   }

   public static String getVersion() {
      return RegexPatterns.DOT_SYMBOL.matcher(((ModContainer)Loader.instance().getModObjectList().inverse().get(Pixelmon.instance)).getVersion()).replaceAll("");
   }

   private static Map getDownloadRequestProperties() {
      Map map = Maps.newHashMap();
      map.put("X-Minecraft-Username", Minecraft.func_71410_x().func_110432_I().func_111285_a());
      map.put("X-Minecraft-UUID", Minecraft.func_71410_x().func_110432_I().func_148255_b());
      map.put("X-Minecraft-Version", "1.12.2");
      map.put("X-Pixelmon-Version", String.valueOf(Pixelmon.getVersion()));
      map.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
      return map;
   }
}
