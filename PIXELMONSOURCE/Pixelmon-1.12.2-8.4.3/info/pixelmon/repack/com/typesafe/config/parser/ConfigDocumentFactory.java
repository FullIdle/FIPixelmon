package info.pixelmon.repack.com.typesafe.config.parser;

import info.pixelmon.repack.com.typesafe.config.ConfigParseOptions;
import info.pixelmon.repack.com.typesafe.config.impl.Parseable;
import java.io.File;
import java.io.Reader;

public final class ConfigDocumentFactory {
   public static ConfigDocument parseReader(Reader reader, ConfigParseOptions options) {
      return Parseable.newReader(reader, options).parseConfigDocument();
   }

   public static ConfigDocument parseReader(Reader reader) {
      return parseReader(reader, ConfigParseOptions.defaults());
   }

   public static ConfigDocument parseFile(File file, ConfigParseOptions options) {
      return Parseable.newFile(file, options).parseConfigDocument();
   }

   public static ConfigDocument parseFile(File file) {
      return parseFile(file, ConfigParseOptions.defaults());
   }

   public static ConfigDocument parseString(String s, ConfigParseOptions options) {
      return Parseable.newString(s, options).parseConfigDocument();
   }

   public static ConfigDocument parseString(String s) {
      return parseString(s, ConfigParseOptions.defaults());
   }
}
