package info.pixelmon.repack.ninja.leaping.configurate.transformation;

import info.pixelmon.repack.ninja.leaping.configurate.ConfigurationNode;
import java.util.Arrays;

public class ChainedConfigurationTransformation extends ConfigurationTransformation {
   private final ConfigurationTransformation[] transformations;

   public ChainedConfigurationTransformation(ConfigurationTransformation[] transformations) {
      this.transformations = (ConfigurationTransformation[])Arrays.copyOf(transformations, transformations.length);
   }

   public void apply(ConfigurationNode node) {
      ConfigurationTransformation[] var2 = this.transformations;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ConfigurationTransformation transformation = var2[var4];
         transformation.apply(node);
      }

   }
}
