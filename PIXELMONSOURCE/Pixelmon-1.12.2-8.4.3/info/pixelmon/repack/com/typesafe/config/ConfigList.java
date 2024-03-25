package info.pixelmon.repack.com.typesafe.config;

import java.util.List;

public interface ConfigList extends List, ConfigValue {
   List unwrapped();

   ConfigList withOrigin(ConfigOrigin var1);
}
