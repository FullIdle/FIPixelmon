package info.pixelmon.repack.com.typesafe.config.impl;

import java.util.Collection;

final class ConfigNodeConcatenation extends ConfigNodeComplexValue {
   ConfigNodeConcatenation(Collection children) {
      super(children);
   }

   protected ConfigNodeConcatenation newNode(Collection nodes) {
      return new ConfigNodeConcatenation(nodes);
   }
}
