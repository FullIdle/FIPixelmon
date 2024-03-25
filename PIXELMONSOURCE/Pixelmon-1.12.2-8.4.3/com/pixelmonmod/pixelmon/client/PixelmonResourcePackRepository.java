package com.pixelmonmod.pixelmon.client;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenWorking;
import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.LegacyV2Adapter;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(Side.CLIENT)
public class PixelmonResourcePackRepository extends ResourcePackRepository {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final FileFilter RESOURCE_PACK_FILTER = (p_accept_1_) -> {
      boolean flag = p_accept_1_.isFile() && p_accept_1_.getName().endsWith(".zip");
      boolean flag1 = p_accept_1_.isDirectory() && (new File(p_accept_1_, "pack.mcmeta")).isFile();
      return flag || flag1;
   };
   private static final Pattern SHA1 = Pattern.compile("^[a-fA-F0-9]{40}$");
   private static final ResourceLocation UNKNOWN_PACK_TEXTURE = new ResourceLocation("textures/misc/unknown_pack.png");
   private final File dirServerResourcepacks;
   private final ReentrantLock lock = new ReentrantLock();
   private ListenableFuture downloadingPacks;
   private static Constructor constructorFile = null;
   private static Constructor constructorIResourcePack = null;

   public PixelmonResourcePackRepository(File dirResourcepacksIn, File dirServerResourcepacksIn, IResourcePack rprDefaultResourcePackIn, MetadataSerializer rprMetadataSerializerIn, GameSettings settings) {
      super(dirResourcepacksIn, dirServerResourcepacksIn, rprDefaultResourcePackIn, rprMetadataSerializerIn, settings);
      this.dirServerResourcepacks = dirServerResourcepacksIn;
   }

   public static Map getDownloadHeaders() {
      Map map = Maps.newHashMap();
      map.put("X-Minecraft-Username", Minecraft.func_71410_x().func_110432_I().func_111285_a());
      map.put("X-Minecraft-UUID", Minecraft.func_71410_x().func_110432_I().func_148255_b());
      map.put("X-Minecraft-Version", "1.12.2");
      return map;
   }

   private IResourcePack getResourcePack(File p_191399_1_) {
      Object iresourcepack;
      if (p_191399_1_.isDirectory()) {
         iresourcepack = new FolderResourcePack(p_191399_1_);
      } else {
         iresourcepack = new FileResourcePack(p_191399_1_);
      }

      try {
         PackMetadataSection packmetadatasection = (PackMetadataSection)((IResourcePack)iresourcepack).func_135058_a(this.field_110621_c, "pack");
         if (packmetadatasection != null && packmetadatasection.func_110462_b() == 2) {
            return new LegacyV2Adapter((IResourcePack)iresourcepack);
         }
      } catch (Exception var4) {
      }

      return (IResourcePack)iresourcepack;
   }

   public ListenableFuture func_180601_a(String url, String hash) {
      String s = DigestUtils.sha1Hex(url);
      final String s1 = SHA1.matcher(hash).matches() ? hash : "";
      final File file1 = new File(this.dirServerResourcepacks, s);
      this.lock.lock();

      ListenableFuture var11;
      try {
         this.func_148529_f();
         if (file1.exists()) {
            if (this.checkHash(s1, file1)) {
               ListenableFuture var15 = this.func_177319_a(file1);
               return var15;
            }

            LOGGER.warn("Deleting file {}", file1);
            FileUtils.deleteQuietly(file1);
         }

         this.deleteOldServerResourcesPacks();
         GuiScreenWorking guiscreenworking = new GuiScreenWorking();
         Map map = getDownloadHeaders();
         Minecraft minecraft = Minecraft.func_71410_x();
         Futures.getUnchecked(minecraft.func_152344_a(() -> {
            minecraft.func_147108_a(guiscreenworking);
         }));
         final SettableFuture settablefuture = SettableFuture.create();
         this.downloadingPacks = HttpUtil.func_180192_a(file1, url, map, 629145600, guiscreenworking, minecraft.func_110437_J());
         Futures.addCallback(this.downloadingPacks, new FutureCallback() {
            public void onSuccess(@Nullable Object p_onSuccess_1_) {
               if (PixelmonResourcePackRepository.this.checkHash(s1, file1)) {
                  PixelmonResourcePackRepository.this.func_177319_a(file1);
                  settablefuture.set((Object)null);
               } else {
                  PixelmonResourcePackRepository.LOGGER.warn("Deleting file {}", file1);
                  FileUtils.deleteQuietly(file1);
               }

            }

            public void onFailure(Throwable p_onFailure_1_) {
               FileUtils.deleteQuietly(file1);
               settablefuture.setException(p_onFailure_1_);
            }
         });
         ListenableFuture listenablefuture = this.downloadingPacks;
         var11 = listenablefuture;
      } finally {
         this.lock.unlock();
      }

      return var11;
   }

