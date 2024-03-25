package com.pixelmonmod.pixelmon.storage.extras;

import com.google.common.collect.Maps;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.util.RegexPatterns;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;

public class ExtrasContact {
   private static final ExecutorService executorService = Executors.newCachedThreadPool();
   private static final Map cache = Maps.newHashMap();
   private static final String DIRECT = "https://api.pixelmonmod.com/arctic/" + getVersion() + "/mc_group.php?uuid=";
   private static final String CACHE = "https://ogn.pixelmonmod.com/arctic/" + getVersion() + "/mc_group.php?uuid=";
   private static final UUID JAY = UUID.fromString("8139124d-840a-486e-802e-d08b4a66a08c");
   private static final UUID ISI = UUID.fromString("f93a4a85-8d0d-415e-8a39-420feeab53e8");

   private static Tuple queryApi(UUID uuid, boolean force) throws IOException {
      if (!Pixelmon.devEnvironment || !uuid.equals(JAY) && !uuid.equals(ISI)) {
         String uuidStr = RegexPatterns.DASH_SYMBOL.matcher(uuid.toString()).replaceAll("");
         URL url = new URL((force ? DIRECT : CACHE) + uuidStr);
         URLConnection connection = url.openConnection();
         connection.setConnectTimeout(5000);
         connection.setReadTimeout(5000);
         String response = IOUtils.toString(connection.getInputStream(), StandardCharsets.UTF_8);
         return parseData(response);
      } else {
         return new Tuple(EnumSet.allOf(Groups.class), new int[]{0, 0, 0});
      }
   }

   private static Tuple parseData(String responce) {
      String[] splits = responce.split(";");
      String[] groups = splits[0].split(",");
      EnumSet list = EnumSet.noneOf(Groups.class);
      String[] var4 = groups;
      int var5 = groups.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String group = var4[var6];
         String g = RegexPatterns.NEWLINE_CHAR.matcher(group).replaceAll("").trim();
         int grp = Integer.parseInt(g);
         if (ExtrasContact.Groups.getFromId(grp) != null) {
            list.add(ExtrasContact.Groups.getFromId(grp));
         }
      }

      int[] colours = new int[3];
      if (splits.length > 1) {
         String[] str = splits[1].split(",");
         colours[0] = Integer.parseInt(str[0]);
         colours[1] = Integer.parseInt(str[1]);
         colours[2] = Integer.parseInt(str[2]);
      }

