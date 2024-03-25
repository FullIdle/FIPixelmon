package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.parser.ConfigNode;
import java.util.Collection;
import java.util.Iterator;

abstract class AbstractConfigNode implements ConfigNode {
   abstract Collection tokens();

   public final String render() {
      StringBuilder origText = new StringBuilder();
      Iterable tokens = this.tokens();
      Iterator var3 = tokens.iterator();

      while(var3.hasNext()) {
         Token t = (Token)var3.next();
         origText.append(t.tokenText());
      }

      return origText.toString();
   }

   public final boolean equals(Object other) {
      return other instanceof AbstractConfigNode && this.render().equals(((AbstractConfigNode)other).render());
   }

   public final int hashCode() {
      return this.render().hashCode();
   }
}
