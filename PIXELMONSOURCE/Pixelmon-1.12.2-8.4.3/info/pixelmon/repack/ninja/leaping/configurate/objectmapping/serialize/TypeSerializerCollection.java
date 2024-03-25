package info.pixelmon.repack.ninja.leaping.configurate.objectmapping.serialize;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class TypeSerializerCollection {
   private final TypeSerializerCollection parent;
   private final Map typeMatches = new ConcurrentHashMap();
   private final Map functionMatches = new ConcurrentHashMap();

   TypeSerializerCollection(TypeSerializerCollection parent) {
      this.parent = parent;
   }

   public TypeSerializer get(TypeToken type) {
      Preconditions.checkNotNull(type, "type");
      type = type.wrap();
      TypeSerializer serial = (TypeSerializer)this.typeMatches.get(type);
      Iterator var3;
      Map.Entry ent;
      if (serial == null) {
         var3 = this.typeMatches.entrySet().iterator();

         while(var3.hasNext()) {
            ent = (Map.Entry)var3.next();
            if (((TypeToken)ent.getKey()).isSupertypeOf(type)) {
               serial = (TypeSerializer)ent.getValue();
               this.typeMatches.put(type, serial);
               break;
            }
         }
      }

      if (serial == null) {
         var3 = this.functionMatches.entrySet().iterator();

         while(var3.hasNext()) {
            ent = (Map.Entry)var3.next();
            if (((Predicate)ent.getKey()).test(type)) {
               serial = (TypeSerializer)ent.getValue();
               this.typeMatches.put(type, serial);
               break;
            }
         }
      }

      if (serial == null && this.parent != null) {
         serial = this.parent.get(type);
      }

      return serial;
   }

   public TypeSerializerCollection registerType(TypeToken type, TypeSerializer serializer) {
      Preconditions.checkNotNull(type, "type");
      Preconditions.checkNotNull(serializer, "serializer");
      this.typeMatches.put(type, serializer);
      return this;
   }

   public TypeSerializerCollection registerPredicate(Predicate type, TypeSerializer serializer) {
      Preconditions.checkNotNull(type, "type");
      Preconditions.checkNotNull(serializer, "serializer");
      this.functionMatches.put(type, serializer);
      return this;
   }

   public TypeSerializerCollection newChild() {
      return new TypeSerializerCollection(this);
   }
}
