package info.pixelmon.repack.ninja.leaping.configurate.loader;

import com.google.common.base.Preconditions;
import java.io.BufferedWriter;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.concurrent.Callable;

public class AtomicFiles {
   private static final FileAttribute[] EMPTY_ATTRIBUTE_ARR = new FileAttribute[0];

   private AtomicFiles() {
   }

   public static Callable createAtomicWriterFactory(Path path, Charset charset) {
      Preconditions.checkNotNull(path, "path");
      return () -> {
         return createAtomicBufferedWriter(path, charset);
      };
   }

   private static Path getTemporaryPath(Path parent, String key) {
      String fileName = System.nanoTime() + ((String)Preconditions.checkNotNull(key, "key")).replaceAll("\\\\|/|:", "-") + ".tmp";
      return parent.resolve(fileName);
   }

   public static BufferedWriter createAtomicBufferedWriter(Path path, Charset charset) throws IOException {
      path = path.toAbsolutePath();
      Path writePath = getTemporaryPath(path.getParent(), path.getFileName().toString());
      if (Files.exists(path, new LinkOption[0])) {
         Files.copy(path, writePath, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
      }

      BufferedWriter output = Files.newBufferedWriter(writePath, charset);
      return new BufferedWriter(new AtomicFileWriter(writePath, path, output));
   }

   private static class AtomicFileWriter extends FilterWriter {
      private final Path targetPath;
      private final Path writePath;

      protected AtomicFileWriter(Path writePath, Path targetPath, Writer wrapping) throws IOException {
         super(wrapping);
         this.writePath = writePath;
         this.targetPath = targetPath;
      }

      public void close() throws IOException {
         super.close();
         Files.move(this.writePath, this.targetPath, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
      }
   }
}
