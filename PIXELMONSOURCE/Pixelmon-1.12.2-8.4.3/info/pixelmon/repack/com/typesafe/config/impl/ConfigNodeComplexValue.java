package info.pixelmon.repack.com.typesafe.config.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

abstract class ConfigNodeComplexValue extends AbstractConfigNodeValue {
   protected final ArrayList children;

   ConfigNodeComplexValue(Collection children) {
      this.children = new ArrayList(children);
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

   protected ConfigNodeComplexValue indentText(AbstractConfigNode indentation) {
      ArrayList childrenCopy = new ArrayList(this.children);

      for(int i = 0; i < childrenCopy.size(); ++i) {
         AbstractConfigNode child = (AbstractConfigNode)childrenCopy.get(i);
         if (child instanceof ConfigNodeSingleToken && Tokens.isNewline(((ConfigNodeSingleToken)child).token())) {
            childrenCopy.add(i + 1, indentation);
            ++i;
         } else if (child instanceof ConfigNodeField) {
            AbstractConfigNode value = ((ConfigNodeField)child).value();
            if (value instanceof ConfigNodeComplexValue) {
               childrenCopy.set(i, ((ConfigNodeField)child).replaceValue(((ConfigNodeComplexValue)value).indentText(indentation)));
            }
         } else if (child instanceof ConfigNodeComplexValue) {
            childrenCopy.set(i, ((ConfigNodeComplexValue)child).indentText(indentation));
         }
      }

      return this.newNode(childrenCopy);
   }

   abstract ConfigNodeComplexValue newNode(Collection var1);
}
