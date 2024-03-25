package info.pixelmon.repack.com.typesafe.config.impl;

import java.util.Collection;
import java.util.Collections;

class ConfigNodeSingleToken extends AbstractConfigNode {
   final Token token;

   ConfigNodeSingleToken(Token t) {
      this.token = t;
   }

   protected Collection tokens() {
      return Collections.singletonList(this.token);
   }

   protected Token token() {
      return this.token;
   }
}
