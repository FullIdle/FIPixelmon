package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigException;
import info.pixelmon.repack.com.typesafe.config.ConfigOrigin;
import info.pixelmon.repack.com.typesafe.config.ConfigRenderOptions;
import info.pixelmon.repack.com.typesafe.config.ConfigValueType;
import java.util.Collection;
import java.util.Collections;

final class ConfigReference extends AbstractConfigValue implements Unmergeable {
   private final SubstitutionExpression expr;
   private final int prefixLength;

   ConfigReference(ConfigOrigin origin, SubstitutionExpression expr) {
      this(origin, expr, 0);
   }

   private ConfigReference(ConfigOrigin origin, SubstitutionExpression expr, int prefixLength) {
      super(origin);
      this.expr = expr;
      this.prefixLength = prefixLength;
   }

   private ConfigException.NotResolved notResolved() {
      return new ConfigException.NotResolved("need to Config#resolve(), see the API docs for Config#resolve(); substitution not resolved: " + this);
   }

   public ConfigValueType valueType() {
      throw this.notResolved();
   }

   public Object unwrapped() {
      throw this.notResolved();
   }

   protected ConfigReference newCopy(ConfigOrigin newOrigin) {
      return new ConfigReference(newOrigin, this.expr, this.prefixLength);
   }

   protected boolean ignoresFallbacks() {
      return false;
   }

   public Collection unmergedValues() {
      return Collections.singleton(this);
   }

   ResolveResult resolveSubstitutions(ResolveContext context, ResolveSource source) {
      ResolveContext newContext = context.addCycleMarker(this);

      AbstractConfigValue v;
      try {
         ResolveSource.ResultWithPath resultWithPath = source.lookupSubst(newContext, this.expr, this.prefixLength);
         newContext = resultWithPath.result.context;
         if (resultWithPath.result.value != null) {
            if (ConfigImpl.traceSubstitutionsEnabled()) {
               ConfigImpl.trace(newContext.depth(), "recursively resolving " + resultWithPath + " which was the resolution of " + this.expr + " against " + source);
            }

            ResolveSource recursiveResolveSource = new ResolveSource((AbstractConfigObject)resultWithPath.pathFromRoot.last(), resultWithPath.pathFromRoot);
            if (ConfigImpl.traceSubstitutionsEnabled()) {
               ConfigImpl.trace(newContext.depth(), "will recursively resolve against " + recursiveResolveSource);
            }

            ResolveResult result = newContext.resolve(resultWithPath.result.value, recursiveResolveSource);
            v = result.value;
            newContext = result.context;
         } else {
            v = null;
         }
      } catch (AbstractConfigValue.NotPossibleToResolve var8) {
         if (ConfigImpl.traceSubstitutionsEnabled()) {
            ConfigImpl.trace(newContext.depth(), "not possible to resolve " + this.expr + ", cycle involved: " + var8.traceString());
         }

         if (!this.expr.optional()) {
            throw new ConfigException.UnresolvedSubstitution(this.origin(), this.expr + " was part of a cycle of substitutions involving " + var8.traceString(), var8);
         }

         v = null;
      }

      if (v == null && !this.expr.optional()) {
         if (newContext.options().getAllowUnresolved()) {
            return ResolveResult.make(newContext.removeCycleMarker(this), this);
         } else {
            throw new ConfigException.UnresolvedSubstitution(this.origin(), this.expr.toString());
         }
      } else {
         return ResolveResult.make(newContext.removeCycleMarker(this), v);
      }
   }

   ResolveStatus resolveStatus() {
      return ResolveStatus.UNRESOLVED;
   }

   ConfigReference relativized(Path prefix) {
      SubstitutionExpression newExpr = this.expr.changePath(this.expr.path().prepend(prefix));
      return new ConfigReference(this.origin(), newExpr, this.prefixLength + prefix.length());
   }

   protected boolean canEqual(Object other) {
      return other instanceof ConfigReference;
   }

   public boolean equals(Object other) {
      if (!(other instanceof ConfigReference)) {
         return false;
      } else {
         return this.canEqual(other) && this.expr.equals(((ConfigReference)other).expr);
      }
   }

   public int hashCode() {
      return this.expr.hashCode();
   }

   protected void render(StringBuilder sb, int indent, boolean atRoot, ConfigRenderOptions options) {
      sb.append(this.expr.toString());
   }

   SubstitutionExpression expression() {
      return this.expr;
   }
}
