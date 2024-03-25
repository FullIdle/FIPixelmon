package info.pixelmon.repack.ninja.leaping.configurate;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.ObjectMappingException;
import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ConfigurationNode {
   int NUMBER_DEF = 0;

   Object getKey();

   Object[] getPath();

   ConfigurationNode getParent();

   ConfigurationOptions getOptions();

   default Object getValue() {
      return this.getValue((Object)null);
   }

   Object getValue(Object var1);

   Object getValue(Supplier var1);

   default Object getValue(Function transformer) {
      return this.getValue((Function)transformer, (Object)null);
   }

   Object getValue(Function var1, Object var2);

   Object getValue(Function var1, Supplier var2);

   List getList(Function var1);

   List getList(Function var1, List var2);

   List getList(Function var1, Supplier var2);

   default List getList(TypeToken type) throws ObjectMappingException {
      return this.getList((TypeToken)type, (List)ImmutableList.of());
   }

   List getList(TypeToken var1, List var2) throws ObjectMappingException;

   List getList(TypeToken var1, Supplier var2) throws ObjectMappingException;

   default String getString() {
      return this.getString((String)null);
   }

   default String getString(String def) {
      return (String)this.getValue((Function)(Types::asString), (Object)def);
   }

   default float getFloat() {
      return this.getFloat(0.0F);
   }

   default float getFloat(float def) {
      return (Float)this.getValue((Function)(Types::asFloat), (Object)def);
   }

   default double getDouble() {
      return this.getDouble(0.0);
   }

   default double getDouble(double def) {
      return (Double)this.getValue((Function)(Types::asDouble), (Object)def);
   }

   default int getInt() {
      return this.getInt(0);
   }

   default int getInt(int def) {
      return (Integer)this.getValue((Function)(Types::asInt), (Object)def);
   }

   default long getLong() {
      return this.getLong(0L);
   }

   default long getLong(long def) {
      return (Long)this.getValue((Function)(Types::asLong), (Object)def);
   }

   default boolean getBoolean() {
      return this.getBoolean(false);
   }

   default boolean getBoolean(boolean def) {
      return (Boolean)this.getValue((Function)(Types::asBoolean), (Object)def);
   }

   ConfigurationNode setValue(Object var1);

   default Object getValue(TypeToken type) throws ObjectMappingException {
      return this.getValue((TypeToken)type, (Object)null);
   }

   Object getValue(TypeToken var1, Object var2) throws ObjectMappingException;

   Object getValue(TypeToken var1, Supplier var2) throws ObjectMappingException;

   default ConfigurationNode setValue(TypeToken type, Object value) throws ObjectMappingException {
      if (value == null) {
         this.setValue((Object)null);
         return this;
      } else {
         TypeSerializer serial = this.getOptions().getSerializers().get(type);
         if (serial != null) {
            serial.serialize(type, value, this);
         } else {
            if (!this.getOptions().acceptsType(value.getClass())) {
               throw new ObjectMappingException("No serializer available for type " + type);
            }

            this.setValue(value);
         }

         return this;
      }
   }

   ConfigurationNode mergeValuesFrom(ConfigurationNode var1);

   boolean hasListChildren();

   boolean hasMapChildren();

   List getChildrenList();

   Map getChildrenMap();

   boolean removeChild(Object var1);

   ConfigurationNode getAppendedNode();

   ConfigurationNode getNode(Object... var1);

   boolean isVirtual();
}
