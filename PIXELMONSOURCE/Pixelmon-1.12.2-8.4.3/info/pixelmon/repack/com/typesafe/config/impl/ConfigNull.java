package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigOrigin;
import info.pixelmon.repack.com.typesafe.config.ConfigRenderOptions;
import info.pixelmon.repack.com.typesafe.config.ConfigValueType;
import java.io.ObjectStreamException;
import java.io.Serializable;

final class ConfigNull extends AbstractConfigValue implements Serializable {
   private static final long serialVersionUID = 2L;

   ConfigNull(ConfigOrigin origin) {
      super(origin);
   }

   public ConfigValueType valueType() {
      return ConfigValueType.NULL;
   }

   public Object unwrapped() {
      return null;
   }

   String transformToString() {
      return "null";
   }

   protected void render(StringBuilder sb, int indent, boolean atRoot, ConfigRenderOptions options) {
      sb.append("null");
   }

   protected ConfigNull newCopy(ConfigOrigin origin) {
      return new ConfigNull(origin);
   }

   private Object writeReplace() throws ObjectStreamException {
      return new SerializedConfigValue(this);
   }
}
