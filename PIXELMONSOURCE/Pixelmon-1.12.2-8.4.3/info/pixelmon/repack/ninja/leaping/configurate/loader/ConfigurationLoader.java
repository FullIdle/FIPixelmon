package info.pixelmon.repack.ninja.leaping.configurate.loader;

import info.pixelmon.repack.ninja.leaping.configurate.ConfigurationNode;
import info.pixelmon.repack.ninja.leaping.configurate.ConfigurationOptions;
import java.io.IOException;

public interface ConfigurationLoader {
   ConfigurationOptions getDefaultOptions();

   default ConfigurationNode load() throws IOException {
      return this.load(this.getDefaultOptions());
   }

   ConfigurationNode load(ConfigurationOptions var1) throws IOException;

   void save(ConfigurationNode var1) throws IOException;

   default ConfigurationNode createEmptyNode() {
      return this.createEmptyNode(this.getDefaultOptions());
   }

   ConfigurationNode createEmptyNode(ConfigurationOptions var1);
}
