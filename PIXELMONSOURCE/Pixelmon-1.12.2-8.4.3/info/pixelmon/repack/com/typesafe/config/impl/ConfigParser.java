package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigException;
import info.pixelmon.repack.com.typesafe.config.ConfigIncludeContext;
import info.pixelmon.repack.com.typesafe.config.ConfigOrigin;
import info.pixelmon.repack.com.typesafe.config.ConfigParseOptions;
import info.pixelmon.repack.com.typesafe.config.ConfigSyntax;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

final class ConfigParser {
   static AbstractConfigValue parse(ConfigNodeRoot document, ConfigOrigin origin, ConfigParseOptions options, ConfigIncludeContext includeContext) {
      ParseContext context = new ParseContext(options.getSyntax(), origin, document, SimpleIncluder.makeFull(options.getIncluder()), includeContext);
      return context.parse();
   }

   private static final class ParseContext {
      private int lineNumber = 1;
      private final ConfigNodeRoot document;
      private final FullIncluder includer;
      private final ConfigIncludeContext includeContext;
      private final ConfigSyntax flavor;
      private final ConfigOrigin baseOrigin;
      private final LinkedList pathStack;
      int arrayCount;

      ParseContext(ConfigSyntax flavor, ConfigOrigin origin, ConfigNodeRoot document, FullIncluder includer, ConfigIncludeContext includeContext) {
         this.document = document;
         this.flavor = flavor;
         this.baseOrigin = origin;
         this.includer = includer;
         this.includeContext = includeContext;
         this.pathStack = new LinkedList();
         this.arrayCount = 0;
      }

      private AbstractConfigValue parseConcatenation(ConfigNodeConcatenation n) {
         if (this.flavor == ConfigSyntax.JSON) {
            throw new ConfigException.BugOrBroken("Found a concatenation node in JSON");
         } else {
            List values = new ArrayList();
            Iterator var3 = n.children().iterator();

            while(var3.hasNext()) {
               AbstractConfigNode node = (AbstractConfigNode)var3.next();
               AbstractConfigValue v = null;
               if (node instanceof AbstractConfigNodeValue) {
                  v = this.parseValue((AbstractConfigNodeValue)node, (List)null);
                  values.add(v);
               }
            }

            return ConfigConcatenation.concatenate(values);
         }
      }

      private SimpleConfigOrigin lineOrigin() {
         return ((SimpleConfigOrigin)this.baseOrigin).withLineNumber(this.lineNumber);
      }

      private ConfigException parseError(String message) {
         return this.parseError(message, (Throwable)null);
      }

      private ConfigException parseError(String message, Throwable cause) {
         return new ConfigException.Parse(this.lineOrigin(), message, cause);
      }

      private Path fullCurrentPath() {
         if (this.pathStack.isEmpty()) {
            throw new ConfigException.BugOrBroken("Bug in parser; tried to get current path when at root");
         } else {
            return new Path(this.pathStack.descendingIterator());
         }
      }

      private AbstractConfigValue parseValue(AbstractConfigNodeValue n, List comments) {
         int startingArrayCount = this.arrayCount;
         Object v;
         if (n instanceof ConfigNodeSimpleValue) {
            v = ((ConfigNodeSimpleValue)n).value();
         } else if (n instanceof ConfigNodeObject) {
            v = this.parseObject((ConfigNodeObject)n);
         } else if (n instanceof ConfigNodeArray) {
            v = this.parseArray((ConfigNodeArray)n);
         } else {
            if (!(n instanceof ConfigNodeConcatenation)) {
               throw this.parseError("Expecting a value but got wrong node type: " + n.getClass());
            }

            v = this.parseConcatenation((ConfigNodeConcatenation)n);
         }

         if (comments != null && !comments.isEmpty()) {
            v = ((AbstractConfigValue)v).withOrigin(((AbstractConfigValue)v).origin().prependComments(new ArrayList(comments)));
            comments.clear();
         }

         if (this.arrayCount != startingArrayCount) {
            throw new ConfigException.BugOrBroken("Bug in config parser: unbalanced array count");
         } else {
            return (AbstractConfigValue)v;
         }
      }

      private static AbstractConfigObject createValueUnderPath(Path path, AbstractConfigValue value) {
         List keys = new ArrayList();
         String key = path.first();

         for(Path remaining = path.remainder(); key != null; remaining = remaining.remainder()) {
            keys.add(key);
            if (remaining == null) {
               break;
            }

            key = remaining.first();
         }

         ListIterator i = keys.listIterator(keys.size());
         String deepest = (String)i.previous();

         SimpleConfigObject o;
         Map m;
         for(o = new SimpleConfigObject(value.origin().withComments((List)null), Collections.singletonMap(deepest, value)); i.hasPrevious(); o = new SimpleConfigObject(value.origin().withComments((List)null), m)) {
            m = Collections.singletonMap(i.previous(), o);
         }

         return o;
      }

