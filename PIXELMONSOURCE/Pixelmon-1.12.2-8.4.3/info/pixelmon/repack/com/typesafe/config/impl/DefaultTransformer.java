package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigValueType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

final class DefaultTransformer {
   static AbstractConfigValue transform(AbstractConfigValue value, ConfigValueType requested) {
      if (value.valueType() == ConfigValueType.STRING) {
         String s = (String)value.unwrapped();
         switch (requested) {
            case NUMBER:
               try {
                  Long v = Long.parseLong(s);
                  return new ConfigLong(value.origin(), v, s);
               } catch (NumberFormatException var10) {
                  try {
                     Double v = Double.parseDouble(s);
                     return new ConfigDouble(value.origin(), v, s);
                  } catch (NumberFormatException var9) {
                     break;
                  }
               }
            case NULL:
               if (s.equals("null")) {
                  return new ConfigNull(value.origin());
               }
               break;
            case BOOLEAN:
               if (s.equals("true") || s.equals("yes") || s.equals("on")) {
                  return new ConfigBoolean(value.origin(), true);
               }

               if (s.equals("false") || s.equals("no") || s.equals("off")) {
                  return new ConfigBoolean(value.origin(), false);
               }
            case LIST:
            case OBJECT:
            case STRING:
         }
      } else if (requested == ConfigValueType.STRING) {
         switch (value.valueType()) {
            case NUMBER:
            case BOOLEAN:
               return new ConfigString.Quoted(value.origin(), value.transformToString());
            case NULL:
            case LIST:
            case OBJECT:
            case STRING:
         }
      } else if (requested == ConfigValueType.LIST && value.valueType() == ConfigValueType.OBJECT) {
         AbstractConfigObject o = (AbstractConfigObject)value;
         Map values = new HashMap();
         Iterator var4 = o.keySet().iterator();

         while(var4.hasNext()) {
            String key = (String)var4.next();

            try {
               int i = Integer.parseInt(key, 10);
               if (i >= 0) {
                  values.put(i, o.get(key));
               }
            } catch (NumberFormatException var8) {
            }
         }

         if (!values.isEmpty()) {
            ArrayList entryList = new ArrayList(values.entrySet());
            Collections.sort(entryList, new Comparator() {
               public int compare(Map.Entry a, Map.Entry b) {
                  return Integer.compare((Integer)a.getKey(), (Integer)b.getKey());
               }
            });
            ArrayList list = new ArrayList();
            Iterator var16 = entryList.iterator();

            while(var16.hasNext()) {
               Map.Entry entry = (Map.Entry)var16.next();
               list.add(entry.getValue());
            }

            return new SimpleConfigList(value.origin(), list);
         }
      }

      return value;
   }
}
