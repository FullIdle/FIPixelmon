package info.pixelmon.repack.com.typesafe.config.impl;

import java.util.Collection;
import java.util.Iterator;

enum ResolveStatus {
   UNRESOLVED,
   RESOLVED;

   static final ResolveStatus fromValues(Collection values) {
      Iterator var1 = values.iterator();

      AbstractConfigValue v;
      do {
         if (!var1.hasNext()) {
            return RESOLVED;
         }

         v = (AbstractConfigValue)var1.next();
      } while(v.resolveStatus() != UNRESOLVED);

      return UNRESOLVED;
   }

   static final ResolveStatus fromBoolean(boolean resolved) {
      return resolved ? RESOLVED : UNRESOLVED;
   }
}
