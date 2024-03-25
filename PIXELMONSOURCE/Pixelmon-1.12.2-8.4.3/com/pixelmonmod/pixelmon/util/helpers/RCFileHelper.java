package com.pixelmonmod.pixelmon.util.helpers;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import net.minecraft.util.ResourceLocation;

public class RCFileHelper {
   public static String encodePath(String path) {
      return path.replaceAll(" ", "%20");
   }

   static Path resourceToPath(URL resource) throws IOException, URISyntaxException {
      Objects.requireNonNull(resource, "Resource URL cannot be null");
      URI uri = resource.toURI();
      String scheme = uri.getScheme();
      if (scheme.equals("file")) {
         return Paths.get(uri);
      } else if (!scheme.equals("jar")) {
         throw new IllegalArgumentException("Cannot convert to Path: " + uri);
      } else {
         String s = encodePath(uri.toString());
         int separator = s.indexOf("!/");
         String entryName = s.substring(separator + 2);
         URI fileURI = URI.create(s.substring(0, separator));

         FileSystem fs;
         try {
            fs = FileSystems.getFileSystem(fileURI);
            if (fs.isOpen()) {
               return fs.getPath(entryName);
            }
         } catch (FileSystemNotFoundException var8) {
         }

         fs = FileSystems.newFileSystem(fileURI, Collections.emptyMap());
         return fs.getPath(entryName);
      }
   }

   public static Path pathFromResourceLocation(ResourceLocation resourceLocation) throws URISyntaxException, IOException {
      URL resource = RCFileHelper.class.getResource(String.format("/assets/%s/%s", resourceLocation.func_110624_b(), resourceLocation.func_110623_a()));
      return resource != null ? resourceToPath(resource.toURI().toURL()) : null;
   }

   public static List listFilesRecursively(Path dir, final DirectoryStream.Filter filter, final boolean recursive) throws IOException {
      final List files = new ArrayList();
      Files.walkFileTree(dir, new SimpleFileVisitor() {
         public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (filter.accept(file)) {
               files.add(file);
            }

            return recursive ? FileVisitResult.CONTINUE : FileVisitResult.SKIP_SUBTREE;
         }
      });
      return files;
   }

   public static void recursiveJSONSearch(String dir, ArrayList jsons) {
      File file = new File(dir);
      String[] var3 = file.list();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String name = var3[var5];
         File subFile = new File(dir + "/" + name);
         if (subFile.isFile() && name.endsWith(".json")) {
            jsons.add(subFile);
         } else if (subFile.isDirectory()) {
            recursiveJSONSearch(dir + "/" + name, jsons);
         }
      }

   }

   public static File getValidatedFolder(File file, boolean create) {
      if (create && !file.exists() && !file.mkdir()) {
         System.out.println("Could not create " + file.getName() + " folder");
      }

      return file.exists() ? file : null;
   }

   public static File getValidatedFolder(File parent, String child, boolean create) {
      return getValidatedFolder(new File(parent, child), create);
   }
}
