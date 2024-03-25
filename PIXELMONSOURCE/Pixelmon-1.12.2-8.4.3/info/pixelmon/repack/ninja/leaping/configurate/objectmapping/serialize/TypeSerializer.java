package info.pixelmon.repack.ninja.leaping.configurate.objectmapping.serialize;

import com.google.common.reflect.TypeToken;
import info.pixelmon.repack.ninja.leaping.configurate.ConfigurationNode;
import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.ObjectMappingException;

public interface TypeSerializer {
   Object deserialize(TypeToken var1, ConfigurationNode var2) throws ObjectMappingException;

   void serialize(TypeToken var1, Object var2, ConfigurationNode var3) throws ObjectMappingException;
}
