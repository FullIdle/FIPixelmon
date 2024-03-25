package com.pixelmonmod.pixelmon.util.helpers;

import com.pixelmonmod.pixelmon.RandomHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import javax.annotation.Nullable;

public class CollectionHelper {
   public static List reverseMapGet(Map map, Object value) {
      List keys = new ArrayList();
      Iterator var3 = map.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry entry = (Map.Entry)var3.next();
         if (entry.getValue().equals(value)) {
            keys.add(entry.getKey());
         }
      }

      return keys;
   }

   public static Object getRandomElement(List list) {
      return list != null && !list.isEmpty() ? list.get(RandomHelper.getRandomNumberBetween(0, list.size() - 1)) : null;
   }

   public static Object getRandomElement(Set set) {
      if (set != null && !set.isEmpty()) {
         int index = ThreadLocalRandom.current().nextInt(set.size());
         Iterator iter = set.iterator();

         for(int i = 0; i < index; ++i) {
            iter.next();
         }

         return iter.next();
      } else {
         return null;
      }
   }

   public static boolean anyMatch(Collection list1, Collection list2) {
      if (list1 != null && list2 != null && !list1.isEmpty() && !list2.isEmpty()) {
         Iterator var2 = list1.iterator();

         Object t;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            t = var2.next();
         } while(!list2.contains(t));

         return true;
      } else {
         return false;
      }
   }

   public static boolean containsAll(List baseList, List mustHave) {
      Iterator var2 = mustHave.iterator();

      Object t;
      do {
         if (!var2.hasNext()) {
            return true;
         }

         t = var2.next();
      } while(baseList.contains(t));

      return false;
   }

   public static boolean containsAll(Set baseList, Set mustHave) {
      Iterator var2 = mustHave.iterator();

      Object t;
      do {
         if (!var2.hasNext()) {
            return true;
         }

         t = var2.next();
      } while(baseList.contains(t));

      return false;
   }

   public static boolean containsA(Iterable container, Class elementClass) {
      if (container != null) {
         Iterator var2 = container.iterator();

         while(var2.hasNext()) {
            Object t = var2.next();
            if (elementClass.isInstance(t)) {
               return true;
            }
         }
      }

      return false;
   }

   @Nullable
   public static Object getFirst(Iterable container, Class elementClass) {
      if (container != null) {
         Iterator var2 = container.iterator();

         while(var2.hasNext()) {
            Object t = var2.next();
            if (elementClass.isInstance(t)) {
               return elementClass.cast(t);
            }
         }
      }

      return null;
   }

   public static void removeAny(List list, Class clazz) {
      if (list != null) {
         list.removeIf((t) -> {
            return clazz.isInstance(t);
         });
      }

   }

   @Nullable
   public static Object find(Iterable container, Predicate condition) {
      Iterator var2 = container.iterator();

      Object t;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         t = var2.next();
      } while(!condition.test(t));

      return t;
   }

   public static boolean some(Iterable container, Predicate condition) {
      return find(container, condition) != null;
   }
}
