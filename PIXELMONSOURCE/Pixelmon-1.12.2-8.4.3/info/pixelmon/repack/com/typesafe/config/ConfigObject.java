package info.pixelmon.repack.com.typesafe.config;

import java.util.Map;

public interface ConfigObject extends ConfigValue, Map {
   Config toConfig();

   Map unwrapped();

   ConfigObject withFallback(ConfigMergeable var1);

   ConfigValue get(Object var1);

   ConfigObject withOnlyKey(String var1);

   ConfigObject withoutKey(String var1);

   ConfigObject withValue(String var1, ConfigValue var2);

   ConfigObject withOrigin(ConfigOrigin var1);
}