      return new Tuple(list, colours);
   }

   @SideOnly(Side.CLIENT)
   public static void updateSelf(PixelExtrasData data, boolean update, Consumer consumer) {
      UUID uuid = Minecraft.func_71410_x().func_110432_I().func_148256_e().getId();
      if (!update && fromCache(data)) {
         consumer.accept(data);
      } else {
         executorService.submit(() -> {
            try {
               data.apiData = queryApi(uuid, update);
               StringJoiner joiner = new StringJoiner(", ", "[", "]");
               ((EnumSet)data.apiData.func_76341_a()).forEach((g) -> {
                  joiner.add(g.id + "");
               });
               Pixelmon.LOGGER.info("Query response: groups" + joiner + " colors" + Arrays.toString((int[])data.apiData.func_76340_b()));
               cache.put(data.id, new Tuple(data.apiData, Instant.now()));
            } catch (IOException var5) {
               if (getVersion().startsWith("8")) {
                  Pixelmon.LOGGER.info("Couldn't connect to Pixelmon servers: " + var5.getLocalizedMessage());
               }
            }

            consumer.accept(data);
         });
      }

   }

   public static void updateData(PixelExtrasData data, int hash, Consumer consumer) {
      if (fromCache(data) && checkCacheHash(data.id, hash)) {
         consumer.accept(data);
      } else {
         executorService.submit(() -> {
            try {
               data.apiData = queryApi(data.id, false);
               cache.put(data.id, new Tuple(data.apiData, Instant.now()));
            } catch (IOException var3) {
               if (getVersion().startsWith("8")) {
                  Pixelmon.LOGGER.info("Couldn't connect to Pixelmon servers: " + var3.getLocalizedMessage());
               }
            }

            consumer.accept(data);
         });
      }

   }

   public static boolean checkCacheHash(UUID uuid, int hash) {
      int h = 0;
      if (!cache.containsKey(uuid)) {
         return false;
      } else {
         Iterator var3 = ((EnumSet)((Tuple)((Tuple)cache.get(uuid)).func_76341_a()).func_76341_a()).iterator();

         while(var3.hasNext()) {
            Groups group = (Groups)var3.next();
            if (group != null) {
               h = 31 * h + group.ordinal() + 1;
            }
         }

         return h == hash;
      }
   }

   static boolean fromCache(PixelExtrasData data) {
      if (data.id.version() != 4) {
         int[] colours = new int[3];
         Arrays.fill(colours, -1);
         data.apiData = new Tuple(EnumSet.noneOf(Groups.class), colours);
         return true;
      } else if (cache.containsKey(data.id) && ((Instant)((Tuple)cache.get(data.id)).func_76340_b()).getEpochSecond() > Instant.now().getEpochSecond() - 604800L) {
         data.apiData = (Tuple)((Tuple)cache.get(data.id)).func_76341_a();
         return true;
      } else {
         return false;
      }
   }

   private static void saveCache() {
      Properties properties = new Properties();
      Iterator iterator = cache.entrySet().iterator();

      while(true) {
         while(iterator.hasNext()) {
            Map.Entry entry = (Map.Entry)iterator.next();
            if (((Instant)((Tuple)entry.getValue()).func_76340_b()).getEpochSecond() < Instant.now().getEpochSecond() - 604800L) {
               iterator.remove();
            } else {
               Instant instant = (Instant)((Tuple)entry.getValue()).func_76340_b();
               EnumSet groups = (EnumSet)((Tuple)((Tuple)entry.getValue()).func_76341_a()).func_76341_a();
               int[] colours = (int[])((Tuple)((Tuple)entry.getValue()).func_76341_a()).func_76340_b();
               ByteBuffer buf = ByteBuffer.allocate(8 + (groups.size() + 1) * 1 + 12);
               buf.putLong(instant.getEpochSecond());
               buf.put((byte)groups.size());
               Iterator var7 = groups.iterator();

               while(var7.hasNext()) {
                  Groups group = (Groups)var7.next();
                  buf.put((byte)group.id);
               }

               buf.putInt(colours[0]).putInt(colours[1]).putInt(colours[2]);
               properties.setProperty(((UUID)entry.getKey()).toString(), Base64.getEncoder().encodeToString(buf.array()));
            }
         }

         if (properties.isEmpty()) {
            return;
         }

         File file = new File(FMLCommonHandler.instance().getSavesDirectory().getParent(), "pixelc.dat");

         try {
            FileOutputStream out = new FileOutputStream(file);
            Throwable var22 = null;

            try {
               properties.store(out, "pixelmon cache");
            } catch (Throwable var17) {
               var22 = var17;
               throw var17;
            } finally {
               if (out != null) {
                  if (var22 != null) {
                     try {
                        out.close();
                     } catch (Throwable var16) {
                        var22.addSuppressed(var16);
                     }
                  } else {
                     out.close();
                  }
               }

            }
         } catch (IOException var19) {
         }

         return;
      }
   }

   private static void loadCache() {
      File file = new File(FMLCommonHandler.instance().getSavesDirectory().getParent(), "pixelc.dat");
      if (file.exists()) {
         Properties properties = new Properties();

         try {
            FileInputStream in = new FileInputStream(file);
            Throwable var3 = null;

            try {
               properties.load(in);
            } catch (Throwable var17) {
               var3 = var17;
               throw var17;
            } finally {
               if (in != null) {
                  if (var3 != null) {
                     try {
                        in.close();
                     } catch (Throwable var16) {
                        var3.addSuppressed(var16);
                     }
                  } else {
                     in.close();
                  }
               }

            }
         } catch (IOException var19) {
            return;
         }

         file.delete();
         Iterator var20 = properties.keySet().iterator();

         while(var20.hasNext()) {
            Object key = var20.next();
            UUID uuid = UUID.fromString((String)key);
            ByteBuffer buf = ByteBuffer.wrap(Base64.getDecoder().decode(properties.getProperty(uuid.toString())));
            Instant instant = Instant.ofEpochSecond(buf.getLong());
            int count = buf.get();
            EnumSet groups = EnumSet.noneOf(Groups.class);

            for(int i = 0; i < count; ++i) {
               Groups group = ExtrasContact.Groups.getFromId(buf.get());
               if (group != null) {
                  groups.add(group);
               }
            }

            int[] colours = new int[]{buf.getInt(), buf.getInt(), buf.getInt()};
            cache.put(uuid, new Tuple(new Tuple(groups, colours), instant));
         }

      }
   }

   public static String getVersion() {
      return RegexPatterns.DOT_SYMBOL.matcher(((ModContainer)Loader.instance().getModObjectList().inverse().get(Pixelmon.instance)).getVersion()).replaceAll("");
   }

   static {
      loadCache();
      Runtime.getRuntime().addShutdownHook(new Thread(ExtrasContact::saveCache));
   }

   static enum Groups {
      Admin(5),
      Junior_Admin(21),
      Modeler(39),
      Developer(43),
      Support(44),
      CompWinner(53),
      Trainer_Cap(45),
      Sash(47),
      Spectral_Jeweller(48),
      Rainbow_Sash(56),
      Shadow_Lugia(62),
      AlterRobe(63),
      Pikahood(64),
      Monocle_Gold(65),
      Monocle_Black(66),
      Wiki(67),
      Wobbuffet(68),
      NitroBooster(69),
      DiscordLinked(70),
      EeveeHood(71),
      SphealHats(72),
      JASFPorygon_Z(73),
      PDSentret(74),
      ELGreninja(75),
      SFXMewtwo(76),
      HaaMask(77),
      QuarantineMask(79),
      Welkin(80),
      Froslass_Yukata(81),
      IRWLucario(82),
      StrikeRobe(83),
      AshenRobe(84),
      CinderaceGoku(85),
      BugcatcherMothim(86),
      ChristmasSpheal(87),
      AnniversaryHood(88);

      int id;

      private Groups(int id) {
         this.id = id;
      }

      public boolean is(Groups... groups) {
         Groups[] var2 = groups;
         int var3 = groups.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Groups group = var2[var4];
            if (this == group) {
               return true;
            }
         }

         return false;
      }

      public static Groups getFromId(int id) {
         Groups[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Groups group = var1[var3];
            if (id == group.id) {
               return group;
            }
         }

         return null;
      }
   }
}
