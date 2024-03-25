package info.pixelmon.repack.ninja.leaping.configurate;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.ObjectMappingException;
import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class SimpleConfigurationNode implements ConfigurationNode {
   private final ConfigurationOptions options;
   volatile boolean attached;
   volatile Object key;
   private SimpleConfigurationNode parent;
   private volatile ConfigValue value;

   public static SimpleConfigurationNode root() {
      return root(ConfigurationOptions.defaults());
   }

   public static SimpleConfigurationNode root(ConfigurationOptions options) {
      return new SimpleConfigurationNode((Object)null, (SimpleConfigurationNode)null, options);
   }

   protected SimpleConfigurationNode(Object key, SimpleConfigurationNode parent, ConfigurationOptions options) {
      Preconditions.checkNotNull(options, "options");
      this.key = key;
      if (parent == null) {
         this.attached = true;
      }

      this.options = options;
      this.parent = parent == null ? null : parent;
      this.value = new NullConfigValue(this);
   }

   public Object getValue(Object def) {
      Object ret = this.value.getValue();
      return ret == null ? this.calculateDef(def) : ret;
   }

   public Object getValue(Supplier defSupplier) {
      Object ret = this.value.getValue();
      return ret == null ? this.calculateDef(defSupplier.get()) : ret;
   }

   public Object getValue(Function transformer, Object def) {
      Object ret = transformer.apply(this.getValue());
      return ret == null ? this.calculateDef(def) : ret;
   }

   public Object getValue(Function transformer, Supplier defSupplier) {
      Object ret = transformer.apply(this.getValue());
      return ret == null ? this.calculateDef(defSupplier.get()) : ret;
   }

   private Object calculateDef(Object defValue) {
      if (defValue != null && this.getOptions().shouldCopyDefaults()) {
         this.setValue(defValue);
      }

      return defValue;
   }

   public List getList(Function transformer) {
      ImmutableList.Builder build = ImmutableList.builder();
      ConfigValue value = this.value;
      if (value instanceof ListConfigValue) {
         Iterator var4 = value.iterateChildren().iterator();

         while(var4.hasNext()) {
            SimpleConfigurationNode o = (SimpleConfigurationNode)var4.next();
            Object transformed = transformer.apply(o.getValue());
            if (transformed != null) {
               build.add(transformed);
            }
         }
      } else {
         Object transformed = transformer.apply(value.getValue());
         if (transformed != null) {
            build.add(transformed);
         }
      }

      return build.build();
   }

   public List getList(Function transformer, List def) {
      List ret = this.getList(transformer);
      return ret.isEmpty() ? (List)this.calculateDef(def) : ret;
   }

   public List getList(Function transformer, Supplier defSupplier) {
      List ret = this.getList(transformer);
      return ret.isEmpty() ? (List)this.calculateDef(defSupplier.get()) : ret;
   }

   public List getList(TypeToken type, List def) throws ObjectMappingException {
      List ret = (List)this.getValue((TypeToken)(new TypeToken() {
      }).where(new TypeParameter() {
      }, type), (Object)def);
      return ret.isEmpty() ? (List)this.calculateDef(def) : ret;
   }

   public List getList(TypeToken type, Supplier defSupplier) throws ObjectMappingException {
      List ret = (List)this.getValue((new TypeToken() {
      }).where(new TypeParameter() {
      }, type), defSupplier);
      return ret.isEmpty() ? (List)this.calculateDef(defSupplier.get()) : ret;
   }

   public SimpleConfigurationNode setValue(Object newValue) {
      if (newValue instanceof ConfigurationNode) {
         ConfigurationNode newNode = (ConfigurationNode)newValue;
         if (newNode.hasListChildren()) {
            this.attachIfNecessary();
            ListConfigValue newList = new ListConfigValue(this);
            synchronized(newNode) {
               List children = newNode.getChildrenList();
               int i = 0;

               while(true) {
                  if (i >= children.size()) {
                     break;
                  }

                  SimpleConfigurationNode child = this.createNode(i);
                  child.attached = true;
                  newList.putChild(i, child);
                  child.setValue(children.get(i));
                  ++i;
               }
            }

            this.value = newList;
            return this;
         }

         if (newNode.hasMapChildren()) {
            this.attachIfNecessary();
            MapConfigValue newMap = new MapConfigValue(this);
            synchronized(newNode) {
               Map children = newNode.getChildrenMap();
               Iterator var6 = children.entrySet().iterator();

               while(true) {
                  if (!var6.hasNext()) {
                     break;
                  }

                  Map.Entry ent = (Map.Entry)var6.next();
                  SimpleConfigurationNode child = this.createNode(ent.getKey());
                  child.attached = true;
                  newMap.putChild(ent.getKey(), child);
                  child.setValue(ent.getValue());
               }
            }

            this.value = newMap;
            return this;
         }

         newValue = newNode.getValue();
      }

      if (newValue == null) {
         if (this.parent == null) {
            this.clear();
         } else {
            this.parent.removeChild(this.key);
         }

         return this;
      } else {
         this.insertNewValue(newValue, false);
         return this;
      }
   }

   public Object getValue(TypeToken type, Object def) throws ObjectMappingException {
      Object value = this.getValue();
      if (value == null) {
         if (def != null && this.getOptions().shouldCopyDefaults()) {
            this.setValue(type, def);
         }

         return def;
      } else {
         TypeSerializer serial = this.getOptions().getSerializers().get(type);
         if (serial == null) {
            if (type.getRawType().isInstance(value)) {
               return type.getRawType().cast(value);
            } else {
               if (def != null && this.getOptions().shouldCopyDefaults()) {
                  this.setValue(type, def);
               }

               return def;
            }
         } else {
            return serial.deserialize(type, this);
         }
      }
   }

   public Object getValue(TypeToken type, Supplier defSupplier) throws ObjectMappingException {
      Object value = this.getValue();
      if (value == null) {
         Object def = defSupplier.get();
         if (def != null && this.getOptions().shouldCopyDefaults()) {
            this.setValue(type, def);
         }

         return def;
      } else {
         TypeSerializer serial = this.getOptions().getSerializers().get(type);
         if (serial == null) {
            if (type.getRawType().isInstance(value)) {
               return type.getRawType().cast(value);
            } else {
               Object def = defSupplier.get();
               if (def != null && this.getOptions().shouldCopyDefaults()) {
                  this.setValue(type, def);
               }

               return def;
            }
         } else {
            return serial.deserialize(type, this);
         }
      }
   }

   private void insertNewValue(Object newValue, boolean onlyIfNull) {
      this.attachIfNecessary();
      synchronized(this) {
         Object value;
         ConfigValue oldValue = value = this.value;
         if (!onlyIfNull || oldValue instanceof NullConfigValue) {
            if (newValue instanceof Collection) {
               if (!(value instanceof ListConfigValue)) {
                  value = new ListConfigValue(this);
               }
            } else if (newValue instanceof Map) {
               if (!(value instanceof MapConfigValue)) {
                  value = new MapConfigValue(this);
               }
            } else if (!(value instanceof ScalarConfigValue)) {
               value = new ScalarConfigValue(this);
            }

            ((ConfigValue)value).setValue(newValue);
            this.value = (ConfigValue)value;
         }
      }
   }

   public ConfigurationNode mergeValuesFrom(ConfigurationNode other) {
      if (other.hasMapChildren()) {
         synchronized(this) {
            Object newValue;
            ConfigValue oldValue = newValue = this.value;
            if (!(oldValue instanceof MapConfigValue)) {
               if (!(oldValue instanceof NullConfigValue)) {
                  return this;
               }

               newValue = new MapConfigValue(this);
            }

            Iterator var5 = other.getChildrenMap().entrySet().iterator();

            while(var5.hasNext()) {
               Map.Entry ent = (Map.Entry)var5.next();
               SimpleConfigurationNode newChild = this.createNode(ent.getKey());
               newChild.attached = true;
               newChild.setValue(ent.getValue());
               SimpleConfigurationNode existing = ((ConfigValue)newValue).putChildIfAbsent(ent.getKey(), newChild);
               if (existing != null) {
                  existing.mergeValuesFrom(newChild);
               }
            }

            this.value = (ConfigValue)newValue;
         }
      } else if (other.getValue() != null) {
         this.insertNewValue(other.getValue(), true);
      }

      return this;
   }

   public SimpleConfigurationNode getNode(Object... path) {
      SimpleConfigurationNode ret = this;
      Object[] var3 = path;
      int var4 = path.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object el = var3[var5];
         ret = ret.getChild(el, false);
      }

      return ret;
   }

   public boolean isVirtual() {
      return !this.attached;
   }

   public boolean hasListChildren() {
      return this.value instanceof ListConfigValue;
   }

   public boolean hasMapChildren() {
      return this.value instanceof MapConfigValue;
   }

   public List getChildrenList() {
      ConfigValue value = this.value;
      return (List)(value instanceof ListConfigValue ? ImmutableList.copyOf((Collection)((ListConfigValue)value).values.get()) : Collections.emptyList());
   }

   public Map getChildrenMap() {
      ConfigValue value = this.value;
      return (Map)(value instanceof MapConfigValue ? ImmutableMap.copyOf(((MapConfigValue)value).values) : Collections.emptyMap());
   }

   protected SimpleConfigurationNode getChild(Object key, boolean attach) {
      SimpleConfigurationNode child = this.value.getChild(key);
      if (child == null) {
         if (attach) {
            this.attachIfNecessary();
            SimpleConfigurationNode existingChild = this.value.putChildIfAbsent(key, child = this.createNode(key));
            if (existingChild != null) {
               child = existingChild;
            } else {
               this.attachChild(child);
            }
         } else {
            child = this.createNode(key);
         }
      }

      return child;
   }

   public boolean removeChild(Object key) {
      return this.possiblyDetach(this.value.putChild(key, (SimpleConfigurationNode)null)) != null;
   }

   private SimpleConfigurationNode possiblyDetach(SimpleConfigurationNode node) {
      if (node != null) {
         node.attached = false;
         node.clear();
      }

      return node;
   }

   public SimpleConfigurationNode getAppendedNode() {
      return this.getChild(-1, false);
   }

   public Object getKey() {
      return this.key;
   }

   public Object[] getPath() {
      LinkedList pathElements = new LinkedList();
      ConfigurationNode ptr = this;
      if (this.getParent() == null) {
         return new Object[]{this.getKey()};
      } else {
         do {
            pathElements.addFirst(((ConfigurationNode)ptr).getKey());
         } while(((ConfigurationNode)(ptr = ((ConfigurationNode)ptr).getParent())).getParent() != null);

         return pathElements.toArray();
      }
   }

   public SimpleConfigurationNode getParent() {
      return this.parent;
   }

   public ConfigurationOptions getOptions() {
      return this.options;
   }

   SimpleConfigurationNode getParentAttached() {
      SimpleConfigurationNode parent = this.parent;
      if (parent.isVirtual()) {
         parent = parent.getParentAttached().attachChildIfAbsent(parent);
      }

      return this.parent = parent;
   }

   private SimpleConfigurationNode attachChildIfAbsent(SimpleConfigurationNode child) {
      if (this.isVirtual()) {
         throw new IllegalStateException("This parent is not currently attached. This is an internal state violation.");
      } else if (!child.getParentAttached().equals(this)) {
         throw new IllegalStateException("Child " + child + " path is not a direct parent of me (" + this + "), cannot attach");
      } else {
         ConfigValue oldValue;
         Object newValue;
         synchronized(this) {
            newValue = oldValue = this.value;
            if (!(oldValue instanceof MapConfigValue)) {
               if (child.key instanceof Integer) {
                  if (oldValue instanceof NullConfigValue) {
                     newValue = new ListConfigValue(this);
                  } else if (!(oldValue instanceof ListConfigValue)) {
                     newValue = new ListConfigValue(this, oldValue.getValue());
                  }
               } else {
                  newValue = new MapConfigValue(this);
               }
            }

            SimpleConfigurationNode oldChild = ((ConfigValue)newValue).putChildIfAbsent(child.key, child);
            if (oldChild != null) {
               return oldChild;
            }

            this.value = (ConfigValue)newValue;
         }

         if (newValue != oldValue) {
            oldValue.clear();
         }

         child.attached = true;
         return child;
      }
   }

   protected SimpleConfigurationNode createNode(Object path) {
      return new SimpleConfigurationNode(path, this, this.options);
   }

   protected void attachIfNecessary() {
      if (!this.attached) {
         this.getParentAttached().attachChild(this);
      }

   }

   protected void attachChild(SimpleConfigurationNode child) {
      if (this.isVirtual()) {
         throw new IllegalStateException("This parent is not currently attached. This is an internal state violation.");
      } else if (!child.getParentAttached().equals(this)) {
         throw new IllegalStateException("Child " + child + " path is not a direct parent of me (" + this + "), cannot attach");
      } else {
         ConfigValue oldValue;
         Object newValue;
         synchronized(this) {
            newValue = oldValue = this.value;
            if (!(oldValue instanceof MapConfigValue)) {
               if (child.key instanceof Integer) {
                  if (oldValue instanceof NullConfigValue) {
                     newValue = new ListConfigValue(this);
                  } else if (!(oldValue instanceof ListConfigValue)) {
                     newValue = new ListConfigValue(this, oldValue.getValue());
                  }
               } else {
                  newValue = new MapConfigValue(this);
               }
            }

            this.possiblyDetach(((ConfigValue)newValue).putChild(child.key, child));
            this.value = (ConfigValue)newValue;
         }

         if (newValue != oldValue) {
            oldValue.clear();
         }

         child.attached = true;
      }
   }

   protected void clear() {
      synchronized(this) {
         ConfigValue oldValue = this.value;
         this.value = new NullConfigValue(this);
         oldValue.clear();
      }
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof SimpleConfigurationNode)) {
         return false;
      } else {
         SimpleConfigurationNode that = (SimpleConfigurationNode)o;
         if (this.attached != that.attached) {
            return false;
         } else {
            label43: {
               if (this.key != null) {
                  if (this.key.equals(that.key)) {
                     break label43;
                  }
               } else if (that.key == null) {
                  break label43;
               }

               return false;
            }

            if (!this.options.equals(that.options)) {
               return false;
            } else {
               if (this.parent != null) {
                  if (!this.parent.equals(that.parent)) {
                     return false;
                  }
               } else if (that.parent != null) {
                  return false;
               }

               if (!this.value.equals(that.value)) {
                  return false;
               } else {
                  return true;
               }
            }
         }
      }
   }

   public int hashCode() {
      int result = this.options.hashCode();
      result = 31 * result + (this.attached ? 1 : 0);
      result = 31 * result + (this.key != null ? this.key.hashCode() : 0);
      result = 31 * result + (this.parent != null ? this.parent.hashCode() : 0);
      result = 31 * result + this.value.hashCode();
      return result;
   }

   public String toString() {
      return "SimpleConfigurationNode{options=" + this.options + ", attached=" + this.attached + ", key=" + this.key + ", parent=" + this.parent + ", value=" + this.value + '}';
   }
}
