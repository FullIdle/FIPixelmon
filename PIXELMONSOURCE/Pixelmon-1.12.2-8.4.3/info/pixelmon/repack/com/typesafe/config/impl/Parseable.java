package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigException;
import info.pixelmon.repack.com.typesafe.config.ConfigIncludeContext;
import info.pixelmon.repack.com.typesafe.config.ConfigObject;
import info.pixelmon.repack.com.typesafe.config.ConfigOrigin;
import info.pixelmon.repack.com.typesafe.config.ConfigParseOptions;
import info.pixelmon.repack.com.typesafe.config.ConfigParseable;
import info.pixelmon.repack.com.typesafe.config.ConfigSyntax;
import info.pixelmon.repack.com.typesafe.config.ConfigValue;
import info.pixelmon.repack.com.typesafe.config.parser.ConfigDocument;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilterReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;

public abstract class Parseable implements ConfigParseable {
   private ConfigIncludeContext includeContext;
   private ConfigParseOptions initialOptions;
   private ConfigOrigin initialOrigin;
   private static final ThreadLocal parseStack = new ThreadLocal() {
      protected LinkedList initialValue() {
         return new LinkedList();
      }
   };
   private static final int MAX_INCLUDE_DEPTH = 50;
   private static final String jsonContentType = "application/json";
   private static final String propertiesContentType = "text/x-java-properties";
   private static final String hoconContentType = "application/hocon";

   protected Parseable() {
   }

   private ConfigParseOptions fixupOptions(ConfigParseOptions baseOptions) {
      ConfigSyntax syntax = baseOptions.getSyntax();
      if (syntax == null) {
         syntax = this.guessSyntax();
      }

      if (syntax == null) {
         syntax = ConfigSyntax.CONF;
      }

      ConfigParseOptions modified = baseOptions.setSyntax(syntax);
      modified = modified.appendIncluder(ConfigImpl.defaultIncluder());
      modified = modified.setIncluder(SimpleIncluder.makeFull(modified.getIncluder()));
      return modified;
   }

   protected void postConstruct(ConfigParseOptions baseOptions) {
      this.initialOptions = this.fixupOptions(baseOptions);
      this.includeContext = new SimpleIncludeContext(this);
      if (this.initialOptions.getOriginDescription() != null) {
         this.initialOrigin = SimpleConfigOrigin.newSimple(this.initialOptions.getOriginDescription());
      } else {
         this.initialOrigin = this.createOrigin();
      }

   }

   protected abstract Reader reader() throws IOException;

   protected Reader reader(ConfigParseOptions options) throws IOException {
      return this.reader();
   }

   protected static void trace(String message) {
      if (ConfigImpl.traceLoadsEnabled()) {
         ConfigImpl.trace(message);
      }

   }

   ConfigSyntax guessSyntax() {
      return null;
   }

   ConfigSyntax contentType() {
      return null;
   }

   ConfigParseable relativeTo(String filename) {
      String resource = filename;
      if (filename.startsWith("/")) {
         resource = filename.substring(1);
      }

      return newResources(resource, this.options().setOriginDescription((String)null));
   }

   ConfigIncludeContext includeContext() {
      return this.includeContext;
   }

   static AbstractConfigObject forceParsedToObject(ConfigValue value) {
      if (value instanceof AbstractConfigObject) {
         return (AbstractConfigObject)value;
      } else {
         throw new ConfigException.WrongType(value.origin(), "", "object at file root", value.valueType().name());
      }
   }

   public ConfigObject parse(ConfigParseOptions baseOptions) {
      LinkedList stack = (LinkedList)parseStack.get();
      if (stack.size() >= 50) {
         throw new ConfigException.Parse(this.initialOrigin, "include statements nested more than 50 times, you probably have a cycle in your includes. Trace: " + stack);
      } else {
         stack.addFirst(this);

         AbstractConfigObject var3;
         try {
            var3 = forceParsedToObject(this.parseValue(baseOptions));
         } finally {
            stack.removeFirst();
            if (stack.isEmpty()) {
               parseStack.remove();
            }

         }

         return var3;
      }
   }

   final AbstractConfigValue parseValue(ConfigParseOptions baseOptions) {
      ConfigParseOptions options = this.fixupOptions(baseOptions);
      Object origin;
      if (options.getOriginDescription() != null) {
         origin = SimpleConfigOrigin.newSimple(options.getOriginDescription());
      } else {
         origin = this.initialOrigin;
      }

      return this.parseValue((ConfigOrigin)origin, options);
   }

