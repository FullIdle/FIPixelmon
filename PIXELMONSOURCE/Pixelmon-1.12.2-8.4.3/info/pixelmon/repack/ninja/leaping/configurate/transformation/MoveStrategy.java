package info.pixelmon.repack.ninja.leaping.configurate.transformation;

import info.pixelmon.repack.ninja.leaping.configurate.ConfigurationNode;

public enum MoveStrategy {
   MERGE {
      public void move(ConfigurationNode source, ConfigurationNode target) {
         target.mergeValuesFrom(source);
      }
   },
   OVERWRITE {
      public void move(ConfigurationNode source, ConfigurationNode target) {
         target.setValue(source);
      }
   };

   private MoveStrategy() {
   }

   public abstract void move(ConfigurationNode var1, ConfigurationNode var2);

   // $FF: synthetic method
   MoveStrategy(Object x2) {
      this();
   }
}
