package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigException;
import info.pixelmon.repack.com.typesafe.config.ConfigOrigin;
import info.pixelmon.repack.com.typesafe.config.ConfigRenderOptions;
import info.pixelmon.repack.com.typesafe.config.ConfigValueType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

final class ConfigDelayedMerge extends AbstractConfigValue implements Unmergeable, ReplaceableMergeStack {
   private final List stack;

   ConfigDelayedMerge(ConfigOrigin origin, List stack) {
      super(origin);
      this.stack = stack;
      if (stack.isEmpty()) {
         throw new ConfigException.BugOrBroken("creating empty delayed merge value");
      } else {
         Iterator var3 = stack.iterator();

         AbstractConfigValue v;
         do {
            if (!var3.hasNext()) {
               return;
            }

            v = (AbstractConfigValue)var3.next();
         } while(!(v instanceof ConfigDelayedMerge) && !(v instanceof ConfigDelayedMergeObject));

         throw new ConfigException.BugOrBroken("placed nested DelayedMerge in a ConfigDelayedMerge, should have consolidated stack");
      }
   }

   public ConfigValueType valueType() {
      throw new ConfigException.NotResolved("called valueType() on value with unresolved substitutions, need to Config#resolve() first, see API docs");
   }

   public Object unwrapped() {
      throw new ConfigException.NotResolved("called unwrapped() on value with unresolved substitutions, need to Config#resolve() first, see API docs");
   }

   ResolveResult resolveSubstitutions(ResolveContext context, ResolveSource source) throws AbstractConfigValue.NotPossibleToResolve {
      return resolveSubstitutions(this, this.stack, context, source);
   }

   static ResolveResult resolveSubstitutions(ReplaceableMergeStack replaceable, List stack, ResolveContext context, ResolveSource source) throws AbstractConfigValue.NotPossibleToResolve {
      AbstractConfigValue merged;
      if (ConfigImpl.traceSubstitutionsEnabled()) {
         ConfigImpl.trace(context.depth(), "delayed merge stack has " + stack.size() + " items:");
         int count = 0;

         for(Iterator var5 = stack.iterator(); var5.hasNext(); ++count) {
            merged = (AbstractConfigValue)var5.next();
            ConfigImpl.trace(context.depth() + 1, count + ": " + merged);
         }
      }

      ResolveContext newContext = context;
      int count = 0;
      merged = null;
      Iterator var7 = stack.iterator();

      while(var7.hasNext()) {
         AbstractConfigValue end = (AbstractConfigValue)var7.next();
         if (end instanceof ReplaceableMergeStack) {
            throw new ConfigException.BugOrBroken("A delayed merge should not contain another one: " + replaceable);
         }

         ResolveSource sourceForEnd;
         if (end instanceof Unmergeable) {
            AbstractConfigValue remainder = replaceable.makeReplacement(context, count + 1);
            if (ConfigImpl.traceSubstitutionsEnabled()) {
               ConfigImpl.trace(newContext.depth(), "remainder portion: " + remainder);
            }

            if (ConfigImpl.traceSubstitutionsEnabled()) {
               ConfigImpl.trace(newContext.depth(), "building sourceForEnd");
            }

            sourceForEnd = source.replaceWithinCurrentParent((AbstractConfigValue)replaceable, remainder);
            if (ConfigImpl.traceSubstitutionsEnabled()) {
               ConfigImpl.trace(newContext.depth(), "  sourceForEnd before reset parents but after replace: " + sourceForEnd);
            }

            sourceForEnd = sourceForEnd.resetParents();
         } else {
            if (ConfigImpl.traceSubstitutionsEnabled()) {
               ConfigImpl.trace(newContext.depth(), "will resolve end against the original source with parent pushed");
            }

            sourceForEnd = source.pushParent(replaceable);
         }

         if (ConfigImpl.traceSubstitutionsEnabled()) {
            ConfigImpl.trace(newContext.depth(), "sourceForEnd      =" + sourceForEnd);
         }

         if (ConfigImpl.traceSubstitutionsEnabled()) {
            ConfigImpl.trace(newContext.depth(), "Resolving highest-priority item in delayed merge " + end + " against " + sourceForEnd + " endWasRemoved=" + (source != sourceForEnd));
         }

         ResolveResult result = newContext.resolve(end, sourceForEnd);
         AbstractConfigValue resolvedEnd = result.value;
         newContext = result.context;
         if (resolvedEnd != null) {
            if (merged == null) {
               merged = resolvedEnd;
            } else {
               if (ConfigImpl.traceSubstitutionsEnabled()) {
                  ConfigImpl.trace(newContext.depth() + 1, "merging " + merged + " with fallback " + resolvedEnd);
               }

               merged = merged.withFallback(resolvedEnd);
            }
         }

         ++count;
         if (ConfigImpl.traceSubstitutionsEnabled()) {
            ConfigImpl.trace(newContext.depth(), "stack merged, yielding: " + merged);
         }
      }

      return ResolveResult.make(newContext, merged);
   }

   public AbstractConfigValue makeReplacement(ResolveContext context, int skipping) {
      return makeReplacement(context, this.stack, skipping);
   }

