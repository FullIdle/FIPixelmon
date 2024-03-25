package info.pixelmon.repack.ninja.leaping.configurate;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;
import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.DefaultObjectMapperFactory;
import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.ObjectMapperFactory;
import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.serialize.TypeSerializerCollection;
import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import info.pixelmon.repack.ninja.leaping.configurate.util.MapFactories;
import info.pixelmon.repack.ninja.leaping.configurate.util.MapFactory;
import java.util.Set;

public class ConfigurationOptions {
   private final MapFactory mapSupplier;
   private final String header;
   private final TypeSerializerCollection serializers;
   private final ImmutableSet acceptedTypes;
   private final ObjectMapperFactory objectMapperFactory;
   private final boolean shouldCopyDefaults;

   private ConfigurationOptions(MapFactory mapSupplier, String header, TypeSerializerCollection serializers, Set acceptedTypes, ObjectMapperFactory objectMapperFactory, boolean shouldCopyDefaults) {
      this.mapSupplier = mapSupplier;
      this.header = header;
      this.serializers = serializers;
      this.acceptedTypes = acceptedTypes == null ? null : ImmutableSet.copyOf(acceptedTypes);
      this.objectMapperFactory = objectMapperFactory;
      this.shouldCopyDefaults = shouldCopyDefaults;
   }

   public static ConfigurationOptions defaults() {
      return new ConfigurationOptions(MapFactories.insertionOrdered(), (String)null, TypeSerializers.getDefaultSerializers(), (Set)null, DefaultObjectMapperFactory.getInstance(), false);
   }

   public MapFactory getMapFactory() {
      return this.mapSupplier;
   }

   public ConfigurationOptions setMapFactory(MapFactory factory) {
      Preconditions.checkNotNull(factory, "factory");
      return new ConfigurationOptions(factory, this.header, this.serializers, this.acceptedTypes, this.objectMapperFactory, this.shouldCopyDefaults);
   }

   public String getHeader() {
      return this.header;
   }

   public ConfigurationOptions setHeader(String header) {
      return new ConfigurationOptions(this.mapSupplier, header, this.serializers, this.acceptedTypes, this.objectMapperFactory, this.shouldCopyDefaults);
   }

   public TypeSerializerCollection getSerializers() {
      return this.serializers;
   }

   public ConfigurationOptions setSerializers(TypeSerializerCollection serializers) {
      return new ConfigurationOptions(this.mapSupplier, this.header, serializers, this.acceptedTypes, this.objectMapperFactory, this.shouldCopyDefaults);
   }

   public ObjectMapperFactory getObjectMapperFactory() {
      return this.objectMapperFactory;
   }

   public ConfigurationOptions setObjectMapperFactory(ObjectMapperFactory factory) {
      Preconditions.checkNotNull(factory, "factory");
      return new ConfigurationOptions(this.mapSupplier, this.header, this.serializers, this.acceptedTypes, factory, this.shouldCopyDefaults);
   }

   public boolean acceptsType(Class type) {
      if (this.acceptedTypes == null) {
         return true;
      } else if (this.acceptedTypes.contains(type)) {
         return true;
      } else {
         UnmodifiableIterator var2 = this.acceptedTypes.iterator();

         Class clazz;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            clazz = (Class)var2.next();
         } while(!clazz.isAssignableFrom(type));

         return true;
      }
   }

   public ConfigurationOptions setAcceptedTypes(Set acceptedTypes) {
      return new ConfigurationOptions(this.mapSupplier, this.header, this.serializers, acceptedTypes, this.objectMapperFactory, this.shouldCopyDefaults);
   }

   public boolean shouldCopyDefaults() {
      return this.shouldCopyDefaults;
   }

   public ConfigurationOptions setShouldCopyDefaults(boolean shouldCopyDefaults) {
      return new ConfigurationOptions(this.mapSupplier, this.header, this.serializers, this.acceptedTypes, this.objectMapperFactory, shouldCopyDefaults);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof ConfigurationOptions)) {
         return false;
      } else {
         ConfigurationOptions that = (ConfigurationOptions)o;
         return Objects.equal(this.shouldCopyDefaults, that.shouldCopyDefaults) && Objects.equal(this.mapSupplier, that.mapSupplier) && Objects.equal(this.header, that.header) && Objects.equal(this.serializers, that.serializers) && Objects.equal(this.acceptedTypes, that.acceptedTypes) && Objects.equal(this.objectMapperFactory, that.objectMapperFactory);
      }
   }

   public int hashCode() {
      return Objects.hashCode(new Object[]{this.mapSupplier, this.header, this.serializers, this.acceptedTypes, this.objectMapperFactory, this.shouldCopyDefaults});
   }
}
