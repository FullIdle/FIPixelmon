package info.pixelmon.repack.ninja.leaping.configurate.loader;

import com.google.common.collect.Collections2;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public enum CommentHandlers implements CommentHandler {
   HASH("#"),
   DOUBLE_SLASH("//"),
   SLASH_BLOCK {
      public Optional extractHeader(BufferedReader reader) throws IOException {
         StringBuilder build = new StringBuilder();
         String line = reader.readLine();
         if (line == null) {
            return Optional.empty();
         } else if (!line.trim().startsWith("/*")) {
            return Optional.empty();
         } else {
            line = line.substring(line.indexOf("/*") + 2);
            if (this.handleSingleLine(build, line)) {
               for(line = reader.readLine(); line != null && this.handleSingleLine(build, line); line = reader.readLine()) {
               }
            }

            line = reader.readLine();
            if (line != null && !line.trim().isEmpty()) {
               return Optional.empty();
            } else {
               return build.length() > 0 ? Optional.of(build.toString()) : Optional.empty();
            }
         }
      }

      private boolean handleSingleLine(StringBuilder builder, String line) {
         boolean moreLines = true;
         if (line.trim().endsWith("*/")) {
            line = line.substring(0, line.lastIndexOf("*/"));
            if (line.endsWith(" ")) {
               line = line.substring(0, line.length() - 1);
            }

            moreLines = false;
            if (line.isEmpty()) {
               return false;
            }
         }

         if (line.trim().startsWith("*")) {
            line = line.substring(line.indexOf("*") + 1);
         }

         if (line.startsWith(" ")) {
            line = line.substring(1);
         }

         if (builder.length() > 0) {
            builder.append("\n");
         }

         builder.append(line.replace("\r", "").replace("\n", "").replace("\r\n", ""));
         return moreLines;
      }

      public Collection toComment(Collection lines) {
         if (lines.size() == 1) {
            return (Collection)lines.stream().map((i) -> {
               return "/* " + i + " */";
            }).collect(Collectors.toList());
         } else {
            Collection ret = new ArrayList();
            ret.add("/*");
            ret.addAll((Collection)lines.stream().map((i) -> {
               return " * " + i;
            }).collect(Collectors.toList()));
            ret.add(" */");
            return ret;
         }
      }
   };

   private static final int READAHEAD_LEN = 4096;
   private final String commentPrefix;

   private CommentHandlers() {
      this.commentPrefix = null;
   }

   private CommentHandlers(String commentPrefix) {
      this.commentPrefix = commentPrefix;
   }

   public Optional extractHeader(BufferedReader reader) throws IOException {
      StringBuilder build = new StringBuilder();

      for(String line = reader.readLine(); line != null; line = reader.readLine()) {
         if (!line.trim().startsWith(this.commentPrefix)) {
            if (!line.trim().isEmpty()) {
               return Optional.empty();
            }
            break;
         }

         line = line.substring(line.indexOf(this.commentPrefix) + 1);
         if (line.startsWith(" ")) {
            line = line.substring(1);
         }

         if (build.length() > 0) {
            build.append("\n");
         }

         build.append(line);
      }

      return build.length() > 0 ? Optional.ofNullable(build.toString()) : Optional.empty();
   }

   public Collection toComment(Collection lines) {
      return Collections2.transform(lines, (s) -> {
         return s.startsWith(" ") ? this.commentPrefix + s : this.commentPrefix + " " + s;
      });
   }

   public static String extractComment(BufferedReader reader, CommentHandler... allowedHeaderTypes) throws IOException {
      reader.mark(4096);
      CommentHandler[] var2 = allowedHeaderTypes;
      int var3 = allowedHeaderTypes.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         CommentHandler handler = var2[var4];
         Optional comment = handler.extractHeader(reader);
         if (comment.isPresent()) {
            return (String)comment.get();
         }

         reader.reset();
      }

      return null;
   }

   // $FF: synthetic method
   CommentHandlers(Object x2) {
      this();
   }
}
