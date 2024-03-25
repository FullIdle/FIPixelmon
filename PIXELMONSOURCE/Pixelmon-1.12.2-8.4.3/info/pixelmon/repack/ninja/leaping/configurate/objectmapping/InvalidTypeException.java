package info.pixelmon.repack.ninja.leaping.configurate.objectmapping;

import com.google.common.reflect.TypeToken;

public class InvalidTypeException extends ObjectMappingException {
   public InvalidTypeException(TypeToken received) {
      super("Invalid type presented to serializer: " + received);
   }
}
