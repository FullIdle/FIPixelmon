package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigOrigin;
import info.pixelmon.repack.com.typesafe.config.ConfigSyntax;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

final class ConfigNodeObject extends ConfigNodeComplexValue {
   ConfigNodeObject(Collection children) {
      super(children);
   }

   protected ConfigNodeObject newNode(Collection nodes) {
      return new ConfigNodeObject(nodes);
   }

   public boolean hasValue(Path desiredPath) {
      Iterator var2 = this.children.iterator();

      while(true) {
         AbstractConfigNode node;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            node = (AbstractConfigNode)var2.next();
         } while(!(node instanceof ConfigNodeField));

         ConfigNodeField field = (ConfigNodeField)node;
         Path key = field.path().value();
         if (key.equals(desiredPath) || key.startsWith(desiredPath)) {
            return true;
         }

         if (desiredPath.startsWith(key) && field.value() instanceof ConfigNodeObject) {
            ConfigNodeObject obj = (ConfigNodeObject)field.value();
            Path remainingPath = desiredPath.subPath(key.length());
            if (obj.hasValue(remainingPath)) {
               return true;
            }
         }
      }
   }

   protected ConfigNodeObject changeValueOnPath(Path desiredPath, AbstractConfigNodeValue value, ConfigSyntax flavor) {
      ArrayList childrenCopy = new ArrayList(super.children);
      boolean seenNonMatching = false;
      AbstractConfigNodeValue valueCopy = value;

      for(int i = childrenCopy.size() - 1; i >= 0; --i) {
         if (childrenCopy.get(i) instanceof ConfigNodeSingleToken) {
            Token t = ((ConfigNodeSingleToken)childrenCopy.get(i)).token();
            if (flavor == ConfigSyntax.JSON && !seenNonMatching && t == Tokens.COMMA) {
               childrenCopy.remove(i);
            }
         } else if (childrenCopy.get(i) instanceof ConfigNodeField) {
            ConfigNodeField node = (ConfigNodeField)childrenCopy.get(i);
            Path key = node.path().value();
            if (valueCopy == null && key.equals(desiredPath) || key.startsWith(desiredPath) && !key.equals(desiredPath)) {
               childrenCopy.remove(i);

               for(int j = i; j < childrenCopy.size() && childrenCopy.get(j) instanceof ConfigNodeSingleToken; ++j) {
                  Token t = ((ConfigNodeSingleToken)childrenCopy.get(j)).token();
                  if (!Tokens.isIgnoredWhitespace(t) && t != Tokens.COMMA) {
                     break;
                  }

                  childrenCopy.remove(j);
                  --j;
               }
            } else if (key.equals(desiredPath)) {
               seenNonMatching = true;
               AbstractConfigNode before = i - 1 > 0 ? (AbstractConfigNode)childrenCopy.get(i - 1) : null;
               Object indentedValue;
               if (value instanceof ConfigNodeComplexValue && before instanceof ConfigNodeSingleToken && Tokens.isIgnoredWhitespace(((ConfigNodeSingleToken)before).token())) {
                  indentedValue = ((ConfigNodeComplexValue)value).indentText(before);
               } else {
                  indentedValue = value;
               }

               childrenCopy.set(i, node.replaceValue((AbstractConfigNodeValue)indentedValue));
               valueCopy = null;
            } else if (desiredPath.startsWith(key)) {
               seenNonMatching = true;
               if (node.value() instanceof ConfigNodeObject) {
                  Path remainingPath = desiredPath.subPath(key.length());
                  childrenCopy.set(i, node.replaceValue(((ConfigNodeObject)node.value()).changeValueOnPath(remainingPath, valueCopy, flavor)));
                  if (valueCopy != null && !node.equals(super.children.get(i))) {
                     valueCopy = null;
                  }
               }
            } else {
               seenNonMatching = true;
            }
         }
      }

      return new ConfigNodeObject(childrenCopy);
   }

   public ConfigNodeObject setValueOnPath(String desiredPath, AbstractConfigNodeValue value) {
      return this.setValueOnPath(desiredPath, value, ConfigSyntax.CONF);
   }

   public ConfigNodeObject setValueOnPath(String desiredPath, AbstractConfigNodeValue value, ConfigSyntax flavor) {
      ConfigNodePath path = PathParser.parsePathNode(desiredPath, flavor);
      return this.setValueOnPath(path, value, flavor);
   }

   private ConfigNodeObject setValueOnPath(ConfigNodePath desiredPath, AbstractConfigNodeValue value, ConfigSyntax flavor) {
      ConfigNodeObject node = this.changeValueOnPath(desiredPath.value(), value, flavor);
      return !node.hasValue(desiredPath.value()) ? node.addValueOnPath(desiredPath, value, flavor) : node;
   }

   private Collection indentation() {
      boolean seenNewLine = false;
      ArrayList indentation = new ArrayList();
      if (this.children.isEmpty()) {
         return indentation;
      } else {
         for(int i = 0; i < this.children.size(); ++i) {
            if (!seenNewLine) {
               if (this.children.get(i) instanceof ConfigNodeSingleToken && Tokens.isNewline(((ConfigNodeSingleToken)this.children.get(i)).token())) {
                  seenNewLine = true;
                  indentation.add(new ConfigNodeSingleToken(Tokens.newLine((ConfigOrigin)null)));
               }
            } else if (this.children.get(i) instanceof ConfigNodeSingleToken && Tokens.isIgnoredWhitespace(((ConfigNodeSingleToken)this.children.get(i)).token()) && i + 1 < this.children.size() && (this.children.get(i + 1) instanceof ConfigNodeField || this.children.get(i + 1) instanceof ConfigNodeInclude)) {
               indentation.add(this.children.get(i));
               return indentation;
            }
         }

         if (indentation.isEmpty()) {
            indentation.add(new ConfigNodeSingleToken(Tokens.newIgnoredWhitespace((ConfigOrigin)null, " ")));
         } else {
            AbstractConfigNode last = (AbstractConfigNode)this.children.get(this.children.size() - 1);
            if (last instanceof ConfigNodeSingleToken && ((ConfigNodeSingleToken)last).token() == Tokens.CLOSE_CURLY) {
               AbstractConfigNode beforeLast = (AbstractConfigNode)this.children.get(this.children.size() - 2);
               String indent = "";
               if (beforeLast instanceof ConfigNodeSingleToken && Tokens.isIgnoredWhitespace(((ConfigNodeSingleToken)beforeLast).token())) {
                  indent = ((ConfigNodeSingleToken)beforeLast).token().tokenText();
               }

               indent = indent + "  ";
               indentation.add(new ConfigNodeSingleToken(Tokens.newIgnoredWhitespace((ConfigOrigin)null, indent)));
               return indentation;
            }
         }

         return indentation;
      }
   }

   protected ConfigNodeObject addValueOnPath(ConfigNodePath desiredPath, AbstractConfigNodeValue value, ConfigSyntax flavor) {
      Path path = desiredPath.value();
      ArrayList childrenCopy = new ArrayList(super.children);
      ArrayList indentation = new ArrayList(this.indentation());
      Object indentedValue;
      if (value instanceof ConfigNodeComplexValue && !indentation.isEmpty()) {
         indentedValue = ((ConfigNodeComplexValue)value).indentText((AbstractConfigNode)indentation.get(indentation.size() - 1));
      } else {
         indentedValue = value;
      }

      boolean sameLine = indentation.size() <= 0 || !(indentation.get(0) instanceof ConfigNodeSingleToken) || !Tokens.isNewline(((ConfigNodeSingleToken)indentation.get(0)).token());
      if (path.length() > 1) {
         for(int i = super.children.size() - 1; i >= 0; --i) {
            if (super.children.get(i) instanceof ConfigNodeField) {
               ConfigNodeField node = (ConfigNodeField)super.children.get(i);
               Path key = node.path().value();
               if (path.startsWith(key) && node.value() instanceof ConfigNodeObject) {
                  ConfigNodePath remainingPath = desiredPath.subPath(key.length());
                  ConfigNodeObject newValue = (ConfigNodeObject)node.value();
                  childrenCopy.set(i, node.replaceValue(newValue.addValueOnPath(remainingPath, value, flavor)));
                  return new ConfigNodeObject(childrenCopy);
               }
            }
         }
      }

      boolean startsWithBrace = !super.children.isEmpty() && super.children.get(0) instanceof ConfigNodeSingleToken && ((ConfigNodeSingleToken)super.children.get(0)).token() == Tokens.OPEN_CURLY;
      ArrayList newNodes = new ArrayList();
      newNodes.addAll(indentation);
      newNodes.add(desiredPath.first());
      newNodes.add(new ConfigNodeSingleToken(Tokens.newIgnoredWhitespace((ConfigOrigin)null, " ")));
      newNodes.add(new ConfigNodeSingleToken(Tokens.COLON));
      newNodes.add(new ConfigNodeSingleToken(Tokens.newIgnoredWhitespace((ConfigOrigin)null, " ")));
      if (path.length() == 1) {
         newNodes.add(indentedValue);
      } else {
         ArrayList newObjectNodes = new ArrayList();
         newObjectNodes.add(new ConfigNodeSingleToken(Tokens.OPEN_CURLY));
         if (indentation.isEmpty()) {
            newObjectNodes.add(new ConfigNodeSingleToken(Tokens.newLine((ConfigOrigin)null)));
         }

         newObjectNodes.addAll(indentation);
         newObjectNodes.add(new ConfigNodeSingleToken(Tokens.CLOSE_CURLY));
         ConfigNodeObject newObject = new ConfigNodeObject(newObjectNodes);
         newNodes.add(newObject.addValueOnPath(desiredPath.subPath(1), (AbstractConfigNodeValue)indentedValue, flavor));
      }

      if (flavor == ConfigSyntax.JSON || startsWithBrace || sameLine) {
         for(int i = childrenCopy.size() - 1; i >= 0; --i) {
            if ((flavor == ConfigSyntax.JSON || sameLine) && childrenCopy.get(i) instanceof ConfigNodeField) {
               if (i + 1 >= childrenCopy.size() || !(childrenCopy.get(i + 1) instanceof ConfigNodeSingleToken) || ((ConfigNodeSingleToken)childrenCopy.get(i + 1)).token() != Tokens.COMMA) {
                  childrenCopy.add(i + 1, new ConfigNodeSingleToken(Tokens.COMMA));
               }
               break;
            }

            if (startsWithBrace && childrenCopy.get(i) instanceof ConfigNodeSingleToken && ((ConfigNodeSingleToken)childrenCopy.get(i)).token == Tokens.CLOSE_CURLY) {
               AbstractConfigNode previous = (AbstractConfigNode)childrenCopy.get(i - 1);
               if (previous instanceof ConfigNodeSingleToken && Tokens.isNewline(((ConfigNodeSingleToken)previous).token())) {
                  childrenCopy.add(i - 1, new ConfigNodeField(newNodes));
                  --i;
               } else if (previous instanceof ConfigNodeSingleToken && Tokens.isIgnoredWhitespace(((ConfigNodeSingleToken)previous).token())) {
                  AbstractConfigNode beforePrevious = (AbstractConfigNode)childrenCopy.get(i - 2);
                  if (sameLine) {
                     childrenCopy.add(i - 1, new ConfigNodeField(newNodes));
                     --i;
                  } else if (beforePrevious instanceof ConfigNodeSingleToken && Tokens.isNewline(((ConfigNodeSingleToken)beforePrevious).token())) {
                     childrenCopy.add(i - 2, new ConfigNodeField(newNodes));
                     i -= 2;
                  } else {
                     childrenCopy.add(i, new ConfigNodeField(newNodes));
                  }
               } else {
                  childrenCopy.add(i, new ConfigNodeField(newNodes));
               }
            }
         }
      }

      if (!startsWithBrace) {
         if (!childrenCopy.isEmpty() && childrenCopy.get(childrenCopy.size() - 1) instanceof ConfigNodeSingleToken && Tokens.isNewline(((ConfigNodeSingleToken)childrenCopy.get(childrenCopy.size() - 1)).token())) {
            childrenCopy.add(childrenCopy.size() - 1, new ConfigNodeField(newNodes));
         } else {
            childrenCopy.add(new ConfigNodeField(newNodes));
         }
      }

      return new ConfigNodeObject(childrenCopy);
   }

   public ConfigNodeObject removeValueOnPath(String desiredPath, ConfigSyntax flavor) {
      Path path = PathParser.parsePathNode(desiredPath, flavor).value();
      return this.changeValueOnPath(path, (AbstractConfigNodeValue)null, flavor);
   }
}
