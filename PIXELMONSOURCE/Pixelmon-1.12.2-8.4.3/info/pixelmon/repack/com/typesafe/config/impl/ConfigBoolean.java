package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigOrigin;
import info.pixelmon.repack.com.typesafe.config.ConfigValueType;
import java.io.ObjectStreamException;
import java.io.Serializable;

final class ConfigBoolean extends AbstractConfigValue implements Serializable {
   private static final long serialVersionUID = 2L;
   private final boolean value;

   ConfigBoolean(ConfigOrigin origin, boolean value) {
      super(origin);
      this.value = value;
   }

   public ConfigValueType valueType() {
      return ConfigValueType.BOOLEAN;
   }

   public Boolean unwrapped() {
      return this.value;
   }

   String transformToString() {
      return this.value ? "true" : "false";
   }

   protected ConfigBoolean newCopy(ConfigOrigin origin) {
      return new ConfigBoolean(origin, this.value);
   }

   private Object writeReplace() throws ObjectStreamException {
      return new SerializedConfigValue(this);
   }
}
