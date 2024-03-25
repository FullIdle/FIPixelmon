package info.pixelmon.repack.ninja.leaping.configurate;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

class ListConfigValue extends ConfigValue {
   final AtomicReference values = new AtomicReference();

   ListConfigValue(SimpleConfigurationNode holder) {
      super(holder);
      this.values.set(new ArrayList());
   }

   ListConfigValue(SimpleConfigurationNode holder, Object startValue) {
      super(holder);
      this.values.set(new ArrayList());
      SimpleConfigurationNode child = holder.createNode(0);
      child.attached = true;
      child.setValue(startValue);
      ((List)this.values.get()).add(child);
   }

   public Object getValue() {
      List values = (List)this.values.get();
      synchronized(values) {
         List ret = new ArrayList(values.size());
         Iterator var4 = values.iterator();

         while(var4.hasNext()) {
            SimpleConfigurationNode obj = (SimpleConfigurationNode)var4.next();
            ret.add(obj.getValue());
         }

         return ret.isEmpty() ? null : ret;
      }
   }

   public void setValue(Object value) {
      if (!(value instanceof Collection)) {
         value = Collections.singleton(value);
      }

      Collection valueList = (Collection)value;
      List newValue = new ArrayList(valueList.size());
      int count = 0;
      Iterator var5 = valueList.iterator();

      while(var5.hasNext()) {
         Object o = var5.next();
         if (o != null) {
            SimpleConfigurationNode child = this.holder.createNode(count);
            newValue.add(count, child);
            child.attached = true;
            child.setValue(o);
            ++count;
         }
      }

      this.detachNodes((List)this.values.getAndSet(newValue));
   }

   public SimpleConfigurationNode putChild(Object key, SimpleConfigurationNode value) {
      return this.putChild(key, value, false);
   }

   SimpleConfigurationNode putChildIfAbsent(Object key, SimpleConfigurationNode value) {
      return this.putChild(key, value, true);
   }

   private SimpleConfigurationNode putChild(Object key, SimpleConfigurationNode value, boolean onlyIfAbsent) {
      SimpleConfigurationNode ret = null;

      List values;
      do {
         values = (List)this.values.get();
         synchronized(values) {
            int index = (Integer)key;
            if (value == null) {
               if (index < values.size()) {
                  ret = (SimpleConfigurationNode)values.remove(index);

                  for(int i = index; i < values.size(); ++i) {
                     ((SimpleConfigurationNode)values.get(i)).key = index;
                  }
               }
            } else if (index >= 0 && index < values.size()) {
               if (onlyIfAbsent) {
                  return (SimpleConfigurationNode)values.get(index);
               }

               ret = (SimpleConfigurationNode)values.set(index, value);
            } else if (index == -1) {
               values.add(value);
               value.key = values.lastIndexOf(value);
            } else {
               values.add(index, value);
            }
         }
      } while(!this.values.compareAndSet(values, values));

      return ret;
   }

   public SimpleConfigurationNode getChild(Object key) {
      List values = (List)this.values.get();
      synchronized(values) {
         Integer value = Types.asInt(key);
         return value != null && value >= 0 && value < values.size() ? (SimpleConfigurationNode)values.get(value) : null;
      }
   }

   public Iterable iterateChildren() {
      List values = (List)this.values.get();
      synchronized(values) {
         return ImmutableList.copyOf(values);
      }
   }

   private void detachNodes(List children) {
      synchronized(children) {
         Iterator var3 = children.iterator();

         while(var3.hasNext()) {
            SimpleConfigurationNode node = (SimpleConfigurationNode)var3.next();
            node.attached = false;
            node.clear();
         }

      }
   }

   public void clear() {
      List oldValues = (List)this.values.getAndSet(new ArrayList());
      this.detachNodes(oldValues);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ListConfigValue that = (ListConfigValue)o;
         return Objects.equal(this.values, that.values);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hashCode(new Object[]{this.values});
   }
}
