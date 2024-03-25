package info.pixelmon.repack.ninja.leaping.configurate;

import com.google.common.base.Objects;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

class MapConfigValue extends ConfigValue {
   volatile ConcurrentMap values = this.newMap();

   public MapConfigValue(SimpleConfigurationNode holder) {
      super(holder);
   }

   public Object getValue() {
      Map value = new LinkedHashMap();
      Iterator var2 = this.values.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry ent = (Map.Entry)var2.next();
         value.put(ent.getKey(), ((SimpleConfigurationNode)ent.getValue()).getValue());
      }

      return value;
   }

   public void setValue(Object value) {
      if (value instanceof Map) {
         ConcurrentMap newValue = this.newMap();
         Iterator var3 = ((Map)value).entrySet().iterator();

         while(var3.hasNext()) {
            Map.Entry ent = (Map.Entry)var3.next();
            if (ent.getValue() != null) {
               SimpleConfigurationNode child = this.holder.createNode(ent.getKey());
               newValue.put(ent.getKey(), child);
               child.attached = true;
               child.setValue(ent.getValue());
            }
         }

         synchronized(this) {
            ConcurrentMap oldMap = this.values;
            this.values = newValue;
            this.detachChildren(oldMap);
         }
      } else {
         throw new IllegalArgumentException("Map configuration values can only be set to values of type Map");
      }
   }

   SimpleConfigurationNode putChild(Object key, SimpleConfigurationNode value) {
      return value == null ? (SimpleConfigurationNode)this.values.remove(key) : (SimpleConfigurationNode)this.values.put(key, value);
   }

   SimpleConfigurationNode putChildIfAbsent(Object key, SimpleConfigurationNode value) {
      return value == null ? (SimpleConfigurationNode)this.values.remove(key) : (SimpleConfigurationNode)this.values.putIfAbsent(key, value);
   }

   public SimpleConfigurationNode getChild(Object key) {
      return (SimpleConfigurationNode)this.values.get(key);
   }

   public Iterable iterateChildren() {
      return this.values.values();
   }

   private void detachChildren(Map map) {
      Iterator var2 = map.values().iterator();

      while(var2.hasNext()) {
         SimpleConfigurationNode value = (SimpleConfigurationNode)var2.next();
         value.attached = false;
         value.clear();
      }

   }

   public void clear() {
      synchronized(this) {
         ConcurrentMap oldMap = this.values;
         this.values = this.newMap();
         this.detachChildren(oldMap);
      }
   }

   private ConcurrentMap newMap() {
      return this.holder.getOptions().getMapFactory().create();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         MapConfigValue that = (MapConfigValue)o;
         return Objects.equal(this.values, that.values);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hashCode(new Object[]{this.values});
   }
}
