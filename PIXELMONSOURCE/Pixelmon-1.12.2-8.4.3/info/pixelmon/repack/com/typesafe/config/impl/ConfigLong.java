package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigOrigin;
import info.pixelmon.repack.com.typesafe.config.ConfigValueType;
import java.io.ObjectStreamException;
import java.io.Serializable;

final class ConfigLong extends ConfigNumber implements Serializable {
   private static final long serialVersionUID = 2L;
   private final long value;

   ConfigLong(ConfigOrigin origin, long value, String originalText) {
      super(origin, originalText);
      this.value = value;
   }

   public ConfigValueType valueType() {
      return ConfigValueType.NUMBER;
   }

   public Long unwrapped() {
      return this.value;
   }

   String transformToString() {
      String s = super.transformToString();
      return s == null ? Long.toString(this.value) : s;
   }

   protected long longValue() {
      return this.value;
   }

   protected double doubleValue() {
      return (double)this.value;
   }

   protected ConfigLong newCopy(ConfigOrigin origin) {
      return new ConfigLong(origin, this.value, this.originalText);
   }

   private Object writeReplace() throws ObjectStreamException {
      return new SerializedConfigValue(this);
   }
}
