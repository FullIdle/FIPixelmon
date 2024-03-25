package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigOrigin;
import info.pixelmon.repack.com.typesafe.config.ConfigValueType;
import java.io.ObjectStreamException;
import java.io.Serializable;

final class ConfigDouble extends ConfigNumber implements Serializable {
   private static final long serialVersionUID = 2L;
   private final double value;

   ConfigDouble(ConfigOrigin origin, double value, String originalText) {
      super(origin, originalText);
      this.value = value;
   }

   public ConfigValueType valueType() {
      return ConfigValueType.NUMBER;
   }

   public Double unwrapped() {
      return this.value;
   }

   String transformToString() {
      String s = super.transformToString();
      return s == null ? Double.toString(this.value) : s;
   }

   protected long longValue() {
      return (long)this.value;
   }

   protected double doubleValue() {
      return this.value;
   }

   protected ConfigDouble newCopy(ConfigOrigin origin) {
      return new ConfigDouble(origin, this.value, this.originalText);
   }

   private Object writeReplace() throws ObjectStreamException {
      return new SerializedConfigValue(this);
   }
}
