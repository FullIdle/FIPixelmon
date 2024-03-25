package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigException;
import info.pixelmon.repack.com.typesafe.config.ConfigOrigin;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

final class PropertiesParser {
   static AbstractConfigObject parse(Reader reader, ConfigOrigin origin) throws IOException {
      Properties props = new Properties();
      props.load(reader);
      return fromProperties(origin, props);
   }

   static String lastElement(String path) {
      int i = path.lastIndexOf(46);
      return i < 0 ? path : path.substring(i + 1);
   }

   static String exceptLastElement(String path) {
      int i = path.lastIndexOf(46);
      return i < 0 ? null : path.substring(0, i);
   }

   static Path pathFromPropertyKey(String key) {
      String last = lastElement(key);
      String exceptLast = exceptLastElement(key);

      Path path;
      for(path = new Path(last, (Path)null); exceptLast != null; path = new Path(last, path)) {
         last = lastElement(exceptLast);
         exceptLast = exceptLastElement(exceptLast);
      }

      return path;
   }

   static AbstractConfigObject fromProperties(ConfigOrigin origin, Properties props) {
      Map pathMap = new HashMap();
      Iterator var3 = props.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry entry = (Map.Entry)var3.next();
         Object key = entry.getKey();
         if (key instanceof String) {
            Path path = pathFromPropertyKey((String)key);
            pathMap.put(path, entry.getValue());
         }
      }

      return fromPathMap(origin, pathMap, true);
   }

   static AbstractConfigObject fromPathMap(ConfigOrigin origin, Map pathExpressionMap) {
      Map pathMap = new HashMap();
      Iterator var3 = pathExpressionMap.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry entry = (Map.Entry)var3.next();
         Object keyObj = entry.getKey();
         if (!(keyObj instanceof String)) {
            throw new ConfigException.BugOrBroken("Map has a non-string as a key, expecting a path expression as a String");
         }

         Path path = Path.newPath((String)keyObj);
         pathMap.put(path, entry.getValue());
      }

      return fromPathMap(origin, pathMap, false);
   }

   private static AbstractConfigObject fromPathMap(ConfigOrigin origin, Map pathMap, boolean convertedFromProperties) {
      Set scopePaths = new HashSet();
      Set valuePaths = new HashSet();
      Iterator var5 = pathMap.keySet().iterator();

      Path path;
      while(var5.hasNext()) {
         path = (Path)var5.next();
         valuePaths.add(path);

         for(Path next = path.parent(); next != null; next = next.parent()) {
            scopePaths.add(next);
         }
      }

      if (convertedFromProperties) {
         valuePaths.removeAll(scopePaths);
      } else {
         var5 = valuePaths.iterator();

         while(var5.hasNext()) {
            path = (Path)var5.next();
            if (scopePaths.contains(path)) {
               throw new ConfigException.BugOrBroken("In the map, path '" + path.render() + "' occurs as both the parent object of a value and as a value. Because Map has no defined ordering, this is a broken situation.");
            }
         }
      }

      Map root = new HashMap();
      Map scopes = new HashMap();
      Iterator var16 = scopePaths.iterator();

      Path path;
      while(var16.hasNext()) {
         path = (Path)var16.next();
         Map scope = new HashMap();
         scopes.put(path, scope);
      }

      var16 = valuePaths.iterator();

      Object rawValue;
      Path scopePath;
      while(var16.hasNext()) {
         path = (Path)var16.next();
         scopePath = path.parent();
         Map parent = scopePath != null ? (Map)scopes.get(scopePath) : root;
         String last = path.last();
         rawValue = pathMap.get(path);
         Object value;
         if (convertedFromProperties) {
            if (rawValue instanceof String) {
               value = new ConfigString.Quoted(origin, (String)rawValue);
            } else {
               value = null;
            }
         } else {
            value = ConfigImpl.fromAnyRef(pathMap.get(path), origin, FromMapMode.KEYS_ARE_PATHS);
         }

         if (value != null) {
            ((Map)parent).put(last, value);
         }
      }

      List sortedScopePaths = new ArrayList();
      sortedScopePaths.addAll(scopePaths);
      Collections.sort(sortedScopePaths, new Comparator() {
         public int compare(Path a, Path b) {
            return b.length() - a.length();
         }
      });
      Iterator var18 = sortedScopePaths.iterator();

      while(var18.hasNext()) {
         scopePath = (Path)var18.next();
         Map scope = (Map)scopes.get(scopePath);
         Path parentPath = scopePath.parent();
         rawValue = parentPath != null ? (Map)scopes.get(parentPath) : root;
         AbstractConfigObject o = new SimpleConfigObject(origin, scope, ResolveStatus.RESOLVED, false);
         ((Map)rawValue).put(scopePath.last(), o);
      }

      return new SimpleConfigObject(origin, root, ResolveStatus.RESOLVED, false);
   }
}
