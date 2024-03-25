package info.pixelmon.repack.com.typesafe.config.impl;

final class MemoKey {
   private final AbstractConfigValue value;
   private final Path restrictToChildOrNull;

   MemoKey(AbstractConfigValue value, Path restrictToChildOrNull) {
      this.value = value;
      this.restrictToChildOrNull = restrictToChildOrNull;
   }

   public final int hashCode() {
      int h = System.identityHashCode(this.value);
      return this.restrictToChildOrNull != null ? h + 41 * (41 + this.restrictToChildOrNull.hashCode()) : h;
   }

   public final boolean equals(Object other) {
      if (other instanceof MemoKey) {
         MemoKey o = (MemoKey)other;
         if (o.value != this.value) {
            return false;
         } else if (o.restrictToChildOrNull == this.restrictToChildOrNull) {
            return true;
         } else {
            return o.restrictToChildOrNull != null && this.restrictToChildOrNull != null ? o.restrictToChildOrNull.equals(this.restrictToChildOrNull) : false;
         }
      } else {
         return false;
      }
   }

   public final String toString() {
      return "MemoKey(" + this.value + "@" + System.identityHashCode(this.value) + "," + this.restrictToChildOrNull + ")";
   }
}
