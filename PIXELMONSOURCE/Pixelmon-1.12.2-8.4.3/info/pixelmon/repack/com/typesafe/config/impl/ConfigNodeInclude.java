package info.pixelmon.repack.com.typesafe.config.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

final class ConfigNodeInclude extends AbstractConfigNode {
   private final ArrayList children;
   private final ConfigIncludeKind kind;
   private final boolean isRequired;

   ConfigNodeInclude(Collection children, ConfigIncludeKind kind, boolean isRequired) {
      this.children = new ArrayList(children);
      this.kind = kind;
      this.isRequired = isRequired;
   }

   public final Collection children() {
      return this.children;
   }

   protected Collection tokens() {
      ArrayList tokens = new ArrayList();
      Iterator var2 = this.children.iterator();

      while(var2.hasNext()) {
         AbstractConfigNode child = (AbstractConfigNode)var2.next();
         tokens.addAll(child.tokens());
      }

      return tokens;
   }

   protected ConfigIncludeKind kind() {
      return this.kind;
   }

   protected boolean isRequired() {
      return this.isRequired;
   }

   protected String name() {
      Iterator var1 = this.children.iterator();

      AbstractConfigNode n;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         n = (AbstractConfigNode)var1.next();
      } while(!(n instanceof ConfigNodeSimpleValue));

      return (String)Tokens.getValue(((ConfigNodeSimpleValue)n).token()).unwrapped();
   }
}
