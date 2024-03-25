package info.pixelmon.repack.ninja.leaping.configurate;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.Collections;

class ScalarConfigValue extends ConfigValue {
   private volatile Object value;

   ScalarConfigValue(SimpleConfigurationNode holder) {
      super(holder);
   }

   public Object getValue() {
      return this.value;
   }

   public void setValue(Object value) {
      Preconditions.checkNotNull(value);
      if (!this.holder.getOptions().acceptsType(value.getClass())) {
         throw new IllegalArgumentException("Configuration does not accept objects of type " + value.getClass());
      } else {
         this.value = value;
      }
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
      this.value = null;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ScalarConfigValue that = (ScalarConfigValue)o;
         return Objects.equal(this.value, that.value);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hashCode(new Object[]{this.value});
   }
}
