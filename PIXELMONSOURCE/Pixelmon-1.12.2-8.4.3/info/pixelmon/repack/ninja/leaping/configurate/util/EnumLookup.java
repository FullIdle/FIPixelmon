package info.pixelmon.repack.ninja.leaping.configurate.util;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class EnumLookup {
   private static final LoadingCache enumFieldCache = CacheBuilder.newBuilder().weakKeys().maximumSize(512L).build(new CacheLoader() {
      public Map load(Class key) throws Exception {
         Map ret = new HashMap();
         Enum[] var3 = (Enum[])key.getEnumConstants();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Enum field = var3[var5];
            ret.put(field.name(), field);
            ret.putIfAbsent(EnumLookup.processKey(field.name()), field);
         }

         return ImmutableMap.copyOf(ret);
      }
   });

   private EnumLookup() {
   }

   private static String processKey(String key) {
      Preconditions.checkNotNull(key, "key");
      return "\ud83c\udf38" + key.toLowerCase().replace("_", "");
   }

   public static Optional lookupEnum(Class clazz, String key) {
      Preconditions.checkNotNull(clazz, "clazz");

      try {
         Map vals = (Map)enumFieldCache.get(clazz);
         Enum possibleRet = (Enum)vals.get(key);
         return possibleRet != null ? Optional.of(possibleRet) : Optional.ofNullable((Enum)vals.get(processKey(key)));
      } catch (ExecutionException var4) {
         return Optional.empty();
      }
   }
}
