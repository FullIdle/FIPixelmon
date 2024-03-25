package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigException;
import info.pixelmon.repack.com.typesafe.config.ConfigOrigin;
import info.pixelmon.repack.com.typesafe.config.ConfigValueType;
import java.util.Iterator;
import java.util.List;

final class Tokens {
   static final Token START;
   static final Token END;
   static final Token COMMA;
   static final Token EQUALS;
   static final Token COLON;
   static final Token OPEN_CURLY;
   static final Token CLOSE_CURLY;
   static final Token OPEN_SQUARE;
   static final Token CLOSE_SQUARE;
   static final Token PLUS_EQUALS;

   static boolean isValue(Token token) {
      return token instanceof Value;
   }

   static AbstractConfigValue getValue(Token token) {
      if (token instanceof Value) {
         return ((Value)token).value();
      } else {
         throw new ConfigException.BugOrBroken("tried to get value of non-value token " + token);
      }
   }

   static boolean isValueWithType(Token t, ConfigValueType valueType) {
      return isValue(t) && getValue(t).valueType() == valueType;
   }

   static boolean isNewline(Token token) {
      return token instanceof Line;
   }

   static boolean isProblem(Token token) {
      return token instanceof Problem;
   }

   static String getProblemWhat(Token token) {
      if (token instanceof Problem) {
         return ((Problem)token).what();
      } else {
         throw new ConfigException.BugOrBroken("tried to get problem what from " + token);
      }
   }

   static String getProblemMessage(Token token) {
      if (token instanceof Problem) {
         return ((Problem)token).message();
      } else {
         throw new ConfigException.BugOrBroken("tried to get problem message from " + token);
      }
   }

   static boolean getProblemSuggestQuotes(Token token) {
      if (token instanceof Problem) {
         return ((Problem)token).suggestQuotes();
      } else {
         throw new ConfigException.BugOrBroken("tried to get problem suggestQuotes from " + token);
      }
   }

   static Throwable getProblemCause(Token token) {
      if (token instanceof Problem) {
         return ((Problem)token).cause();
      } else {
         throw new ConfigException.BugOrBroken("tried to get problem cause from " + token);
      }
   }

   static boolean isComment(Token token) {
      return token instanceof Comment;
   }

   static String getCommentText(Token token) {
      if (token instanceof Comment) {
         return ((Comment)token).text();
      } else {
         throw new ConfigException.BugOrBroken("tried to get comment text from " + token);
      }
   }

   static boolean isUnquotedText(Token token) {
      return token instanceof UnquotedText;
   }

   static String getUnquotedText(Token token) {
      if (token instanceof UnquotedText) {
         return ((UnquotedText)token).value();
      } else {
         throw new ConfigException.BugOrBroken("tried to get unquoted text from " + token);
      }
   }

   static boolean isIgnoredWhitespace(Token token) {
      return token instanceof IgnoredWhitespace;
   }

   static boolean isSubstitution(Token token) {
      return token instanceof Substitution;
   }

   static List getSubstitutionPathExpression(Token token) {
      if (token instanceof Substitution) {
         return ((Substitution)token).value();
      } else {
         throw new ConfigException.BugOrBroken("tried to get substitution from " + token);
      }
   }

   static boolean getSubstitutionOptional(Token token) {
      if (token instanceof Substitution) {
         return ((Substitution)token).optional();
      } else {
         throw new ConfigException.BugOrBroken("tried to get substitution optionality from " + token);
      }
   }

   static Token newLine(ConfigOrigin origin) {
      return new Line(origin);
   }

   static Token newProblem(ConfigOrigin origin, String what, String message, boolean suggestQuotes, Throwable cause) {
      return new Problem(origin, what, message, suggestQuotes, cause);
   }

   static Token newCommentDoubleSlash(ConfigOrigin origin, String text) {
      return new Comment.DoubleSlashComment(origin, text);
   }

   static Token newCommentHash(ConfigOrigin origin, String text) {
      return new Comment.HashComment(origin, text);
   }

   static Token newUnquotedText(ConfigOrigin origin, String s) {
      return new UnquotedText(origin, s);
   }

   static Token newIgnoredWhitespace(ConfigOrigin origin, String s) {
      return new IgnoredWhitespace(origin, s);
   }

   static Token newSubstitution(ConfigOrigin origin, boolean optional, List expression) {
      return new Substitution(origin, optional, expression);
   }

   static Token newValue(AbstractConfigValue value) {
      return new Value(value);
   }