   private boolean checkHash(String p_190113_1_, File p_190113_2_) {
      InputStream is = null;

      try {
         String s = DigestUtils.sha1Hex(is = new FileInputStream(p_190113_2_));
         boolean var5;
         if (p_190113_1_.isEmpty()) {
            LOGGER.info("Found file {} without verification hash", p_190113_2_);
            var5 = true;
            return var5;
         }

         if (s.toLowerCase(Locale.ROOT).equals(p_190113_1_.toLowerCase(Locale.ROOT))) {
            LOGGER.info("Found file {} matching requested hash {}", p_190113_2_, p_190113_1_);
            var5 = true;
            return var5;
         }

         LOGGER.warn("File {} had wrong hash (expected {}, found {}).", p_190113_2_, p_190113_1_, s);
      } catch (IOException var9) {
         LOGGER.warn("File {} couldn't be hashed.", p_190113_2_, var9);
      } finally {
         IOUtils.closeQuietly(is);
      }

      return false;
   }

   private boolean validatePack(File p_190112_1_) {
      ResourcePackRepository.Entry resourcepackrepository$entry = this.constructEntry(p_190112_1_);

      try {
         resourcepackrepository$entry.func_110516_a();
         return true;
      } catch (Exception var4) {
         LOGGER.warn("Server resourcepack is invalid, ignoring it", var4);
         return false;
      }
   }

   private void deleteOldServerResourcesPacks() {
      try {
         List list = Lists.newArrayList(FileUtils.listFiles(this.dirServerResourcepacks, TrueFileFilter.TRUE, (IOFileFilter)null));
         Collections.sort(list, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
         int i = 0;
         Iterator var3 = list.iterator();

         while(var3.hasNext()) {
            File file1 = (File)var3.next();
            if (i++ >= 10) {
               LOGGER.info("Deleting old server resource pack {}", file1.getName());
               FileUtils.deleteQuietly(file1);
            }
         }
      } catch (IllegalArgumentException var5) {
         LOGGER.error("Error while deleting old server resource pack : {}", var5.getMessage());
      }

   }

   private ResourcePackRepository.Entry constructEntry(File resourcePackFileIn) {
      if (constructorFile == null) {
         try {
            constructorFile = ResourcePackRepository.Entry.class.getDeclaredConstructor(File.class);
            constructorFile.setAccessible(true);
         } catch (Exception var4) {
         }
      }

      try {
         return (ResourcePackRepository.Entry)constructorFile.newInstance(resourcePackFileIn);
      } catch (Exception var3) {
         return null;
      }
   }

   private ResourcePackRepository.Entry constructEntry(IResourcePack reResourcePackIn) {
      if (constructorIResourcePack == null) {
         try {
            constructorIResourcePack = ResourcePackRepository.Entry.class.getDeclaredConstructor(IResourcePack.class);
            constructorIResourcePack.setAccessible(true);
         } catch (Exception var4) {
         }
      }

      try {
         return (ResourcePackRepository.Entry)constructorIResourcePack.newInstance(reResourcePackIn);
      } catch (Exception var3) {
         return null;
      }
   }
}
