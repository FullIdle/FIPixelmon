package info.pixelmon.repack.com.typesafe.config;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class DefaultConfigLoadingStrategy implements ConfigLoadingStrategy {
   public Config parseApplicationConfig(ConfigParseOptions parseOptions) {
      ClassLoader loader = parseOptions.getClassLoader();
      if (loader == null) {
         throw new ConfigException.BugOrBroken("ClassLoader should have been set here; bug in ConfigFactory. (You can probably work around this bug by passing in a class loader or calling currentThread().setContextClassLoader() though.)");
      } else {
         int specified = 0;
         String resource = System.getProperty("config.resource");
         if (resource != null) {
            ++specified;
         }

         String file = System.getProperty("config.file");
         if (file != null) {
            ++specified;
         }

         String url = System.getProperty("config.url");
         if (url != null) {
            ++specified;
         }

         if (specified == 0) {
            return ConfigFactory.parseResourcesAnySyntax("application", parseOptions);
         } else if (specified > 1) {
            throw new ConfigException.Generic("You set more than one of config.file='" + file + "', config.url='" + url + "', config.resource='" + resource + "'; don't know which one to use!");
         } else {
            ConfigParseOptions overrideOptions = parseOptions.setAllowMissing(false);
            if (resource != null) {
               if (resource.startsWith("/")) {
                  resource = resource.substring(1);
               }

               return ConfigFactory.parseResources(loader, resource, overrideOptions);
            } else if (file != null) {
               return ConfigFactory.parseFile(new File(file), overrideOptions);
            } else {
               try {
                  return ConfigFactory.parseURL(new URL(url), overrideOptions);
               } catch (MalformedURLException var9) {
                  throw new ConfigException.Generic("Bad URL in config.url system property: '" + url + "': " + var9.getMessage(), var9);
               }
            }
         }
      }
   }
}
