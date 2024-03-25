package info.pixelmon.repack.com.typesafe.config;

public final class ConfigMemorySize {
   private final long bytes;

   private ConfigMemorySize(long bytes) {
      if (bytes < 0L) {
         throw new IllegalArgumentException("Attempt to construct ConfigMemorySize with negative number: " + bytes);
      } else {
         this.bytes = bytes;
      }
   }

   public static ConfigMemorySize ofBytes(long bytes) {
      return new ConfigMemorySize(bytes);
   }

   public long toBytes() {
      return this.bytes;
   }

   public String toString() {
      return "ConfigMemorySize(" + this.bytes + ")";
   }

   public boolean equals(Object other) {
      if (other instanceof ConfigMemorySize) {
         return ((ConfigMemorySize)other).bytes == this.bytes;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Long.valueOf(this.bytes).hashCode();
   }
}
