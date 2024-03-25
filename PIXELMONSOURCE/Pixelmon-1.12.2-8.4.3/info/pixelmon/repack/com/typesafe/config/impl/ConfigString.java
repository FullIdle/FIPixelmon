package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigOrigin;
import info.pixelmon.repack.com.typesafe.config.ConfigRenderOptions;
import info.pixelmon.repack.com.typesafe.config.ConfigValueType;
import java.io.ObjectStreamException;
import java.io.Serializable;

abstract class ConfigString extends AbstractConfigValue implements Serializable {
   private static final long serialVersionUID = 2L;
   protected final String value;

   protected ConfigString(ConfigOrigin origin, String value) {
      super(origin);
      this.value = value;
   }

   boolean wasQuoted() {
      return this instanceof Quoted;
   }

   public ConfigValueType valueType() {
      return ConfigValueType.STRING;
   }

   public String unwrapped() {
      return this.value;
   }

   String transformToString() {
      return this.value;
   }

   protected void render(StringBuilder sb, int indent, boolean atRoot, ConfigRenderOptions options) {
      String rendered;
      if (options.getJson()) {
         rendered = ConfigImplUtil.renderJsonString(this.value);
      } else {
         rendered = ConfigImplUtil.renderStringUnquotedIfPossible(this.value);
      }

      sb.append(rendered);
   }

   static final class Unquoted extends ConfigString {
      Unquoted(ConfigOrigin origin, String value) {
         super(origin, value);
      }

      protected Unquoted newCopy(ConfigOrigin origin) {
         return new Unquoted(origin, this.value);
      }

      private Object writeReplace() throws ObjectStreamException {
         return new SerializedConfigValue(this);
      }
   }

   static final class Quoted extends ConfigString {
      Quoted(ConfigOrigin origin, String value) {
         super(origin, value);
      }

      protected Quoted newCopy(ConfigOrigin origin) {
         return new Quoted(origin, this.value);
      }

      private Object writeReplace() throws ObjectStreamException {
         return new SerializedConfigValue(this);
      }
   }
}
