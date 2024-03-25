package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigException;
import info.pixelmon.repack.com.typesafe.config.ConfigOrigin;
import info.pixelmon.repack.com.typesafe.config.ConfigParseOptions;
import info.pixelmon.repack.com.typesafe.config.ConfigSyntax;
import info.pixelmon.repack.com.typesafe.config.ConfigValueType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

final class ConfigDocumentParser {
   static ConfigNodeRoot parse(Iterator tokens, ConfigOrigin origin, ConfigParseOptions options) {
      ConfigSyntax syntax = options.getSyntax() == null ? ConfigSyntax.CONF : options.getSyntax();
      ParseContext context = new ParseContext(syntax, origin, tokens);
      return context.parse();
   }

   static AbstractConfigNodeValue parseValue(Iterator tokens, ConfigOrigin origin, ConfigParseOptions options) {
      ConfigSyntax syntax = options.getSyntax() == null ? ConfigSyntax.CONF : options.getSyntax();
      ParseContext context = new ParseContext(syntax, origin, tokens);
      return context.parseSingleValue();
   }

   private static final class ParseContext {
      private int lineNumber = 1;
      private final Stack buffer = new Stack();
      private final Iterator tokens;
      private final ConfigSyntax flavor;
      private final ConfigOrigin baseOrigin;
      int equalsCount;
      private final String ExpectingClosingParenthesisError = "expecting a close parentheses ')' here, not: ";

      ParseContext(ConfigSyntax flavor, ConfigOrigin origin, Iterator tokens) {
         this.tokens = tokens;
         this.flavor = flavor;
         this.equalsCount = 0;
         this.baseOrigin = origin;
      }

      private Token popToken() {
         return this.buffer.isEmpty() ? (Token)this.tokens.next() : (Token)this.buffer.pop();
      }

      private Token nextToken() {
         Token t = this.popToken();
         if (this.flavor == ConfigSyntax.JSON) {
            if (Tokens.isUnquotedText(t) && !isUnquotedWhitespace(t)) {
               throw this.parseError("Token not allowed in valid JSON: '" + Tokens.getUnquotedText(t) + "'");
            }

            if (Tokens.isSubstitution(t)) {
               throw this.parseError("Substitutions (${} syntax) not allowed in JSON");
            }
         }

         return t;
      }

      private Token nextTokenCollectingWhitespace(Collection nodes) {
         while(true) {
            Token t = this.nextToken();
            if (!Tokens.isIgnoredWhitespace(t) && !Tokens.isNewline(t) && !isUnquotedWhitespace(t)) {
               if (!Tokens.isComment(t)) {
                  int newNumber = t.lineNumber();
                  if (newNumber >= 0) {
                     this.lineNumber = newNumber;
                  }

                  return t;
               }

               nodes.add(new ConfigNodeComment(t));
            } else {
               nodes.add(new ConfigNodeSingleToken(t));
               if (Tokens.isNewline(t)) {
                  this.lineNumber = t.lineNumber() + 1;
               }
            }
         }
      }

      private void putBack(Token token) {
         this.buffer.push(token);
      }

      private boolean checkElementSeparator(Collection nodes) {
         if (this.flavor == ConfigSyntax.JSON) {
            Token t = this.nextTokenCollectingWhitespace(nodes);
            if (t == Tokens.COMMA) {
               nodes.add(new ConfigNodeSingleToken(t));
               return true;
            } else {
               this.putBack(t);
               return false;
            }
         } else {
            boolean sawSeparatorOrNewline = false;
            Token t = this.nextToken();

            while(true) {
               if (!Tokens.isIgnoredWhitespace(t) && !isUnquotedWhitespace(t)) {
                  if (Tokens.isComment(t)) {
                     nodes.add(new ConfigNodeComment(t));
                  } else {
                     if (!Tokens.isNewline(t)) {
                        if (t == Tokens.COMMA) {
                           nodes.add(new ConfigNodeSingleToken(t));
                           return true;
                        }

                        this.putBack(t);
                        return sawSeparatorOrNewline;
                     }

                     sawSeparatorOrNewline = true;
                     ++this.lineNumber;
                     nodes.add(new ConfigNodeSingleToken(t));
                  }
               } else {
                  nodes.add(new ConfigNodeSingleToken(t));
               }

               t = this.nextToken();
            }
         }
      }

