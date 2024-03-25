package info.pixelmon.repack.ninja.leaping.configurate.objectmapping;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.ExecutionException;

public class DefaultObjectMapperFactory implements ObjectMapperFactory {
   private static final ObjectMapperFactory INSTANCE = new DefaultObjectMapperFactory();
   private final LoadingCache mapperCache = CacheBuilder.newBuilder().weakKeys().maximumSize(500L).build(new CacheLoader() {
      public ObjectMapper load(Class key) throws Exception {
         return new ObjectMapper(key);
      }
   });

   public ObjectMapper getMapper(Class type) throws ObjectMappingException {
      Preconditions.checkNotNull(type, "type");

      try {
         return (ObjectMapper)this.mapperCache.get(type);
      } catch (ExecutionException var3) {
         if (var3.getCause() instanceof ObjectMappingException) {
            throw (ObjectMappingException)var3.getCause();
         } else {
            throw new ObjectMappingException(var3);
         }
      }
   }

   public static ObjectMapperFactory getInstance() {
      return INSTANCE;
   }
}
