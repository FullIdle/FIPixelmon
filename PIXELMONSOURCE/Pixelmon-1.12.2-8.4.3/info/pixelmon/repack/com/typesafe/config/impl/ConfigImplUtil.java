package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigException;
import info.pixelmon.repack.com.typesafe.config.ConfigOrigin;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class ConfigImplUtil {
   static boolean equalsHandlingNull(Object a, Object b) {
      if (a == null && b != null) {
         return false;
      } else if (a != null && b == null) {
         return false;
      } else {
         return a == b ? true : a.equals(b);
      }
   }

   static boolean isC0Control(int codepoint) {
      return codepoint >= 0 && codepoint <= 31;
   }

   public static String renderJsonString(String s) {
      StringBuilder sb = new StringBuilder();
      sb.append('"');

      for(int i = 0; i < s.length(); ++i) {
         char c = s.charAt(i);
         switch (c) {
            case '\b':
               sb.append("\\b");
               break;
            case '\t':
               sb.append("\\t");
               break;
            case '\n':
               sb.append("\\n");
               break;
            case '\f':
               sb.append("\\f");
               break;
            case '\r':
               sb.append("\\r");
               break;
            case '"':
               sb.append("\\\"");
               break;
            case '\\':
               sb.append("\\\\");
               break;
            default:
               if (isC0Control(c)) {
                  sb.append(String.format("\\u%04x", Integer.valueOf(c)));
               } else {
                  sb.append(c);
               }
         }
      }

      sb.append('"');
      return sb.toString();
   }

   static String renderStringUnquotedIfPossible(String s) {
      if (s.length() == 0) {
         return renderJsonString(s);
      } else {
         int first = s.codePointAt(0);
         if (!Character.isDigit(first) && first != 45) {
            if (!s.startsWith("include") && !s.startsWith("true") && !s.startsWith("false") && !s.startsWith("null") && !s.contains("//")) {
               for(int i = 0; i < s.length(); ++i) {
                  char c = s.charAt(i);
                  if (!Character.isLetter(c) && !Character.isDigit(c) && c != '-') {
                     return renderJsonString(s);
                  }
               }

               return s;
            } else {
               return renderJsonString(s);
            }
         } else {
            return renderJsonString(s);
         }
      }
   }

   static boolean isWhitespace(int codepoint) {
      switch (codepoint) {
         case 10:
         case 32:
         case 160:
         case 8199:
         case 8239:
         case 65279:
            return true;
         default:
            return Character.isWhitespace(codepoint);
      }
   }

   public static String unicodeTrim(String s) {
      int length = s.length();
      if (length == 0) {
         return s;
      } else {
         int start = 0;

         int end;
         while(start < length) {
            end = s.charAt(start);
            if (end != 32 && end != 10) {
               int cp = s.codePointAt(start);
               if (!isWhitespace(cp)) {
                  break;
               }

               start += Character.charCount(cp);
            } else {
               ++start;
            }
         }

         end = length;

         while(end > start) {
            char c = s.charAt(end - 1);
            if (c != ' ' && c != '\n') {
               int cp;
               byte delta;
               if (Character.isLowSurrogate(c)) {
                  cp = s.codePointAt(end - 2);
                  delta = 2;
               } else {
                  cp = s.codePointAt(end - 1);
                  delta = 1;
               }

               if (!isWhitespace(cp)) {
                  break;
               }

               end -= delta;
            } else {
               --end;
            }
         }

         return s.substring(start, end);
      }
   }

   public static ConfigException extractInitializerError(ExceptionInInitializerError e) {
      Throwable cause = e.getCause();
      if (cause != null && cause instanceof ConfigException) {
         return (ConfigException)cause;
      } else {
         throw e;
      }
   }

   static File urlToFile(URL url) {
      try {
         return new File(url.toURI());
      } catch (URISyntaxException var2) {
         return new File(url.getPath());
      } catch (IllegalArgumentException var3) {
         return new File(url.getPath());
      }
   }

   public static String joinPath(String... elements) {
      return (new Path(elements)).render();
   }

   public static String joinPath(List elements) {
      return joinPath((String[])elements.toArray(new String[0]));
   }

   public static List splitPath(String path) {
      Path p = Path.newPath(path);

      ArrayList elements;
      for(elements = new ArrayList(); p != null; p = p.remainder()) {
         elements.add(p.first());
      }

      return elements;
   }

   public static ConfigOrigin readOrigin(ObjectInputStream in) throws IOException {
      return SerializedConfigValue.readOrigin(in, (SimpleConfigOrigin)null);
   }

   public static void writeOrigin(ObjectOutputStream out, ConfigOrigin origin) throws IOException {
      SerializedConfigValue.writeOrigin(new DataOutputStream(out), (SimpleConfigOrigin)origin, (SimpleConfigOrigin)null);
   }

   static String toCamelCase(String originalName) {
      String[] words = originalName.split("-+");
      StringBuilder nameBuilder = new StringBuilder(originalName.length());
      String[] var3 = words;
      int var4 = words.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String word = var3[var5];
         if (nameBuilder.length() == 0) {
            nameBuilder.append(word);
         } else {
            nameBuilder.append(word.substring(0, 1).toUpperCase());
            nameBuilder.append(word.substring(1));
         }
      }

      return nameBuilder.toString();
   }
}
