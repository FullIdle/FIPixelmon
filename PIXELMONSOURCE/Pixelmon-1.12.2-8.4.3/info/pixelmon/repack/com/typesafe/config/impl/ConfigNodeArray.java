package info.pixelmon.repack.com.typesafe.config.impl;

import java.util.Collection;

final class ConfigNodeArray extends ConfigNodeComplexValue {
   ConfigNodeArray(Collection children) {
      super(children);
   }

   protected ConfigNodeArray newNode(Collection nodes) {
      return new ConfigNodeArray(nodes);
   }
}
