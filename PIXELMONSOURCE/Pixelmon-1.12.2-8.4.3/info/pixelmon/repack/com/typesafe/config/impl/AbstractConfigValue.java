package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigException;
import info.pixelmon.repack.com.typesafe.config.ConfigMergeable;
import info.pixelmon.repack.com.typesafe.config.ConfigObject;
import info.pixelmon.repack.com.typesafe.config.ConfigOrigin;
import info.pixelmon.repack.com.typesafe.config.ConfigRenderOptions;
import info.pixelmon.repack.com.typesafe.config.ConfigValue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

abstract class AbstractConfigValue implements ConfigValue, MergeableValue {
   private final SimpleConfigOrigin origin;

   AbstractConfigValue(ConfigOrigin origin) {
      this.origin = (SimpleConfigOrigin)origin;
   }

   public SimpleConfigOrigin origin() {
      return this.origin;
   }

   ResolveResult resolveSubstitutions(ResolveContext context, ResolveSource source) throws NotPossibleToResolve {
      return ResolveResult.make(context, this);
   }

   ResolveStatus resolveStatus() {
      return ResolveStatus.RESOLVED;
   }

   protected static List replaceChildInList(List list, AbstractConfigValue child, AbstractConfigValue replacement) {
      int i;
      for(i = 0; i < list.size() && list.get(i) != child; ++i) {
      }

      if (i == list.size()) {
         throw new ConfigException.BugOrBroken("tried to replace " + child + " which is not in " + list);
      } else {
         List newStack = new ArrayList(list);
         if (replacement != null) {
            newStack.set(i, replacement);
         } else {
            newStack.remove(i);
         }

         return newStack.isEmpty() ? null : newStack;
      }
   }

   protected static boolean hasDescendantInList(List list, AbstractConfigValue descendant) {
      Iterator var2 = list.iterator();

      AbstractConfigValue v;
      do {
         if (!var2.hasNext()) {
            var2 = list.iterator();

            do {
               if (!var2.hasNext()) {
                  return false;
               }

               v = (AbstractConfigValue)var2.next();
            } while(!(v instanceof Container) || !((Container)v).hasDescendant(descendant));

            return true;
         }

         v = (AbstractConfigValue)var2.next();
      } while(v != descendant);

      return true;
   }

   AbstractConfigValue relativized(Path prefix) {
      return this;
   }

   public AbstractConfigValue toFallbackValue() {
      return this;
   }

   protected abstract AbstractConfigValue newCopy(ConfigOrigin var1);

   protected boolean ignoresFallbacks() {
      return this.resolveStatus() == ResolveStatus.RESOLVED;
   }

   protected AbstractConfigValue withFallbacksIgnored() {
      if (this.ignoresFallbacks()) {
         return this;
      } else {
         throw new ConfigException.BugOrBroken("value class doesn't implement forced fallback-ignoring " + this);
      }
   }

   protected final void requireNotIgnoringFallbacks() {
      if (this.ignoresFallbacks()) {
         throw new ConfigException.BugOrBroken("method should not have been called with ignoresFallbacks=true " + this.getClass().getSimpleName());
      }
   }

   protected AbstractConfigValue constructDelayedMerge(ConfigOrigin origin, List stack) {
      return new ConfigDelayedMerge(origin, stack);
   }

   protected final AbstractConfigValue mergedWithTheUnmergeable(Collection stack, Unmergeable fallback) {
      this.requireNotIgnoringFallbacks();
      List newStack = new ArrayList();
      newStack.addAll(stack);
      newStack.addAll(fallback.unmergedValues());
      return this.constructDelayedMerge(AbstractConfigObject.mergeOrigins((Collection)newStack), newStack);
   }

   private final AbstractConfigValue delayMerge(Collection stack, AbstractConfigValue fallback) {
      List newStack = new ArrayList();
      newStack.addAll(stack);
      newStack.add(fallback);
      return this.constructDelayedMerge(AbstractConfigObject.mergeOrigins((Collection)newStack), newStack);
   }

   protected final AbstractConfigValue mergedWithObject(Collection stack, AbstractConfigObject fallback) {
      this.requireNotIgnoringFallbacks();
      if (this instanceof AbstractConfigObject) {
         throw new ConfigException.BugOrBroken("Objects must reimplement mergedWithObject");
      } else {
         return this.mergedWithNonObject(stack, fallback);
      }
   }

   protected final AbstractConfigValue mergedWithNonObject(Collection stack, AbstractConfigValue fallback) {
      this.requireNotIgnoringFallbacks();
      return this.resolveStatus() == ResolveStatus.RESOLVED ? this.withFallbacksIgnored() : this.delayMerge(stack, fallback);
   }

   protected AbstractConfigValue mergedWithTheUnmergeable(Unmergeable fallback) {
      this.requireNotIgnoringFallbacks();
      return this.mergedWithTheUnmergeable(Collections.singletonList(this), fallback);
   }

   protected AbstractConfigValue mergedWithObject(AbstractConfigObject fallback) {
      this.requireNotIgnoringFallbacks();
      return this.mergedWithObject(Collections.singletonList(this), fallback);
   }

