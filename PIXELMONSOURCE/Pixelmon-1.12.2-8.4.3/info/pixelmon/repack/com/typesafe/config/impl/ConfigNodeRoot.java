package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigException;
import info.pixelmon.repack.com.typesafe.config.ConfigOrigin;
import info.pixelmon.repack.com.typesafe.config.ConfigSyntax;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

final class ConfigNodeRoot extends ConfigNodeComplexValue {
   private final ConfigOrigin origin;

   ConfigNodeRoot(Collection children, ConfigOrigin origin) {
      super(children);
      this.origin = origin;
   }

   protected ConfigNodeRoot newNode(Collection nodes) {
      throw new ConfigException.BugOrBroken("Tried to indent the root object");
   }

   protected ConfigNodeComplexValue value() {
      Iterator var1 = this.children.iterator();

      AbstractConfigNode node;
      do {
         if (!var1.hasNext()) {
            throw new ConfigException.BugOrBroken("ConfigNodeRoot did not contain a value");
         }

         node = (AbstractConfigNode)var1.next();
      } while(!(node instanceof ConfigNodeComplexValue));

      return (ConfigNodeComplexValue)node;
   }

   protected ConfigNodeRoot setValue(String desiredPath, AbstractConfigNodeValue value, ConfigSyntax flavor) {
      ArrayList childrenCopy = new ArrayList(this.children);

      for(int i = 0; i < childrenCopy.size(); ++i) {
         AbstractConfigNode node = (AbstractConfigNode)childrenCopy.get(i);
         if (node instanceof ConfigNodeComplexValue) {
            if (node instanceof ConfigNodeArray) {
               throw new ConfigException.WrongType(this.origin, "The ConfigDocument had an array at the root level, and values cannot be modified inside an array.");
            }

            if (node instanceof ConfigNodeObject) {
               if (value == null) {
                  childrenCopy.set(i, ((ConfigNodeObject)node).removeValueOnPath(desiredPath, flavor));
               } else {
                  childrenCopy.set(i, ((ConfigNodeObject)node).setValueOnPath(desiredPath, value, flavor));
               }

               return new ConfigNodeRoot(childrenCopy, this.origin);
            }
         }
      }

      throw new ConfigException.BugOrBroken("ConfigNodeRoot did not contain a value");
   }

   protected boolean hasValue(String desiredPath) {
      Path path = PathParser.parsePath(desiredPath);
      ArrayList childrenCopy = new ArrayList(this.children);

      for(int i = 0; i < childrenCopy.size(); ++i) {
         AbstractConfigNode node = (AbstractConfigNode)childrenCopy.get(i);
         if (node instanceof ConfigNodeComplexValue) {
            if (node instanceof ConfigNodeArray) {
               throw new ConfigException.WrongType(this.origin, "The ConfigDocument had an array at the root level, and values cannot be modified inside an array.");
            }

            if (node instanceof ConfigNodeObject) {
               return ((ConfigNodeObject)node).hasValue(path);
            }
         }
      }

      throw new ConfigException.BugOrBroken("ConfigNodeRoot did not contain a value");
   }
}
