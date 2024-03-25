package info.pixelmon.repack.com.typesafe.config;

import info.pixelmon.repack.com.typesafe.config.impl.ConfigImpl;
import java.util.Map;

public final class ConfigValueFactory {
   private ConfigValueFactory() {
   }

   public static ConfigValue fromAnyRef(Object object, String originDescription) {
      return ConfigImpl.fromAnyRef(object, originDescription);
   }

   public static ConfigObject fromMap(Map values, String originDescription) {
      return (ConfigObject)fromAnyRef(values, originDescription);
   }

   public static ConfigList fromIterable(Iterable values, String originDescription) {
      return (ConfigList)fromAnyRef(values, originDescription);
   }

   public static ConfigValue fromAnyRef(Object object) {
      return fromAnyRef(object, (String)null);
   }

   public static ConfigObject fromMap(Map values) {
      return fromMap(values, (String)null);
   }

   public static ConfigList fromIterable(Iterable values) {
      return fromIterable(values, (String)null);
   }
}