   private final AbstractConfigValue parseValue(ConfigOrigin origin, ConfigParseOptions finalOptions) {
      try {
         return this.rawParseValue(origin, finalOptions);
      } catch (IOException var4) {
         if (finalOptions.getAllowMissing()) {
            return SimpleConfigObject.emptyMissing(origin);
         } else {
            trace("exception loading " + origin.description() + ": " + var4.getClass().getName() + ": " + var4.getMessage());
            throw new ConfigException.IO(origin, var4.getClass().getName() + ": " + var4.getMessage(), var4);
         }
      }
   }

   final ConfigDocument parseDocument(ConfigParseOptions baseOptions) {
      ConfigParseOptions options = this.fixupOptions(baseOptions);
      Object origin;
      if (options.getOriginDescription() != null) {
         origin = SimpleConfigOrigin.newSimple(options.getOriginDescription());
      } else {
         origin = this.initialOrigin;
      }

      return this.parseDocument((ConfigOrigin)origin, options);
   }

   private final ConfigDocument parseDocument(ConfigOrigin origin, ConfigParseOptions finalOptions) {
      try {
         return this.rawParseDocument(origin, finalOptions);
      } catch (IOException var5) {
         if (finalOptions.getAllowMissing()) {
            ArrayList children = new ArrayList();
            children.add(new ConfigNodeObject(new ArrayList()));
            return new SimpleConfigDocument(new ConfigNodeRoot(children, origin), finalOptions);
         } else {
            trace("exception loading " + origin.description() + ": " + var5.getClass().getName() + ": " + var5.getMessage());
            throw new ConfigException.IO(origin, var5.getClass().getName() + ": " + var5.getMessage(), var5);
         }
      }
   }

   protected AbstractConfigValue rawParseValue(ConfigOrigin origin, ConfigParseOptions finalOptions) throws IOException {
      Reader reader = this.reader(finalOptions);
      ConfigSyntax contentType = this.contentType();
      ConfigParseOptions optionsWithContentType;
      if (contentType != null) {
         if (ConfigImpl.traceLoadsEnabled() && finalOptions.getSyntax() != null) {
            trace("Overriding syntax " + finalOptions.getSyntax() + " with Content-Type which specified " + contentType);
         }

         optionsWithContentType = finalOptions.setSyntax(contentType);
      } else {
         optionsWithContentType = finalOptions;
      }

      AbstractConfigValue var6;
      try {
         var6 = this.rawParseValue(reader, origin, optionsWithContentType);
      } finally {
         reader.close();
      }

      return var6;
   }

   private AbstractConfigValue rawParseValue(Reader reader, ConfigOrigin origin, ConfigParseOptions finalOptions) throws IOException {
      if (finalOptions.getSyntax() == ConfigSyntax.PROPERTIES) {
         return PropertiesParser.parse(reader, origin);
      } else {
         Iterator tokens = Tokenizer.tokenize(origin, reader, finalOptions.getSyntax());
         ConfigNodeRoot document = ConfigDocumentParser.parse(tokens, origin, finalOptions);
         return ConfigParser.parse(document, origin, finalOptions, this.includeContext());
      }
   }

   protected ConfigDocument rawParseDocument(ConfigOrigin origin, ConfigParseOptions finalOptions) throws IOException {
      Reader reader = this.reader(finalOptions);
      ConfigSyntax contentType = this.contentType();
      ConfigParseOptions optionsWithContentType;
      if (contentType != null) {
         if (ConfigImpl.traceLoadsEnabled() && finalOptions.getSyntax() != null) {
            trace("Overriding syntax " + finalOptions.getSyntax() + " with Content-Type which specified " + contentType);
         }

         optionsWithContentType = finalOptions.setSyntax(contentType);
      } else {
         optionsWithContentType = finalOptions;
      }

      ConfigDocument var6;
      try {
         var6 = this.rawParseDocument(reader, origin, optionsWithContentType);
      } finally {
         reader.close();
      }

      return var6;
   }

   private ConfigDocument rawParseDocument(Reader reader, ConfigOrigin origin, ConfigParseOptions finalOptions) throws IOException {
      Iterator tokens = Tokenizer.tokenize(origin, reader, finalOptions.getSyntax());
      return new SimpleConfigDocument(ConfigDocumentParser.parse(tokens, origin, finalOptions), finalOptions);
   }

   public ConfigObject parse() {
      return forceParsedToObject(this.parseValue(this.options()));
   }

   public ConfigDocument parseConfigDocument() {
      return this.parseDocument(this.options());
   }

   AbstractConfigValue parseValue() {
      return this.parseValue(this.options());
   }

   public final ConfigOrigin origin() {
      return this.initialOrigin;
   }

   protected abstract ConfigOrigin createOrigin();

