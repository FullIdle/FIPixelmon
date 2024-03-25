package info.pixelmon.repack.ninja.leaping.configurate.objectmapping;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Injector;
import java.util.concurrent.ExecutionException;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class GuiceObjectMapperFactory implements ObjectMapperFactory {
   private final LoadingCache cache = CacheBuilder.newBuilder().weakKeys().maximumSize(512L).build(new CacheLoader() {
      public ObjectMapper load(Class key) throws Exception {
         return new GuiceObjectMapper(GuiceObjectMapperFactory.this.injector, key);
      }
   });
   private final Injector injector;

   @Inject
   protected GuiceObjectMapperFactory(Injector baseInjector) {
      this.injector = baseInjector;
   }

   public ObjectMapper getMapper(Class type) throws ObjectMappingException {
      Preconditions.checkNotNull(type, "type");

      try {
         return (ObjectMapper)this.cache.get(type);
      } catch (ExecutionException var3) {
         if (var3.getCause() instanceof ObjectMappingException) {
            throw (ObjectMappingException)var3.getCause();
         } else {
            throw new RuntimeException(var3);
         }
      }
   }
}