      private AbstractConfigNodeValue consolidateValues(Collection nodes) {
         if (this.flavor == ConfigSyntax.JSON) {
            return null;
         } else {
            ArrayList values = new ArrayList();
            int valueCount = 0;
            Token t = this.nextTokenCollectingWhitespace(nodes);

            while(true) {
               while(true) {
                  AbstractConfigNodeValue value = null;
                  if (Tokens.isIgnoredWhitespace(t)) {
                     values.add(new ConfigNodeSingleToken(t));
                     t = this.nextToken();
                  } else {
                     if (!Tokens.isValue(t) && !Tokens.isUnquotedText(t) && !Tokens.isSubstitution(t) && t != Tokens.OPEN_CURLY && t != Tokens.OPEN_SQUARE) {
                        this.putBack(t);
                        if (valueCount < 2) {
                           value = null;
                           Iterator var6 = values.iterator();

                           while(var6.hasNext()) {
                              AbstractConfigNode node = (AbstractConfigNode)var6.next();
                              if (node instanceof AbstractConfigNodeValue) {
                                 value = (AbstractConfigNodeValue)node;
                              } else if (value == null) {
                                 nodes.add(node);
                              } else {
                                 this.putBack((Token)(new ArrayList(node.tokens())).get(0));
                              }
                           }

                           return value;
                        }

                        for(int i = values.size() - 1; i >= 0 && values.get(i) instanceof ConfigNodeSingleToken; --i) {
                           this.putBack(((ConfigNodeSingleToken)values.get(i)).token());
                           values.remove(i);
                        }

                        return new ConfigNodeConcatenation(values);
                     }

                     value = this.parseValue(t);
                     ++valueCount;
                     if (value == null) {
                        throw new ConfigException.BugOrBroken("no value");
                     }

                     values.add(value);
                     t = this.nextToken();
                  }
               }
            }
         }
      }

      private ConfigException parseError(String message) {
         return this.parseError(message, (Throwable)null);
      }

      private ConfigException parseError(String message, Throwable cause) {
         return new ConfigException.Parse(this.baseOrigin.withLineNumber(this.lineNumber), message, cause);
      }

      private String addQuoteSuggestion(String badToken, String message) {
         return this.addQuoteSuggestion((Path)null, this.equalsCount > 0, badToken, message);
      }

      private String addQuoteSuggestion(Path lastPath, boolean insideEquals, String badToken, String message) {
         String previousFieldName = lastPath != null ? lastPath.render() : null;
         String part;
         if (badToken.equals(Tokens.END.toString())) {
            if (previousFieldName == null) {
               return message;
            }

            part = message + " (if you intended '" + previousFieldName + "' to be part of a value, instead of a key, try adding double quotes around the whole value";
         } else if (previousFieldName != null) {
            part = message + " (if you intended " + badToken + " to be part of the value for '" + previousFieldName + "', try enclosing the value in double quotes";
         } else {
            part = message + " (if you intended " + badToken + " to be part of a key or string value, try enclosing the key or value in double quotes";
         }

         return insideEquals ? part + ", or you may be able to rename the file .properties rather than .conf)" : part + ")";
      }

      private AbstractConfigNodeValue parseValue(Token t) {
         AbstractConfigNodeValue v = null;
         int startingEqualsCount = this.equalsCount;
         if (!Tokens.isValue(t) && !Tokens.isUnquotedText(t) && !Tokens.isSubstitution(t)) {
            if (t == Tokens.OPEN_CURLY) {
               v = this.parseObject(true);
            } else {
               if (t != Tokens.OPEN_SQUARE) {
                  throw this.parseError(this.addQuoteSuggestion(t.toString(), "Expecting a value but got wrong token: " + t));
               }

               v = this.parseArray();
            }
         } else {
            v = new ConfigNodeSimpleValue(t);
         }

         if (this.equalsCount != startingEqualsCount) {
            throw new ConfigException.BugOrBroken("Bug in config parser: unbalanced equals count");
         } else {
            return (AbstractConfigNodeValue)v;
         }
      }

      private ConfigNodePath parseKey(Token token) {
         if (this.flavor == ConfigSyntax.JSON) {
            if (Tokens.isValueWithType(token, ConfigValueType.STRING)) {
               return PathParser.parsePathNodeExpression(Collections.singletonList(token).iterator(), (ConfigOrigin)null);
            } else {
               throw this.parseError("Expecting close brace } or a field name here, got " + token);
            }
         } else {
            List expression = new ArrayList();

            Token t;
            for(t = token; Tokens.isValue(t) || Tokens.isUnquotedText(t); t = this.nextToken()) {
               expression.add(t);
            }

            if (expression.isEmpty()) {
               throw this.parseError("expecting a close parentheses ')' here, not: " + t);
            } else {
               this.putBack(t);
               return PathParser.parsePathNodeExpression(expression.iterator(), (ConfigOrigin)null);
            }
         }
      }

