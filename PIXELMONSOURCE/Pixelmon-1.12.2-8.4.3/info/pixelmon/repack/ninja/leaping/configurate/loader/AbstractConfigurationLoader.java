package info.pixelmon.repack.ninja.leaping.configurate.loader;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import info.pixelmon.repack.ninja.leaping.configurate.ConfigurationNode;
import info.pixelmon.repack.ninja.leaping.configurate.ConfigurationOptions;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.Callable;

public abstract class AbstractConfigurationLoader implements ConfigurationLoader {
   public static final String CONFIGURATE_LINE_SEPARATOR = "\n";
   protected static final Splitter LINE_SPLITTER = Splitter.on("\n");
   protected static final String SYSTEM_LINE_SEPARATOR = System.lineSeparator();
   protected final Callable source;
   private final Callable sink;
   private final CommentHandler[] commentHandlers;
   private final HeaderMode headerMode;
   private final ConfigurationOptions defaultOptions;

   protected AbstractConfigurationLoader(Builder builder, CommentHandler[] commentHandlers) {
      this.source = builder.getSource();
      this.sink = builder.getSink();
      this.headerMode = builder.getHeaderMode();
      this.defaultOptions = builder.getDefaultOptions();
      this.commentHandlers = commentHandlers;
   }

   public CommentHandler getDefaultCommentHandler() {
      return this.commentHandlers[0];
   }

   public ConfigurationNode load(ConfigurationOptions options) throws IOException {
      if (!this.canLoad()) {
         throw new IOException("No source present to read from!");
      } else {
         try {
            BufferedReader reader = (BufferedReader)this.source.call();
            Throwable var3 = null;

            ConfigurationNode var20;
            try {
               if (this.headerMode == HeaderMode.PRESERVE || this.headerMode == HeaderMode.NONE) {
                  String comment = CommentHandlers.extractComment(reader, this.commentHandlers);
                  if (comment != null && comment.length() > 0) {
                     options = options.setHeader(comment);
                  }
               }

               ConfigurationNode node = this.createEmptyNode(options);
               this.loadInternal(node, reader);
               var20 = node;
            } catch (Throwable var16) {
               var3 = var16;
               throw var16;
            } finally {
               if (reader != null) {
                  if (var3 != null) {
                     try {
                        reader.close();
                     } catch (Throwable var15) {
                        var3.addSuppressed(var15);
                     }
                  } else {
                     reader.close();
                  }
               }

            }

            return var20;
         } catch (NoSuchFileException | FileNotFoundException var18) {
            return this.createEmptyNode(options);
         } catch (Exception var19) {
            if (var19 instanceof IOException) {
               throw (IOException)var19;
            } else {
               throw new IOException(var19);
            }
         }
      }
   }

   protected abstract void loadInternal(ConfigurationNode var1, BufferedReader var2) throws IOException;

   public void save(ConfigurationNode node) throws IOException {
      if (!this.canSave()) {
         throw new IOException("No sink present to write to!");
      } else {
         try {
            Writer writer = (Writer)this.sink.call();
            Throwable var3 = null;

            try {
               if (this.headerMode != HeaderMode.NONE) {
                  String header = node.getOptions().getHeader();
                  if (header != null && !header.isEmpty()) {
                     Iterator var5 = this.getDefaultCommentHandler().toComment(ImmutableList.copyOf(LINE_SPLITTER.split(header))).iterator();

                     while(var5.hasNext()) {
                        String line = (String)var5.next();
                        writer.write(line);
                        writer.write(SYSTEM_LINE_SEPARATOR);
                     }

                     writer.write(SYSTEM_LINE_SEPARATOR);
                  }
               }

               this.saveInternal(node, writer);
            } catch (Throwable var15) {
               var3 = var15;
               throw var15;
            } finally {
               if (writer != null) {
                  if (var3 != null) {
                     try {
                        writer.close();
                     } catch (Throwable var14) {
                        var3.addSuppressed(var14);
                     }
                  } else {
                     writer.close();
                  }
               }

            }

         } catch (Exception var17) {
            if (var17 instanceof IOException) {
               throw (IOException)var17;
            } else {
               throw new IOException(var17);
            }
         }
      }
   }

   protected abstract void saveInternal(ConfigurationNode var1, Writer var2) throws IOException;

   public ConfigurationOptions getDefaultOptions() {
      return this.defaultOptions;
   }

   public boolean canLoad() {
      return this.source != null;
   }

   public boolean canSave() {
      return this.sink != null;
   }

   protected abstract static class Builder {
      protected HeaderMode headerMode;
      protected Callable source;
      protected Callable sink;
      protected ConfigurationOptions defaultOptions;

      protected Builder() {
         this.headerMode = HeaderMode.PRESERVE;
         this.defaultOptions = ConfigurationOptions.defaults();
      }

      private Builder self() {
         return this;
      }

      public Builder setFile(File file) {
         return this.setPath(file.toPath());
      }

      public Builder setPath(Path path) {
         Path absPath = ((Path)Objects.requireNonNull(path, "path")).toAbsolutePath();
         this.source = () -> {
            return Files.newBufferedReader(absPath, StandardCharsets.UTF_8);
         };
         this.sink = AtomicFiles.createAtomicWriterFactory(absPath, StandardCharsets.UTF_8);
         return this.self();
      }

      public Builder setURL(URL url) {
         this.source = () -> {
            return new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(), StandardCharsets.UTF_8));
         };
         return this.self();
      }

      public Builder setSource(Callable source) {
         this.source = source;
         return this.self();
      }

      public Builder setSink(Callable sink) {
         this.sink = sink;
         return this.self();
      }

      public Callable getSource() {
         return this.source;
      }

      public Callable getSink() {
         return this.sink;
      }

      /** @deprecated */
      @Deprecated
      public Builder setPreservesHeader(boolean preservesHeader) {
         this.headerMode = preservesHeader ? HeaderMode.PRESERVE : HeaderMode.PRESET;
         return this.self();
      }

      /** @deprecated */
      @Deprecated
      public boolean preservesHeader() {
         return this.headerMode == HeaderMode.PRESERVE;
      }

      public Builder setHeaderMode(HeaderMode mode) {
         this.headerMode = mode;
         return this.self();
      }

      public HeaderMode getHeaderMode() {
         return this.headerMode;
      }

      public Builder setDefaultOptions(ConfigurationOptions defaultOptions) {
         this.defaultOptions = (ConfigurationOptions)Objects.requireNonNull(defaultOptions, "defaultOptions");
         return this.self();
      }

      public ConfigurationOptions getDefaultOptions() {
         return this.defaultOptions;
      }

      public abstract AbstractConfigurationLoader build();
   }
}
