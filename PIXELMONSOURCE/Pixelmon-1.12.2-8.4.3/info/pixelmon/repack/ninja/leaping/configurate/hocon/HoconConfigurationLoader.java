package info.pixelmon.repack.ninja.leaping.configurate.hocon;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import info.pixelmon.repack.com.typesafe.config.Config;
import info.pixelmon.repack.com.typesafe.config.ConfigFactory;
import info.pixelmon.repack.com.typesafe.config.ConfigList;
import info.pixelmon.repack.com.typesafe.config.ConfigObject;
import info.pixelmon.repack.com.typesafe.config.ConfigOrigin;
import info.pixelmon.repack.com.typesafe.config.ConfigOriginFactory;
import info.pixelmon.repack.com.typesafe.config.ConfigParseOptions;
import info.pixelmon.repack.com.typesafe.config.ConfigRenderOptions;
import info.pixelmon.repack.com.typesafe.config.ConfigValue;
import info.pixelmon.repack.com.typesafe.config.ConfigValueFactory;
import info.pixelmon.repack.ninja.leaping.configurate.ConfigurationNode;
import info.pixelmon.repack.ninja.leaping.configurate.ConfigurationOptions;
import info.pixelmon.repack.ninja.leaping.configurate.commented.CommentedConfigurationNode;
import info.pixelmon.repack.ninja.leaping.configurate.commented.SimpleCommentedConfigurationNode;
import info.pixelmon.repack.ninja.leaping.configurate.loader.AbstractConfigurationLoader;
import info.pixelmon.repack.ninja.leaping.configurate.loader.CommentHandler;
import info.pixelmon.repack.ninja.leaping.configurate.loader.CommentHandlers;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class HoconConfigurationLoader extends AbstractConfigurationLoader {
   public static final Pattern CRLF_MATCH = Pattern.compile("\r\n?");
   private final ConfigRenderOptions render;
   private final ConfigParseOptions parse;
   private static final ConfigOrigin CONFIGURATE_ORIGIN = ConfigOriginFactory.newSimple("configurate-hocon");
   private static final Constructor CONFIG_OBJECT_CONSTRUCTOR;
   private static final Constructor CONFIG_LIST_CONSTRUCTOR;

   public static Builder builder() {
      return new Builder();
   }

   private HoconConfigurationLoader(Builder build) {
      super(build, new CommentHandler[]{CommentHandlers.HASH, CommentHandlers.DOUBLE_SLASH});
      this.render = build.getRenderOptions();
      this.parse = build.getParseOptions();
   }

   public void loadInternal(CommentedConfigurationNode node, BufferedReader reader) throws IOException {
      Config hoconConfig = ConfigFactory.parseReader(reader, this.parse);
      hoconConfig = hoconConfig.resolve();
      Iterator var4 = hoconConfig.root().entrySet().iterator();

      while(var4.hasNext()) {
         Map.Entry ent = (Map.Entry)var4.next();
         this.readConfigValue((ConfigValue)ent.getValue(), node.getNode(ent.getKey()));
      }

   }

   private void readConfigValue(ConfigValue value, CommentedConfigurationNode node) {
      if (!value.origin().comments().isEmpty()) {
         node.setComment(CRLF_MATCH.matcher(Joiner.on('\n').join(value.origin().comments())).replaceAll(""));
      }

      switch (value.valueType()) {
         case OBJECT:
            if (((ConfigObject)value).isEmpty()) {
               node.setValue(ImmutableMap.of());
               break;
            } else {
               Iterator var5 = ((ConfigObject)value).entrySet().iterator();

               while(var5.hasNext()) {
                  Map.Entry ent = (Map.Entry)var5.next();
                  this.readConfigValue((ConfigValue)ent.getValue(), node.getNode(ent.getKey()));
               }

               return;
            }
         case LIST:
            List values = (ConfigList)value;

            for(int i = 0; i < values.size(); ++i) {
               this.readConfigValue((ConfigValue)values.get(i), node.getNode(i));
            }

            return;
         case NULL:
            return;
         default:
            node.setValue(value.unwrapped());
      }

   }

   protected void saveInternal(ConfigurationNode node, Writer writer) throws IOException {
      if (!node.hasMapChildren()) {
         if (node.getValue() == null) {
            writer.write(SYSTEM_LINE_SEPARATOR);
         } else {
            throw new IOException("HOCON cannot write nodes not in map format!");
         }
      } else {
         ConfigValue value = this.fromValue(node);
         String renderedValue = value.render(this.render);
         writer.write(renderedValue);
      }
   }

   private ConfigValue fromValue(ConfigurationNode node) {
      ConfigValue ret;
      Iterator var4;
      if (node.hasMapChildren()) {
         Map children = node.getOptions().getMapFactory().create();
         var4 = node.getChildrenMap().entrySet().iterator();

         while(var4.hasNext()) {
            Map.Entry ent = (Map.Entry)var4.next();
            children.put(String.valueOf(ent.getKey()), this.fromValue((ConfigurationNode)ent.getValue()));
         }

         ret = this.newConfigObject(children);
      } else if (node.hasListChildren()) {
         List children = new ArrayList();
         var4 = node.getChildrenList().iterator();

         while(var4.hasNext()) {
            ConfigurationNode ent = (ConfigurationNode)var4.next();
            children.add(this.fromValue(ent));
         }

         ret = this.newConfigList(children);
      } else {
         ret = ConfigValueFactory.fromAnyRef(node.getValue(), "configurate-hocon");
      }

      if (node instanceof CommentedConfigurationNode) {
         CommentedConfigurationNode commentedNode = (CommentedConfigurationNode)node;
         ret = (ConfigValue)commentedNode.getComment().map((comment) -> {
            return ret.withOrigin(ret.origin().withComments(LINE_SPLITTER.splitToList(comment)));
         }).orElse(ret);
      }

      return ret;
   }

   ConfigValue newConfigObject(Map vals) {
      try {
         return (ConfigValue)CONFIG_OBJECT_CONSTRUCTOR.newInstance(CONFIGURATE_ORIGIN, vals);
      } catch (IllegalAccessException | InvocationTargetException | InstantiationException var3) {
         throw new RuntimeException(var3);
      }
   }

   ConfigValue newConfigList(List vals) {
      try {
         return (ConfigValue)CONFIG_LIST_CONSTRUCTOR.newInstance(CONFIGURATE_ORIGIN, vals);
      } catch (IllegalAccessException | InvocationTargetException | InstantiationException var3) {
         throw new RuntimeException(var3);
      }
   }

   public CommentedConfigurationNode createEmptyNode(ConfigurationOptions options) {
      options = options.setAcceptedTypes(ImmutableSet.of(Map.class, List.class, Double.class, Long.class, Integer.class, Boolean.class, new Class[]{String.class, Number.class}));
      return SimpleCommentedConfigurationNode.root(options);
   }

   // $FF: synthetic method
   HoconConfigurationLoader(Builder x0, Object x1) {
      this(x0);
   }

   static {
      Class objectClass;
      Class listClass;
      try {
         objectClass = Class.forName("info.pixelmon.repack.com.typesafe.config.impl.SimpleConfigObject").asSubclass(ConfigValue.class);
         listClass = Class.forName("info.pixelmon.repack.com.typesafe.config.impl.SimpleConfigList").asSubclass(ConfigValue.class);
      } catch (ClassNotFoundException var4) {
         throw new ExceptionInInitializerError(var4);
      }

      try {
         CONFIG_OBJECT_CONSTRUCTOR = objectClass.getDeclaredConstructor(ConfigOrigin.class, Map.class);
         CONFIG_OBJECT_CONSTRUCTOR.setAccessible(true);
         CONFIG_LIST_CONSTRUCTOR = listClass.getDeclaredConstructor(ConfigOrigin.class, List.class);
         CONFIG_LIST_CONSTRUCTOR.setAccessible(true);
      } catch (NoSuchMethodException var3) {
         throw new ExceptionInInitializerError(var3);
      }
   }

   public static class Builder extends AbstractConfigurationLoader.Builder {
      private ConfigRenderOptions render = ConfigRenderOptions.defaults().setOriginComments(false).setJson(false);
      private ConfigParseOptions parse = ConfigParseOptions.defaults();

      protected Builder() {
      }

      public ConfigRenderOptions getRenderOptions() {
         return this.render;
      }

      public ConfigParseOptions getParseOptions() {
         return this.parse;
      }

      public Builder setRenderOptions(ConfigRenderOptions options) {
         this.render = options;
         return this;
      }

      public Builder setParseOptions(ConfigParseOptions options) {
         this.parse = options;
         return this;
      }

      public HoconConfigurationLoader build() {
         return new HoconConfigurationLoader(this);
      }
   }
}
