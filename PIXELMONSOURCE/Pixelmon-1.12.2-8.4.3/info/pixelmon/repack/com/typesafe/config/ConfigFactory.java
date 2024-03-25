package info.pixelmon.repack.com.typesafe.config;

import info.pixelmon.repack.com.typesafe.config.impl.ConfigImpl;
import info.pixelmon.repack.com.typesafe.config.impl.Parseable;
import java.io.File;
import java.io.Reader;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;

public final class ConfigFactory {
   private static final String STRATEGY_PROPERTY_NAME = "config.strategy";

   private ConfigFactory() {
   }

   public static Config load(String resourceBasename) {
      return load(resourceBasename, ConfigParseOptions.defaults(), ConfigResolveOptions.defaults());
   }

   public static Config load(ClassLoader loader, String resourceBasename) {
      return load(resourceBasename, ConfigParseOptions.defaults().setClassLoader(loader), ConfigResolveOptions.defaults());
   }

   public static Config load(String resourceBasename, ConfigParseOptions parseOptions, ConfigResolveOptions resolveOptions) {
      ConfigParseOptions withLoader = ensureClassLoader(parseOptions, "load");
      Config appConfig = parseResourcesAnySyntax(resourceBasename, withLoader);
      return load(withLoader.getClassLoader(), appConfig, resolveOptions);
   }

   public static Config load(ClassLoader loader, String resourceBasename, ConfigParseOptions parseOptions, ConfigResolveOptions resolveOptions) {
      return load(resourceBasename, parseOptions.setClassLoader(loader), resolveOptions);
   }

   private static ClassLoader checkedContextClassLoader(String methodName) {
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      if (loader == null) {
         throw new ConfigException.BugOrBroken("Context class loader is not set for the current thread; if Thread.currentThread().getContextClassLoader() returns null, you must pass a ClassLoader explicitly to ConfigFactory." + methodName);
      } else {
         return loader;
      }
   }

   private static ConfigParseOptions ensureClassLoader(ConfigParseOptions options, String methodName) {
      return options.getClassLoader() == null ? options.setClassLoader(checkedContextClassLoader(methodName)) : options;
   }

   public static Config load(Config config) {
      return load(checkedContextClassLoader("load"), config);
   }

   public static Config load(ClassLoader loader, Config config) {
      return load(loader, config, ConfigResolveOptions.defaults());
   }

   public static Config load(Config config, ConfigResolveOptions resolveOptions) {
      return load(checkedContextClassLoader("load"), config, resolveOptions);
   }

   public static Config load(ClassLoader loader, Config config, ConfigResolveOptions resolveOptions) {
      return defaultOverrides(loader).withFallback(config).withFallback(defaultReference(loader)).resolve(resolveOptions);
   }

   public static Config load() {
      ClassLoader loader = checkedContextClassLoader("load");
      return load(loader);
   }

   public static Config load(ConfigParseOptions parseOptions) {
      return load(parseOptions, ConfigResolveOptions.defaults());
   }

   public static Config load(final ClassLoader loader) {
      final ConfigParseOptions withLoader = ConfigParseOptions.defaults().setClassLoader(loader);
      return ConfigImpl.computeCachedConfig(loader, "load", new Callable() {
         public Config call() {
            return ConfigFactory.load(loader, ConfigFactory.defaultApplication(withLoader));
         }
      });
   }

   public static Config load(ClassLoader loader, ConfigParseOptions parseOptions) {
      return load(parseOptions.setClassLoader(loader));
   }

   public static Config load(ClassLoader loader, ConfigResolveOptions resolveOptions) {
      return load(loader, ConfigParseOptions.defaults(), resolveOptions);
   }

   public static Config load(ClassLoader loader, ConfigParseOptions parseOptions, ConfigResolveOptions resolveOptions) {
      ConfigParseOptions withLoader = ensureClassLoader(parseOptions, "load");
      return load(loader, defaultApplication(withLoader), resolveOptions);
   }

   public static Config load(ConfigParseOptions parseOptions, ConfigResolveOptions resolveOptions) {
      ConfigParseOptions withLoader = ensureClassLoader(parseOptions, "load");
      return load(defaultApplication(withLoader), resolveOptions);
   }

   public static Config defaultReference() {
      return defaultReference(checkedContextClassLoader("defaultReference"));
   }

   public static Config defaultReference(ClassLoader loader) {
      return ConfigImpl.defaultReference(loader);
   }

   public static Config defaultOverrides() {
      return systemProperties();
   }

   public static Config defaultOverrides(ClassLoader loader) {
      return systemProperties();
   }

   public static Config defaultApplication() {
      return defaultApplication(ConfigParseOptions.defaults());
   }

   public static Config defaultApplication(ClassLoader loader) {
      return defaultApplication(ConfigParseOptions.defaults().setClassLoader(loader));
   }

   public static Config defaultApplication(ConfigParseOptions options) {
      return getConfigLoadingStrategy().parseApplicationConfig(ensureClassLoader(options, "defaultApplication"));
   }