   public ConfigParseOptions options() {
      return this.initialOptions;
   }

   public String toString() {
      return this.getClass().getSimpleName();
   }

   private static ConfigSyntax syntaxFromExtension(String name) {
      if (name.endsWith(".json")) {
         return ConfigSyntax.JSON;
      } else if (name.endsWith(".conf")) {
         return ConfigSyntax.CONF;
      } else {
         return name.endsWith(".properties") ? ConfigSyntax.PROPERTIES : null;
      }
   }

   private static Reader readerFromStream(InputStream input) {
      return readerFromStream(input, "UTF-8");
   }

   private static Reader readerFromStream(InputStream input, String encoding) {
      try {
         Reader reader = new InputStreamReader(input, encoding);
         return new BufferedReader(reader);
      } catch (UnsupportedEncodingException var3) {
         throw new ConfigException.BugOrBroken("Java runtime does not support UTF-8", var3);
      }
   }

   private static Reader doNotClose(Reader input) {
      return new FilterReader(input) {
         public void close() {
         }
      };
   }

   static URL relativeTo(URL url, String filename) {
      if ((new File(filename)).isAbsolute()) {
         return null;
      } else {
         try {
            URI siblingURI = url.toURI();
            URI relative = new URI(filename);
            URL resolved = siblingURI.resolve(relative).toURL();
            return resolved;
         } catch (MalformedURLException var5) {
            return null;
         } catch (URISyntaxException var6) {
            return null;
         } catch (IllegalArgumentException var7) {
            return null;
         }
      }
   }

   static File relativeTo(File file, String filename) {
      File child = new File(filename);
      if (child.isAbsolute()) {
         return null;
      } else {
         File parent = file.getParentFile();
         return parent == null ? null : new File(parent, filename);
      }
   }

   public static Parseable newNotFound(String whatNotFound, String message, ConfigParseOptions options) {
      return new ParseableNotFound(whatNotFound, message, options);
   }

   public static Parseable newReader(Reader reader, ConfigParseOptions options) {
      return new ParseableReader(doNotClose(reader), options);
   }

   public static Parseable newString(String input, ConfigParseOptions options) {
      return new ParseableString(input, options);
   }

   public static Parseable newURL(URL input, ConfigParseOptions options) {
      return (Parseable)(input.getProtocol().equals("file") ? newFile(ConfigImplUtil.urlToFile(input), options) : new ParseableURL(input, options));
   }

   public static Parseable newFile(File input, ConfigParseOptions options) {
      return new ParseableFile(input, options);
   }

   private static Parseable newResourceURL(URL input, ConfigParseOptions options, String resource, Relativizer relativizer) {
      return new ParseableResourceURL(input, options, resource, relativizer);
   }

   public static Parseable newResources(Class klass, String resource, ConfigParseOptions options) {
      return newResources(convertResourceName(klass, resource), options.setClassLoader(klass.getClassLoader()));
   }

   private static String convertResourceName(Class klass, String resource) {
      if (resource.startsWith("/")) {
         return resource.substring(1);
      } else {
         String className = klass.getName();
         int i = className.lastIndexOf(46);
         if (i < 0) {
            return resource;
         } else {
            String packageName = className.substring(0, i);
            String packagePath = packageName.replace('.', '/');
            return packagePath + "/" + resource;
         }
      }
   }

   public static Parseable newResources(String resource, ConfigParseOptions options) {
      if (options.getClassLoader() == null) {
         throw new ConfigException.BugOrBroken("null class loader; pass in a class loader or use Thread.currentThread().setContextClassLoader()");
      } else {
         return new ParseableResources(resource, options);
      }
   }

   public static Parseable newProperties(Properties properties, ConfigParseOptions options) {
      return new ParseableProperties(properties, options);
   }

   private static final class ParseableProperties extends Parseable {
      private final Properties props;

      ParseableProperties(Properties props, ConfigParseOptions options) {
         this.props = props;
         this.postConstruct(options);
      }

      protected Reader reader() throws IOException {
         throw new ConfigException.BugOrBroken("reader() should not be called on props");
      }

      protected AbstractConfigObject rawParseValue(ConfigOrigin origin, ConfigParseOptions finalOptions) {
         if (ConfigImpl.traceLoadsEnabled()) {
            trace("Loading config from properties " + this.props);
         }

         return PropertiesParser.fromProperties(origin, this.props);
      }

      ConfigSyntax guessSyntax() {
         return ConfigSyntax.PROPERTIES;
      }

      protected ConfigOrigin createOrigin() {
         return SimpleConfigOrigin.newSimple("properties");
      }

