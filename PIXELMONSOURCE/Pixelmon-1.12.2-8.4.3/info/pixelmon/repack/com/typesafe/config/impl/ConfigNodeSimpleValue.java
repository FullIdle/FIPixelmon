package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

final class ConfigNodeSimpleValue extends AbstractConfigNodeValue {
   final Token token;

   ConfigNodeSimpleValue(Token value) {
      this.token = value;
   }

   protected Collection tokens() {
      return Collections.singletonList(this.token);
   }

   protected Token token() {
      return this.token;
   }

   protected AbstractConfigValue value() {
      if (Tokens.isValue(this.token)) {
         return Tokens.getValue(this.token);
      } else if (Tokens.isUnquotedText(this.token)) {
         return new ConfigString.Unquoted(this.token.origin(), Tokens.getUnquotedText(this.token));
      } else if (Tokens.isSubstitution(this.token)) {
         List expression = Tokens.getSubstitutionPathExpression(this.token);
         Path path = PathParser.parsePathExpression(expression.iterator(), this.token.origin());
         boolean optional = Tokens.getSubstitutionOptional(this.token);
         return new ConfigReference(this.token.origin(), new SubstitutionExpression(path, optional));
      } else {
         throw new ConfigException.BugOrBroken("ConfigNodeSimpleValue did not contain a valid value token");
      }
   }
}
