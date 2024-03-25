package info.pixelmon.repack.com.typesafe.config;

public final class ConfigResolveOptions {
   private final boolean useSystemEnvironment;
   private final boolean allowUnresolved;

   private ConfigResolveOptions(boolean useSystemEnvironment, boolean allowUnresolved) {
      this.useSystemEnvironment = useSystemEnvironment;
      this.allowUnresolved = allowUnresolved;
   }

   public static ConfigResolveOptions defaults() {
      return new ConfigResolveOptions(true, false);
   }

   public static ConfigResolveOptions noSystem() {
      return defaults().setUseSystemEnvironment(false);
   }

   public ConfigResolveOptions setUseSystemEnvironment(boolean value) {
      return new ConfigResolveOptions(value, this.allowUnresolved);
   }

   public boolean getUseSystemEnvironment() {
      return this.useSystemEnvironment;
   }

   public ConfigResolveOptions setAllowUnresolved(boolean value) {
      return new ConfigResolveOptions(this.useSystemEnvironment, value);
   }

   public boolean getAllowUnresolved() {
      return this.allowUnresolved;
   }
}
