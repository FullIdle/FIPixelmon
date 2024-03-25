package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigException;
import info.pixelmon.repack.com.typesafe.config.ConfigOrigin;
import java.io.ObjectStreamException;
import java.io.Serializable;

abstract class ConfigNumber extends AbstractConfigValue implements Serializable {
   private static final long serialVersionUID = 2L;
   protected final String originalText;

   protected ConfigNumber(ConfigOrigin origin, String originalText) {
      super(origin);
      this.originalText = originalText;
   }

   public abstract Number unwrapped();

   String transformToString() {
      return this.originalText;
   }

   int intValueRangeChecked(String path) {
      long l = this.longValue();
      if (l >= -2147483648L && l <= 2147483647L) {
         return (int)l;
      } else {
         throw new ConfigException.WrongType(this.origin(), path, "32-bit integer", "out-of-range value " + l);
      }
   }

   protected abstract long longValue();

   protected abstract double doubleValue();

   private boolean isWhole() {
      long asLong = this.longValue();
      return (double)asLong == this.doubleValue();
   }

   protected boolean canEqual(Object other) {
      return other instanceof ConfigNumber;
   }

   public boolean equals(Object other) {
      if (other instanceof ConfigNumber && this.canEqual(other)) {
         ConfigNumber n = (ConfigNumber)other;
         if (this.isWhole()) {
            return n.isWhole() && this.longValue() == n.longValue();
         } else {
            return !n.isWhole() && this.doubleValue() == n.doubleValue();
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      long asLong;
      if (this.isWhole()) {
         asLong = this.longValue();
      } else {
         asLong = Double.doubleToLongBits(this.doubleValue());
      }

      return (int)(asLong ^ asLong >>> 32);
   }

   static ConfigNumber newNumber(ConfigOrigin origin, long number, String originalText) {
      return (ConfigNumber)(number <= 2147483647L && number >= -2147483648L ? new ConfigInt(origin, (int)number, originalText) : new ConfigLong(origin, number, originalText));
   }

   static ConfigNumber newNumber(ConfigOrigin origin, double number, String originalText) {
      long asLong = (long)number;
      return (ConfigNumber)((double)asLong == number ? newNumber(origin, asLong, originalText) : new ConfigDouble(origin, number, originalText));
   }

   private Object writeReplace() throws ObjectStreamException {
      return new SerializedConfigValue(this);
   }
}