      private void parseInclude(Map values, ConfigNodeInclude n) {
         boolean isRequired = n.isRequired();
         ConfigIncludeContext cic = this.includeContext.setParseOptions(this.includeContext.parseOptions().setAllowMissing(!isRequired));
         AbstractConfigObject obj;
         switch (n.kind()) {
            case URL:
               URL url;
               try {
                  url = new URL(n.name());
               } catch (MalformedURLException var10) {
                  throw this.parseError("include url() specifies an invalid URL: " + n.name(), var10);
               }

               obj = (AbstractConfigObject)this.includer.includeURL(cic, url);
               break;
            case FILE:
               obj = (AbstractConfigObject)this.includer.includeFile(cic, new File(n.name()));
               break;
            case CLASSPATH:
               obj = (AbstractConfigObject)this.includer.includeResources(cic, n.name());
               break;
            case HEURISTIC:
               obj = (AbstractConfigObject)this.includer.include(cic, n.name());
               break;
            default:
               throw new ConfigException.BugOrBroken("should not be reached");
         }

         if (this.arrayCount > 0 && obj.resolveStatus() != ResolveStatus.RESOLVED) {
            throw this.parseError("Due to current limitations of the config parser, when an include statement is nested inside a list value, ${} substitutions inside the included file cannot be resolved correctly. Either move the include outside of the list value or remove the ${} statements from the included file.");
         } else {
            if (!this.pathStack.isEmpty()) {
               Path prefix = this.fullCurrentPath();
               obj = obj.relativized(prefix);
            }

            Iterator var12 = obj.keySet().iterator();

            while(var12.hasNext()) {
               String key = (String)var12.next();
               AbstractConfigValue v = obj.get(key);
               AbstractConfigValue existing = (AbstractConfigValue)values.get(key);
               if (existing != null) {
                  values.put(key, v.withFallback(existing));
               } else {
                  values.put(key, v);
               }
            }

         }
      }

      private AbstractConfigObject parseObject(ConfigNodeObject n) {
         Map values = new HashMap();
         SimpleConfigOrigin objectOrigin = this.lineOrigin();
         boolean lastWasNewline = false;
         ArrayList nodes = new ArrayList(n.children());
         List comments = new ArrayList();

         for(int i = 0; i < nodes.size(); ++i) {
            AbstractConfigNode node = (AbstractConfigNode)nodes.get(i);
            if (node instanceof ConfigNodeComment) {
               lastWasNewline = false;
               comments.add(((ConfigNodeComment)node).commentText());
            } else if (node instanceof ConfigNodeSingleToken && Tokens.isNewline(((ConfigNodeSingleToken)node).token())) {
               ++this.lineNumber;
               if (lastWasNewline) {
                  comments.clear();
               }

               lastWasNewline = true;
            } else if (this.flavor != ConfigSyntax.JSON && node instanceof ConfigNodeInclude) {
               this.parseInclude(values, (ConfigNodeInclude)node);
               lastWasNewline = false;
            } else if (node instanceof ConfigNodeField) {
               lastWasNewline = false;
               Path path = ((ConfigNodeField)node).path().value();
               comments.addAll(((ConfigNodeField)node).comments());
               this.pathStack.push(path);
               if (((ConfigNodeField)node).separator() == Tokens.PLUS_EQUALS) {
                  if (this.arrayCount > 0) {
                     throw this.parseError("Due to current limitations of the config parser, += does not work nested inside a list. += expands to a ${} substitution and the path in ${} cannot currently refer to list elements. You might be able to move the += outside of the list and then refer to it from inside the list with ${}.");
                  }

                  ++this.arrayCount;
               }

               AbstractConfigNodeValue valueNode = ((ConfigNodeField)node).value();
               AbstractConfigValue newValue = this.parseValue(valueNode, comments);
               if (((ConfigNodeField)node).separator() == Tokens.PLUS_EQUALS) {
                  --this.arrayCount;
                  List concat = new ArrayList(2);
                  AbstractConfigValue previousRef = new ConfigReference(newValue.origin(), new SubstitutionExpression(this.fullCurrentPath(), true));
                  AbstractConfigValue list = new SimpleConfigList(newValue.origin(), Collections.singletonList(newValue));
                  concat.add(previousRef);
                  concat.add(list);
                  newValue = ConfigConcatenation.concatenate(concat);
               }

               if (i < nodes.size() - 1) {
                  ++i;

                  while(i < nodes.size()) {
                     if (nodes.get(i) instanceof ConfigNodeComment) {
                        ConfigNodeComment comment = (ConfigNodeComment)nodes.get(i);
                        newValue = newValue.withOrigin(newValue.origin().appendComments(Collections.singletonList(comment.commentText())));
                        break;
                     }

                     if (!(nodes.get(i) instanceof ConfigNodeSingleToken)) {
                        --i;
                        break;
                     }

                     ConfigNodeSingleToken curr = (ConfigNodeSingleToken)nodes.get(i);
                     if (curr.token() != Tokens.COMMA && !Tokens.isIgnoredWhitespace(curr.token())) {
                        --i;
                        break;
                     }

                     ++i;
                  }
               }

               this.pathStack.pop();
               String key = path.first();
               Path remaining = path.remainder();
               if (remaining == null) {
                  AbstractConfigValue existing = (AbstractConfigValue)values.get(key);
                  if (existing != null) {
                     if (this.flavor == ConfigSyntax.JSON) {
                        throw this.parseError("JSON does not allow duplicate fields: '" + key + "' was already seen at " + existing.origin().description());
                     }

                     newValue = newValue.withFallback(existing);
                  }

                  values.put(key, newValue);
               } else {
                  if (this.flavor == ConfigSyntax.JSON) {
                     throw new ConfigException.BugOrBroken("somehow got multi-element path in JSON mode");
                  }

                  AbstractConfigObject obj = createValueUnderPath(remaining, newValue);
                  AbstractConfigValue existing = (AbstractConfigValue)values.get(key);
                  if (existing != null) {
                     obj = obj.withFallback(existing);
                  }

                  values.put(key, obj);
               }
            }
         }

         return new SimpleConfigObject(objectOrigin, values);
      }

