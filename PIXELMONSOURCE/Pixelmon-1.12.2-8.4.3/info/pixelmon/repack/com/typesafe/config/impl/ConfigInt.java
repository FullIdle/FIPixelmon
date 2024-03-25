package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigOrigin;
import info.pixelmon.repack.com.typesafe.config.ConfigValueType;
import java.io.ObjectStreamException;
import java.io.Serializable;

final class ConfigInt extends ConfigNumber implements Serializable {
   private static final long serialVersionUID = 2L;
   private final int value;

   ConfigInt(ConfigOrigin origin, int value, String originalText) {
      super(origin, originalText);
      this.value = value;
   }

   public ConfigValueType valueType() {
      return ConfigValueType.NUMBER;
   }

   public Integer unwrapped() {
      return this.value;
   }

   String transformToString() {
      String s = super.transformToString();
      return s == null ? Integer.toString(this.value) : s;
   }

   protected long longValue() {
      return (long)this.value;
   }

   protected double doubleValue() {
      return (double)this.value;
   }

   protected ConfigInt newCopy(ConfigOrigin origin) {
      return new ConfigInt(origin, this.value, this.originalText);
   }

   private Object writeReplace() throws ObjectStreamException {
      return new SerializedConfigValue(this);
   }
}
