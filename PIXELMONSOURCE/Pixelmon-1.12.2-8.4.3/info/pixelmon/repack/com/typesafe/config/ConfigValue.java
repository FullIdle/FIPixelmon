package info.pixelmon.repack.com.typesafe.config;

public interface ConfigValue extends ConfigMergeable {
   ConfigOrigin origin();

   ConfigValueType valueType();

   Object unwrapped();

   String render();

   String render(ConfigRenderOptions var1);

   ConfigValue withFallback(ConfigMergeable var1);

   Config atPath(String var1);

   Config atKey(String var1);

   ConfigValue withOrigin(ConfigOrigin var1);
}