   public static void invalidateCaches() {
      ConfigImpl.reloadSystemPropertiesConfig();
   }

   public static Config empty() {
      return empty((String)null);
   }

   public static Config empty(String originDescription) {
      return ConfigImpl.emptyConfig(originDescription);
   }

   public static Config systemProperties() {
      return ConfigImpl.systemPropertiesAsConfig();
   }

   public static Config systemEnvironment() {
      return ConfigImpl.envVariablesAsConfig();
   }

   public static Config parseProperties(Properties properties, ConfigParseOptions options) {
      return Parseable.newProperties(properties, options).parse().toConfig();
   }

   public static Config parseProperties(Properties properties) {
      return parseProperties(properties, ConfigParseOptions.defaults());
   }

   public static Config parseReader(Reader reader, ConfigParseOptions options) {
      return Parseable.newReader(reader, options).parse().toConfig();
   }

   public static Config parseReader(Reader reader) {
      return parseReader(reader, ConfigParseOptions.defaults());
   }

   public static Config parseURL(URL url, ConfigParseOptions options) {
      return Parseable.newURL(url, options).parse().toConfig();
   }

   public static Config parseURL(URL url) {
      return parseURL(url, ConfigParseOptions.defaults());
   }

   public static Config parseFile(File file, ConfigParseOptions options) {
      return Parseable.newFile(file, options).parse().toConfig();
   }

   public static Config parseFile(File file) {
      return parseFile(file, ConfigParseOptions.defaults());
   }

   public static Config parseFileAnySyntax(File fileBasename, ConfigParseOptions options) {
      return ConfigImpl.parseFileAnySyntax(fileBasename, options).toConfig();
   }

   public static Config parseFileAnySyntax(File fileBasename) {
      return parseFileAnySyntax(fileBasename, ConfigParseOptions.defaults());
   }

   public static Config parseResources(Class klass, String resource, ConfigParseOptions options) {
      return Parseable.newResources(klass, resource, options).parse().toConfig();
   }

   public static Config parseResources(Class klass, String resource) {
      return parseResources(klass, resource, ConfigParseOptions.defaults());
   }

   public static Config parseResourcesAnySyntax(Class klass, String resourceBasename, ConfigParseOptions options) {
      return ConfigImpl.parseResourcesAnySyntax(klass, resourceBasename, options).toConfig();
   }

   public static Config parseResourcesAnySyntax(Class klass, String resourceBasename) {
      return parseResourcesAnySyntax(klass, resourceBasename, ConfigParseOptions.defaults());
   }

   public static Config parseResources(ClassLoader loader, String resource, ConfigParseOptions options) {
      return parseResources(resource, options.setClassLoader(loader));
   }

   public static Config parseResources(ClassLoader loader, String resource) {
      return parseResources(loader, resource, ConfigParseOptions.defaults());
   }

   public static Config parseResourcesAnySyntax(ClassLoader loader, String resourceBasename, ConfigParseOptions options) {
      return ConfigImpl.parseResourcesAnySyntax(resourceBasename, options.setClassLoader(loader)).toConfig();
   }

   public static Config parseResourcesAnySyntax(ClassLoader loader, String resourceBasename) {
      return parseResourcesAnySyntax(loader, resourceBasename, ConfigParseOptions.defaults());
   }

   public static Config parseResources(String resource, ConfigParseOptions options) {
      ConfigParseOptions withLoader = ensureClassLoader(options, "parseResources");
      return Parseable.newResources(resource, withLoader).parse().toConfig();
   }

   public static Config parseResources(String resource) {
      return parseResources(resource, ConfigParseOptions.defaults());
   }

   public static Config parseResourcesAnySyntax(String resourceBasename, ConfigParseOptions options) {
      return ConfigImpl.parseResourcesAnySyntax(resourceBasename, options).toConfig();
   }

   public static Config parseResourcesAnySyntax(String resourceBasename) {
      return parseResourcesAnySyntax(resourceBasename, ConfigParseOptions.defaults());
   }

   public static Config parseString(String s, ConfigParseOptions options) {
      return Parseable.newString(s, options).parse().toConfig();
   }

   public static Config parseString(String s) {
      return parseString(s, ConfigParseOptions.defaults());
   }

   public static Config parseMap(Map values, String originDescription) {
      return ConfigImpl.fromPathMap(values, originDescription).toConfig();
   }

   public static Config parseMap(Map values) {
      return parseMap(values, (String)null);
   }

   private static ConfigLoadingStrategy getConfigLoadingStrategy() {
      String className = System.getProperties().getProperty("config.strategy");
      if (className != null) {
         try {
            return (ConfigLoadingStrategy)ConfigLoadingStrategy.class.cast(Class.forName(className).newInstance());
         } catch (Throwable var2) {
            throw new ConfigException.BugOrBroken("Failed to load strategy: " + className, var2);
         }
      } else {
         return new DefaultConfigLoadingStrategy();
      }
   }
}