      public String toString() {
         return this.getClass().getSimpleName() + "(" + this.props.size() + " props)";
      }
   }

   private static final class ParseableResources extends Parseable implements Relativizer {
      private final String resource;

      ParseableResources(String resource, ConfigParseOptions options) {
         this.resource = resource;
         this.postConstruct(options);
      }

      protected Reader reader() throws IOException {
         throw new ConfigException.BugOrBroken("reader() should not be called on resources");
      }

      protected AbstractConfigObject rawParseValue(ConfigOrigin origin, ConfigParseOptions finalOptions) throws IOException {
         ClassLoader loader = finalOptions.getClassLoader();
         if (loader == null) {
            throw new ConfigException.BugOrBroken("null class loader; pass in a class loader or use Thread.currentThread().setContextClassLoader()");
         } else {
            Enumeration e = loader.getResources(this.resource);
            if (!e.hasMoreElements()) {
               if (ConfigImpl.traceLoadsEnabled()) {
                  trace("Loading config from class loader " + loader + " but there were no resources called " + this.resource);
               }

               throw new IOException("resource not found on classpath: " + this.resource);
            } else {
               Object merged;
               AbstractConfigValue v;
               for(merged = SimpleConfigObject.empty(origin); e.hasMoreElements(); merged = ((AbstractConfigObject)merged).withFallback(v)) {
                  URL url = (URL)e.nextElement();
                  if (ConfigImpl.traceLoadsEnabled()) {
                     trace("Loading config from resource '" + this.resource + "' URL " + url.toExternalForm() + " from class loader " + loader);
                  }

                  Parseable element = Parseable.newResourceURL(url, finalOptions, this.resource, this);
                  v = element.parseValue();
               }

               return (AbstractConfigObject)merged;
            }
         }
      }

      ConfigSyntax guessSyntax() {
         return Parseable.syntaxFromExtension(this.resource);
      }

      static String parent(String resource) {
         int i = resource.lastIndexOf(47);
         return i < 0 ? null : resource.substring(0, i);
      }

      public ConfigParseable relativeTo(String sibling) {
         if (sibling.startsWith("/")) {
            return newResources(sibling.substring(1), this.options().setOriginDescription((String)null));
         } else {
            String parent = parent(this.resource);
            return parent == null ? newResources(sibling, this.options().setOriginDescription((String)null)) : newResources(parent + "/" + sibling, this.options().setOriginDescription((String)null));
         }
      }

      protected ConfigOrigin createOrigin() {
         return SimpleConfigOrigin.newResource(this.resource);
      }

      public String toString() {
         return this.getClass().getSimpleName() + "(" + this.resource + ")";
      }
   }

   private static final class ParseableResourceURL extends ParseableURL {
      private final Relativizer relativizer;
      private final String resource;

      ParseableResourceURL(URL input, ConfigParseOptions options, String resource, Relativizer relativizer) {
         super(input);
         this.relativizer = relativizer;
         this.resource = resource;
         this.postConstruct(options);
      }

      protected ConfigOrigin createOrigin() {
         return SimpleConfigOrigin.newResource(this.resource, this.input);
      }

      ConfigParseable relativeTo(String filename) {
         return this.relativizer.relativeTo(filename);
      }
   }

   private static final class ParseableFile extends Parseable {
      private final File input;

      ParseableFile(File input, ConfigParseOptions options) {
         this.input = input;
         this.postConstruct(options);
      }

      protected Reader reader() throws IOException {
         if (ConfigImpl.traceLoadsEnabled()) {
            trace("Loading config from a file: " + this.input);
         }

         InputStream stream = new FileInputStream(this.input);
         return Parseable.readerFromStream(stream);
      }

      ConfigSyntax guessSyntax() {
         return Parseable.syntaxFromExtension(this.input.getName());
      }

      ConfigParseable relativeTo(String filename) {
         File sibling;
         if ((new File(filename)).isAbsolute()) {
            sibling = new File(filename);
         } else {
            sibling = relativeTo(this.input, filename);
         }

         if (sibling == null) {
            return null;
         } else if (sibling.exists()) {
            trace(sibling + " exists, so loading it as a file");
            return newFile(sibling, this.options().setOriginDescription((String)null));
         } else {
            trace(sibling + " does not exist, so trying it as a classpath resource");
            return super.relativeTo(filename);
         }
      }

      protected ConfigOrigin createOrigin() {
         return SimpleConfigOrigin.newFile(this.input.getPath());
      }

      public String toString() {
         return this.getClass().getSimpleName() + "(" + this.input.getPath() + ")";
      }
   }

   private static class ParseableURL extends Parseable {
      protected final URL input;
      private String contentType;

