package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigException;
import info.pixelmon.repack.com.typesafe.config.ConfigParseOptions;
import info.pixelmon.repack.com.typesafe.config.ConfigRenderOptions;
import info.pixelmon.repack.com.typesafe.config.ConfigValue;
import info.pixelmon.repack.com.typesafe.config.parser.ConfigDocument;
import java.io.StringReader;
import java.util.Iterator;

final class SimpleConfigDocument implements ConfigDocument {
   private ConfigNodeRoot configNodeTree;
   private ConfigParseOptions parseOptions;

   SimpleConfigDocument(ConfigNodeRoot parsedNode, ConfigParseOptions parseOptions) {
      this.configNodeTree = parsedNode;
      this.parseOptions = parseOptions;
   }

   public ConfigDocument withValueText(String path, String newValue) {
      if (newValue == null) {
         throw new ConfigException.BugOrBroken("null value for " + path + " passed to withValueText");
      } else {
         SimpleConfigOrigin origin = SimpleConfigOrigin.newSimple("single value parsing");
         StringReader reader = new StringReader(newValue);
         Iterator tokens = Tokenizer.tokenize(origin, reader, this.parseOptions.getSyntax());
         AbstractConfigNodeValue parsedValue = ConfigDocumentParser.parseValue(tokens, origin, this.parseOptions);
         reader.close();
         return new SimpleConfigDocument(this.configNodeTree.setValue(path, parsedValue, this.parseOptions.getSyntax()), this.parseOptions);
      }
   }

   public ConfigDocument withValue(String path, ConfigValue newValue) {
      if (newValue == null) {
         throw new ConfigException.BugOrBroken("null value for " + path + " passed to withValue");
      } else {
         ConfigRenderOptions options = ConfigRenderOptions.defaults();
         options = options.setOriginComments(false);
         return this.withValueText(path, newValue.render(options).trim());
      }
   }

   public ConfigDocument withoutPath(String path) {
      return new SimpleConfigDocument(this.configNodeTree.setValue(path, (AbstractConfigNodeValue)null, this.parseOptions.getSyntax()), this.parseOptions);
   }

   public boolean hasPath(String path) {
      return this.configNodeTree.hasValue(path);
   }

   public String render() {
      return this.configNodeTree.render();
   }

   public boolean equals(Object other) {
      return other instanceof ConfigDocument && this.render().equals(((ConfigDocument)other).render());
   }

   public int hashCode() {
      return this.render().hashCode();
   }
}
