package info.pixelmon.repack.ninja.leaping.configurate.objectmapping;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import info.pixelmon.repack.ninja.leaping.configurate.ConfigurationNode;
import info.pixelmon.repack.ninja.leaping.configurate.commented.CommentedConfigurationNode;
import info.pixelmon.repack.ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ObjectMapper {
   private final Class clazz;
   private final Constructor constructor;
   private final Map cachedFields = new HashMap();

   public static ObjectMapper forClass(Class clazz) throws ObjectMappingException {
      return DefaultObjectMapperFactory.getInstance().getMapper(clazz);
   }

   public static BoundInstance forObject(Object obj) throws ObjectMappingException {
      Preconditions.checkNotNull(obj);
      return forClass(obj.getClass()).bind(obj);
   }

   protected ObjectMapper(Class clazz) throws ObjectMappingException {
      this.clazz = clazz;
      Constructor constructor = null;

      try {
         constructor = clazz.getDeclaredConstructor();
         constructor.setAccessible(true);
      } catch (NoSuchMethodException var4) {
      }

      this.constructor = constructor;
      Class collectClass = clazz;

      do {
         this.collectFields(this.cachedFields, collectClass);
      } while(!(collectClass = collectClass.getSuperclass()).equals(Object.class));

   }

   protected void collectFields(Map cachedFields, Class clazz) throws ObjectMappingException {
      Field[] var3 = clazz.getDeclaredFields();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Field field = var3[var5];
         if (field.isAnnotationPresent(Setting.class)) {
            Setting setting = (Setting)field.getAnnotation(Setting.class);
            String path = setting.value();
            if (path.isEmpty()) {
               path = field.getName();
            }

            FieldData data = new FieldData(field, setting.comment());
            field.setAccessible(true);
            if (!cachedFields.containsKey(path)) {
               cachedFields.put(path, data);
            }
         }
      }

   }

   protected Object constructObject() throws ObjectMappingException {
      if (this.constructor == null) {
         throw new ObjectMappingException("No zero-arg constructor is available for class " + this.clazz + " but is required to construct new instances!");
      } else {
         try {
            return this.constructor.newInstance();
         } catch (InstantiationException var2) {
            throw new ObjectMappingException("Unable to create instance of target class " + this.clazz, var2);
         } catch (IllegalAccessException var3) {
            throw new ObjectMappingException("Unable to create instance of target class " + this.clazz, var3);
         } catch (InvocationTargetException var4) {
            throw new ObjectMappingException("Unable to create instance of target class " + this.clazz, var4);
         }
      }
   }

   public boolean canCreateInstances() {
      return this.constructor != null;
   }

   public BoundInstance bind(Object instance) {
      return new BoundInstance(instance);
   }

   public BoundInstance bindToNew() throws ObjectMappingException {
      return new BoundInstance(this.constructObject());
   }

   public Class getMappedType() {
      return this.clazz;
   }

   public class BoundInstance {
      private final Object boundInstance;

      protected BoundInstance(Object boundInstance) {
         this.boundInstance = boundInstance;
      }

      public Object populate(ConfigurationNode source) throws ObjectMappingException {
         Iterator var2 = ObjectMapper.this.cachedFields.entrySet().iterator();

         while(var2.hasNext()) {
            Map.Entry ent = (Map.Entry)var2.next();
            ConfigurationNode node = source.getNode(ent.getKey());
            ((FieldData)ent.getValue()).deserializeFrom(this.boundInstance, node);
         }

         return this.boundInstance;
      }

      public void serialize(ConfigurationNode target) throws ObjectMappingException {
         Iterator var2 = ObjectMapper.this.cachedFields.entrySet().iterator();

         while(var2.hasNext()) {
            Map.Entry ent = (Map.Entry)var2.next();
            ConfigurationNode node = target.getNode(ent.getKey());
            ((FieldData)ent.getValue()).serializeTo(this.boundInstance, node);
         }

      }

      public Object getInstance() {
         return this.boundInstance;
      }
   }

   protected static class FieldData {
      private final Field field;
      private final TypeToken fieldType;
      private final String comment;

      public FieldData(Field field, String comment) throws ObjectMappingException {
         this.field = field;
         this.comment = comment;
         this.fieldType = TypeToken.of(field.getGenericType());
      }

      public void deserializeFrom(Object instance, ConfigurationNode node) throws ObjectMappingException {
         TypeSerializer serial = node.getOptions().getSerializers().get(this.fieldType);
         if (serial == null) {
            throw new ObjectMappingException("No TypeSerializer found for field " + this.field.getName() + " of type " + this.fieldType);
         } else {
            Object newVal = node.isVirtual() ? null : serial.deserialize(this.fieldType, node);

            try {
               if (newVal == null) {
                  Object existingVal = this.field.get(instance);
                  if (existingVal != null) {
                     this.serializeTo(instance, node);
                  }
               } else {
                  this.field.set(instance, newVal);
               }

            } catch (IllegalAccessException var6) {
               throw new ObjectMappingException("Unable to deserialize field " + this.field.getName(), var6);
            }
         }
      }

      public void serializeTo(Object instance, ConfigurationNode node) throws ObjectMappingException {
         try {
            Object fieldVal = this.field.get(instance);
            if (fieldVal == null) {
               node.setValue((Object)null);
            } else {
               TypeSerializer serial = node.getOptions().getSerializers().get(this.fieldType);
               if (serial == null) {
                  throw new ObjectMappingException("No TypeSerializer found for field " + this.field.getName() + " of type " + this.fieldType);
               }

               serial.serialize(this.fieldType, fieldVal, node);
            }

            if (node instanceof CommentedConfigurationNode && this.comment != null && !this.comment.isEmpty()) {
               CommentedConfigurationNode commentNode = (CommentedConfigurationNode)node;
               if (!commentNode.getComment().isPresent()) {
                  commentNode.setComment(this.comment);
               }
            }

         } catch (IllegalAccessException var5) {
            throw new ObjectMappingException("Unable to serialize field " + this.field.getName(), var5);
         }
      }
   }
}
