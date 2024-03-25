package info.pixelmon.repack.com.typesafe.config;

import info.pixelmon.repack.com.typesafe.config.impl.ConfigBeanImpl;

public class ConfigBeanFactory {
   public static Object create(Config config, Class clazz) {
      return ConfigBeanImpl.createInternal(config, clazz);
   }
}
