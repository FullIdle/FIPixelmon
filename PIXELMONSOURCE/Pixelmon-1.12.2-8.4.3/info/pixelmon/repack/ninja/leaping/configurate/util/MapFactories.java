package info.pixelmon.repack.ninja.leaping.configurate.util;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class MapFactories {
   private MapFactories() {
   }

   public static MapFactory unordered() {
      return new EqualsSupplier() {
         public ConcurrentMap create() {
            return new ConcurrentHashMap();
         }
      };
   }

   public static MapFactory sorted(final Comparator comparator) {
      return new EqualsSupplier() {
         public ConcurrentMap create() {
            return new ConcurrentSkipListMap(comparator);
         }
      };
   }

   public static MapFactory sortedNatural() {
      return new EqualsSupplier() {
         public ConcurrentMap create() {
            return new ConcurrentSkipListMap();
         }
      };
   }

   public static MapFactory insertionOrdered() {
      return new EqualsSupplier() {
         public ConcurrentMap create() {
            return new SynchronizedWrapper(new LinkedHashMap());
         }
      };
   }

   private abstract static class EqualsSupplier implements MapFactory {
      private EqualsSupplier() {
      }

      public boolean equals(Object o) {
         return o.getClass().equals(this.getClass());
      }

      // $FF: synthetic method
      EqualsSupplier(Object x0) {
         this();
      }
   }

   private static class SynchronizedWrapper implements ConcurrentMap {
      private final Map wrapped;

      private SynchronizedWrapper(Map wrapped) {
         this.wrapped = wrapped;
      }

      public Object putIfAbsent(Object k, Object v) {
         synchronized(this.wrapped) {
            if (!this.wrapped.containsKey(k)) {
               this.wrapped.put(k, v);
               return null;
            } else {
               return this.wrapped.get(k);
            }
         }
      }

      public boolean remove(Object key, Object expected) {
         synchronized(this.wrapped) {
            if (Objects.equal(expected, this.wrapped.get(key))) {
               return this.wrapped.remove(key) != null;
            } else {
               return false;
            }
         }
      }

      public boolean replace(Object key, Object old, Object replace) {
         synchronized(this.wrapped) {
            if (Objects.equal(old, this.wrapped.get(key))) {
               this.wrapped.put(key, replace);
               return true;
            } else {
               return false;
            }
         }
      }

      public Object replace(Object k, Object v) {
         synchronized(this.wrapped) {
            return this.wrapped.containsKey(k) ? this.wrapped.put(k, v) : null;
         }
      }

      public int size() {
         synchronized(this.wrapped) {
            return this.wrapped.size();
         }
      }

      public boolean isEmpty() {
         synchronized(this.wrapped) {
            return this.wrapped.isEmpty();
         }
      }

      public boolean containsKey(Object o) {
         synchronized(this.wrapped) {
            return this.wrapped.containsKey(o);
         }
      }

      public boolean containsValue(Object o) {
         synchronized(this.wrapped) {
            return this.wrapped.containsKey(o);
         }
      }

      public Object get(Object o) {
         synchronized(this.wrapped) {
            return this.wrapped.get(o);
         }
      }

      public Object put(Object k, Object v) {
         synchronized(this.wrapped) {
            return this.wrapped.put(k, v);
         }
      }

      public Object remove(Object o) {
         synchronized(this.wrapped) {
            return this.wrapped.remove(o);
         }
      }

      public void putAll(Map map) {
         synchronized(this.wrapped) {
            this.wrapped.putAll(map);
         }
      }

      public void clear() {
         synchronized(this.wrapped) {
            this.wrapped.clear();
         }
      }

      public Set keySet() {
         synchronized(this.wrapped) {
            return ImmutableSet.copyOf(this.wrapped.keySet());
         }
      }

      public Collection values() {
         synchronized(this.wrapped) {
            return ImmutableSet.copyOf(this.wrapped.values());
         }
      }

      public Set entrySet() {
         synchronized(this.wrapped) {
            return ImmutableSet.copyOf(this.wrapped.entrySet());
         }
      }

      // $FF: synthetic method
      SynchronizedWrapper(Map x0, Object x1) {
         this(x0);
      }
   }
}
