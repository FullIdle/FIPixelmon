package info.pixelmon.repack.com.typesafe.config;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface Config extends ConfigMergeable {
   ConfigObject root();

   ConfigOrigin origin();

   Config withFallback(ConfigMergeable var1);

   Config resolve();

   Config resolve(ConfigResolveOptions var1);

   boolean isResolved();

   Config resolveWith(Config var1);

   Config resolveWith(Config var1, ConfigResolveOptions var2);

   void checkValid(Config var1, String... var2);

   boolean hasPath(String var1);

   boolean hasPathOrNull(String var1);

   boolean isEmpty();

   Set entrySet();

   boolean getIsNull(String var1);

   boolean getBoolean(String var1);

   Number getNumber(String var1);

   int getInt(String var1);

   long getLong(String var1);

   double getDouble(String var1);

   String getString(String var1);

   Enum getEnum(Class var1, String var2);

   ConfigObject getObject(String var1);

   Config getConfig(String var1);

   Object getAnyRef(String var1);

   ConfigValue getValue(String var1);

   Long getBytes(String var1);

   ConfigMemorySize getMemorySize(String var1);

   /** @deprecated */
   @Deprecated
   Long getMilliseconds(String var1);

   /** @deprecated */
   @Deprecated
   Long getNanoseconds(String var1);

   long getDuration(String var1, TimeUnit var2);

   Duration getDuration(String var1);

   ConfigList getList(String var1);

   List getBooleanList(String var1);

   List getNumberList(String var1);

   List getIntList(String var1);

   List getLongList(String var1);

   List getDoubleList(String var1);

   List getStringList(String var1);

   List getEnumList(Class var1, String var2);

   List getObjectList(String var1);

   List getConfigList(String var1);

   List getAnyRefList(String var1);

   List getBytesList(String var1);

   List getMemorySizeList(String var1);

   /** @deprecated */
   @Deprecated
   List getMillisecondsList(String var1);

   /** @deprecated */
   @Deprecated
   List getNanosecondsList(String var1);

   List getDurationList(String var1, TimeUnit var2);

   List getDurationList(String var1);

   Config withOnlyPath(String var1);

   Config withoutPath(String var1);

   Config atPath(String var1);

   Config atKey(String var1);

   Config withValue(String var1, ConfigValue var2);
}
