package info.pixelmon.repack.ninja.leaping.configurate;

import java.util.Collections;

class NullConfigValue extends ConfigValue {
   NullConfigValue(SimpleConfigurationNode holder) {
      super(holder);
   }

   public Object getValue() {
      return null;
   }

   public void setValue(Object value) {
   }

   SimpleConfigurationNode putChild(Object key, SimpleConfigurationNode value) {
      return null;
   }

   SimpleConfigurationNode putChildIfAbsent(Object key, SimpleConfigurationNode value) {
      return null;
   }

   public SimpleConfigurationNode getChild(Object key) {
      return null;
   }

   public Iterable iterateChildren() {
      return Collections.emptySet();
   }

   public void clear() {
   }

   public boolean equals(Object o) {
      return o instanceof NullConfigValue;
   }

   public int hashCode() {
      return 1009;
   }
}
