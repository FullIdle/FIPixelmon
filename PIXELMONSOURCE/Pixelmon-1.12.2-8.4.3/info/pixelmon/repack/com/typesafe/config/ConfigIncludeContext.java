package info.pixelmon.repack.com.typesafe.config;

public interface ConfigIncludeContext {
   ConfigParseable relativeTo(String var1);

   ConfigParseOptions parseOptions();

   ConfigIncludeContext setParseOptions(ConfigParseOptions var1);
}
