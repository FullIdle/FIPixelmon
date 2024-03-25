package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigException;

final class ResolveResult {
   public final ResolveContext context;
   public final AbstractConfigValue value;

   private ResolveResult(ResolveContext context, AbstractConfigValue value) {
      this.context = context;
      this.value = value;
   }

   static ResolveResult make(ResolveContext context, AbstractConfigValue value) {
      return new ResolveResult(context, value);
   }

   ResolveResult asObjectResult() {
      if (!(this.value instanceof AbstractConfigObject)) {
         throw new ConfigException.BugOrBroken("Expecting a resolve result to be an object, but it was " + this.value);
      } else {
         return (ResolveResult)this;
      }
   }

   ResolveResult asValueResult() {
      return (ResolveResult)this;
   }

   ResolveResult popTrace() {
      return make(this.context.popTrace(), this.value);
   }

   public String toString() {
      return "ResolveResult(" + this.value + ")";
   }
}