   static Token newValue(AbstractConfigValue value, String origText) {
      return new Value(value, origText);
   }

   static Token newString(ConfigOrigin origin, String value, String origText) {
      return newValue(new ConfigString.Quoted(origin, value), origText);
   }

   static Token newInt(ConfigOrigin origin, int value, String origText) {
      return newValue(ConfigNumber.newNumber(origin, (long)value, origText), origText);
   }

   static Token newDouble(ConfigOrigin origin, double value, String origText) {
      return newValue(ConfigNumber.newNumber(origin, value, origText), origText);
   }

   static Token newLong(ConfigOrigin origin, long value, String origText) {
      return newValue(ConfigNumber.newNumber(origin, value, origText), origText);
   }

   static Token newNull(ConfigOrigin origin) {
      return newValue(new ConfigNull(origin), "null");
   }

   static Token newBoolean(ConfigOrigin origin, boolean value) {
      return newValue(new ConfigBoolean(origin, value), "" + value);
   }

   static {
      START = Token.newWithoutOrigin(TokenType.START, "start of file", "");
      END = Token.newWithoutOrigin(TokenType.END, "end of file", "");
      COMMA = Token.newWithoutOrigin(TokenType.COMMA, "','", ",");
      EQUALS = Token.newWithoutOrigin(TokenType.EQUALS, "'='", "=");
      COLON = Token.newWithoutOrigin(TokenType.COLON, "':'", ":");
      OPEN_CURLY = Token.newWithoutOrigin(TokenType.OPEN_CURLY, "'{'", "{");
      CLOSE_CURLY = Token.newWithoutOrigin(TokenType.CLOSE_CURLY, "'}'", "}");
      OPEN_SQUARE = Token.newWithoutOrigin(TokenType.OPEN_SQUARE, "'['", "[");
      CLOSE_SQUARE = Token.newWithoutOrigin(TokenType.CLOSE_SQUARE, "']'", "]");
      PLUS_EQUALS = Token.newWithoutOrigin(TokenType.PLUS_EQUALS, "'+='", "+=");
   }

   private static class Substitution extends Token {
      private final boolean optional;
      private final List value;

      Substitution(ConfigOrigin origin, boolean optional, List expression) {
         super(TokenType.SUBSTITUTION, origin);
         this.optional = optional;
         this.value = expression;
      }

      boolean optional() {
         return this.optional;
      }

      List value() {
         return this.value;
      }

      public String tokenText() {
         return "${" + (this.optional ? "?" : "") + Tokenizer.render(this.value.iterator()) + "}";
      }

      public String toString() {
         StringBuilder sb = new StringBuilder();
         Iterator var2 = this.value.iterator();

         while(var2.hasNext()) {
            Token t = (Token)var2.next();
            sb.append(t.toString());
         }

         return "'${" + sb.toString() + "}'";
      }

      protected boolean canEqual(Object other) {
         return other instanceof Substitution;
      }

      public boolean equals(Object other) {
         return super.equals(other) && ((Substitution)other).value.equals(this.value);
      }

      public int hashCode() {
         return 41 * (41 + super.hashCode()) + this.value.hashCode();
      }
   }

   private abstract static class Comment extends Token {
      private final String text;

      Comment(ConfigOrigin origin, String text) {
         super(TokenType.COMMENT, origin);
         this.text = text;
      }

      String text() {
         return this.text;
      }

      public String toString() {
         StringBuilder sb = new StringBuilder();
         sb.append("'#");
         sb.append(this.text);
         sb.append("' (COMMENT)");
         return sb.toString();
      }

      protected boolean canEqual(Object other) {
         return other instanceof Comment;
      }

      public boolean equals(Object other) {
         return super.equals(other) && ((Comment)other).text.equals(this.text);
      }

      public int hashCode() {
         int h = 41 * (41 + super.hashCode());
         h = 41 * (h + this.text.hashCode());
         return h;
      }

      static final class HashComment extends Comment {
         HashComment(ConfigOrigin origin, String text) {
            super(origin, text);
         }

         public String tokenText() {
            return "#" + super.text;
         }
      }

      static final class DoubleSlashComment extends Comment {
         DoubleSlashComment(ConfigOrigin origin, String text) {
            super(origin, text);
         }

         public String tokenText() {
            return "//" + super.text;
         }
      }
   }

   private static class Problem extends Token {
      private final String what;
      private final String message;
      private final boolean suggestQuotes;
      private final Throwable cause;

