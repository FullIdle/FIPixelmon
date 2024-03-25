package info.pixelmon.repack.ninja.leaping.configurate.transformation;

import info.pixelmon.repack.ninja.leaping.configurate.ConfigurationNode;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;

class VersionedTransformation extends ConfigurationTransformation {
   private final Object[] versionPath;
   private final SortedMap versionTransformations;

   VersionedTransformation(Object[] versionPath, SortedMap versionTransformations) {
      this.versionPath = versionPath;
      this.versionTransformations = versionTransformations;
   }

   public void apply(ConfigurationNode node) {
      ConfigurationNode versionNode = node.getNode(this.versionPath);
      int currentVersion = versionNode.getInt(-1);
      Iterator var4 = this.versionTransformations.entrySet().iterator();

      while(var4.hasNext()) {
         Map.Entry entry = (Map.Entry)var4.next();
         if ((Integer)entry.getKey() > currentVersion) {
            ((ConfigurationTransformation)entry.getValue()).apply(node);
            currentVersion = (Integer)entry.getKey();
         }
      }

      versionNode.setValue(currentVersion);
   }
}
