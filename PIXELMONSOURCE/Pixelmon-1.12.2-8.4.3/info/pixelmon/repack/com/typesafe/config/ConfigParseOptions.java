package info.pixelmon.repack.com.typesafe.config;

public final class ConfigParseOptions {
   final ConfigSyntax syntax;
   final String originDescription;
   final boolean allowMissing;
   final ConfigIncluder includer;
   final ClassLoader classLoader;

   private ConfigParseOptions(ConfigSyntax syntax, String originDescription, boolean allowMissing, ConfigIncluder includer, ClassLoader classLoader) {
      this.syntax = syntax;
      this.originDescription = originDescription;
      this.allowMissing = allowMissing;
      this.includer = includer;
      this.classLoader = classLoader;
   }

   public static ConfigParseOptions defaults() {
      return new ConfigParseOptions((ConfigSyntax)null, (String)null, true, (ConfigIncluder)null, (ClassLoader)null);
   }

   public ConfigParseOptions setSyntax(ConfigSyntax syntax) {
      return this.syntax == syntax ? this : new ConfigParseOptions(syntax, this.originDescription, this.allowMissing, this.includer, this.classLoader);
   }

   public ConfigSyntax getSyntax() {
      return this.syntax;
   }

   public ConfigParseOptions setOriginDescription(String originDescription) {
      if (this.originDescription == originDescription) {
         return this;
      } else {
         return this.originDescription != null && originDescription != null && this.originDescription.equals(originDescription) ? this : new ConfigParseOptions(this.syntax, originDescription, this.allowMissing, this.includer, this.classLoader);
      }
   }

   public String getOriginDescription() {
      return this.originDescription;
   }

   ConfigParseOptions withFallbackOriginDescription(String originDescription) {
      return this.originDescription == null ? this.setOriginDescription(originDescription) : this;
   }

   public ConfigParseOptions setAllowMissing(boolean allowMissing) {
      return this.allowMissing == allowMissing ? this : new ConfigParseOptions(this.syntax, this.originDescription, allowMissing, this.includer, this.classLoader);
   }

   public boolean getAllowMissing() {
      return this.allowMissing;
   }

   public ConfigParseOptions setIncluder(ConfigIncluder includer) {
      return this.includer == includer ? this : new ConfigParseOptions(this.syntax, this.originDescription, this.allowMissing, includer, this.classLoader);
   }

   public ConfigParseOptions prependIncluder(ConfigIncluder includer) {
      if (includer == null) {
         throw new NullPointerException("null includer passed to prependIncluder");
      } else if (this.includer == includer) {
         return this;
      } else {
         return this.includer != null ? this.setIncluder(includer.withFallback(this.includer)) : this.setIncluder(includer);
      }
   }

   public ConfigParseOptions appendIncluder(ConfigIncluder includer) {
      if (includer == null) {
         throw new NullPointerException("null includer passed to appendIncluder");
      } else if (this.includer == includer) {
         return this;
      } else {
         return this.includer != null ? this.setIncluder(this.includer.withFallback(includer)) : this.setIncluder(includer);
      }
   }

   public ConfigIncluder getIncluder() {
      return this.includer;
   }

   public ConfigParseOptions setClassLoader(ClassLoader loader) {
      return this.classLoader == loader ? this : new ConfigParseOptions(this.syntax, this.originDescription, this.allowMissing, this.includer, loader);
   }

   public ClassLoader getClassLoader() {
      return this.classLoader == null ? Thread.currentThread().getContextClassLoader() : this.classLoader;
   }
}