      private static boolean isIncludeKeyword(Token t) {
         return Tokens.isUnquotedText(t) && Tokens.getUnquotedText(t).equals("include");
      }

      private static boolean isUnquotedWhitespace(Token t) {
         if (!Tokens.isUnquotedText(t)) {
            return false;
         } else {
            String s = Tokens.getUnquotedText(t);

            for(int i = 0; i < s.length(); ++i) {
               char c = s.charAt(i);
               if (!ConfigImplUtil.isWhitespace(c)) {
                  return false;
               }
            }

            return true;
         }
      }

      private boolean isKeyValueSeparatorToken(Token t) {
         if (this.flavor == ConfigSyntax.JSON) {
            return t == Tokens.COLON;
         } else {
            return t == Tokens.COLON || t == Tokens.EQUALS || t == Tokens.PLUS_EQUALS;
         }
      }

      private ConfigNodeInclude parseInclude(ArrayList children) {
         Token t = this.nextTokenCollectingWhitespace(children);
         if (Tokens.isUnquotedText(t)) {
            String kindText = Tokens.getUnquotedText(t);
            if (kindText.startsWith("required(")) {
               String r = kindText.replaceFirst("required\\(", "");
               if (r.length() > 0) {
                  this.putBack(Tokens.newUnquotedText(t.origin(), r));
               }

               children.add(new ConfigNodeSingleToken(t));
               ConfigNodeInclude res = this.parseIncludeResource(children, true);
               t = this.nextTokenCollectingWhitespace(children);
               if (Tokens.isUnquotedText(t) && Tokens.getUnquotedText(t).equals(")")) {
                  return res;
               } else {
                  throw this.parseError("expecting a close parentheses ')' here, not: " + t);
               }
            } else {
               this.putBack(t);
               return this.parseIncludeResource(children, false);
            }
         } else {
            this.putBack(t);
            return this.parseIncludeResource(children, false);
         }
      }

      private ConfigNodeInclude parseIncludeResource(ArrayList children, boolean isRequired) {
         Token t = this.nextTokenCollectingWhitespace(children);
         if (Tokens.isUnquotedText(t)) {
            String kindText = Tokens.getUnquotedText(t);
            ConfigIncludeKind kind;
            String prefix;
            if (kindText.startsWith("url(")) {
               kind = ConfigIncludeKind.URL;
               prefix = "url(";
            } else if (kindText.startsWith("file(")) {
               kind = ConfigIncludeKind.FILE;
               prefix = "file(";
            } else {
               if (!kindText.startsWith("classpath(")) {
                  throw this.parseError("expecting include parameter to be quoted filename, file(), classpath(), or url(). No spaces are allowed before the open paren. Not expecting: " + t);
               }

               kind = ConfigIncludeKind.CLASSPATH;
               prefix = "classpath(";
            }

            String r = kindText.replaceFirst("[^(]*\\(", "");
            if (r.length() > 0) {
               this.putBack(Tokens.newUnquotedText(t.origin(), r));
            }

            children.add(new ConfigNodeSingleToken(t));
            t = this.nextTokenCollectingWhitespace(children);
            if (!Tokens.isValueWithType(t, ConfigValueType.STRING)) {
               throw this.parseError("expecting include " + prefix + ") parameter to be a quoted string, rather than: " + t);
            } else {
               children.add(new ConfigNodeSimpleValue(t));
               t = this.nextTokenCollectingWhitespace(children);
               if (Tokens.isUnquotedText(t) && Tokens.getUnquotedText(t).startsWith(")")) {
                  String rest = Tokens.getUnquotedText(t).substring(1);
                  if (rest.length() > 0) {
                     this.putBack(Tokens.newUnquotedText(t.origin(), rest));
                  }

                  return new ConfigNodeInclude(children, kind, isRequired);
               } else {
                  throw this.parseError("expecting a close parentheses ')' here, not: " + t);
               }
            }
         } else if (Tokens.isValueWithType(t, ConfigValueType.STRING)) {
            children.add(new ConfigNodeSimpleValue(t));
            return new ConfigNodeInclude(children, ConfigIncludeKind.HEURISTIC, isRequired);
         } else {
            throw this.parseError("include keyword is not followed by a quoted string, but by: " + t);
         }
      }