      protected ParseableURL(URL input) {
         this.contentType = null;
         this.input = input;
      }

      ParseableURL(URL input, ConfigParseOptions options) {
         this(input);
         this.postConstruct(options);
      }

      protected Reader reader() throws IOException {
         throw new ConfigException.BugOrBroken("reader() without options should not be called on ParseableURL");
      }

      private static String acceptContentType(ConfigParseOptions options) {
         if (options.getSyntax() == null) {
            return null;
         } else {
            switch (options.getSyntax()) {
               case JSON:
                  return "application/json";
               case CONF:
                  return "application/hocon";
               case PROPERTIES:
                  return "text/x-java-properties";
               default:
                  return null;
            }
         }
      }

      protected Reader reader(ConfigParseOptions options) throws IOException {
         try {
            if (ConfigImpl.traceLoadsEnabled()) {
               trace("Loading config from a URL: " + this.input.toExternalForm());
            }

            URLConnection connection = this.input.openConnection();
            String acceptContent = acceptContentType(options);
            if (acceptContent != null) {
               connection.setRequestProperty("Accept", acceptContent);
            }

            connection.connect();
            this.contentType = connection.getContentType();
            if (this.contentType != null) {
               if (ConfigImpl.traceLoadsEnabled()) {
                  trace("URL sets Content-Type: '" + this.contentType + "'");
               }

               this.contentType = this.contentType.trim();
               int semi = this.contentType.indexOf(59);
               if (semi >= 0) {
                  this.contentType = this.contentType.substring(0, semi);
               }
            }

            InputStream stream = connection.getInputStream();
            return Parseable.readerFromStream(stream);
         } catch (FileNotFoundException var5) {
            throw var5;
         } catch (IOException var6) {
            throw new ConfigException.BugOrBroken("Cannot load config from URL: " + this.input.toExternalForm(), var6);
         }
      }

      ConfigSyntax guessSyntax() {
         return Parseable.syntaxFromExtension(this.input.getPath());
      }

      ConfigSyntax contentType() {
         if (this.contentType != null) {
            if (this.contentType.equals("application/json")) {
               return ConfigSyntax.JSON;
            } else if (this.contentType.equals("text/x-java-properties")) {
               return ConfigSyntax.PROPERTIES;
            } else if (this.contentType.equals("application/hocon")) {
               return ConfigSyntax.CONF;
            } else {
               if (ConfigImpl.traceLoadsEnabled()) {
                  trace("'" + this.contentType + "' isn't a known content type");
               }

               return null;
            }
         } else {
            return null;
         }
      }

      ConfigParseable relativeTo(String filename) {
         URL url = relativeTo(this.input, filename);
         return url == null ? null : newURL(url, this.options().setOriginDescription((String)null));
      }

      protected ConfigOrigin createOrigin() {
         return SimpleConfigOrigin.newURL(this.input);
      }

      public String toString() {
         return this.getClass().getSimpleName() + "(" + this.input.toExternalForm() + ")";
      }
   }

   private static final class ParseableString extends Parseable {
      private final String input;

      ParseableString(String input, ConfigParseOptions options) {
         this.input = input;
         this.postConstruct(options);
      }

      protected Reader reader() {
         if (ConfigImpl.traceLoadsEnabled()) {
            trace("Loading config from a String " + this.input);
         }

         return new StringReader(this.input);
      }

      protected ConfigOrigin createOrigin() {
         return SimpleConfigOrigin.newSimple("String");
      }

      public String toString() {
         return this.getClass().getSimpleName() + "(" + this.input + ")";
      }
   }

   private static final class ParseableReader extends Parseable {
      private final Reader reader;

      ParseableReader(Reader reader, ConfigParseOptions options) {
         this.reader = reader;
         this.postConstruct(options);
      }

      protected Reader reader() {
         if (ConfigImpl.traceLoadsEnabled()) {
            trace("Loading config from reader " + this.reader);
         }

         return this.reader;
      }

      protected ConfigOrigin createOrigin() {
         return SimpleConfigOrigin.newSimple("Reader");
      }
   }

   private static final class ParseableNotFound extends Parseable {
      private final String what;
      private final String message;

      ParseableNotFound(String what, String message, ConfigParseOptions options) {
         this.what = what;
         this.message = message;
         this.postConstruct(options);
      }

      protected Reader reader() throws IOException {
         throw new FileNotFoundException(this.message);
      }

      protected ConfigOrigin createOrigin() {
         return SimpleConfigOrigin.newSimple(this.what);
      }
   }

   protected interface Relativizer {
      ConfigParseable relativeTo(String var1);
   }
}
