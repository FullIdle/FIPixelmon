package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigException;
import info.pixelmon.repack.com.typesafe.config.ConfigOrigin;

class Token {
   private final TokenType tokenType;
   private final String debugString;
   private final ConfigOrigin origin;
   private final String tokenText;

   Token(TokenType tokenType, ConfigOrigin origin) {
      this(tokenType, origin, (String)null);
   }

   Token(TokenType tokenType, ConfigOrigin origin, String tokenText) {
      this(tokenType, origin, tokenText, (String)null);
   }

   Token(TokenType tokenType, ConfigOrigin origin, String tokenText, String debugString) {
      this.tokenType = tokenType;
      this.origin = origin;
      this.debugString = debugString;
      this.tokenText = tokenText;
   }

   static Token newWithoutOrigin(TokenType tokenType, String debugString, String tokenText) {
      return new Token(tokenType, (ConfigOrigin)null, tokenText, debugString);
   }

   final TokenType tokenType() {
      return this.tokenType;
   }

   public String tokenText() {
      return this.tokenText;
   }

   final ConfigOrigin origin() {
      if (this.origin == null) {
         throw new ConfigException.BugOrBroken("tried to get origin from token that doesn't have one: " + this);
      } else {
         return this.origin;
      }
   }

   final int lineNumber() {
      return this.origin != null ? this.origin.lineNumber() : -1;
   }

   public String toString() {
      return this.debugString != null ? this.debugString : this.tokenType.name();
   }

   protected boolean canEqual(Object other) {
      return other instanceof Token;
   }

   public boolean equals(Object other) {
      if (!(other instanceof Token)) {
         return false;
      } else {
         return this.canEqual(other) && this.tokenType == ((Token)other).tokenType;
      }
   }

   public int hashCode() {
      return this.tokenType.hashCode();
   }
}