      private ConfigNodeComplexValue parseObject(boolean hadOpenCurly) {
         boolean afterComma = false;
         Path lastPath = null;
         boolean lastInsideEquals = false;
         ArrayList objectNodes = new ArrayList();
         HashMap keys = new HashMap();
         if (hadOpenCurly) {
            objectNodes.add(new ConfigNodeSingleToken(Tokens.OPEN_CURLY));
         }

         while(true) {
            Token t = this.nextTokenCollectingWhitespace(objectNodes);
            if (t == Tokens.CLOSE_CURLY) {
               if (this.flavor == ConfigSyntax.JSON && afterComma) {
                  throw this.parseError(this.addQuoteSuggestion(t.toString(), "expecting a field name after a comma, got a close brace } instead"));
               }

               if (!hadOpenCurly) {
                  throw this.parseError(this.addQuoteSuggestion(t.toString(), "unbalanced close brace '}' with no open brace"));
               }

               objectNodes.add(new ConfigNodeSingleToken(Tokens.CLOSE_CURLY));
               break;
            }

            if (t == Tokens.END && !hadOpenCurly) {
               this.putBack(t);
               break;
            }

            if (this.flavor != ConfigSyntax.JSON && isIncludeKeyword(t)) {
               ArrayList includeNodes = new ArrayList();
               includeNodes.add(new ConfigNodeSingleToken(t));
               objectNodes.add(this.parseInclude(includeNodes));
               afterComma = false;
            } else {
               ArrayList keyValueNodes = new ArrayList();
               ConfigNodePath path = this.parseKey(t);
               keyValueNodes.add(path);
               Token afterKey = this.nextTokenCollectingWhitespace(keyValueNodes);
               boolean insideEquals = false;
               AbstractConfigNodeValue nextValue;
               if (this.flavor == ConfigSyntax.CONF && afterKey == Tokens.OPEN_CURLY) {
                  nextValue = this.parseValue(afterKey);
               } else {
                  if (!this.isKeyValueSeparatorToken(afterKey)) {
                     throw this.parseError(this.addQuoteSuggestion(afterKey.toString(), "Key '" + path.render() + "' may not be followed by token: " + afterKey));
                  }

                  keyValueNodes.add(new ConfigNodeSingleToken(afterKey));
                  if (afterKey == Tokens.EQUALS) {
                     insideEquals = true;
                     ++this.equalsCount;
                  }

                  nextValue = this.consolidateValues(keyValueNodes);
                  if (nextValue == null) {
                     nextValue = this.parseValue(this.nextTokenCollectingWhitespace(keyValueNodes));
                  }
               }

               keyValueNodes.add(nextValue);
               if (insideEquals) {
                  --this.equalsCount;
               }

               lastInsideEquals = insideEquals;
               String key = path.value().first();
               Path remaining = path.value().remainder();
               if (remaining == null) {
                  Boolean existing = (Boolean)keys.get(key);
                  if (existing != null && this.flavor == ConfigSyntax.JSON) {
                     throw this.parseError("JSON does not allow duplicate fields: '" + key + "' was already seen");
                  }

                  keys.put(key, true);
               } else {
                  if (this.flavor == ConfigSyntax.JSON) {
                     throw new ConfigException.BugOrBroken("somehow got multi-element path in JSON mode");
                  }

                  keys.put(key, true);
               }

               afterComma = false;
               objectNodes.add(new ConfigNodeField(keyValueNodes));
            }

            if (!this.checkElementSeparator(objectNodes)) {
               t = this.nextTokenCollectingWhitespace(objectNodes);
               if (t == Tokens.CLOSE_CURLY) {
                  if (!hadOpenCurly) {
                     throw this.parseError(this.addQuoteSuggestion((Path)lastPath, lastInsideEquals, t.toString(), "unbalanced close brace '}' with no open brace"));
                  }

                  objectNodes.add(new ConfigNodeSingleToken(t));
               } else {
                  if (hadOpenCurly) {
                     throw this.parseError(this.addQuoteSuggestion((Path)lastPath, lastInsideEquals, t.toString(), "Expecting close brace } or a comma, got " + t));
                  }

                  if (t != Tokens.END) {
                     throw this.parseError(this.addQuoteSuggestion((Path)lastPath, lastInsideEquals, t.toString(), "Expecting end of input or a comma, got " + t));
                  }

                  this.putBack(t);
               }
               break;
            }

            afterComma = true;
         }

         return new ConfigNodeObject(objectNodes);
      }