   protected AbstractConfigValue mergedWithNonObject(AbstractConfigValue fallback) {
      this.requireNotIgnoringFallbacks();
      return this.mergedWithNonObject(Collections.singletonList(this), fallback);
   }

   public AbstractConfigValue withOrigin(ConfigOrigin origin) {
      return this.origin == origin ? this : this.newCopy(origin);
   }

   public AbstractConfigValue withFallback(ConfigMergeable mergeable) {
      if (this.ignoresFallbacks()) {
         return this;
      } else {
         ConfigValue other = ((MergeableValue)mergeable).toFallbackValue();
         if (other instanceof Unmergeable) {
            return this.mergedWithTheUnmergeable((Unmergeable)other);
         } else {
            return other instanceof AbstractConfigObject ? this.mergedWithObject((AbstractConfigObject)other) : this.mergedWithNonObject((AbstractConfigValue)other);
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof ConfigValue;
   }

   public boolean equals(Object other) {
      if (!(other instanceof ConfigValue)) {
         return false;
      } else {
         return this.canEqual(other) && this.valueType() == ((ConfigValue)other).valueType() && ConfigImplUtil.equalsHandlingNull(this.unwrapped(), ((ConfigValue)other).unwrapped());
      }
   }

   public int hashCode() {
      Object o = this.unwrapped();
      return o == null ? 0 : o.hashCode();
   }

   public String toString() {
      StringBuilder sb = new StringBuilder();
      this.render(sb, 0, true, (String)null, ConfigRenderOptions.concise());
      return this.getClass().getSimpleName() + "(" + sb.toString() + ")";
   }

   protected static void indent(StringBuilder sb, int indent, ConfigRenderOptions options) {
      if (options.getFormatted()) {
         for(int remaining = indent; remaining > 0; --remaining) {
            sb.append("    ");
         }
      }

   }

   protected void render(StringBuilder sb, int indent, boolean atRoot, String atKey, ConfigRenderOptions options) {
      if (atKey != null) {
         String renderedKey;
         if (options.getJson()) {
            renderedKey = ConfigImplUtil.renderJsonString(atKey);
         } else {
            renderedKey = ConfigImplUtil.renderStringUnquotedIfPossible(atKey);
         }

         sb.append(renderedKey);
         if (options.getJson()) {
            if (options.getFormatted()) {
               sb.append(" : ");
            } else {
               sb.append(":");
            }
         } else if (this instanceof ConfigObject) {
            if (options.getFormatted()) {
               sb.append(' ');
            }
         } else {
            sb.append("=");
         }
      }

      this.render(sb, indent, atRoot, options);
   }

   protected void render(StringBuilder sb, int indent, boolean atRoot, ConfigRenderOptions options) {
      Object u = this.unwrapped();
      sb.append(u.toString());
   }

   public final String render() {
      return this.render(ConfigRenderOptions.defaults());
   }

   public final String render(ConfigRenderOptions options) {
      StringBuilder sb = new StringBuilder();
      this.render(sb, 0, true, (String)null, options);
      return sb.toString();
   }

   String transformToString() {
      return null;
   }

   SimpleConfig atKey(ConfigOrigin origin, String key) {
      Map m = Collections.singletonMap(key, this);
      return (new SimpleConfigObject(origin, m)).toConfig();
   }

   public SimpleConfig atKey(String key) {
      return this.atKey(SimpleConfigOrigin.newSimple("atKey(" + key + ")"), key);
   }

   SimpleConfig atPath(ConfigOrigin origin, Path path) {
      Path parent = path.parent();

      SimpleConfig result;
      for(result = this.atKey(origin, path.last()); parent != null; parent = parent.parent()) {
         String key = parent.last();
         result = result.atKey(origin, key);
      }

      return result;
   }

   public SimpleConfig atPath(String pathExpression) {
      SimpleConfigOrigin origin = SimpleConfigOrigin.newSimple("atPath(" + pathExpression + ")");
      return this.atPath(origin, Path.newPath(pathExpression));
   }

   protected abstract class NoExceptionsModifier implements Modifier {
      public final AbstractConfigValue modifyChildMayThrow(String keyOrNull, AbstractConfigValue v) throws Exception {
         try {
            return this.modifyChild(keyOrNull, v);
         } catch (RuntimeException var4) {
            throw var4;
         } catch (Exception var5) {
            throw new ConfigException.BugOrBroken("Unexpected exception", var5);
         }
      }

      abstract AbstractConfigValue modifyChild(String var1, AbstractConfigValue var2);
   }

   protected interface Modifier {
      AbstractConfigValue modifyChildMayThrow(String var1, AbstractConfigValue var2) throws Exception;
   }

   static class NotPossibleToResolve extends Exception {
      private static final long serialVersionUID = 1L;
      private final String traceString;

      NotPossibleToResolve(ResolveContext context) {
         super("was not possible to resolve");
         this.traceString = context.traceString();
      }

      String traceString() {
         return this.traceString;
      }
   }
}