      Problem(ConfigOrigin origin, String what, String message, boolean suggestQuotes, Throwable cause) {
         super(TokenType.PROBLEM, origin);
         this.what = what;
         this.message = message;
         this.suggestQuotes = suggestQuotes;
         this.cause = cause;
      }

      String what() {
         return this.what;
      }

      String message() {
         return this.message;
      }

      boolean suggestQuotes() {
         return this.suggestQuotes;
      }

      Throwable cause() {
         return this.cause;
      }

      public String toString() {
         StringBuilder sb = new StringBuilder();
         sb.append('\'');
         sb.append(this.what);
         sb.append('\'');
         sb.append(" (");
         sb.append(this.message);
         sb.append(")");
         return sb.toString();
      }

      protected boolean canEqual(Object other) {
         return other instanceof Problem;
      }

      public boolean equals(Object other) {
         return super.equals(other) && ((Problem)other).what.equals(this.what) && ((Problem)other).message.equals(this.message) && ((Problem)other).suggestQuotes == this.suggestQuotes && ConfigImplUtil.equalsHandlingNull(((Problem)other).cause, this.cause);
      }

      public int hashCode() {
         int h = 41 * (41 + super.hashCode());
         h = 41 * (h + this.what.hashCode());
         h = 41 * (h + this.message.hashCode());
         h = 41 * (h + Boolean.valueOf(this.suggestQuotes).hashCode());
         if (this.cause != null) {
            h = 41 * (h + this.cause.hashCode());
         }

         return h;
      }
   }

   private static class IgnoredWhitespace extends Token {
      private final String value;

      IgnoredWhitespace(ConfigOrigin origin, String s) {
         super(TokenType.IGNORED_WHITESPACE, origin);
         this.value = s;
      }

      public String toString() {
         return "'" + this.value + "' (WHITESPACE)";
      }

      protected boolean canEqual(Object other) {
         return other instanceof IgnoredWhitespace;
      }

      public boolean equals(Object other) {
         return super.equals(other) && ((IgnoredWhitespace)other).value.equals(this.value);
      }

      public int hashCode() {
         return 41 * (41 + super.hashCode()) + this.value.hashCode();
      }

      public String tokenText() {
         return this.value;
      }
   }

   private static class UnquotedText extends Token {
      private final String value;

      UnquotedText(ConfigOrigin origin, String s) {
         super(TokenType.UNQUOTED_TEXT, origin);
         this.value = s;
      }

      String value() {
         return this.value;
      }

      public String toString() {
         return "'" + this.value + "'";
      }

      protected boolean canEqual(Object other) {
         return other instanceof UnquotedText;
      }

      public boolean equals(Object other) {
         return super.equals(other) && ((UnquotedText)other).value.equals(this.value);
      }

      public int hashCode() {
         return 41 * (41 + super.hashCode()) + this.value.hashCode();
      }

      public String tokenText() {
         return this.value;
      }
   }

   private static class Line extends Token {
      Line(ConfigOrigin origin) {
         super(TokenType.NEWLINE, origin);
      }

      public String toString() {
         return "'\\n'@" + this.lineNumber();
      }

      protected boolean canEqual(Object other) {
         return other instanceof Line;
      }

      public boolean equals(Object other) {
         return super.equals(other) && ((Line)other).lineNumber() == this.lineNumber();
      }

      public int hashCode() {
         return 41 * (41 + super.hashCode()) + this.lineNumber();
      }

      public String tokenText() {
         return "\n";
      }
   }

   private static class Value extends Token {
      private final AbstractConfigValue value;

      Value(AbstractConfigValue value) {
         this(value, (String)null);
      }

      Value(AbstractConfigValue value, String origText) {
         super(TokenType.VALUE, value.origin(), origText);
         this.value = value;
      }

      AbstractConfigValue value() {
         return this.value;
      }

      public String toString() {
         return this.value().resolveStatus() == ResolveStatus.RESOLVED ? "'" + this.value().unwrapped() + "' (" + this.value.valueType().name() + ")" : "'<unresolved value>' (" + this.value.valueType().name() + ")";
      }

      protected boolean canEqual(Object other) {
         return other instanceof Value;
      }

      public boolean equals(Object other) {
         return super.equals(other) && ((Value)other).value.equals(this.value);
      }

      public int hashCode() {
         return 41 * (41 + super.hashCode()) + this.value.hashCode();
      }
   }
}