      private ConfigNodeComplexValue parseArray() {
         ArrayList children = new ArrayList();
         children.add(new ConfigNodeSingleToken(Tokens.OPEN_SQUARE));
         AbstractConfigNodeValue nextValue = this.consolidateValues(children);
         Token t;
         if (nextValue != null) {
            children.add(nextValue);
         } else {
            t = this.nextTokenCollectingWhitespace(children);
            if (t == Tokens.CLOSE_SQUARE) {
               children.add(new ConfigNodeSingleToken(t));
               return new ConfigNodeArray(children);
            }

            if (!Tokens.isValue(t) && t != Tokens.OPEN_CURLY && t != Tokens.OPEN_SQUARE && !Tokens.isUnquotedText(t) && !Tokens.isSubstitution(t)) {
               throw this.parseError("List should have ] or a first element after the open [, instead had token: " + t + " (if you want " + t + " to be part of a string value, then double-quote it)");
            }

            nextValue = this.parseValue(t);
            children.add(nextValue);
         }

         while(true) {
            while(true) {
               while(this.checkElementSeparator(children)) {
                  nextValue = this.consolidateValues(children);
                  if (nextValue == null) {
                     t = this.nextTokenCollectingWhitespace(children);
                     if (!Tokens.isValue(t) && t != Tokens.OPEN_CURLY && t != Tokens.OPEN_SQUARE && !Tokens.isUnquotedText(t) && !Tokens.isSubstitution(t)) {
                        if (this.flavor == ConfigSyntax.JSON || t != Tokens.CLOSE_SQUARE) {
                           throw this.parseError("List should have had new element after a comma, instead had token: " + t + " (if you want the comma or " + t + " to be part of a string value, then double-quote it)");
                        }

                        this.putBack(t);
                     } else {
                        nextValue = this.parseValue(t);
                        children.add(nextValue);
                     }
                  } else {
                     children.add(nextValue);
                  }
               }

               t = this.nextTokenCollectingWhitespace(children);
               if (t == Tokens.CLOSE_SQUARE) {
                  children.add(new ConfigNodeSingleToken(t));
                  return new ConfigNodeArray(children);
               }

               throw this.parseError("List should have ended with ] or had a comma, instead had token: " + t + " (if you want " + t + " to be part of a string value, then double-quote it)");
            }
         }
      }

      ConfigNodeRoot parse() {
         ArrayList children = new ArrayList();
         Token t = this.nextToken();
         if (t != Tokens.START) {
            throw new ConfigException.BugOrBroken("token stream did not begin with START, had " + t);
         } else {
            t = this.nextTokenCollectingWhitespace(children);
            AbstractConfigNode result = null;
            boolean missingCurly = false;
            if (t != Tokens.OPEN_CURLY && t != Tokens.OPEN_SQUARE) {
               if (this.flavor == ConfigSyntax.JSON) {
                  if (t == Tokens.END) {
                     throw this.parseError("Empty document");
                  }

                  throw this.parseError("Document must have an object or array at root, unexpected token: " + t);
               }

               this.putBack(t);
               missingCurly = true;
               result = this.parseObject(false);
            } else {
               result = this.parseValue(t);
            }

            if (result instanceof ConfigNodeObject && missingCurly) {
               children.addAll(((ConfigNodeComplexValue)result).children());
            } else {
               children.add(result);
            }

            t = this.nextTokenCollectingWhitespace(children);
            if (t == Tokens.END) {
               return missingCurly ? new ConfigNodeRoot(Collections.singletonList(new ConfigNodeObject(children)), this.baseOrigin) : new ConfigNodeRoot(children, this.baseOrigin);
            } else {
               throw this.parseError("Document has trailing tokens after first object or array: " + t);
            }
         }
      }

      AbstractConfigNodeValue parseSingleValue() {
         Token t = this.nextToken();
         if (t == Tokens.START) {
            t = this.nextToken();
            if (!Tokens.isIgnoredWhitespace(t) && !Tokens.isNewline(t) && !isUnquotedWhitespace(t) && !Tokens.isComment(t)) {
               if (t == Tokens.END) {
                  throw this.parseError("Empty value");
               } else if (this.flavor == ConfigSyntax.JSON) {
                  AbstractConfigNodeValue node = this.parseValue(t);
                  t = this.nextToken();
                  if (t == Tokens.END) {
                     return node;
                  } else {
                     throw this.parseError("Parsing JSON and the value set in withValueText was either a concatenation or had trailing whitespace, newlines, or comments");
                  }
               } else {
                  this.putBack(t);
                  ArrayList nodes = new ArrayList();
                  AbstractConfigNodeValue node = this.consolidateValues(nodes);
                  t = this.nextToken();
                  if (t == Tokens.END) {
                     return node;
                  } else {
                     throw this.parseError("The value from withValueText cannot have leading or trailing newlines, whitespace, or comments");
                  }
               }
            } else {
               throw this.parseError("The value from withValueText cannot have leading or trailing newlines, whitespace, or comments");
            }
         } else {
            throw new ConfigException.BugOrBroken("token stream did not begin with START, had " + t);
         }
      }
   }
}
