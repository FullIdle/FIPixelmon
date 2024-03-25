package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

final class ConfigNodeField extends AbstractConfigNode {
   private final ArrayList children;

   public ConfigNodeField(Collection children) {
      this.children = new ArrayList(children);
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

   public ConfigNodeField replaceValue(AbstractConfigNodeValue newValue) {
      ArrayList childrenCopy = new ArrayList(this.children);

      for(int i = 0; i < childrenCopy.size(); ++i) {
         if (childrenCopy.get(i) instanceof AbstractConfigNodeValue) {
            childrenCopy.set(i, newValue);
            return new ConfigNodeField(childrenCopy);
         }
      }

      throw new ConfigException.BugOrBroken("Field node doesn't have a value");
   }

   public AbstractConfigNodeValue value() {
      for(int i = 0; i < this.children.size(); ++i) {
         if (this.children.get(i) instanceof AbstractConfigNodeValue) {
            return (AbstractConfigNodeValue)this.children.get(i);
         }
      }

      throw new ConfigException.BugOrBroken("Field node doesn't have a value");
   }

   public ConfigNodePath path() {
      for(int i = 0; i < this.children.size(); ++i) {
         if (this.children.get(i) instanceof ConfigNodePath) {
            return (ConfigNodePath)this.children.get(i);
         }
      }

      throw new ConfigException.BugOrBroken("Field node doesn't have a path");
   }

   protected Token separator() {
      Iterator var1 = this.children.iterator();

      Token t;
      do {
         AbstractConfigNode child;
         do {
            if (!var1.hasNext()) {
               return null;
            }

            child = (AbstractConfigNode)var1.next();
         } while(!(child instanceof ConfigNodeSingleToken));

         t = ((ConfigNodeSingleToken)child).token();
      } while(t != Tokens.PLUS_EQUALS && t != Tokens.COLON && t != Tokens.EQUALS);

      return t;
   }

   protected List comments() {
      List comments = new ArrayList();
      Iterator var2 = this.children.iterator();

      while(var2.hasNext()) {
         AbstractConfigNode child = (AbstractConfigNode)var2.next();
         if (child instanceof ConfigNodeComment) {
            comments.add(((ConfigNodeComment)child).commentText());
         }
      }

      return comments;
   }
}
