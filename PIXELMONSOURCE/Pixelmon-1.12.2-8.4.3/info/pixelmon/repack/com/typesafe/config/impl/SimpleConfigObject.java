package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigException;
import info.pixelmon.repack.com.typesafe.config.ConfigObject;
import info.pixelmon.repack.com.typesafe.config.ConfigOrigin;
import info.pixelmon.repack.com.typesafe.config.ConfigRenderOptions;
import info.pixelmon.repack.com.typesafe.config.ConfigValue;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class SimpleConfigObject extends AbstractConfigObject implements Serializable {
   private static final long serialVersionUID = 2L;
   private final Map value;
   private final boolean resolved;
   private final boolean ignoresFallbacks;
   private static final String EMPTY_NAME = "empty config";
   private static final SimpleConfigObject emptyInstance = empty(SimpleConfigOrigin.newSimple("empty config"));

   SimpleConfigObject(ConfigOrigin origin, Map value, ResolveStatus status, boolean ignoresFallbacks) {
      super(origin);
      if (value == null) {
         throw new ConfigException.BugOrBroken("creating config object with null map");
      } else {
         this.value = value;
         this.resolved = status == ResolveStatus.RESOLVED;
         this.ignoresFallbacks = ignoresFallbacks;
         if (status != ResolveStatus.fromValues(value.values())) {
            throw new ConfigException.BugOrBroken("Wrong resolved status on " + this);
         }
      }
   }

   SimpleConfigObject(ConfigOrigin origin, Map value) {
      this(origin, value, ResolveStatus.fromValues(value.values()), false);
   }

   public SimpleConfigObject withOnlyKey(String key) {
      return this.withOnlyPath(Path.newKey(key));
   }

   public SimpleConfigObject withoutKey(String key) {
      return this.withoutPath(Path.newKey(key));
   }

   protected SimpleConfigObject withOnlyPathOrNull(Path path) {
      String key = path.first();
      Path next = path.remainder();
      AbstractConfigValue v = (AbstractConfigValue)this.value.get(key);
      if (next != null) {
         if (v != null && v instanceof AbstractConfigObject) {
            v = ((AbstractConfigObject)v).withOnlyPathOrNull(next);
         } else {
            v = null;
         }
      }

      return v == null ? null : new SimpleConfigObject(this.origin(), Collections.singletonMap(key, v), ((AbstractConfigValue)v).resolveStatus(), this.ignoresFallbacks);
   }

   SimpleConfigObject withOnlyPath(Path path) {
      SimpleConfigObject o = this.withOnlyPathOrNull(path);
      return o == null ? new SimpleConfigObject(this.origin(), Collections.emptyMap(), ResolveStatus.RESOLVED, this.ignoresFallbacks) : o;
   }

   SimpleConfigObject withoutPath(Path path) {
      String key = path.first();
      Path next = path.remainder();
      AbstractConfigValue v = (AbstractConfigValue)this.value.get(key);
      HashMap smaller;
      if (v != null && next != null && v instanceof AbstractConfigObject) {
         AbstractConfigValue v = ((AbstractConfigObject)v).withoutPath(next);
         smaller = new HashMap(this.value);
         smaller.put(key, v);
         return new SimpleConfigObject(this.origin(), smaller, ResolveStatus.fromValues(smaller.values()), this.ignoresFallbacks);
      } else if (next == null && v != null) {
         smaller = new HashMap(this.value.size() - 1);
         Iterator var6 = this.value.entrySet().iterator();

         while(var6.hasNext()) {
            Map.Entry old = (Map.Entry)var6.next();
            if (!((String)old.getKey()).equals(key)) {
               smaller.put(old.getKey(), old.getValue());
            }
         }

         return new SimpleConfigObject(this.origin(), smaller, ResolveStatus.fromValues(smaller.values()), this.ignoresFallbacks);
      } else {
         return this;
      }
   }

   public SimpleConfigObject withValue(String key, ConfigValue v) {
      if (v == null) {
         throw new ConfigException.BugOrBroken("Trying to store null ConfigValue in a ConfigObject");
      } else {
         Object newMap;
         if (this.value.isEmpty()) {
            newMap = Collections.singletonMap(key, (AbstractConfigValue)v);
         } else {
            newMap = new HashMap(this.value);
            ((Map)newMap).put(key, (AbstractConfigValue)v);
         }

         return new SimpleConfigObject(this.origin(), (Map)newMap, ResolveStatus.fromValues(((Map)newMap).values()), this.ignoresFallbacks);
      }
   }

   SimpleConfigObject withValue(Path path, ConfigValue v) {
      String key = path.first();
      Path next = path.remainder();
      if (next == null) {
         return this.withValue(key, v);
      } else {
         AbstractConfigValue child = (AbstractConfigValue)this.value.get(key);
         if (child != null && child instanceof AbstractConfigObject) {
            return this.withValue((String)key, ((AbstractConfigObject)child).withValue(next, v));
         } else {
            SimpleConfig subtree = ((AbstractConfigValue)v).atPath(SimpleConfigOrigin.newSimple("withValue(" + next.render() + ")"), next);
            return this.withValue((String)key, subtree.root());
         }
      }
   }

   protected AbstractConfigValue attemptPeekWithPartialResolve(String key) {
      return (AbstractConfigValue)this.value.get(key);
   }

   private SimpleConfigObject newCopy(ResolveStatus newStatus, ConfigOrigin newOrigin, boolean newIgnoresFallbacks) {
      return new SimpleConfigObject(newOrigin, this.value, newStatus, newIgnoresFallbacks);
   }

   protected SimpleConfigObject newCopy(ResolveStatus newStatus, ConfigOrigin newOrigin) {
      return this.newCopy(newStatus, newOrigin, this.ignoresFallbacks);
   }

   protected SimpleConfigObject withFallbacksIgnored() {
      return this.ignoresFallbacks ? this : this.newCopy(this.resolveStatus(), this.origin(), true);
   }

   ResolveStatus resolveStatus() {
      return ResolveStatus.fromBoolean(this.resolved);
   }

   public SimpleConfigObject replaceChild(AbstractConfigValue child, AbstractConfigValue replacement) {
      HashMap newChildren = new HashMap(this.value);
      Iterator var4 = newChildren.entrySet().iterator();

      Map.Entry old;
      do {
         if (!var4.hasNext()) {
            throw new ConfigException.BugOrBroken("SimpleConfigObject.replaceChild did not find " + child + " in " + this);
         }

         old = (Map.Entry)var4.next();
      } while(old.getValue() != child);

      if (replacement != null) {
         old.setValue(replacement);
      } else {
         newChildren.remove(old.getKey());
      }

      return new SimpleConfigObject(this.origin(), newChildren, ResolveStatus.fromValues(newChildren.values()), this.ignoresFallbacks);
   }

   public boolean hasDescendant(AbstractConfigValue descendant) {
      Iterator var2 = this.value.values().iterator();

      AbstractConfigValue child;
      do {
         if (!var2.hasNext()) {
            var2 = this.value.values().iterator();

            do {
               if (!var2.hasNext()) {
                  return false;
               }

               child = (AbstractConfigValue)var2.next();
            } while(!(child instanceof Container) || !((Container)child).hasDescendant(descendant));

            return true;
         }

         child = (AbstractConfigValue)var2.next();
      } while(child != descendant);

      return true;
   }

   protected boolean ignoresFallbacks() {
      return this.ignoresFallbacks;
   }

   public Map unwrapped() {
      Map m = new HashMap();
      Iterator var2 = this.value.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry e = (Map.Entry)var2.next();
         m.put(e.getKey(), ((AbstractConfigValue)e.getValue()).unwrapped());
      }

      return m;
   }

   protected SimpleConfigObject mergedWithObject(AbstractConfigObject abstractFallback) {
      this.requireNotIgnoringFallbacks();
      if (!(abstractFallback instanceof SimpleConfigObject)) {
         throw new ConfigException.BugOrBroken("should not be reached (merging non-SimpleConfigObject)");
      } else {
         SimpleConfigObject fallback = (SimpleConfigObject)abstractFallback;
         boolean changed = false;
         boolean allResolved = true;
         Map merged = new HashMap();
         Set allKeys = new HashSet();
         allKeys.addAll(this.keySet());
         allKeys.addAll(fallback.keySet());
         Iterator var7 = allKeys.iterator();

         while(var7.hasNext()) {
            String key = (String)var7.next();
            AbstractConfigValue first = (AbstractConfigValue)this.value.get(key);
            AbstractConfigValue second = (AbstractConfigValue)fallback.value.get(key);
            AbstractConfigValue kept;
            if (first == null) {
               kept = second;
            } else if (second == null) {
               kept = first;
            } else {
               kept = first.withFallback(second);
            }

            merged.put(key, kept);
            if (first != kept) {
               changed = true;
            }

            if (kept.resolveStatus() == ResolveStatus.UNRESOLVED) {
               allResolved = false;
            }
         }

         ResolveStatus newResolveStatus = ResolveStatus.fromBoolean(allResolved);
         boolean newIgnoresFallbacks = fallback.ignoresFallbacks();
         if (changed) {
            return new SimpleConfigObject(mergeOrigins(new AbstractConfigObject[]{this, fallback}), merged, newResolveStatus, newIgnoresFallbacks);
         } else if (newResolveStatus == this.resolveStatus() && newIgnoresFallbacks == this.ignoresFallbacks()) {
            return this;
         } else {
            return this.newCopy(newResolveStatus, this.origin(), newIgnoresFallbacks);
         }
      }
   }

   private SimpleConfigObject modify(AbstractConfigValue.NoExceptionsModifier modifier) {
      try {
         return this.modifyMayThrow(modifier);
      } catch (RuntimeException var3) {
         throw var3;
      } catch (Exception var4) {
         throw new ConfigException.BugOrBroken("unexpected checked exception", var4);
      }
   }

   private SimpleConfigObject modifyMayThrow(AbstractConfigValue.Modifier modifier) throws Exception {
      Map changes = null;
      Iterator var3 = this.keySet().iterator();

      while(var3.hasNext()) {
         String k = (String)var3.next();
         AbstractConfigValue v = (AbstractConfigValue)this.value.get(k);
         AbstractConfigValue modified = modifier.modifyChildMayThrow(k, v);
         if (modified != v) {
            if (changes == null) {
               changes = new HashMap();
            }

            changes.put(k, modified);
         }
      }

      if (changes == null) {
         return this;
      } else {
         Map modified = new HashMap();
         boolean sawUnresolved = false;
         Iterator var10 = this.keySet().iterator();

         while(var10.hasNext()) {
            String k = (String)var10.next();
            AbstractConfigValue newValue;
            if (changes.containsKey(k)) {
               newValue = (AbstractConfigValue)changes.get(k);
               if (newValue != null) {
                  modified.put(k, newValue);
                  if (newValue.resolveStatus() == ResolveStatus.UNRESOLVED) {
                     sawUnresolved = true;
                  }
               }
            } else {
               newValue = (AbstractConfigValue)this.value.get(k);
               modified.put(k, newValue);
               if (newValue.resolveStatus() == ResolveStatus.UNRESOLVED) {
                  sawUnresolved = true;
               }
            }
         }

         return new SimpleConfigObject(this.origin(), modified, sawUnresolved ? ResolveStatus.UNRESOLVED : ResolveStatus.RESOLVED, this.ignoresFallbacks());
      }
   }

   ResolveResult resolveSubstitutions(ResolveContext context, ResolveSource source) throws AbstractConfigValue.NotPossibleToResolve {
      if (this.resolveStatus() == ResolveStatus.RESOLVED) {
         return ResolveResult.make(context, this);
      } else {
         ResolveSource sourceWithParent = source.pushParent(this);

         try {
            ResolveModifier modifier = new ResolveModifier(context, sourceWithParent);
            AbstractConfigValue value = this.modifyMayThrow(modifier);
            return ResolveResult.make(modifier.context, value).asObjectResult();
         } catch (AbstractConfigValue.NotPossibleToResolve var6) {
            throw var6;
         } catch (RuntimeException var7) {
            throw var7;
         } catch (Exception var8) {
            throw new ConfigException.BugOrBroken("unexpected checked exception", var8);
         }
      }
   }

   SimpleConfigObject relativized(final Path prefix) {
      return this.modify(new AbstractConfigValue.NoExceptionsModifier() {
         public AbstractConfigValue modifyChild(String key, AbstractConfigValue v) {
            return v.relativized(prefix);
         }
      });
   }

   protected void render(StringBuilder sb, int indent, boolean atRoot, ConfigRenderOptions options) {
      if (this.isEmpty()) {
         sb.append("{}");
      } else {
         boolean outerBraces = options.getJson() || !atRoot;
         int innerIndent;
         if (outerBraces) {
            innerIndent = indent + 1;
            sb.append("{");
            if (options.getFormatted()) {
               sb.append('\n');
            }
         } else {
            innerIndent = indent;
         }

         int separatorCount = 0;
         String[] keys = (String[])this.keySet().toArray(new String[this.size()]);
         Arrays.sort(keys, new RenderComparator());
         String[] var9 = keys;
         int var10 = keys.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            String k = var9[var11];
            AbstractConfigValue v = (AbstractConfigValue)this.value.get(k);
            if (options.getOriginComments()) {
               String[] lines = v.origin().description().split("\n");
               String[] var15 = lines;
               int var16 = lines.length;

               for(int var17 = 0; var17 < var16; ++var17) {
                  String l = var15[var17];
                  indent(sb, indent + 1, options);
                  sb.append('#');
                  if (!l.isEmpty()) {
                     sb.append(' ');
                  }

                  sb.append(l);
                  sb.append("\n");
               }
            }

            if (options.getComments()) {
               Iterator var19 = v.origin().comments().iterator();

               while(var19.hasNext()) {
                  String comment = (String)var19.next();
                  indent(sb, innerIndent, options);
                  sb.append("#");
                  if (!comment.startsWith(" ")) {
                     sb.append(' ');
                  }

                  sb.append(comment);
                  sb.append("\n");
               }
            }

            indent(sb, innerIndent, options);
            v.render(sb, innerIndent, false, k, options);
            if (options.getFormatted()) {
               if (options.getJson()) {
                  sb.append(",");
                  separatorCount = 2;
               } else {
                  separatorCount = 1;
               }

               sb.append('\n');
            } else {
               sb.append(",");
               separatorCount = 1;
            }
         }

         sb.setLength(sb.length() - separatorCount);
         if (outerBraces) {
            if (options.getFormatted()) {
               sb.append('\n');
               if (outerBraces) {
                  indent(sb, indent, options);
               }
            }

            sb.append("}");
         }
      }

      if (atRoot && options.getFormatted()) {
         sb.append('\n');
      }

   }

   public AbstractConfigValue get(Object key) {
      return (AbstractConfigValue)this.value.get(key);
   }

   private static boolean mapEquals(Map a, Map b) {
      if (a == b) {
         return true;
      } else {
         Set aKeys = a.keySet();
         Set bKeys = b.keySet();
         if (!aKeys.equals(bKeys)) {
            return false;
         } else {
            Iterator var4 = aKeys.iterator();

            String key;
            do {
               if (!var4.hasNext()) {
                  return true;
               }

               key = (String)var4.next();
            } while(((ConfigValue)a.get(key)).equals(b.get(key)));

            return false;
         }
      }
   }

   private static int mapHash(Map m) {
      List keys = new ArrayList();
      keys.addAll(m.keySet());
      Collections.sort(keys);
      int valuesHash = 0;

      String k;
      for(Iterator var3 = keys.iterator(); var3.hasNext(); valuesHash += ((ConfigValue)m.get(k)).hashCode()) {
         k = (String)var3.next();
      }

      return 41 * (41 + keys.hashCode()) + valuesHash;
   }

   protected boolean canEqual(Object other) {
      return other instanceof ConfigObject;
   }

   public boolean equals(Object other) {
      if (!(other instanceof ConfigObject)) {
         return false;
      } else {
         return this.canEqual(other) && mapEquals(this, (ConfigObject)other);
      }
   }

   public int hashCode() {
      return mapHash(this);
   }

   public boolean containsKey(Object key) {
      return this.value.containsKey(key);
   }

   public Set keySet() {
      return this.value.keySet();
   }

   public boolean containsValue(Object v) {
      return this.value.containsValue(v);
   }

   public Set entrySet() {
      HashSet entries = new HashSet();
      Iterator var2 = this.value.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry e = (Map.Entry)var2.next();
         entries.add(new AbstractMap.SimpleImmutableEntry(e.getKey(), e.getValue()));
      }

      return entries;
   }

   public boolean isEmpty() {
      return this.value.isEmpty();
   }

   public int size() {
      return this.value.size();
   }

   public Collection values() {
      return new HashSet(this.value.values());
   }

   static final SimpleConfigObject empty() {
      return emptyInstance;
   }

   static final SimpleConfigObject empty(ConfigOrigin origin) {
      return origin == null ? empty() : new SimpleConfigObject(origin, Collections.emptyMap());
   }

   static final SimpleConfigObject emptyMissing(ConfigOrigin baseOrigin) {
      return new SimpleConfigObject(SimpleConfigOrigin.newSimple(baseOrigin.description() + " (not found)"), Collections.emptyMap());
   }

   private Object writeReplace() throws ObjectStreamException {
      return new SerializedConfigValue(this);
   }

   private static final class RenderComparator implements Comparator, Serializable {
      private static final long serialVersionUID = 1L;

      private RenderComparator() {
      }

      private static boolean isAllDigits(String s) {
         int length = s.length();
         if (length == 0) {
            return false;
         } else {
            for(int i = 0; i < length; ++i) {
               char c = s.charAt(i);
               if (!Character.isDigit(c)) {
                  return false;
               }
            }

            return true;
         }
      }

      public int compare(String a, String b) {
         boolean aDigits = isAllDigits(a);
         boolean bDigits = isAllDigits(b);
         if (aDigits && bDigits) {
            return Integer.compare(Integer.parseInt(a), Integer.parseInt(b));
         } else if (aDigits) {
            return -1;
         } else {
            return bDigits ? 1 : a.compareTo(b);
         }
      }

      // $FF: synthetic method
      RenderComparator(Object x0) {
         this();
      }
   }

   private static final class ResolveModifier implements AbstractConfigValue.Modifier {
      final Path originalRestrict;
      ResolveContext context;
      final ResolveSource source;

      ResolveModifier(ResolveContext context, ResolveSource source) {
         this.context = context;
         this.source = source;
         this.originalRestrict = context.restrictToChild();
      }

      public AbstractConfigValue modifyChildMayThrow(String key, AbstractConfigValue v) throws AbstractConfigValue.NotPossibleToResolve {
         if (this.context.isRestrictedToChild()) {
            if (key.equals(this.context.restrictToChild().first())) {
               Path remainder = this.context.restrictToChild().remainder();
               if (remainder != null) {
                  ResolveResult result = this.context.restrict(remainder).resolve(v, this.source);
                  this.context = result.context.unrestricted().restrict(this.originalRestrict);
                  return result.value;
               } else {
                  return v;
               }
            } else {
               return v;
            }
         } else {
            ResolveResult result = this.context.unrestricted().resolve(v, this.source);
            this.context = result.context.unrestricted().restrict(this.originalRestrict);
            return result.value;
         }
      }
   }
}
