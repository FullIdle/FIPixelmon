package info.pixelmon.repack.ninja.leaping.configurate.objectmapping;

import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.Key;

class GuiceObjectMapper extends ObjectMapper {
   private final Injector injector;
   private final Key typeKey;

   protected GuiceObjectMapper(Injector injector, Class clazz) throws ObjectMappingException {
      super(clazz);
      this.injector = injector;
      this.typeKey = Key.get(clazz);
   }

   public boolean canCreateInstances() {
      try {
         this.injector.getProvider(this.typeKey);
         return true;
      } catch (ConfigurationException var2) {
         return false;
      }
   }

   protected Object constructObject() throws ObjectMappingException {
      return this.injector.getInstance(this.typeKey);
   }
}