   static AbstractConfigValue makeReplacement(ResolveContext context, List stack, int skipping) {
      List subStack = stack.subList(skipping, stack.size());
      if (subStack.isEmpty()) {
         if (ConfigImpl.traceSubstitutionsEnabled()) {
            ConfigImpl.trace(context.depth(), "Nothing else in the merge stack, replacing with null");
         }

         return null;
      } else {
         AbstractConfigValue merged = null;
         Iterator var5 = subStack.iterator();

         while(var5.hasNext()) {
            AbstractConfigValue v = (AbstractConfigValue)var5.next();
            if (merged == null) {
               merged = v;
            } else {
               merged = merged.withFallback(v);
            }
         }

         return merged;
      }
   }

   ResolveStatus resolveStatus() {
      return ResolveStatus.UNRESOLVED;
   }

   public AbstractConfigValue replaceChild(AbstractConfigValue child, AbstractConfigValue replacement) {
      List newStack = replaceChildInList(this.stack, child, replacement);
      return newStack == null ? null : new ConfigDelayedMerge(this.origin(), newStack);
   }

   public boolean hasDescendant(AbstractConfigValue descendant) {
      return hasDescendantInList(this.stack, descendant);
   }

   ConfigDelayedMerge relativized(Path prefix) {
      List newStack = new ArrayList();
      Iterator var3 = this.stack.iterator();

      while(var3.hasNext()) {
         AbstractConfigValue o = (AbstractConfigValue)var3.next();
         newStack.add(o.relativized(prefix));
      }

      return new ConfigDelayedMerge(this.origin(), newStack);
   }

   static boolean stackIgnoresFallbacks(List stack) {
      AbstractConfigValue last = (AbstractConfigValue)stack.get(stack.size() - 1);
      return last.ignoresFallbacks();
   }

   protected boolean ignoresFallbacks() {
      return stackIgnoresFallbacks(this.stack);
   }

   protected AbstractConfigValue newCopy(ConfigOrigin newOrigin) {
      return new ConfigDelayedMerge(newOrigin, this.stack);
   }

   protected final ConfigDelayedMerge mergedWithTheUnmergeable(Unmergeable fallback) {
      return (ConfigDelayedMerge)this.mergedWithTheUnmergeable(this.stack, fallback);
   }

   protected final ConfigDelayedMerge mergedWithObject(AbstractConfigObject fallback) {
      return (ConfigDelayedMerge)this.mergedWithObject(this.stack, fallback);
   }

   protected ConfigDelayedMerge mergedWithNonObject(AbstractConfigValue fallback) {
      return (ConfigDelayedMerge)this.mergedWithNonObject(this.stack, fallback);
   }

   public Collection unmergedValues() {
      return this.stack;
   }

   protected boolean canEqual(Object other) {
      return other instanceof ConfigDelayedMerge;
   }

   public boolean equals(Object other) {
      if (!(other instanceof ConfigDelayedMerge)) {
         return false;
      } else {
         return this.canEqual(other) && (this.stack == ((ConfigDelayedMerge)other).stack || this.stack.equals(((ConfigDelayedMerge)other).stack));
      }
   }

   public int hashCode() {
      return this.stack.hashCode();
   }

   protected void render(StringBuilder sb, int indent, boolean atRoot, String atKey, ConfigRenderOptions options) {
      render(this.stack, sb, indent, atRoot, atKey, options);
   }

   protected void render(StringBuilder sb, int indent, boolean atRoot, ConfigRenderOptions options) {
      this.render(sb, indent, atRoot, (String)null, options);
   }

   static void render(List stack, StringBuilder sb, int indent, boolean atRoot, String atKey, ConfigRenderOptions options) {
      boolean commentMerge = options.getComments();
      if (commentMerge) {
         sb.append("# unresolved merge of " + stack.size() + " values follows (\n");
         if (atKey == null) {
            indent(sb, indent, options);
            sb.append("# this unresolved merge will not be parseable because it's at the root of the object\n");
            indent(sb, indent, options);
            sb.append("# the HOCON format has no way to list multiple root objects in a single file\n");
         }
      }

      List reversed = new ArrayList();
      reversed.addAll(stack);
      Collections.reverse(reversed);
      int i = 0;
      Iterator var9 = reversed.iterator();

      while(var9.hasNext()) {
         AbstractConfigValue v = (AbstractConfigValue)var9.next();
         if (commentMerge) {
            indent(sb, indent, options);
            if (atKey != null) {
               sb.append("#     unmerged value " + i + " for key " + ConfigImplUtil.renderJsonString(atKey) + " from ");
            } else {
               sb.append("#     unmerged value " + i + " from ");
            }

            ++i;
            sb.append(v.origin().description());
            sb.append("\n");
            Iterator var11 = v.origin().comments().iterator();

            while(var11.hasNext()) {
               String comment = (String)var11.next();
               indent(sb, indent, options);
               sb.append("# ");
               sb.append(comment);
               sb.append("\n");
            }
         }

         indent(sb, indent, options);
         if (atKey != null) {
            sb.append(ConfigImplUtil.renderJsonString(atKey));
            if (options.getFormatted()) {
               sb.append(" : ");
            } else {
               sb.append(":");
            }
         }

         v.render(sb, indent, atRoot, options);
         sb.append(",");
         if (options.getFormatted()) {
            sb.append('\n');
         }
      }

      sb.setLength(sb.length() - 1);
      if (options.getFormatted()) {
         sb.setLength(sb.length() - 1);
         sb.append("\n");
      }

      if (commentMerge) {
         indent(sb, indent, options);
         sb.append("# ) end of unresolved merge\n");
      }

   }
}
