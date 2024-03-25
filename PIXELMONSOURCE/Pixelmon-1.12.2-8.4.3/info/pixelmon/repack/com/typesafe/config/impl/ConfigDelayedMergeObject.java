package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigException;
import info.pixelmon.repack.com.typesafe.config.ConfigList;
import info.pixelmon.repack.com.typesafe.config.ConfigMergeable;
import info.pixelmon.repack.com.typesafe.config.ConfigOrigin;
import info.pixelmon.repack.com.typesafe.config.ConfigRenderOptions;
import info.pixelmon.repack.com.typesafe.config.ConfigValue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class ConfigDelayedMergeObject extends AbstractConfigObject implements Unmergeable, ReplaceableMergeStack {
   private final List stack;

   ConfigDelayedMergeObject(ConfigOrigin origin, List stack) {
      super(origin);
      this.stack = stack;
      if (stack.isEmpty()) {
         throw new ConfigException.BugOrBroken("creating empty delayed merge object");
      } else if (!(stack.get(0) instanceof AbstractConfigObject)) {
         throw new ConfigException.BugOrBroken("created a delayed merge object not guaranteed to be an object");
      } else {
         Iterator var3 = stack.iterator();

         AbstractConfigValue v;
         do {
            if (!var3.hasNext()) {
               return;
            }

            v = (AbstractConfigValue)var3.next();
         } while(!(v instanceof ConfigDelayedMerge) && !(v instanceof ConfigDelayedMergeObject));

         throw new ConfigException.BugOrBroken("placed nested DelayedMerge in a ConfigDelayedMergeObject, should have consolidated stack");
      }
   }

   protected ConfigDelayedMergeObject newCopy(ResolveStatus status, ConfigOrigin origin) {
      if (status != this.resolveStatus()) {
         throw new ConfigException.BugOrBroken("attempt to create resolved ConfigDelayedMergeObject");
      } else {
         return new ConfigDelayedMergeObject(origin, this.stack);
      }
   }

   ResolveResult resolveSubstitutions(ResolveContext context, ResolveSource source) throws AbstractConfigValue.NotPossibleToResolve {
      ResolveResult merged = ConfigDelayedMerge.resolveSubstitutions(this, this.stack, context, source);
      return merged.asObjectResult();
   }

   public AbstractConfigValue makeReplacement(ResolveContext context, int skipping) {
      return ConfigDelayedMerge.makeReplacement(context, this.stack, skipping);
   }

   ResolveStatus resolveStatus() {
      return ResolveStatus.UNRESOLVED;
   }

   public AbstractConfigValue replaceChild(AbstractConfigValue child, AbstractConfigValue replacement) {
      List newStack = replaceChildInList(this.stack, child, replacement);
      return newStack == null ? null : new ConfigDelayedMergeObject(this.origin(), newStack);
   }

   public boolean hasDescendant(AbstractConfigValue descendant) {
      return hasDescendantInList(this.stack, descendant);
   }

   ConfigDelayedMergeObject relativized(Path prefix) {
      List newStack = new ArrayList();
      Iterator var3 = this.stack.iterator();

      while(var3.hasNext()) {
         AbstractConfigValue o = (AbstractConfigValue)var3.next();
         newStack.add(o.relativized(prefix));
      }

      return new ConfigDelayedMergeObject(this.origin(), newStack);
   }

   protected boolean ignoresFallbacks() {
      return ConfigDelayedMerge.stackIgnoresFallbacks(this.stack);
   }

   protected final ConfigDelayedMergeObject mergedWithTheUnmergeable(Unmergeable fallback) {
      this.requireNotIgnoringFallbacks();
      return (ConfigDelayedMergeObject)this.mergedWithTheUnmergeable(this.stack, fallback);
   }

   protected final ConfigDelayedMergeObject mergedWithObject(AbstractConfigObject fallback) {
      return this.mergedWithNonObject(fallback);
   }

   protected final ConfigDelayedMergeObject mergedWithNonObject(AbstractConfigValue fallback) {
      this.requireNotIgnoringFallbacks();
      return (ConfigDelayedMergeObject)this.mergedWithNonObject(this.stack, fallback);
   }

   public ConfigDelayedMergeObject withFallback(ConfigMergeable mergeable) {
      return (ConfigDelayedMergeObject)super.withFallback(mergeable);
   }

   public ConfigDelayedMergeObject withOnlyKey(String key) {
      throw notResolved();
   }

   public ConfigDelayedMergeObject withoutKey(String key) {
      throw notResolved();
   }

   protected AbstractConfigObject withOnlyPathOrNull(Path path) {
      throw notResolved();
   }

   AbstractConfigObject withOnlyPath(Path path) {
      throw notResolved();
   }

   AbstractConfigObject withoutPath(Path path) {
      throw notResolved();
   }

   public ConfigDelayedMergeObject withValue(String key, ConfigValue value) {
      throw notResolved();
   }

   ConfigDelayedMergeObject withValue(Path path, ConfigValue value) {
      throw notResolved();
   }

   public Collection unmergedValues() {
      return this.stack;
   }

   protected boolean canEqual(Object other) {
      return other instanceof ConfigDelayedMergeObject;
   }

   public boolean equals(Object other) {
      if (!(other instanceof ConfigDelayedMergeObject)) {
         return false;
      } else {
         return this.canEqual(other) && (this.stack == ((ConfigDelayedMergeObject)other).stack || this.stack.equals(((ConfigDelayedMergeObject)other).stack));
      }
   }

   public int hashCode() {
      return this.stack.hashCode();
   }

   protected void render(StringBuilder sb, int indent, boolean atRoot, String atKey, ConfigRenderOptions options) {
      ConfigDelayedMerge.render(this.stack, sb, indent, atRoot, atKey, options);
   }

   protected void render(StringBuilder sb, int indent, boolean atRoot, ConfigRenderOptions options) {
      this.render(sb, indent, atRoot, (String)null, options);
   }

   private static ConfigException notResolved() {
      return new ConfigException.NotResolved("need to Config#resolve() before using this object, see the API docs for Config#resolve()");
   }

   public Map unwrapped() {
      throw notResolved();
   }

   public AbstractConfigValue get(Object key) {
      throw notResolved();
   }

   public boolean containsKey(Object key) {
      throw notResolved();
   }

   public boolean containsValue(Object value) {
      throw notResolved();
   }

   public Set entrySet() {
      throw notResolved();
   }

   public boolean isEmpty() {
      throw notResolved();
   }

   public Set keySet() {
      throw notResolved();
   }

   public int size() {
      throw notResolved();
   }

   public Collection values() {
      throw notResolved();
   }

   protected AbstractConfigValue attemptPeekWithPartialResolve(String key) {
      Iterator var2 = this.stack.iterator();

      while(var2.hasNext()) {
         AbstractConfigValue layer = (AbstractConfigValue)var2.next();
         if (!(layer instanceof AbstractConfigObject)) {
            if (layer instanceof Unmergeable) {
               throw new ConfigException.NotResolved("Key '" + key + "' is not available at '" + this.origin().description() + "' because value at '" + layer.origin().description() + "' has not been resolved and may turn out to contain or hide '" + key + "'. Be sure to Config#resolve() before using a config object.");
            }

            if (layer.resolveStatus() == ResolveStatus.UNRESOLVED) {
               if (!(layer instanceof ConfigList)) {
                  throw new ConfigException.BugOrBroken("Expecting a list here, not " + layer);
               }

               return null;
            }

            if (!layer.ignoresFallbacks()) {
               throw new ConfigException.BugOrBroken("resolved non-object should ignore fallbacks");
            }

            return null;
         }

         AbstractConfigObject objectLayer = (AbstractConfigObject)layer;
         AbstractConfigValue v = objectLayer.attemptPeekWithPartialResolve(key);
         if (v != null) {
            if (v.ignoresFallbacks()) {
               return v;
            }
         } else if (layer instanceof Unmergeable) {
            throw new ConfigException.BugOrBroken("should not be reached: unmergeable object returned null value");
         }
      }

      throw new ConfigException.BugOrBroken("Delayed merge stack does not contain any unmergeable values");
   }
}
