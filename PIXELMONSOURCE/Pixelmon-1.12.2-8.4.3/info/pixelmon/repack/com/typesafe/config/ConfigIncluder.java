package info.pixelmon.repack.com.typesafe.config;

public interface ConfigIncluder {
   ConfigIncluder withFallback(ConfigIncluder var1);

   ConfigObject include(ConfigIncludeContext var1, String var2);
}
