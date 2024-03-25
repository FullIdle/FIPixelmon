package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigValue;

interface Container extends ConfigValue {
   AbstractConfigValue replaceChild(AbstractConfigValue var1, AbstractConfigValue var2);

   boolean hasDescendant(AbstractConfigValue var1);
}