      private SimpleConfigList parseArray(ConfigNodeArray n) {
         ++this.arrayCount;
         SimpleConfigOrigin arrayOrigin = this.lineOrigin();
         List values = new ArrayList();
         boolean lastWasNewLine = false;
         List comments = new ArrayList();
         AbstractConfigValue v = null;
         Iterator var7 = n.children().iterator();

         while(true) {
            while(var7.hasNext()) {
               AbstractConfigNode node = (AbstractConfigNode)var7.next();
               if (node instanceof ConfigNodeComment) {
                  comments.add(((ConfigNodeComment)node).commentText());
                  lastWasNewLine = false;
               } else if (node instanceof ConfigNodeSingleToken && Tokens.isNewline(((ConfigNodeSingleToken)node).token())) {
                  ++this.lineNumber;
                  if (lastWasNewLine && v == null) {
                     comments.clear();
                  } else if (v != null) {
                     values.add(v.withOrigin(v.origin().appendComments(new ArrayList(comments))));
                     comments.clear();
                     v = null;
                  }

                  lastWasNewLine = true;
               } else if (node instanceof AbstractConfigNodeValue) {
                  lastWasNewLine = false;
                  if (v != null) {
                     values.add(v.withOrigin(v.origin().appendComments(new ArrayList(comments))));
                     comments.clear();
                  }

                  v = this.parseValue((AbstractConfigNodeValue)node, comments);
               }
            }

            if (v != null) {
               values.add(v.withOrigin(v.origin().appendComments(new ArrayList(comments))));
            }

            --this.arrayCount;
            return new SimpleConfigList(arrayOrigin, values);
         }
      }

      AbstractConfigValue parse() {
         AbstractConfigValue result = null;
         ArrayList comments = new ArrayList();
         boolean lastWasNewLine = false;
         Iterator var4 = this.document.children().iterator();

         while(var4.hasNext()) {
            AbstractConfigNode node = (AbstractConfigNode)var4.next();
            if (node instanceof ConfigNodeComment) {
               comments.add(((ConfigNodeComment)node).commentText());
               lastWasNewLine = false;
            } else if (node instanceof ConfigNodeSingleToken) {
               Token t = ((ConfigNodeSingleToken)node).token();
               if (Tokens.isNewline(t)) {
                  ++this.lineNumber;
                  if (lastWasNewLine && result == null) {
                     comments.clear();
                  } else if (result != null) {
                     result = result.withOrigin(result.origin().appendComments(new ArrayList(comments)));
                     comments.clear();
                     break;
                  }

                  lastWasNewLine = true;
               }
            } else if (node instanceof ConfigNodeComplexValue) {
               result = this.parseValue((ConfigNodeComplexValue)node, comments);
               lastWasNewLine = false;
            }
         }

         return result;
      }
   }
}
